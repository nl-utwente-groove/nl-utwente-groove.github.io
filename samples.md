---
title: # required
    "Sample GROOVE rule systems"
# tags: # optional, need to be included in _data/tags_doc.yml and have a page in tags/
# 	[overview]
# keywords: # optional, used in metadata for findability
# 	groove home
# last_updated: # optional, appears in footer
sidebar: # required
    home_sidebar
# summary: # optional
permalink: # required, must match filename.html
    samples.html
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
toc: # optional, false to exclude from initial table of contents
    false
---
{% assign groove_url = "https://github.com/nl-utwente-groove" %}
{% capture samples_url %}{{ groove_url }}/samples{% endcapture %}
{% capture samples_dir %}{{ samples_url }}/releases/latest/download{% endcapture %}

A number of sample Graph Transformaton Systems demonstrating the various features of GROOVE can be found (and downloaded collectively) at <{{ samples_url }}>. Here is a list.

- [attributed-graphs]({{ samples_dir }}/attributed-graphs.gps.zip). This demonstrates the syntax for attributed graphs and rules. GROOVE has a user syntax and a system syntax for attribute manipulation; though it is unlikely that you will need the latter, the rules are given in both versions.

- [attribute-types]({{ samples_dir }}/attribute-types.gps.zip). This shows that (attribute and normal) edges with the same name are distinguished by teir target type, and also that attribute edges can be individually deleted and created (like normal edges) but also _changed_ (which amounts to simultaneous deletion+creation, and is closer to the way we usually think of edges).

- [bridge.gps]({{ samples_dir }}/bridge.gps.zip). This is a _tour-de-force_ example of how to use a combination of Groovy and so-called "nodified edges". (The latter is a special rendering mode for nodes so they look more like edges.) Check the Groovy tab and the type graph to see how this is done.

- [circular-buffe]({{ samples_dir }}/circular-buffer.gps.zip). This is a prototypical rule system, used in many tutorials, showing the basic features of GROOVE: ordinary rules that delete, create and forbid nodes and edges, as well as the effect of unifying isomorphic states. The rule system models a first-in-first-out buffer.

- [colours.gps]({{ samples_dir }}/colours.gps.zip). This shows the _colouring_ feature of GROOVE: nodes can have a colour determined by their node type, or an individual colour set in the start graph or changed by a rule application. It should be noted that colours are not part of the graph structure, and so two graphs can be isomorphic despite being differently coloured.

- [control.gps]({{ samples_dir }}/control.gps.zip). A demonstration of rule control, without parameters, on the basis of a simple, three-rule model in which passengers get on and off trains, and trains move over a track. There are three different control programs that order the passenger transfer slightly differently.

- [copy-graph.gps]({{ samples_dir }}/copy-graph.gps.zip). This demonstrates three features: _nested rules_, _label variables_ and _conditional creation_. One rule, `copyNodes`, copies all nodes, using a special `copy`-edge to retain the connection between the originals and their copies. Node creation is conditional, using `cnew:` rather than `new:`, to ensure that only nodes that do not already have a copy get one. The rule is quantified non-vacuously so that if there is no node without a  copy, the rule does not get applied. A second rule, `copyEdges`, also non-vacuously quantified on the outer level, copies over all edges from the original nodes to their copies, using a label variable to cover all possible labels in one go; and it also deleted the special `copy`-edge introduced by `copyNodes`.

- [copy-graph-typed.gps]({{ samples_dir }}/copy-graph-typed.gps.zip). This is a variation on [copy-graph.gps]({{ samples_dir }}/copy-graph.gps.zip) above in which the graphs are _typed_. The difference is visible in several respects: there are _two_ type graphs, one called `type` which serves to type the copyable graphs themselves, and one called `copy` which contains the additional structure necessary to make the rule system work; notably, `copy` introduces a *`Top`* type of which all others are subtypes, as well as the special `copy` edge type used to connect the copy of a node with its original. Furthermore, the type labels of the nodes get created during the `copyNodes` rule, whereas the `copyEdges` rule also copies _flag_, apart from binary edges. Since we need to copy _both_ flags _and_ binary edges, this requires a conjunction of two universally quantified sub-rules; in order for that to work, we need an existential quantifier in between the outer and the inner universals - otherwise, the inner ones would be disjunctively interpreted, meaning that for all nodes, _either_ their outgoing edges _or_ their flags get copied.

- [counting.gps]({{ samples_dir }}/counting.gps.zip). This demonstrates a primitive kind of counting, achieved by local injectivity constraints (in this case, negative ones). A more advanced counting mechanism exists in the form of `count`-edges of universal quantifiers; see 


- [fibonacci.gps]({{ samples_dir }}/fibonacci.gps.zip).


- [import.gps]({{ samples_dir }}/import.gps.zip).


- [inheritance.gps]({{ samples_dir }}/inheritance.gps.zip).


- [injectivity.gps]({{ samples_dir }}/injectivity.gps.zip).


- [label-variables.gps]({{ samples_dir }}/label-variables.gps.zip).


- [loose-nodes.gps]({{ samples_dir }}/loose-nodes.gps.zip).


- [mergers.gps]({{ samples_dir }}/mergers.gps.zip).

- [ordered-list.gps]({{ samples_dir }}/ordered-list.gps.zip).

- [parameters.gps]({{ samples_dir }}/parameters.gps.zip).

- [priorities.gps]({{ samples_dir }}/priorities.gps.zip).

- [regexpr.gps]({{ samples_dir }}/regexpr.gps.zip).

- [wildcards.gps]({{ samples_dir }}/wildcards.gps.zip).

