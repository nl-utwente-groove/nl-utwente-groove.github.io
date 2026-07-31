---
title: # required
    "Introduction"
permalink: # required, must match filename.html
    manual_introduction.html
summary:
    "What GROOVE is, the concepts behind it, and the components of the tool set"
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
    introduction, concepts, components, terminology
---

## What is GROOVE?

GROOVE is a tool for modelling the structure and behaviour of systems as *graphs* and *graph transformation rules*. The states of a system are captured as graphs; the dynamics — anything that changes those states — are captured as rules that match a part of a graph and modify it. Given a start graph and a set of rules, GROOVE can apply the rules interactively (simulation) or exhaustively (state space exploration), producing a transition system in which every state is itself a graph. That transition system can then be analysed, for instance through model checking of temporal-logic properties.

This approach is a good fit for any system with a natural graph-like structure and discrete dynamics: object-oriented designs, network protocols, games and puzzles, dynamic data structures, biological or social networks, and the operational semantics of programming languages, among others.

GROOVE is developed at the University of Twente and distributed as open source under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

## Concepts and terminology

The following terms are used throughout this manual and in the tool itself.

- **Graph.** GROOVE works with directed, edge-labelled graphs. Nodes have no identity of their own visible in the visual representation; all information is in the (labelled) edges. Node labels as you see them on screen are in fact self-edges, displayed inside the node for readability. Graphs may in addition be *typed* (conform to a type graph) and carry *attributes* (data values such as integers and strings).

- **Host graph.** A graph representing a state of the modelled system. One host graph is designated the *start graph*, the initial state of the exploration.

- **Rule.** A transformation rule specifies a graph pattern to be matched in a host graph, together with the changes to be made when it is applied: elements to be deleted, elements to be created, and conditions that forbid application (so-called *negative application conditions*). In GROOVE, all of this is combined into a single rule graph, using colours and label prefixes (*aspects*) to distinguish the roles; see the [Graphs and rules](manual_basics.html) chapter.

- **Graph grammar.** The complete collection of resources that together define a transformation system: rules, host graph(s), and optionally type graphs, control programs, Prolog programs and configuration properties. On disk, a grammar is a directory with extension `.gps` ("graph production system") holding one file per resource.

- **Exploration.** The process of recursively applying all enabled rules to all reachable host graphs. The result is a *graph transition system* (GTS): a labelled transition system whose states are host graphs and whose transitions are rule applications. GROOVE collapses states that are isomorphic, which drastically reduces the size of the state space. The order and extent of exploration is determined by a selectable *exploration strategy*; see the [Exploration and verification](manual_verification.html) chapter.

- **Control.** By default, all rules are always enabled, and rule application order is arbitrary. A *control program* constrains this, using a small imperative language with sequencing, choice, loops and function/recipe definitions; see the [Control language](manual_control.html) chapter.

- **Verification.** Properties of the explored state space can be checked by model checking: temporal-logic formulae in CTL or LTL, in which the atomic propositions are rule applicability or rule applications; see the [Exploration and verification](manual_verification.html) chapter.

## The GROOVE tool set

A GROOVE installation offers the following runnable components:

- **Simulator.** The main, GUI-based tool. It lets you create and edit grammars (host graphs, rules, type graphs, control programs), apply rules interactively, explore and inspect the state space, and run model checking — all visually. If you are new to GROOVE, this is the place to start.

- **Generator.** A command-line tool for state space exploration, without the overhead (and memory footprint) of the GUI. Its principal use is for large explorations and for scripted or batch experiments. The exploration strategy and other options are set through command-line parameters.

- **ModelChecker.** A command-line tool that explores a grammar's state space and checks CTL formulae against it.

- **Viewer.** A stand-alone, read-only viewer for individual GROOVE graphs and rules, outside the context of a grammar in the Simulator.

- **Imager.** A converter from GROOVE graphs and rules to image formats (among others PNG, SVG, PDF and TikZ), for inclusion in documents. It can be invoked from the command line or through a small GUI.

Each component is available as a runnable jar in the `bin` subdirectory of the installation (`Simulator.jar`, `Generator.jar`, and so on); the command-line tools print a summary of their options when invoked with `-h`.

## Downloading and installing

Releases are published on [GitHub]({{site.groove_url}}/code/releases/latest); at the time of writing, the current release is 7.5.3, which requires Java 21 or higher. See the [installation page](installing.html) for detailed instructions, and the [Maven Central coordinates](https://central.sonatype.com/artifact/nl.utwente.groove/groove) if you want to use GROOVE as a library.

## How this manual is organised

- [Graphs and rules](manual_basics.html) explains how to read and edit host graphs and transformation rules — the day-to-day basics of working with the Simulator.
- [Advanced rule features](manual_advanced.html) covers data attributes, universally quantified (nested) rules, type graphs, and the available rule properties.
- [Control language](manual_control.html) describes the language for programming the order of rule applications.
- [Exploration and verification](manual_verification.html) covers exploration strategies, acceptors, and CTL/LTL model checking.
- [Import and export](manual_io.html) describes the supported exchange formats, including Ecore/EMF models.

## Getting help

- Within the Simulator, the editors provide context help: the right-hand panel of the graph and control editors lists the available syntax, with tooltips explaining each option.
- The [tutorials page](tutorials.html) links to a series of video tutorials.
- The [samples page](samples.html) describes ready-made example grammars that ship with the tool.
- For questions and bug reports, use the [GitHub issue tracker]({{site.groove_url}}/code/issues) or see the [contact page](contact.html).
