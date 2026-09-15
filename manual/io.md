---
title: # required
    "Import and export"
permalink: # required, must match filename.html
    manual_io.html
summary:
    "Storage formats, and exchanging graphs and grammars with other tools"
sidebar:
    manual_sidebar
toc: 
    true
last_updated:
    false
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
tags:      # need to be included in _data/tags_doc.yml and have a page in tags/
keywords:  # used in metadata for findability
    import, export, GXL, Ecore, EMF, XMI, image, TikZ, dot, aut, storage
---

This chapter describes how GROOVE stores grammars and graphs on disk, and how models can be exchanged with other tools: importing and exporting individual resources, foreign model formats such as Ecore/EMF, and rendering graphs to image and document formats.

## Grammars on disk

A graph grammar is stored as a directory with extension `.gps` (for *graph production system*), containing one file per resource:

| Resource | Extension |
| :--- | :--- |
| Rule | `.gpr` (*graph production rule*) |
| Host graph | `.gst` (*graph state*) |
| Type graph | `.gty` (*graph type*) |
| Control program | `.gcp` (*graph control program*) |
| Prolog program | `.pro` or `.pl` |
| Groovy script | `.groovy` |
| Settings | `.properties`, in a subdirectory named after the schema (`explore`, `ecore`) |
| System properties | `system.properties` |

