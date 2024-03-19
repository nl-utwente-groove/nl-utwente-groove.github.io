---
title:
    "GROOVE home page"
summary:
    "First impressions: key capabilities, runnable components and screenshots"
permalink:
    index.html
summary:
sidebar:
    home_sidebar
toc: 
    true
last_updated:
    false
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
tags:      # need to be included in _data/tags_doc.yml and have a page in tags/
keywords:  # used in metadata for findability
    home screenshot
---

## Introduction

GROOVE is a modelling and verification tool that will enable you to quickly and easily create graph transformation rules and apply them.
Its strength lies in state space generation: once you have defined a start graph and a set of transformation
rules, you can automatically apply the rules iteratively, generating all reachable graphs (or at least a finite
number of them). This results in a (state) space of graphs, the analysis of which can help to establish correctness of the modelled system.

Beyond "simple" graphs and rules, GROOVE has some very powerful capabilities:

1. _Automatic isomorphism detection._
2. _Attribute manipulation._
3. _Nested graph conditions and transformation._
4. _Controlled rule application._
5. _Advanced state space exploration strategies._
6. _Model checking._

## <a name="runnable">Runnable components</a>

The GROOVE tool set has a number of runnable components:

- **Simulator.** A GUI that allows creation and graphical editing of Graph Transformation Systems (GTSs), and integrates the functionality of the Generator (see below). In addition, the Simulator allows the user to interactively explore the state space, by manually applying rules to a host graph.

- **Generator.** A command line tool that generates the state space of an existing GTS. The state space is stored as a Labelled Transition System (LTS), where each state is a graph and transitions are labelled by the rule applications. The strategy according to which the state space is explored (e.g., depth-first, breadth-first, etc) can be set as a parameter.

- **Viewer.** A GUI that allows viewing of individual graphs (host graphs, rules or type graphs) outside the context of a GTS.

- **Imager.** A tool that converts individual graphs (host graphs, rules or type graphs) to an image format such as JPG, GIF, SVG or PDF, which can in turn be included into other documents. The Imager can be invoked as a command-line tool or with a GUI.

<!--- Not available right now
    Model Checker. A command line tool that checks if properties expressed in CTL temporal logic hold in a state space model produced by the Generator.
--->

## <a name="screenshots">Screenshots</a>

Here are some screenshots of the Simulator, just to give you a first idea of the capabilities of GROOVE:

<figure>
    <img src="/images/solitaire.png"
         alt="Game board for solitaire">
    <figcaption>Graph representation of a solitaire board</figcaption>
</figure>

<figure>
    <img src="/images/append.png"
         alt="State space of concurrent list append">
    <figcaption>State space of two concurrent list append actions</figcaption>
</figure>

<figure>
    <img src="/images/sierpinski-5.png"
         alt="Sierpinski triangle, 5th iteration">
    <figcaption>Sierpinski triangle arising as the result of a repeated (5x) rule application</figcaption>
</figure>
