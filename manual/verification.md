---
title: # required
    "Exploration and verification"
permalink: # required, must match filename.html
    manual_verification.html
summary:
    "Exploration strategies and model checking"
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
    exploration, strategy, acceptor, generator, model checking, CTL, LTL, temporal logic
---

Exploration is the recursive application of all scheduled actions to all reachable graphs, producing a transition system (also called the LTS or, in full, the graph transition system, GTS) in which every state is a graph and every transition an action application. This chapter describes how to configure exploration — in the Simulator and in the command-line Generator — and how to verify temporal properties of the resulting state space by model checking.

## Exploration in the Simulator

The Explore menu of the Simulator offers one-click *full exploration* (also available as a toolbar button), which explores the complete state space reachable from the current state, subject to the control program. The LTS panel displays the explored transition system; states can be inspected individually, and partial (step-by-step) exploration is possible by applying matches by hand.

Beyond full exploration, the *exploration dialog* (Explore menu) lets you compose a custom exploration from a set of orthogonal settings — the same vocabulary described in the next section — and warns about combinations that are not realisable. A grammar can record its default exploration in the `exploration` system property, using the same `key=value` vocabulary.

## The exploration configuration

An exploration is composed from the following settings; every key has a default (marked *), so only deviations need to be given.

| Key | Values | Meaning |
| :--- | :--- | :--- |
| `next` | `oldest`*, `newest`, `random` | Which open state to explore next: oldest gives breadth-first, newest depth-first behaviour |
| `successor` | `all`*, `all-random`, `single`, `single-random` | Which successors to generate per state; `single` gives linear exploration |
| `frontier` | `complete`*, `single`, *n* | Restriction of the exploration frontier (*n* = beam width) |
| `heuristic` | `none`*, `nen` | State quality function guiding the exploration |
| `cost` | `none`*, `uniform`, `rule` | Transition cost function |
| `goal` | `final`*, `none`, `any`, `graph:`*id*, `condition:`*c*, `fires:`*id*, `ltl:`*prop*, `ctl:`*prop* | What counts as a result state (see below) |
| `outcome` | `satisfy`*, `violate` | Whether results should satisfy or violate the goal |
| `shape` | `state`*, `trace` | Whether results are states or traces leading to them |
| `count` | `all`*, `first`, *n* | Number of results after which exploration halts |
| `bound` | `none`*, `cost:`*max*[+*inc*], `size:`*max*[+*inc*], `nodes:`*max*[+*inc*], `edges:`*id*>*n*,…, `upto:`[`!`]*id*, `include:`[`!`]*id* | Bound on the exploration |
| `persistence` | `all`*, `none` | Whether explored states are stored |
| `collapse` | `grammar`*, `equality`, `isomorphism`, `hash` | When two states are considered the same |
| `algebra` | `grammar`*, `default`, `big`, `point`, `term` | Algebra used for data values |

