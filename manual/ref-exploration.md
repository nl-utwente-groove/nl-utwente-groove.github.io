---
title:
    "Reference: exploration keys"
permalink:
    manual_ref_exploration.html
summary:
    "The keys and values of the exploration configuration, and the legacy -s/-a options"
sidebar:
    manual_sidebar
toc:
    true
last_updated:
    false
datatable:
    false
tags:
keywords:
    exploration, configuration, strategy, acceptor, reference
---

*This page is generated from the GROOVE 8.0.0 source code by `manual/make-refs.sh`; do not edit it by hand.*

An exploration is configured by choosing a value for each of the *keys* below. Every key has a default (marked *), so only deviations need to be given. The vocabulary is shared by the exploration dialog of the Simulator, the `explore` settings resources of a grammar and the `-x` option of the Generator, where a configuration is written as a space-separated list of `key=value` pairs; see the [Exploration and verification](manual_verification.html) chapter. A value is the name of one of the alternatives of the key; if the alternative takes *content*, that follows the name after a colon, as in `bound=nodes:200`. Where the content column says so, the content may also be given on its own, as in `count=5`.

## Overview

| Key | Values | Meaning |
| :--- | :--- | :--- |
| [`next`](#next) | `oldest`\*, `newest`, `random` | Selection of the next state to be explored |
| [`successor`](#successor) | `all`\*, `all-random`, `single`, `single-random` | Selection of the successor states to be generated |
| [`frontier`](#frontier) | `complete`\*, `single`, *n* | Size restriction on the exploration frontier |
| [`heuristic`](#heuristic) | `none`\*, `nen` | Quality function guiding the selection of the next state |
| [`cost`](#cost) | `none`\*, `uniform`, `rule` | Cost of a single transition |
| [`goal`](#goal) | `none`, `any`, `final`\*, `condition:`*formula*, `fires:`*action* | Condition determining when a result has been found |
| [`outcome`](#outcome) | `satisfy`\*, `violate` | Whether the goal condition is to be satisfied or violated |
| [`shape`](#shape) | `state`\*, `trace` | Shape of the results the exploration yields |
| [`count`](#count) | `all`\*, `first`, *n* | Number of results after which exploration halts |
| [`bound`](#bound) | `none`\*, `initial`, `cost:`*max*, `size:`*max*, `nodes:`*max*, `edges:`*bounds*, `upto:`*rule*, `include:`*rule* | Bound on the states to be explored |
| [`persistence`](#persistence) | `all`\*, `none` | Degree to which discovered states are stored in the GTS |
| [`collapse`](#collapse) | `grammar`\*, `equality`, `isomorphism`, `hash` | Condition under which a fresh state is considered equal to a known state |
| [`algebra`](#algebra) | `grammar`\*, `default`, `big`, `point`, `term` | Interpretation of data values |
| [`seed`](#seed) | `auto`\*, *n* | Master random seed governing the randomised features |

## `next`

Selection of the next state to be explored.

| Value | Content | Description |
| :--- | :--- | :--- |
| `oldest`\* (bfs) | &mdash; | The oldest state in the frontier is explored next (breadth-first search) |
| `newest` (dfs) | &mdash; | The newest state in the frontier is explored next (depth-first search) |
| `random` | &mdash; | A random state in the frontier is explored next |

## `successor`

Selection of the successor states to be generated.

| Value | Content | Description |
| :--- | :--- | :--- |
| `all`\* | &mdash; | All unexplored successor states are generated, in their natural order |
| `all-random` | &mdash; | All unexplored successor states are generated, in random order |
| `single` | &mdash; | Only the first unexplored successor state is generated |
| `single-random` | &mdash; | Only a single, randomly chosen unexplored successor state is generated |

## `frontier`

Size restriction on the exploration frontier.

| Value | Content | Description |
| :--- | :--- | :--- |
| `complete`\* | &mdash; | No states are dropped from the frontier |
| `single` | &mdash; | The frontier holds a single state, giving rise to a linear search |
| `beam` | the maximal frontier size (at least 2); the name may be omitted | The frontier is restricted to a given maximum size (beam search) |

## `heuristic`

Quality function guiding the selection of the next state.

| Value | Content | Description |
| :--- | :--- | :--- |
| `none`\* | &mdash; | No heuristic is used; all states have the same quality |
| `nen` | &mdash; | Count of node/edge/node tuples missing with respect to the goal (requires the goal to be a complete graph or unnested, NAC-free rule) |

## `cost`

Cost of a single transition.

| Value | Content | Description |
| :--- | :--- | :--- |
| `none`\* | &mdash; | Transitions have no cost; the cost of a path is ignored |
| `uniform` | &mdash; | Every transition has cost 1, so the cost of a path equals its length |
| `rule` | &mdash; | Each rule application has a cost, fixed per rule or exposed as a rule parameter |

## `goal`

Condition determining when a result has been found.

| Value | Content | Description |
| :--- | :--- | :--- |
| `none` | &mdash; | There is no goal; exploration continues until the frontier is empty |
| `any` | &mdash; | Every state is a result |
| `final`\* | &mdash; | A state without outgoing transitions |
| `condition` | a propositional formula over rule names, built with `!`, `&&`, `||` and `->` | A state whose graph satisfies a propositional condition over rule names (regardless of whether the rules are scheduled) |
| `fires` | the name of a rule or recipe | A state in which the named action (a rule or recipe) fires, as scheduled |

## `outcome`

Whether the goal condition is to be satisfied or violated.

| Value | Content | Description |
| :--- | :--- | :--- |
| `satisfy`\* | &mdash; | A result satisfies the goal condition |
| `violate` | &mdash; | A result violates the goal condition |

## `shape`

Shape of the results the exploration yields.

| Value | Content | Description |
| :--- | :--- | :--- |
| `state`\* | &mdash; | A result is a state |
| `trace` | &mdash; | A result is a trace from the start state to a goal state |

## `count`

Number of results after which exploration halts.

| Value | Content | Description |
| :--- | :--- | :--- |
| `all`\* | &mdash; | Exploration continues until the entire state space has been explored |
| `first` | &mdash; | Exploration stops as soon as a single result has been found |
| `value` | the number of results (at least 2); the name may be omitted | Exploration stops as soon as the given number of results has been found |

## `bound`

Bound on the states to be explored.

| Value | Content | Description |
| :--- | :--- | :--- |
| `none`\* | &mdash; | There is no bound; the state space is explored up to arbitrary depth |
| `initial` | &mdash; | Only the initial state is explored, and no further states are |
| `cost` | a maximum *max*, optionally followed by `+`*inc* for iterative deepening, e.g. `200` or `200+50` | States are bounded by the cost of the path leading to them |
| `size` | a maximum *max*, optionally followed by `+`*inc* for iterative deepening, e.g. `200` or `200+50` | States are bounded by the total number of their graph elements |
| `nodes` | a maximum *max*, optionally followed by `+`*inc* for iterative deepening, e.g. `200` or `200+50` | States are bounded by their number of nodes |
| `edges` | a comma-separated list of *label*`>`*bound* pairs, e.g. `a>2,type:B>3`; node type and flag labels carry their `type:` or `flag:` prefix | States are bounded by their number of edges of given types (a comma-separated list of label&gt;bound pairs; labels may carry a type: or flag: prefix) |
| `upto` | a rule name, optionally negated by a `!` prefix | States satisfying the named rule condition (negated if prefixed with '!') are not explored |
| `include` | a rule name, optionally negated by a `!` prefix | States satisfying the named rule condition (negated if prefixed with '!') are the last to be explored |

## `persistence`

Degree to which discovered states are stored in the GTS.

| Value | Content | Description |
| :--- | :--- | :--- |
| `all`\* | &mdash; | All discovered states are stored in the GTS |
| `none` | &mdash; | No states are stored in the GTS |

## `collapse`

Condition under which a fresh state is considered equal to a known state.

| Value | Content | Description |
| :--- | :--- | :--- |
| `grammar`\* | &mdash; | As determined by the grammar property 'checkIsomorphism' |
| `equality` | &mdash; | Graphs are compared for equality (the strongest condition) |
| `isomorphism` | &mdash; | Graphs are compared up to isomorphism |
| `hash` | &mdash; | Graphs are considered equivalent if their isomorphism hash codes coincide (may collapse non-isomorphic states) |

## `algebra`

Interpretation of data values.

| Value | Content | Description |
| :--- | :--- | :--- |
| `grammar`\* | &mdash; | As determined by the grammar property 'algebraFamily' |
| `default` | &mdash; | Java-based representation of data values |
| `big` | &mdash; | Arbitrary-precision representation of data values |
| `point` | &mdash; | All data values are collapsed to a single point |
| `term` | &mdash; | Data values are represented symbolically, as terms |

## `seed`

Master random seed governing the randomised features.

| Value | Content | Description |
| :--- | :--- | :--- |
| `auto`\* | &mdash; | The master seed is left as it is: the -seed option or the groove.randomSeed system property if given, a freshly generated (and reported) seed otherwise |
| `value` | a 64-bit seed value, e.g. as recorded in a saved LTS; the name may be omitted | The given value becomes the master seed for the rest of the session, making the randomised features of this and subsequent explorations reproducible |

## Legacy options `-s` and `-a`

The Generator's options `-s` (strategy), `-a` (acceptor) and `-r` (result count) predate the configuration keys (up to release 7.5.3 they were the only way to give an exploration, as was the `explorationStrategy` grammar property) and are kept indefinitely as shorthand. A combination of them stands for a configuration in the vocabulary above, which the Generator reports when it runs; the LTL model checking strategies are the exception, as they are not configuration-based. The table lists every keyword of `-s` and `-a` with an example of a *strategy* *acceptor* pair and the configuration it translates to.

| Legacy keyword | Example | Equivalent configuration |
| :--- | :--- | :--- |
| `bfs` | <code>bfs final</code> | *(the default configuration)* |
| `bfs:`*n* | <code>bfs:5 final</code> | <code>cost=uniform bound=cost:5</code> |
| `dfs` | <code>dfs final</code> | <code>next=newest</code> |
| `dfs:`*n* | <code>dfs:5 final</code> | <code>next=newest cost=uniform bound=cost:5</code> |
| `linear` | <code>linear final</code> | <code>successor=single frontier=single</code> |
| `random` | <code>random final</code> | <code>successor=single-random frontier=single</code> |
| `state` | <code>state final</code> | <code>bound=initial</code> |
| `crule:`[`!`]*rule* | <code>crule:done final</code> | <code>bound=upto:done</code> |
| `cnbound:`*n* | <code>cnbound:20 final</code> | <code>bound=nodes:20</code> |
| `cebound:`*label*`>`*n*`,`… | <code>cebound:item&gt;3,flag:busy&gt;1 final</code> | <code>bound=edges:item&gt;3,flag:busy&gt;1</code> |
| `uptorule:`(`bfs`&#124;`dfs`)`->`[`!`]*rule* | <code>uptorule:dfs-&gt;done final</code> | <code>next=newest bound=upto:done</code> |
| `uptorule:`(`bfs`&#124;`dfs`)`=>`[`!`]*rule* | <code>uptorule:bfs=&gt;!done final</code> | <code>bound=include:!done</code> |
| `ltl:`*formula* | <code>ltl:G(!error) cycle</code> | <code>ltl:G(!error)</code> (not configuration-based: Nested Depth-First Search for a given LTL formula.) |
| `ltlbounded:`*bound*`;`*formula* | <code>ltlbounded:10,5;G(!error) cycle</code> | <code>ltlbounded:10,5;G(!error)</code> (not configuration-based: Nested Depth-First Search for a given LTL formula, using incremental bounds based on graph size or rule applications) |
| `ltlpocket:`*bound*`;`*formula* | <code>ltlpocket:grow;G(!error) cycle</code> | <code>ltlpocket:grow;G(!error)</code> (not configuration-based: Nested Depth-First Search for a given LTL formula, using incremental bounds based on graph size or rule applications and optimised to avoid reexploring connected components ('pockets')) |
| `final` | <code>bfs final</code> | *(the default configuration)* |
| `any` | <code>bfs any</code> | <code>goal=any</code> |
| `none` | <code>bfs none</code> | <code>goal=none</code> |
| `cycle` | <code>ltl:G(!error) cycle</code> | <code>ltl:G(!error)</code> (not configuration-based: Nested Depth-First Search for a given LTL formula.) |
| `ruleapp:`*rule* | <code>bfs ruleapp:done</code> | <code>goal=fires:done</code> |
| `inv:`*rule* | <code>bfs inv:error</code> | <code>goal=condition:error</code> |
| `inv:!`*rule* | <code>bfs inv:!safe</code> | <code>goal=condition:!safe</code> |
| `formula:`*formula* | <code>bfs formula:done&amp;&amp;!error</code> | <code>goal=condition:done&amp;&amp;!error</code> |
