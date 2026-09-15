---
title: # required
    "Exploration and verification"
permalink: # required, must match filename.html
    manual_verification.html
summary:
    "Configuring exploration, and model checking"
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

The Explore menu of the Simulator offers *Explore State Space* (also available as a toolbar button), which runs the grammar's exploration from the current state; unless configured otherwise, this is a full breadth-first exploration of everything reachable, subject to the control program. The LTS panel displays the explored transition system; states can be inspected individually, and partial (step-by-step) exploration is possible by applying matches by hand or by exploring a single state.

The exploration a grammar runs is part of the grammar. It is stored as a *settings resource*: a properties file in the `explore` folder of the `.gps` directory, listed in the Simulator under the Settings tab, with one `key=value` line per deviation from the default vocabulary described in the next section. The `exploration` system property names the settings resource in use; that one is shown in bold in the resource list, and enabling another there switches the grammar over to it. A grammar without such a resource explores with all defaults. Grammars saved before GROOVE 8.0.0 kept their exploration in the `explorationStrategy` property; it is still read, and migrated to a settings resource the first time the exploration dialog saves.

*Customize Exploration ...* (Explore menu) opens the *exploration dialog*, which shows the settings resource in use as one row per key, each with its alternatives and their content, and reports combinations that cannot be realised. A selector switches between the grammar's settings resources and activates the chosen one. The composed settings are stored with *Save* (under the current name) or *Save As...* (under a new name, which then becomes the grammar's exploration), or discarded with *Revert*; *Restart* and *Continue*, which explore afresh or continue the current state space, are enabled only once the settings are saved and consistent. Switching to another settings resource does not discard the state space, so a partial exploration can be continued under different settings, except that the keys shaping the transition system itself — `collapse`, `algebra` and `persistence` — may not change on *Continue*. Errors in a stored settings resource, such as an unknown rule name in a goal, are reported as grammar errors.

## The exploration configuration

An exploration is composed from fourteen independent *keys*, each with a default, so that only deviations need to be given. The complete vocabulary — every key, its alternatives and the content they take — is in the generated [exploration keys reference](manual_ref_exploration.html); this section explains how the keys combine.