The qualified (hierarchical) names of rules and other resources, discussed in [Graphs and rules](manual_basics.html#rule-names), map directly onto subdirectories: rule `a.b` is stored as `b.gpr` in subdirectory `a` of the `.gps` directory.

The system properties file is a plain-text Java properties file, with lines of the form `key = value` for the properties discussed in [Advanced rule features](manual_advanced.html#system-properties); the graph-based resources are GXL files as described below; control, Prolog and Groovy resources are plain text. Before GROOVE 8.0.0 the properties file was named after the grammar, as `<grammar name>.properties`; such a file is still read, and renamed to `system.properties` the next time the grammar is saved.

*Settings resources* are properties files as well, but with a *schema* that fixes the admissible keys and values, and they are checked against it, with errors reported per line. The schema is given by the subdirectory a settings resource lives in: `explore` for exploration configurations (see [Exploration and verification](manual_verification.html#exploration-in-the-simulator)) and `ecore` for the Ecore import and export options (see [below](#ecoreemf-exchange)). The Simulator lists them under the Settings tab, where new ones are created from a template of the schema; the names `system.properties` and `system` are reserved.

A grammar can also be loaded from a *zipped* `.gps` directory (a `.zip` or `.jar` file containing one), including directly from a URL; the archive is transparently unpacked. Saving works on the directory form only.

## The native graph format

Graphs, rules and type graphs are all stored in [GXL](http://www.gupro.de/GXL/) (Graph eXchange Language), an XML format for graph interchange; the extension (`.gst`, `.gpr`, `.gty`, or plain `.gxl` for standalone graphs) determines the role. The encoding follows a few GROOVE-specific conventions:

- **Edge labels** are stored as GXL edge attributes of type `String`, containing the *full* label as seen in the edit view — including all aspect prefixes. Node labels (types, flags) are stored as self-edges, in line with their interpretation (see [Graphs and rules](manual_basics.html#graphs)).
- **Graph attributes** store additional per-resource information, such as the graph role and version, and rule properties like priority and enabledness.
- **Layout** is stored in dedicated node and edge attributes: node bounds, edge bend points and label positions, in an ad hoc textual encoding that is independent of the rendering backend.
- **Parallel edges** (see [Graphs and rules](manual_basics.html#parallel-edges)) are stored as separate GXL edges; the `edgeids` attribute of the `graph` element records whether the graph is a multigraph. Simple graphs are written exactly as before 8.0.0, so their files stay readable by older versions.

Standalone `.gxl` graph files (outside any grammar) can be viewed with the Viewer and converted with the Imager.

## Importing and exporting

The File menu of the Simulator offers *Import* and *Export* actions. Importing adds a resource to the current grammar; exporting saves the resource (or the graph currently displayed) in an external format. The supported formats:

| Format | Extension(s) | Direction | Notes |
| :--- | :--- | :--- | :--- |
| Native resources | `.gst`, `.gpr`, `.gty`, `.gxl`, `.gcp`, `.pro`, `.groovy`, `.properties` | import + export | Exchange individual rules, graphs, type graphs, text resources and settings between grammars |
| Ecore meta-models | `.ecore` | import + export | Converted to/from GROOVE *type graphs* |
| XMI instance models | `.xmi` | import + export | Converted to/from GROOVE *host graphs*, against the corresponding Ecore meta-model |
| CADP automata | `.aut` | import + export | Graphs and transition systems in the CADP `.aut` format |
| DIMACS graphs | `.col` | import only | Graph-colouring benchmark format |
| FSM | `.fsm` | export only | Transition systems in the FSM layout format |
| GraphViz | `.dot` | export only | Graph structure for GraphViz processing |
| Raster images | `.png`, `.jpg` | export only | Renders the graph as displayed |
| Vector graphics | `.eps`, `.pdf`, `.svg` | export only | Renders the graph as displayed |
| LaTeX | `.tikz` | export only | TikZ code for inclusion in LaTeX documents (requires the `groove2tikz` style) |
| Control program | `.gcp` | export only | Converts an explored LTS into a control program that enforces exactly its transitions |

The image, vector and TikZ exporters render the graph *as displayed*, with all visual embellishments — they apply to whatever graph panel is currently shown, including the LTS panel. (In fact, all the SVG figures in this manual are produced this way, via the Imager.) These three are available in the Simulator and the Imager only; the other formats are also produced headlessly, by the Generator's `-o` and `-f` options. Text formats (`.aut`, `.gxl`, `.fsm`, `.dot`, control programs) are written in UTF-8 with LF line ends on every platform.

### Ecore/EMF exchange

The Ecore porter connects GROOVE to the Eclipse Modeling Framework: an Ecore meta-model imports as a GROOVE type graph, and an XMI instance model as a host graph conforming to it — importing the instance model brings its meta-model along, as the type graph is needed to make sense of the instance. Both export again: a type graph as an `.ecore` file and a host graph as an `.xmi` file, the latter against the grammar's active type graphs, so the `.ecore` file has to be exported next to it for the result to be importable. The translation is direct: classes become node types, attributes and references become edges, multiplicities become `out=`*lo*`..`*hi*`:` edge multiplicities, containment references become `part:` edges, and abstract classes and enums become abstract types (`abs:`), the literals of an enum being subtypes (`sub:`) of it. Custom data types become strings. Constructs that the encoding cannot represent at all, such as a reference to a class outside the imported packages, are reported as errors.

The options of the translation live in an `ecore` settings resource (see [above](#grammars-on-disk)), which the import and export actions offer to edit in a dialog:

- `ordering` controls how *ordered* or non-unique many-valued features are encoded: as plain edges (`none`, the default — the order and duplicates are lost), or through intermediate nodes carrying an `index` attribute (`index`); the setting can be overridden per feature, as `class.feature.ordering`;
- `useIdentifiers` controls whether `xmi:id` values are retained as GROOVE node identifiers (`id:` aspects), and regenerated on export;
- `classifier.typeName`, `enum.literalStyle` and `enum.literal.typeName` rename a type, choose between qualified and plain enum literal names, and rename a single literal.

This makes it possible to use GROOVE as a *model transformation* engine for EMF models: import the meta-model and instance, transform by rules, and export the result. The `rhsIsNAC` and `typePolicy` system properties are natural companions in such a setup.

### Saving the transition system

The explored state space itself can be saved from the Simulator (as a GXL graph, via the LTS panel) or, more flexibly, by the Generator's `-o` option, which also controls special state labels and can restrict the output to a spanning tree or to result traces; see [Exploration and verification](manual_verification.html#the-generator). The format follows from the extension of the file name: `.gxl` (the default if there is none), `.aut`, `.fsm`, `.dot` or `.gcp`; result states saved with `-f` default to `.gst`. Large state spaces are written through a streaming view rather than a copy, so saving needs little memory beyond the state space itself. The control program produced by the `.gcp` export enforces exactly the transitions of the LTS; node-valued rule arguments are rendered as `_`, so the program compiles.

## The Imager

The Imager converts GROOVE graphs and rules to image formats without opening the Simulator. It can be started with a GUI (double-click, or no arguments) or used from the command line:

    java -jar Imager.jar -f svg mygraph.gst mygraph.svg

Options include `-f` to select the output format (from the image, vector and TikZ formats listed above, among others), `-e` to render the *editor* view instead of the display view, and `-b` to select the graph backend to render with (`jgraph` or `yfiles`; see the [installation page](installing.html#the-yfiles-add-on)), falling back to the default backend with a warning if the requested one is not installed. When given a directory (for instance a whole `.gps`), the Imager converts all graph files in it. Invoke `Imager -h` for the full list of options.
