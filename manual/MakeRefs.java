import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import nl.utwente.groove.explore.AcceptorValue;
import nl.utwente.groove.explore.StrategyValue;
import nl.utwente.groove.algebra.Sort;
import nl.utwente.groove.algebra.Operator;
import nl.utwente.groove.control.parse.CtrlDoc;
import nl.utwente.groove.grammar.aspect.AspectKind;
import nl.utwente.groove.graph.GraphRole;

/**
 * Generates the reference pages of the web manual from the GROOVE sources.
 * Invoked by make-refs.sh; see that script for usage.
 *
 * Arguments: [0] the GROOVE version documented, [1] the output directory.
 */
public class MakeRefs {
    public static void main(String[] args) throws IOException {
        String version = args[0];
        Path outDir = Path.of(args[1]);
        new MakeRefs(version, outDir).run();
    }

    private MakeRefs(String version, Path outDir) {
        this.version = version;
        this.outDir = outDir;
    }

    private final String version;
    private final Path outDir;

    private void run() throws IOException {
        writePrefixes();
        writeControl();
        writeExploration();
        writeOperators();
    }

    private void writePrefixes() throws IOException {
        StringBuilder b = frontMatter("Reference: aspect prefixes", "manual_ref_prefixes.html",
            "All node and edge label syntax, per graph role", "aspect, prefix, syntax, reference");
        b.append(generatedNote());
        b.append("""
            The tables below list the complete label syntax for the three kinds of aspect graph, \
            as also shown in the help panel of the graph editor in the Simulator. \
            Optional parts are shown in square brackets.

            """);
        record RolePage(GraphRole role, String title) {}
        var roles = List.of(new RolePage(GraphRole.HOST, "Host graphs"),
            new RolePage(GraphRole.RULE, "Rules"), new RolePage(GraphRole.TYPE, "Type graphs"));
        for (var rp : roles) {
            b.append("## " + rp.title() + "\n\n");
            b.append("### Node labels\n\n");
            appendHelpTable(b, AspectKind.getNodeDocMap(rp.role()));
            b.append("### Edge labels\n\n");
            appendHelpTable(b, AspectKind.getEdgeDocMap(rp.role()));
        }
        write("ref-prefixes.md", b);
    }

    private void appendHelpTable(StringBuilder b, Map<String,String> docMap) {
        b.append("| Syntax | Explanation |\n| :--- | :--- |\n");
        for (var e : docMap.entrySet()) {
            b.append("| " + code(e.getKey()) + " | " + cell(e.getValue()) + " |\n");
        }
        b.append("\n");
    }

    private void writeControl() throws IOException {
        StringBuilder b = frontMatter("Reference: control grammar", "manual_ref_control.html",
            "The full grammar of the control language", "control, grammar, syntax, reference");
        b.append(generatedNote());
        b.append("""
            The grammar of the control language (see the [Control language](manual_control.html) \
            chapter), one nonterminal per section, as also shown in the help panel of the control \
            editor in the Simulator. The first alternative of every nonterminal appears in the \
            heading of its section.

            """);
        CtrlDoc doc = new CtrlDoc();
        Map<?,String> toolTips = doc.getToolTipMap();
        for (var e : doc.getItemTree().entrySet()) {
            b.append("## " + text(e.getKey().toString()) + "\n\n");
            b.append("| Syntax | Explanation |\n| :--- | :--- |\n");
            for (Object entry : e.getValue()) {
                b.append("| " + code(entry.toString()) + " | " + cell(toolTips.get(entry)) + " |\n");
            }
            b.append("\n");
        }
        write("ref-control.md", b);
    }

    private void writeExploration() throws IOException {
        StringBuilder b = frontMatter("Reference: strategies and acceptors",
            "manual_ref_exploration.html",
            "The named exploration strategies and acceptors of the legacy -s/-a options",
            "strategy, acceptor, exploration, reference");
        b.append(generatedNote());
        b.append("""
            The named strategies and acceptors below constitute the vocabulary of the Generator's \
            deprecated `-s` and `-a` options (see the \
            [Exploration and verification](manual_verification.html) chapter); the preferred way \
            to configure an exploration is the `-x` configuration vocabulary described there. \
            Development-only strategies are omitted.

            ## Strategies

            | Keyword | Name | Description |
            | :--- | :--- | :--- |
            """);
        for (StrategyValue strategy : StrategyValue.values()) {
            if (strategy.isDevelopment()) {
                continue;
            }
            b.append("| `" + strategy.getKeyword() + "` | " + strategy.getName() + " | "
                + cell(strategy.getDescription()) + " |\n");
        }
        b.append("""

            ## Acceptors

            | Keyword | Name | Description |
            | :--- | :--- | :--- |
            """);
        for (AcceptorValue acceptor : AcceptorValue.values()) {
            b.append("| `" + acceptor.getKeyword() + "` | " + acceptor.getName() + " | "
                + cell(acceptor.getDescription()) + " |\n");
        }
        write("ref-exploration.md", b);
    }

