---
title: # required
    "GROOVE tool web site"
tags: # optional, need to be included in _data/tags_doc.yml and have a page in tags/
    [overview]
keywords: # optional, used in metadata for findability
    groove home
# last_updated: # optional, appears in footer
sidebar: # required
    home_sidebar
summary: # optional
    "This web site is the starting point for information on GROOVE, a stand-alone tool for graph transformation and verification" # optional
permalink: # required, must match filename.html
    index.html
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
toc: # optional, false to exclude from initial table of contents
    false
---

## Intro

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

Here are some screenshots, just to give you an idea:

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

## Downloading and installing

## Tutorials

## User manual

## Sample rule systems

## Developers

See the [list of contributors](./DEVELOPERS).

## License

See the [License File](./LICENSE.md).