The `goal` condition `condition:`*c* takes a propositional formula over rule names (`r`, `!P`, `P&&Q`, `P||Q`, `P->Q`), satisfied in a state if the formula holds under the interpretation "rule is applicable"; `fires:`*id* requires an actual application of rule *id*; `graph:`*id* compares against a saved goal graph; and `ltl:`/`ctl:` embed model checking into exploration (see [below](#model-checking)).

For example, breadth-first exploration is simply all defaults; depth-first exploration is `next=newest`; and "explore until a state is found where rule `done` is applicable" is `goal=condition:done count=first`.

## The Generator

For large state spaces, the command-line Generator avoids the overhead of the GUI:

    java -jar Generator.jar [options] <grammar>.gps [<start graph names>]

The exploration is configured with `-x`, taking the vocabulary above as a space-separated (quoted) list:

    java -jar Generator.jar -x "next=newest count=first goal=condition:mygoal" mygrammar.gps

Further options include:

- `-o file` — save the generated LTS; the accompanying `-ef` option controls extra state labels (start, final, open and result states, state numbers, transient states), and `-spanning`/`-traces` restrict the saved LTS to the spanning tree or the result traces;
- `-f file` — save the result states as individual graph files;
- `-P file` / `-D key=val` — override the grammar's system properties for this run (e.g. `-D typeGraph+=names` to extend the active type graphs);
- `-l dir` — log the generation process;
- `-r num` — stop after `num` result states.

Invoke `Generator -h` for the complete list. The older options `-s` (strategy) and `-a` (acceptor) are still recognised but deprecated in favour of `-x`; their vocabulary of named *strategies* and *acceptors*, listed in the generated [strategies and acceptors reference](manual_ref_exploration.html), maps onto combinations of the configuration keys above.

## Model checking

Beyond reachability-style questions, GROOVE can verify *temporal properties* of the state space by model checking, in two flavours: CTL (branching time) and LTL (linear time).

### Temporal formulae

Formulae are built from atomic propositions using propositional and temporal operators:

| Syntax | Meaning |
| :---: | :--- |
| `true`, `false` | Constant propositions |
| *rule* | Atom: holds in a state if the action is *enabled* (has a match) |
| *rule*`(`*args*`)` | Atom with arguments; `_` is a wildcard argument |
| `'`*label*`'` | Atom: holds if *label* exactly matches a flag or outgoing transition label |
| `start`, `final` | Special propositions for the start state and final states |
| `!f` | Negation |
| `f & g`, <code>f &#124; g</code> | Conjunction, disjunction |
| `f -> g`, `f <- g`, `f <-> g` | Implication, inverse implication, equivalence |
| `X f` | Next: `f` holds in the next state of the path |
| `F f` | Eventually: `f` holds in some state of the path |
| `G f` | Globally: `f` holds in all states of the path |
| `f U g` | Until: `f` holds until eventually `g` holds |
| `f W g` | Weak until: as `U`, but `g` may never hold, in which case `f` holds forever |
| `f R g` | Release: `g` holds up to and including a state where `f` also holds (which may never happen) |
| `f M g` | Strong release: as `R`, but `f` must eventually hold |
| `A f` | For all paths from the current state, `f` holds |
| `E f` | For some path from the current state, `f` holds |

Note the semantics of a rule-name atom: it holds in a state if the rule is *enabled* there — not if it has just been executed. Atoms that are not plain identifiers can be quoted (single or double quotes).

In **CTL formulae**, every temporal operator (`X`, `F`, `G`, `U`, …) must be directly preceded by a path quantifier (`A` or `E`), as in the well-known combinations `AG f` ("f holds always, on all paths"), `EF f` ("f is reachable"), or `AF f` ("f is unavoidable"). In **LTL formulae**, path quantifiers do not occur: the formula is evaluated over all paths.

Typical examples, for a grammar with rules `error` and `done`:

- `AG !error` — CTL: in no reachable state is `error` applicable;
- `EF done` — CTL: a state where `done` is applicable is reachable;
- `G F done` — LTL: on every path, `done` is enabled infinitely often.

### Model checking in the Simulator

The Verify menu offers:

- **Check CTL property** — either on the *full state space* (exploring it first, if needed) or on the *current* (partially explored) state space. The states satisfying, respectively violating, the formula are marked in the LTS panel.
- **Check LTL property** — on the full state space, or with *bounded* exploration. LTL checking is implemented as a nested depth-first search for an accepting cycle in the product of the state space with a Büchi automaton derived from the negated formula; if the property is violated, a counterexample path is reported.

CTL checking operates on the explored state space, so its verdict is only as complete as the exploration; make sure the state space is fully explored (or accept that the result refers to the explored part). LTL checking by contrast drives its own exploration and can conclude violation before the full state space has been generated.

### The ModelChecker command-line tool

The ModelChecker combines generation and CTL/LTL checking headlessly:

    java -jar ModelChecker.jar -ctl "AG !error" [-g "<generator args>"] <grammar>.gps

Options: `-ctl prop` and `-ltl prop` specify the properties to check (each may occur multiple times); `-g args` passes options through to the embedded Generator; for CTL, a previously saved GXL state space can be checked instead of a grammar. Invoke it with `-h` for details.

## Prolog queries

As a third analysis vehicle besides exploration and model checking, GROOVE embeds a Prolog interpreter: the Prolog panel of the Simulator lets you query the current graph and state space through a library of built-in predicates (with the possibility to add your own predicates via the `prolog` system property). This is chiefly of interest for ad-hoc inspection and custom analyses; the built-in predicates are documented in the Prolog panel itself.