    private void writeOperators() throws IOException {
        StringBuilder b = frontMatter("Reference: data operations", "manual_ref_operators.html",
            "All built-in operations on the data sorts", "attribute, operation, sort, reference");
        b.append(generatedNote());
        b.append("""
            The built-in operations per data sort, usable in the attribute expressions described \
            in the [Advanced rule features](manual_advanced.html) chapter. Operations with a \
            symbol can be written in prefix or infix notation; all operations can be called by \
            name, in functional style.

            """);
        for (Sort sort : Sort.values()) {
            Map<String,Operator> ops = sort.getOperatorMap();
            if (ops.isEmpty()) {
                continue;
            }
            b.append("## Sort `" + sort.getName() + "`\n\n");
            b.append("| Operation | Symbol | Description |\n| :--- | :---: | :--- |\n");
            for (Operator op : ops.values()) {
                StringBuilder sig = new StringBuilder(op.getName() + "(");
                boolean first = true;
                for (Sort par : op.getParamSorts()) {
                    if (!first) {
                        sig.append(", ");
                    }
                    first = false;
                    sig.append(par.getName());
                }
                sig.append("): " + op.getResultSort().getName());
                String symbol = op.hasSymbol()
                    ? code(op.getSymbol())
                    : "";
                b.append("| " + code(sig.toString()) + " | " + symbol + " | "
                    + cell(op.getDescription()) + " |\n");
            }
            b.append("\n");
        }
        write("ref-operators.md", b);
    }

    private StringBuilder frontMatter(String title, String permalink, String summary,
                                      String keywords) {
        StringBuilder b = new StringBuilder();
        b.append("---\n");
        b.append("title:\n    \"" + title + "\"\n");
        b.append("permalink:\n    " + permalink + "\n");
        b.append("summary:\n    \"" + summary + "\"\n");
        b.append("sidebar:\n    manual_sidebar\n");
        b.append("toc:\n    true\n");
        b.append("last_updated:\n    false\n");
        b.append("datatable:\n    false\n");
        b.append("tags:\n");
        b.append("keywords:\n    " + keywords + "\n");
        b.append("---\n\n");
        return b;
    }

    private String generatedNote() {
        return "*This page is generated from the GROOVE " + this.version
            + " source code by `manual/make-refs.sh`; do not edit it by hand.*\n\n";
    }

    /** Renders an HTML help string as a plain-text markdown table cell. */
    private static String cell(String html) {
        if (html == null) {
            return "";
        }
        return mdEscape(text(html));
    }

    /** Renders an HTML syntax string as an inline-code markdown table cell. */
    private static String code(String html) {
        String s = text(html);
        s = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        s = s.replace("|", "&#124;");
        return s.isEmpty()
            ? ""
            : "<code>" + s + "</code>";
    }

    /** Strips HTML markup, retaining the text content. */
    private static String text(String html) {
        String s = html;
        s = s.replaceAll("(?i)<li>", " • ");
        s = s.replaceAll("(?i)<tr[^>]*>", "; ");
        s = s.replaceAll("(?i)<(br|p|/p|/div)\\s*/?>", " ");
        s = s.replaceAll("<[^>]+>", "");
        s = s.replace("&lt;", "<").replace("&gt;", ">").replace("&#60;", "<")
            .replace("&#62;", ">").replace("&#124;", "|").replace("&nbsp;", " ")
            .replace("&quot;", "\"").replace("&amp;", "&");
        return s.replaceAll("\\s+", " ").trim();
    }

    /** Escapes characters that have a special meaning in markdown table cells. */
    private static String mdEscape(String s) {
        return s.replace("|", "&#124;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void write(String name, StringBuilder content) throws IOException {
        Path file = this.outDir.resolve(name);
        Files.writeString(file, content.toString());
        System.out.println("Generated " + file);
    }
}
