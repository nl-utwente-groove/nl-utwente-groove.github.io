---
title:
    "Reference: exploration configuration"
permalink:
    manual_ref_exploration.html
summary:
    "The keys and settings of the exploration configuration vocabulary, and the legacy strategy and acceptor keywords"
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

*This page is generated from the GROOVE 7.6.0 source code by `manual/make-refs.sh`; do not edit it by hand.*

The keys and settings below constitute the vocabulary of the Generator's `-x` option, of the exploration dialog in the Simulator and of the `exploration` grammar property (see the [Exploration and verification](manual_verification.html) chapter). The textual form of a configuration is a space-separated list of `key=setting` entries; a key that is absent takes its default.

## Keys

| Key | Default | Description |
| :--- | :--- | :--- |
| <code>next</code> | <code>oldest</code> | Selection of the next state to be explored |
| <code>successor</code> | <code>all</code> | Selection of the successor states to be generated |
| <code>frontier</code> | <code>complete</code> | Size restriction on the exploration frontier |
| <code>heuristic</code> | <code>none</code> | Quality function guiding the selection of the next state |
| <code>cost</code> | <code>none</code> | Cost of a single transition |
| <code>goal</code> | <code>final</code> | Condition determining when a result has been found |
| <code>outcome</code> | <code>satisfy</code> | Whether the goal condition is to be satisfied or violated |
| <code>shape</code> | <code>state</code> | Shape of the results the exploration yields |
| <code>count</code> | <code>all</code> | Number of results after which exploration halts |
| <code>bound</code> | <code>none</code> | Bound on the states to be explored |
| <code>persistence</code> | <code>all</code> | Degree to which discovered states are stored in the GTS |
| <code>collapse</code> | <code>grammar</code> | Condition under which a fresh state is considered equal to a known state |
| <code>algebra</code> | <code>grammar</code> | Interpretation of data values |
| <code>seed</code> | <code>auto</code> | Master random seed governing the randomised features |

## Settings

The settings per key; the default is marked with an asterisk. In the setting forms, *n* stands for a number, *max*[`+`*inc*] for a maximum with an optional increment for iterative deepening, and *text* for a name or formula as explained in the description.

| Key | Setting | Description |
| :--- | :--- | :--- |
| <code>next</code> | <code>oldest</code>\* | The oldest state in the frontier is explored next (breadth-first search) |
|  | <code>newest</code> | The newest state in the frontier is explored next (depth-first search) |
|  | <code>random</code> | A random state in the frontier is explored next |
| <code>successor</code> | <code>all</code>\* | All unexplored successor states are generated, in their natural order |
|  | <code>all-random</code> | All unexplored successor states are generated, in random order |
|  | <code>single</code> | Only the first unexplored successor state is generated |
|  | <code>single-random</code> | Only a single, randomly chosen unexplored successor state is generated |
| <code>frontier</code> | <code>complete</code>\* | No states are dropped from the frontier |
|  | <code>single</code> | The frontier holds a single state, giving rise to a linear search |
|  | *n* or <code>beam:</code>*n* | The frontier is restricted to a given maximum size (beam search) |
| <code>heuristic</code> | <code>none</code>\* | No heuristic is used; all states have the same quality |
|  | <code>nen</code> | Count of node/edge/node tuples missing with respect to the goal (requires the goal to be a complete graph or unnested, NAC-free rule) |
| <code>cost</code> | <code>none</code>\* | Transitions have no cost; the cost of a path is ignored |
|  | <code>uniform</code> | Every transition has cost 1, so the cost of a path equals its length |
|  | <code>rule</code> | Each rule application has a cost, fixed per rule or exposed as a rule parameter |
| <code>goal</code> | <code>none</code> | There is no goal; exploration continues until the frontier is empty |
|  | <code>any</code> | Every state is a result |
|  | <code>final</code>\* | A state without outgoing transitions |
|  | <code>graph:</code>*text* | A state whose graph is isomorphic to the named graph |
|  | <code>condition:</code>*text* | A state whose graph satisfies a propositional condition over rule names (regardless of whether the rules are scheduled) |
|  | <code>fires:</code>*text* | A state in which the named action (a rule or recipe) fires, as scheduled |
|  | <code>ltl:</code>*text* | A trace satisfying an LTL formula |
|  | <code>ctl:</code>*text* | A state space satisfying a CTL formula |
| <code>outcome</code> | <code>satisfy</code>\* | A result satisfies the goal condition |
|  | <code>violate</code> | A result violates the goal condition |
| <code>shape</code> | <code>state</code>\* | A result is a state |
|  | <code>trace</code> | A result is a trace from the start state to a goal state |
| <code>count</code> | <code>all</code>\* | Exploration continues until the entire state space has been explored |
|  | <code>first</code> | Exploration stops as soon as a single result has been found |
|  | *n* or <code>value:</code>*n* | Exploration stops as soon as the given number of results has been found |
| <code>bound</code> | <code>none</code>\* | There is no bound; the state space is explored up to arbitrary depth |
|  | <code>initial</code> | Only the initial state is explored, and no further states are |
|  | <code>cost:</code>*max*[`+`*inc*] | States are bounded by the cost of the path leading to them |
|  | <code>size:</code>*max*[`+`*inc*] | States are bounded by the total number of their graph elements |
|  | <code>nodes:</code>*max*[`+`*inc*] | States are bounded by their number of nodes |
|  | <code>edges:</code>*text* | States are bounded by their number of edges of given types (a comma-separated list of label&gt;bound pairs; labels may carry a type: or flag: prefix) |
|  | <code>upto:</code>*text* | States satisfying the named rule condition (negated if prefixed with '!') are not explored |
|  | <code>include:</code>*text* | States satisfying the named rule condition (negated if prefixed with '!') are the last to be explored |
| <code>persistence</code> | <code>all</code>\* | All discovered states are stored in the GTS |
|  | <code>none</code> | No states are stored in the GTS |
| <code>collapse</code> | <code>grammar</code>\* | As determined by the grammar property 'checkIsomorphism' |
|  | <code>equality</code> | Graphs are compared for equality (the strongest condition) |
|  | <code>isomorphism</code> | Graphs are compared up to isomorphism |
|  | <code>hash</code> | Graphs are considered equivalent if their isomorphism hash codes coincide (may collapse non-isomorphic states) |
| <code>algebra</code> | <code>grammar</code>\* | As determined by the grammar property 'algebraFamily' |
|  | <code>default</code> | Java-based representation of data values |
|  | <code>big</code> | Arbitrary-precision representation of data values |
|  | <code>point</code> | All data values are collapsed to a single point |
|  | <code>term</code> | Data values are represented symbolically, as terms |
| <code>seed</code> | <code>auto</code>\* | The master seed is left as it is: the -seed option or the groove.randomSeed system property if given, a freshly generated (and reported) seed otherwise |
|  | *n* or <code>value:</code>*n* | The given value becomes the master seed for the rest of the session, making the randomised features of this and subsequent explorations reproducible |

## Legacy strategies and acceptors

Up to release 7.5.3, an exploration was given by a named strategy and acceptor: the keywords of the Generator's `-s` and `-a` options (still accepted as legacy shorthand) and of the `explorationStrategy` grammar property. Each combination maps onto a configuration, except for the LTL model checking strategies, which are not configuration-based. The table lists every keyword with an example of a legacy *strategy* *acceptor* pair and the configuration it translates to.

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
