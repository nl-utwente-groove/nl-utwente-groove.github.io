import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nl.utwente.groove.explore.ExploreType;
import nl.utwente.groove.explore.LTLExploreType;
import nl.utwente.groove.explore.config.ConfiguredExploreType;
import nl.utwente.groove.explore.config.ExploreConfig;
import nl.utwente.groove.explore.config.LegacySyntaxParser;
import nl.utwente.groove.explore.feature.ExploreKey;
import nl.utwente.groove.explore.feature.Setting;
import nl.utwente.groove.algebra.Sort;
import nl.utwente.groove.algebra.Operator;
import nl.utwente.groove.control.parse.CtrlDoc;
import nl.utwente.groove.grammar.aspect.AspectKind;
import nl.utwente.groove.graph.GraphRole;
import nl.utwente.groove.util.parse.FormatException;

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
        StringBuilder b = frontMatter("Reference: exploration configuration",
            "manual_ref_exploration.html",
            "The keys and settings of the exploration configuration vocabulary,"
                + " and the legacy strategy and acceptor keywords",
            "exploration, configuration, strategy, acceptor, reference");
        b.append(generatedNote());
        b.append("""
            The keys and settings below constitute the vocabulary of the Generator's `-x` option, \
            of the exploration dialog in the Simulator and of the `exploration` grammar property \
            (see the [Exploration and verification](manual_verification.html) chapter). \
            The textual form of a configuration is a space-separated list of \
            `key=setting` entries; a key that is absent takes its default.

            ## Keys

            | Key | Default | Description |
            | :--- | :--- | :--- |
            """);
        for (ExploreKey key : ExploreKey.values()) {
            b.append("| " + code(key.getName()) + " | " + code(key.getDefaultKind().getName())
                + " | " + cell(key.getExplanation()) + " |\n");
        }
        b.append("""

            ## Settings

            The settings per key; the default is marked with an asterisk. In the setting forms, \
            *n* stands for a number, *max*[`+`*inc*] for a maximum with an optional increment \
            for iterative deepening, and *text* for a name or formula as explained in the \
            description.

            | Key | Setting | Description |
            | :--- | :--- | :--- |
            """);
        for (ExploreKey key : ExploreKey.values()) {
            boolean first = true;
            for (Setting.Kind kind : key.getKindType().getEnumConstants()) {
                String arg = switch (kind.contentType()) {
                case NULL -> "";
                case INTEGER, LONG -> "*n*";
                case LIMIT -> "*max*[`+`*inc*]";
                case STRING -> "*text*";
                };
                String form = arg.isEmpty()
                    ? code(kind.getName())
                    : code(kind.getName() + ":") + arg;
                if (!arg.isEmpty() && key.getKindMap().get("") == kind) {
                    // the kind name may be omitted
                    form = arg + " or " + form;
                }
                if (kind == key.getDefaultKind()) {
                    form += "\\*";
                }
                b.append("| " + (first
                    ? code(key.getName())
                    : "") + " | " + form + " | " + cell(kind.getExplanation()) + " |\n");
                first = false;
            }
        }
        b.append("""

            ## Legacy strategies and acceptors

            Up to release 7.5.3, an exploration was given by a named strategy and acceptor: \
            the keywords of the Generator's `-s` and `-a` options (still accepted as legacy \
            shorthand) and of the `explorationStrategy` grammar property. Each combination maps \
            onto a configuration, except for the LTL model checking strategies, which are not \
            configuration-based. The table lists every keyword with an example of a legacy \
            *strategy* *acceptor* pair and the configuration it translates to.

            | Legacy keyword | Example | Equivalent configuration |
            | :--- | :--- | :--- |
            """);
        record Legacy(String syntax, String example) {}
        var legacy = List.of(new Legacy("`bfs`", "bfs final"),
            new Legacy("`bfs:`*n*", "bfs:5 final"), new Legacy("`dfs`", "dfs final"),
            new Legacy("`dfs:`*n*", "dfs:5 final"), new Legacy("`linear`", "linear final"),
            new Legacy("`random`", "random final"), new Legacy("`state`", "state final"),
            new Legacy("`crule:`[`!`]*rule*", "crule:done final"),
            new Legacy("`cnbound:`*n*", "cnbound:20 final"),
            new Legacy("`cebound:`*label*`>`*n*`,`…", "cebound:item>3,flag:busy>1 final"),
            new Legacy("`uptorule:`(`bfs`&#124;`dfs`)`->`[`!`]*rule*", "uptorule:dfs->done final"),
            new Legacy("`uptorule:`(`bfs`&#124;`dfs`)`=>`[`!`]*rule*",
                "uptorule:bfs=>!done final"),
            new Legacy("`ltl:`*formula*", "ltl:G(!error) cycle"),
            new Legacy("`ltlbounded:`*bound*`;`*formula*", "ltlbounded:10,5;G(!error) cycle"),
            new Legacy("`ltlpocket:`*bound*`;`*formula*", "ltlpocket:grow;G(!error) cycle"),
            new Legacy("`final`", "bfs final"), new Legacy("`any`", "bfs any"),
            new Legacy("`none`", "bfs none"), new Legacy("`cycle`", "ltl:G(!error) cycle"),
            new Legacy("`ruleapp:`*rule*", "bfs ruleapp:done"),
            new Legacy("`inv:`*rule*", "bfs inv:error"),
            new Legacy("`inv:!`*rule*", "bfs inv:!safe"),
            new Legacy("`formula:`*formula*", "bfs formula:done&&!error"));
        for (Legacy l : legacy) {
            String equivalent;
            try {
                ExploreType type = LegacySyntaxParser.parse(l.example());
                if (type instanceof ConfiguredExploreType configured) {
                    String text = configured.getConfig().unparse();
                    ExploreConfig.parse(text);
                    equivalent = text.isEmpty()
                        ? "*(the default configuration)*"
                        : code(text);
                } else if (type instanceof LTLExploreType) {
                    String id = type.getIdentifier();
                    var kind = Arrays.stream(LTLExploreType.Kind.values())
                        .filter(k -> id.startsWith(k.getKeyword() + ":"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                            "No LTL kind for exploration type '" + id + "'"));
                    equivalent = code(id) + " (not configuration-based: "
                        + cell(kind.getDescription()) + ")";
                } else {
                    throw new IllegalStateException("Legacy example '" + l.example()
                        + "' yields unexpected exploration type " + type.getClass().getName());
                }
            } catch (FormatException e) {
                throw new IllegalStateException(
                    "Legacy example '" + l.example() + "' does not parse: " + e.getMessage(), e);
            }
            b.append("| " + l.syntax() + " | " + code(l.example()) + " | " + equivalent + " |\n");
        }
        write("ref-exploration.md", b);
    }

    private void writeOperators() throws IOException {
        StringBuilder b = frontMatter("Reference: data operations", "manual_ref_operators.html",
            "All built-in operations on the data sorts", "attribute, operation, sort, reference");
        b.append(generatedNote());
        b.append("""
            The built-in operations per data sort, usable in the attribute expressions described \
            in the [Using data](manual_data.html) chapter. Operations with a \
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
