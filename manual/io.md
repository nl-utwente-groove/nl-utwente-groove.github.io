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
| System properties | `system.properties` |

The qualified (hierarchical) names of rules and other resources, discussed in [Graphs and rules](manual_basics.html#rule-names), map directly onto subdirectories: rule `a.b` is stored as `b.gpr` in subdirectory `a` of the `.gps` directory.

The system properties file is a plain-text Java properties file, with lines of the form `key = value` for the properties discussed in [Advanced rule features](manual_advanced.html#system-properties); the graph-based resources are GXL files as described below; control, Prolog and Groovy resources are plain text.

A grammar can also be loaded from a *zipped* `.gps` directory (a `.zip` or `.jar` file containing one), including directly from a URL; the archive is transparently unpacked. Saving works on the directory form only.

## The native graph format

Graphs, rules and type graphs are all stored in [GXL](http://www.gupro.de/GXL/) (Graph eXchange Language), an XML format for graph interchange; the extension (`.gst`, `.gpr`, `.gty`, or plain `.gxl` for standalone graphs) determines the role. The encoding follows a few GROOVE-specific conventions:

- **Edge labels** are stored as GXL edge attributes of type `String`, containing the *full* label as seen in the edit view — including all aspect prefixes. Node labels (types, flags) are stored as self-edges, in line with their interpretation (see [Graphs and rules](manual_basics.html#graphs)).
- **Graph attributes** store additional per-resource information, such as the graph role and version, and rule properties like priority and enabledness.
- **Layout** is stored in dedicated node and edge attributes; the encoding is ad hoc, based on the internal representation of the JGraph rendering library.

Standalone `.gxl` graph files (outside any grammar) can be viewed with the Viewer and converted with the Imager.

## Importing and exporting

The File menu of the Simulator offers *Import* and *Export* actions. Importing adds a resource to the current grammar; exporting saves the resource (or the graph currently displayed) in an external format. The supported formats:

| Format | Extension(s) | Direction | Notes |
| :--- | :--- | :--- | :--- |
| Native resources | `.gst`, `.gpr`, `.gty`, `.gxl` | import + export | Exchange individual rules, graphs and type graphs between grammars |
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

The image, vector and TikZ exporters render the graph *as displayed*, with all visual embellishments — they apply to whatever graph panel is currently shown, including the LTS panel. (In fact, all the SVG figures in this manual are produced this way, via the Imager.)

### Ecore/EMF exchange

The Ecore porter connects GROOVE to the Eclipse Modeling Framework: an Ecore meta-model corresponds to a GROOVE type graph, and an XMI instance model to a host graph conforming to it. The translation runs through an intermediate conceptual representation, so not every construct of either side has an exact counterpart; the porter reports constructs it cannot translate. Two system properties tune the instance-model translation (see [Advanced rule features](manual_advanced.html#system-properties)):

- `ecoreOrdering` controls how *ordered* or non-unique many-valued features are encoded: not at all (`none`, the default — the order is lost), or through intermediate nodes carrying an `index` attribute (`index`);
- `ecoreUseIdentifiers` controls whether `xmi:id` values are retained as GROOVE node identifiers.

This makes it possible to use GROOVE as a *model transformation* engine for EMF models: import the meta-model and instance, transform by rules, and export the result. The `rhsIsNAC` and `typePolicy` system properties are natural companions in such a setup.

### Saving the transition system

The explored state space itself can be saved from the Simulator (as a GXL graph, via the LTS panel) or, more flexibly, by the Generator's `-o` option, which also controls special state labels and can restrict the output to a spanning tree or to result traces; see [Exploration and verification](manual_verification.html#the-generator).

## The Imager

The Imager converts GROOVE graphs and rules to image formats without opening the Simulator. It can be started with a GUI (double-click, or no arguments) or used from the command line:

    java -jar Imager.jar -f svg mygraph.gst mygraph.svg

Options include `-f` to select the output format (from the image, vector and TikZ formats listed above, among others) and `-e` to render the *editor* view instead of the display view. When given a directory (for instance a whole `.gps`), the Imager converts all graph files in it. Invoke `Imager -h` for the full list of options.