- **Search order.** `next` selects the state to explore next from the frontier: the `oldest` (breadth-first, the default), the `newest` (depth-first) or a `random` one. `successor` selects which successors of that state are generated: `all` of them, or a `single` one, which gives a linear exploration; the `-random` variants shuffle. `frontier` restricts the size of the frontier, to a `single` state or to a beam of *n* states.
- **Results.** `goal` says what counts as a result: a `final` state (one without outgoing transitions, the default), `any` state, a state satisfying a `condition:`*formula* over rule names (`r`, `!P`, `P&&Q`, `P||Q`, `P->Q`, with the usual precedence), read as "rule *r* is applicable", or a state in which a named action `fires:`*action*. `outcome` turns the goal around (`violate` makes the states *not* satisfying it the results), `count` says after how many results exploration halts (`all`, `first`, or a number), and `shape` whether a result is the `state` itself or the `trace` leading to it from the start state.
- **Bounds and cost.** `bound` limits the states explored: by the `cost` of the path to them, their `size`, number of `nodes` or number of `edges` per label, or by a rule condition that states may be explored `upto` or `include`d; `initial` explores the start state only. Numeric bounds take an optional increment for iterative deepening. `cost` assigns transition costs, `uniform` (every transition costs 1, so a cost bound is a depth bound) or `rule` (a cost per rule); a `heuristic` orders the frontier by estimated distance to the goal.
- **State space.** `persistence=none` explores without storing the states, so that memory is reclaimed for fruitless subtrees; this needs a finite unfolding or a bound. `collapse` says when a fresh state counts as an already known one (up to isomorphism, equality or a hash), and `algebra` how data values are interpreted; both default to what the grammar's system properties say.
- **Randomness.** `seed` fixes the master seed from which every random choice derives, making such an exploration reproducible; see [below](#randomness-and-determinism).

For example, breadth-first exploration is simply all defaults; depth-first exploration is `next=newest`; "explore until a state is found where rule `done` is applicable" is `goal=condition:done count=first`; and "explore breadth-first up to depth 10" is `cost=uniform bound=cost:10`. A depth bound means the same under breadth-first and depth-first exploration: the maximal depth of the states added.

### Randomness and determinism

Exploration is deterministic: two runs of the same grammar under the same configuration produce the same state space in the same order, also across separate runs of the tool. The random alternatives (`next=random` and the `-random` successor variants) draw from a single *master seed*, which is generated afresh for each run unless fixed by the `seed` key, the Generator's `-seed` option or the system property `groove.randomSeed`. After an exploration that actually drew random values, the effective seed is stored in the transition system, saved with the LTS and shown in the Simulator's LTS status line, so that any run can be reproduced.

### Too many matches

The number of matches collected in a single state is capped by the `matchBound` system property (10000 by default; `0` disables the cap). Exceeding it — typically through rule amalgamation — halts the exploration with the offending state flagged as an error state; in the Simulator the state space explored so far remains available, while the Generator and ModelChecker fail with a message rather than report a truncated state space as complete.

## The Generator

For large state spaces, the command-line Generator avoids the overhead of the GUI:

    java -jar Generator.jar [options] <grammar>.gps [<start graph names>]

By default the Generator runs the grammar's own exploration, as the Simulator does. Another exploration is configured with `-x`, taking the vocabulary above as a space-separated (quoted) list of `key=value` pairs, exactly as in a settings resource:

    java -jar Generator.jar -x "next=newest count=first goal=condition:mygoal" mygrammar.gps

Alternatively, `-D exploration=name` selects another of the grammar's settings resources. Further options include:

- `-o file` — save the generated LTS; the accompanying `-ef` option controls extra state labels (start, final, open and result states, state numbers, transient states), and `-spanning`/`-traces` restrict the saved LTS to the spanning tree or the result traces;
- `-f file` — save the result states as individual graph files;
- `-P file` / `-D key=val` — override the grammar's system properties for this run (e.g. `-D typeGraph+=names` to extend the active type graphs);
- `-seed num` — fix the master random seed (see [above](#randomness-and-determinism));
- `-l dir` — write a log of the generation run, including its outcome, to a directory;
- `-log level[:subsystem]` — print diagnostic messages of the given level (`trace`, `debug`, `info`, `warning` or `error`) and upwards to the standard error stream, optionally for a single subsystem only, such as `explore.timing`; may be repeated. This option is common to all command-line tools;
- `-r num` — stop after `num` result states.

Invoke `Generator -h` for the complete list. The options `-s` (strategy), `-a` (acceptor) and `-r` predate `-x` and are kept indefinitely as shorthand: each combination of them stands for a configuration in the vocabulary above, which the Generator prints as the equivalent `-x` when they are used. Their own vocabulary of named *strategies* and *acceptors* is listed at the end of the [exploration keys reference](manual_ref_exploration.html). Two combinations are rejected: the `cycle` acceptor with anything but an LTL strategy, and the `none` acceptor with a result count. LTL model checking from the command line goes through the `ltl` strategies of `-s`; see [below](#the-modelchecker-command-line-tool).

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

Note the semantics of a rule-name atom: it holds in a state if the rule is *enabled* there — not if it has just been executed. A rule that carries a `transitionLabel` property is referred to by that label. Atoms that are not plain identifiers can be quoted (single or double quotes).

In **CTL formulae**, every temporal operator (`X`, `F`, `G`, `U`, …) must be directly preceded by a path quantifier (`A` or `E`), as in the well-known combinations `AG f` ("f holds always, on all paths"), `EF f` ("f is reachable"), or `AF f` ("f is unavoidable"). In **LTL formulae**, path quantifiers do not occur: the formula is evaluated over all paths.

Typical examples, for a grammar with rules `error` and `done`:

- `AG !error` — CTL: in no reachable state is `error` applicable;
- `EF done` — CTL: a state where `done` is applicable is reachable;
- `G F done` — LTL: on every path, `done` is enabled infinitely often.

### Model checking in the Simulator

The Verify menu offers:

- **Check CTL property** — either on the *full state space*, which is explored first if any open states remain, or on the *current* state space as explored so far, in which case the verdict refers to that part only. The states satisfying, respectively violating, the formula are marked in the LTS panel.
- **Check LTL property** — on the full state space, or with *bounded* exploration, for which a dialog asks for the bound. LTL checking is implemented as a nested depth-first search for an accepting cycle in the product of the state space with a Büchi automaton derived from the negated formula; it drives its own exploration and can conclude violation before the full state space has been generated. If the property is violated, the counterexample — a lasso consisting of a prefix and a cycle — is reported and highlighted in the LTS panel.

### The ModelChecker command-line tool

The ModelChecker combines generation and CTL checking headlessly:

    java -jar ModelChecker.jar -ctl "AG !error" [-g "<generator args>"] <grammar>.gps

Options: `-ctl prop` specifies a property to check (may occur multiple times); `-g args` passes options through to the embedded Generator; a previously saved GXL state space can be checked instead of a grammar. Invoke it with `-h` for details. LTL properties are checked from the command line by the Generator, through its legacy `-s ltl:prop` strategy (with `ltlbounded` and `ltlpocket` as bounded variants).

## Prolog queries

As a third analysis vehicle besides exploration and model checking, GROOVE embeds a Prolog interpreter: the Prolog panel of the Simulator lets you query the current graph and state space through a library of built-in predicates (with the possibility to add your own predicates via the `prolog` system property). This is chiefly of interest for ad-hoc inspection and custom analyses; the built-in predicates are documented in the Prolog panel itself. Queries can also be run headlessly with the PrologChecker command-line tool, except that predicates which open a window, such as `show_graph`, are available in the Simulator only.
