import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import nl.utwente.groove.algebra.Operator;
import nl.utwente.groove.algebra.Sort;
import nl.utwente.groove.control.parse.CtrlDoc;
import nl.utwente.groove.explore.Generator;
import nl.utwente.groove.explore.feature.Bound;
import nl.utwente.groove.explore.feature.ExploreKey;
import nl.utwente.groove.explore.feature.Goal;
import nl.utwente.groove.explore.feature.Setting;
import nl.utwente.groove.grammar.aspect.AspectKind;
import nl.utwente.groove.graph.GraphRole;
import nl.utwente.groove.util.Version;

/**
 * Generates the reference pages of the web manual from the GROOVE sources.
 * Invoked by make-refs.sh; see that script for usage.
 *
 * Arguments: [0] the output directory; [1] optionally the GROOVE version
 * documented, which defaults to the version of the build on the classpath.
 */
public class MakeRefs {
    public static void main(String[] args) throws IOException {
        Path outDir = Path.of(args[0]);
        String version = args.length > 1
            ? args[1]
            : Version.getCurrentGrooveVersion();
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
        StringBuilder b = frontMatter("Reference: exploration keys", "manual_ref_exploration.html",
            "The keys and values of the exploration configuration, and the legacy -s/-a options",
            "exploration, configuration, strategy, acceptor, reference");
        b.append(generatedNote());
        b.append("""
            An exploration is configured by choosing a value for each of the *keys* below. Every key \
            has a default (marked *), so only deviations need to be given. The vocabulary is shared \
            by the exploration dialog of the Simulator, the `explore` settings resources of a grammar \
            and the `-x` option of the Generator, where a configuration is written as a \
            space-separated list of `key=value` pairs; see the \
            [Exploration and verification](manual_verification.html) chapter. A value is the name \
            of one of the alternatives of the key; if the alternative takes *content*, that follows \
            the name after a colon, as in `bound=nodes:200`. Where the content column says so, the \
            content may also be given on its own, as in `count=5`.

            ## Overview

            | Key | Values | Meaning |
            | :--- | :--- | :--- |
            """);
        for (ExploreKey key : ExploreKey.values()) {
            StringBuilder values = new StringBuilder();
            for (Setting.Kind kind : kindsOf(key)) {
                if (values.length() > 0) {
                    values.append(", ");
                }
                values.append(valueForm(key, kind));
            }
            b.append("| [`" + key.getName() + "`](#" + key.getName() + ") | " + values + " | "
                + cell(key.getExplanation()) + " |\n");
        }
        b.append("\n");
        for (ExploreKey key : ExploreKey.values()) {
            b.append("## `" + key.getName() + "`\n\n");
            b.append(cell(key.getExplanation()) + ".\n\n");
            b.append("| Value | Content | Description |\n| :--- | :--- | :--- |\n");
            for (Setting.Kind kind : kindsOf(key)) {
                b.append("| " + valueName(key, kind) + " | " + content(key, kind) + " | "
                    + cell(kind.getExplanation()) + " |\n");
            }
            b.append("\n");
        }
        b.append("""
            ## Legacy options `-s` and `-a`

            The Generator's options `-s` (strategy), `-a` (acceptor) and `-r` (result count) \
            predate the configuration keys and are kept indefinitely as shorthand; a combination of \
            them stands for a configuration in the vocabulary above, which the Generator reports \
            when it runs. Their vocabulary is given by their usage texts:

            ```
            -s <strgy>
            """);
        b.append(usage(Generator.STRATEGY_USAGE));
        b.append("""
            ```

            ```
            -a <acc>
            """);
        b.append(usage(Generator.ACCEPTOR_USAGE));
        b.append("""
            ```

            ```
            -r <num>
            """);
        b.append(usage(Generator.RESULT_USAGE));
        b.append("```\n");
        write("ref-exploration.md", b);
    }

    /** Returns the alternatives of a key, in declaration order, minus the ones not documented. */
    private static List<Setting.Kind> kindsOf(ExploreKey key) {
        Setting.Kind[] kinds = key.getKindType().getEnumConstants();
        return List.of(kinds).stream().filter(k -> !OMITTED.contains(k)).toList();
    }

    /** Alternatives that exist in the code but are not part of the released feature set. */
    private static final List<Setting.Kind> OMITTED = List.of(Goal.GRAPH);

    /** Renders the name of an alternative, with the default marker and the hint if any. */
    private static String valueName(ExploreKey key, Setting.Kind kind) {
        String result = "`" + kind.getName() + "`";
        if (kind == key.getDefaultKind()) {
            result += "\\*";
        }
        if (kind.getHint() != null) {
            result += " (" + kind.getHint() + ")";
        }
        return result;
    }

    /** Renders the textual form of an alternative for the overview: its name, with a
     * placeholder for its content if it takes any, or the placeholder alone if the name
     * may be omitted. */
    private static String valueForm(ExploreKey key, Setting.Kind kind) {
        String placeholder;
        if (kind == Bound.EDGES) {
            placeholder = "bounds";
        } else if (kind == Bound.UPTO || kind == Bound.INCLUDE) {
            placeholder = "rule";
        } else if (kind == Goal.FIRES) {
            placeholder = "action";
        } else {
            placeholder = switch (kind.contentType()) {
            case NULL -> null;
            case INTEGER, LONG -> "n";
            case LIMIT -> "max";
            case STRING -> "formula";
            };
        }
        String result;
        if (placeholder == null) {
            result = "`" + kind.getName() + "`";
        } else if (key.getKindMap().get("") == kind) {
            result = "*" + placeholder + "*";
        } else {
            result = "`" + kind.getName() + ":`*" + placeholder + "*";
        }
        if (kind == key.getDefaultKind()) {
            result += "\\*";
        }
        return result;
    }

    /** Describes the content an alternative takes; mirrors the hints of the exploration dialog. */
    private static String content(ExploreKey key, Setting.Kind kind) {
        String result;
        if (kind == Bound.EDGES) {
            result = "a comma-separated list of *label*`>`*bound* pairs, e.g. `a>2,type:B>3`;"
                + " node type and flag labels carry their `type:` or `flag:` prefix";
        } else if (kind == Bound.UPTO || kind == Bound.INCLUDE) {
            result = "a rule name, optionally negated by a `!` prefix";
        } else if (kind == Goal.CONDITION) {
            result = "a propositional formula over rule names, built with `!`, `&&`, `||` and `->`";
        } else if (kind == Goal.FIRES) {
            result = "the name of a rule or recipe";
        } else if (kind == Goal.LTL) {
            result = "an LTL formula";
        } else if (kind == Goal.CTL) {
            result = "a CTL formula";
        } else {
            result = switch (kind.contentType()) {
            case NULL -> "&mdash;";
            case INTEGER -> key == ExploreKey.FRONTIER
                ? "the maximal frontier size (at least 2)"
                : "the number of results (at least 2)";
            case LONG -> "a 64-bit seed value, e.g. as recorded in a saved LTS";
            case LIMIT -> "a maximum *max*, optionally followed by `+`*inc* for iterative"
                + " deepening, e.g. `200` or `200+50`";
            case STRING -> "a name";
            };
        }
        if (key.getKindMap().get("") == kind) {
            result += "; the name may be omitted";
        }
        return result;
    }

    /** Renders a picocli usage text as code block content, without trailing blanks. */
    private static String usage(String text) {
        StringBuilder result = new StringBuilder();
        for (String line : text.split("\n")) {
            result.append(line.stripTrailing()).append('\n');
        }
        return result.toString();
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
