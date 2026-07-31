---
title:
    "Reference: strategies and acceptors"
permalink:
    manual_ref_exploration.html
summary:
    "The named exploration strategies and acceptors of the legacy -s/-a options"
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
    strategy, acceptor, exploration, reference
---

*This page is generated from the GROOVE 7.5.3 source code by `manual/make-refs.sh`; do not edit it by hand.*

The named strategies and acceptors below constitute the vocabulary of the Generator's deprecated `-s` and `-a` options (see the [Exploration and verification](manual_verification.html) chapter); the preferred way to configure an exploration is the `-x` configuration vocabulary described there. Development-only strategies are omitted.

## Strategies

| Keyword | Name | Description |
| :--- | :--- | :--- |
| `bfs` | Breadth-First Exploration | This strategy first generates all possible transitions from each open state, and then continues in a breadth-first fashion. A non-zero bound makes exploration stop at the indicated depth. |
| `dfs` | Depth-First Exploration | This strategy first generates all possible transitions from each open state, and then continues in a depth-first fashion. A non-zero bound makes exploration stop at the indicated depth. |
| `linear` | Linear Exploration | This strategy chooses one transition from each open state. The transition of choice will be the same within one incarnation of Groove. |
| `random` | Random Linear Exploration | This strategy chooses one transition from each open state. The transition is chosen randomly. |
| `state` | Single-State Exploration | This strategy fully explores the current state. |
| `uptorule` | Exploration Up To Rule Applicability | This strategy performs a conditional, optionally bounded, depth- or breadth-first exploration. A state is hit if a given rule is [not] applicable. A hit state is either not explored ('up to') or the last one to be explored ('include'). The rule in question does not have to be scheduled to be used for this purpose. All other states are explored normally. |
| `cnbound` | BFS Exploration Up To Node Bound | This strategy performs a conditional breadth-first exploration. If the number of nodes in a newly reached state exceeds a given bound, it is not explored. All other states are explored normally. |
| `cebound` | BFS Exploration Up To Edge Bound | This strategy performs a conditional breadth-first exploration. If the number of edges in a newly reached state exceeds a given bound, it is not explored. All other states are explored normally. |
| `ltl` | LTL Model Checking | Nested Depth-First Search for a given LTL formula. |
| `ltlbounded` | Bounded LTL Model Checking | Nested Depth-First Search for a given LTL formula,using incremental bounds based on graph size or rule applications |
| `ltlpocket` | Pocket LTL Model Checking | Nested Depth-First Search for a given LTL formula,using incremental bounds based on graph size or rule applicationsand optimised to avoid reexploring connected components ('pockets') |
| `remote` | Remote Exploration | This strategy sends the result as an STS to a remote server. |

## Acceptors

| Keyword | Name | Description |
| :--- | :--- | :--- |
| `final` | Final States | This acceptor succeeds when a state is added to the LTS that is final. A state is final when no modifying rule isapplicable on it. |
| `inv` | Check Invariant | This acceptor succeeds when a state is reached in which the indicated rule is applicable. Note that this is detected before the rule has been applied. This acceptor ignores rule priorities. |
| `ruleapp` | Rule Application | This acceptor succeeds when a transition of the indicated rule is added to the LTS. Note that this is detected after the rule has been applied (which means that rule scheduling is taken into account). |
| `formula` | Rule Formula | This acceptor is a variant of Check Invariant that succeeds when a state is reached in which an arbitrary rule formula is applicable. |
| `any` | Any State | This acceptor succeeds whenever an (exposed) state is added to the LTS. |
| `cycle` | Cycles | This acceptor listens to pairs of graph states and Buchi states,and succeeds when a pair is added that lies on a cycle with anaccepting Buchi state. Should only be used in conjunction with LTL model checking. |
| `none` | No State | This acceptor always fails whenever a state is added to the LTS. |
