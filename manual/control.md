---
title: # required
    "Control language"
permalink: # required, must match filename.html
    manual_control.html
summary:
    "Programming the order of rule applications"
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
    control, program, schedule, recipe, function, package, variable, atomic
---

Control is about scheduling rule applications: it determines, in every state, which actions may be applied next. It is a much stronger mechanism than rule priorities (see [Graphs and rules](manual_basics.html#rule-properties)). Control is specified in the form of one or more *control programs* — resources with extension `.gcp` — written in a small imperative language described in this chapter.

The full grammar of the language is given in the generated [control grammar reference](manual_ref_control.html); the same information is built into the tool, in the right-hand help panel of the control editor in the Simulator.

## Programs and terminology

A control program consists of an optional package declaration, a list of imports, and a mixture of *function* definitions, *recipe* definitions and top-level statements. The top-level statements together form the *main body* of the program.

Which control programs are in effect is determined by the `controlProgram` system property, which holds a list of enabled programs. All enabled programs are combined: procedures declared in any of them can be called from all of them, and at most one of them may have a main body. If no enabled program declares a main body, the default is

    # *.any;

meaning: apply any action, from any package, as long as possible. (In other words: a grammar without control explores all rules — the behaviour described in the previous chapters.) A procedure declared in a program that is disabled, or that has errors, cannot be called from an enabled one; the error on such a call says which program the procedure lives in and why it is unavailable.

Terminology used throughout:

- an **action** is a rule or recipe: a named atomic step, visible as a transition in the resulting transition system;
- a **procedure** is a function or recipe: a named, parameterised statement; the difference is that recipes are executed atomically (see [below](#procedures-functions-and-recipes));
- a **callable** is anything that can be invoked by a call: a rule or a procedure.

Note that control is about scheduling *transformer* actions. Rules whose `actionRole` is `condition`, `invariant` or `forbidden` (see [Graphs and rules](manual_basics.html#rule-properties)) are not scheduled by the control program; they are checked at every state regardless.

## Calls and expressions

The elementary building block of a control program is the *rule call*: the (possibly qualified) name of a rule, optionally with an argument list (see [Variables and arguments](#variables-and-arguments)). Where a rule name appears, only that rule is scheduled at that point; if the rule has no match, the call fails.

Control and rule priorities (see [Graphs and rules](manual_basics.html#rule-properties)) do not mix: as soon as the actions of a grammar carry more than one distinct priority, an explicit call of a rule or recipe with a non-zero priority is a compile-time error ("Explicit call of prioritised rule not allowed"), since the call would override the scheduling the priorities express. Group calls (`any` and `other`, see [below](#group-calls-any-and-other)) honour the priorities. If all actions have the same priority, priorities have no effect and every action can be called explicitly.

Control *expressions* are built from calls by the following operators:

| Expression | Meaning |
| :---: | :--- |
| `e1; e2` | Sequential composition: `e1` followed by `e2` |
| <code>e1 &#124; e2</code> | Nondeterministic choice between `e1` and `e2` |
| `e*` | Zero or more repetitions of `e` |
| `e+` | One or more repetitions of `e` |
| `#e` | Repeat `e` *as long as possible* |
| `(e)` | Grouping |

Two points deserve emphasis:

- **Nondeterminism means branching.** A choice <code>a &#124; b</code> does not pick one alternative: during state space exploration, *both* alternatives are explored (where applicable). Likewise `a*` may, in every state, either continue with `a` or stop; all possibilities become part of the state space.
- **`a*` versus `#a`.** The repetition `a*` may nondeterministically stop even while `a` is still applicable; `#a` schedules `a` for as long as it is applicable, and only continues when `a` has no match. Consequently `#a` produces a linear reduction of the state space compared to `a*`.

## Statements

Expressions terminated by a semicolon are the simplest statements. Beyond these, the language offers:

- `{ stat* }` — a block: a possibly empty sequence of statements.

- `if (cond) stat1 [else stat2]` — if the condition is enabled it is *executed*, followed by `stat1`; otherwise `stat2` (if present) is executed.

- `try stat1 [else stat2]` — `stat1` is executed if it is *enabled* (i.e., its first action can be applied); otherwise `stat2` is executed. This allows more complex conditions than `if`: the condition is, in effect, the first action of `stat1`. For instance, `try { a; b; } else { c; d; }` executes `c; d;` exactly if rule `a` has no match.

- `while (cond) stat` — as long as `cond` is enabled, it is executed, followed by `stat`; equivalent to `alap { cond; stat }`.

- `until (cond) stat` — as long as `cond` *fails*, `stat` is executed. If the loop terminates, the last action is an application of `cond` itself.

- `do stat while (cond)` / `do stat until (cond)` — as `while`/`until`, but with the body executed before the first test.

- `alap stat` — *as long as possible*: `stat` is repeated until it is no longer enabled. This is the statement-level analogue of the `#` operator.

- `choice stat1 or stat2 [or ...]` — nondeterministic choice at statement level, the analogue of the `|` operator.

- `< stat* >` — an *atomic* block: the enclosed statements are executed as a single step from the perspective of the transition system; the intermediate transitions only become part of the state space if the entire block completes successfully. (Recipes, below, are the named, parameterised form of this.)

- `halt` — stops exploration at this point, without marking the state as final.

The *conditions* of `if`, `while`, `until` and `do` are restricted: a condition is a rule call, the keyword `true`, or a choice (`|`) of conditions. A condition holds if at least one of its alternatives has a match — and note that, unlike in conventional languages, an enabled condition is actually *applied*, not just tested.

## Variables and arguments

Control programs can declare typed variables and pass values into and out of rules, connecting to the rule parameters described in the [previous chapter](manual_advanced.html#rule-parameters). The available types are `node` (all non-value nodes) and the data sorts `bool`, `int`, `real`, `string` and `user`.

    node x, y;
    int n;
    string s := getName();

A rule call's argument list corresponds, position by position, to the rule's declared parameters:

- an *input argument* is a literal (`true`, `false`, `"text"`, integer or real constant), a bound variable, or an operator expression over these (e.g. `f(n+1)`);
- an *output argument* is written `out x`: the variable `x` receives the value matched or produced by the rule;
- a *don't-care argument* is written `_`: the parameter does not constrain the match, and its value is not recorded.

Alternatively, output parameters can be received with assignment syntax: `x, y := f(a)` is equivalent to calling `f` with `a` as input and `out x`, `out y` for its output parameters.

A variable must be *bound* (by an output argument or an initialising declaration) before it can be used as an input.

The value of a variable is part of the control state only while the variable is *live*, that is, while its value can still be read by a later call. Two states that differ only in the value of a variable that is never used again are therefore the same state; a program that receives an output parameter and does nothing with it does not blow up the state space.

## Group calls: any and other

The keywords `any` and `other` act as wildcards over the available actions:

- `any` calls *any* action;
- `other` calls any action *not explicitly called elsewhere* in the control program.

Both can be qualified by a package name: `pkg.any` calls any action in package `pkg`, and `pkg.*.any` also includes all subpackages (`*.any` on its own covers every package). For instance, given rules `a`, `b` and `c`, the program `a; any; b; other;` first schedules only `a`, then any of the three rules, then `b`, then `c` — the only rule not called explicitly.

Group calls are only allowed if none of the actions they cover have input-only parameters (there would be no way to provide the values). Whether output parameters of implicitly called actions are recorded in the transition arguments is controlled by the `storeOutParameters` system property.

## Procedures: functions and recipes

Procedures are named, parameterised statements, declared with the keyword `function` or `recipe`, followed by the name, a parameter list, and a block for the body:

    function moveOne(node x) {
        pickUp(x);
        putDown(x);
    }

    recipe move(node x) priority 1 {
        pickUp(x);
        putDown(x);
    }

Parameters are declared like variables, with `out` marking output parameters. Procedures may call each other recursively.

The difference between the two kinds:

- A **function** is a transparent abbreviation: its body's rule applications appear individually in the transition system, exactly as if the body had been written in place of the call.
- A **recipe** is *atomic*: its entire execution is a single action, appearing as one transition, labelled with the recipe name (and arguments). Intermediate states are hidden, and only committed to the state space if the recipe completes successfully — a recipe that gets stuck halfway contributes nothing. Recipes are actions in the same sense as rules; they can, for instance, be covered by `any`/`other` group calls. An optional `priority n` clause gives the recipe a priority like that of a rule, with the same consequence: once the grammar's actions carry more than one distinct priority, a prioritised recipe may only be reached through group calls (see [Calls and expressions](#calls-and-expressions)). A recipe called with an input argument whose value is undefined is inapplicable. An output parameter of a recipe whose node is deleted by a later step of the recipe is undefined, and shown as `_` in the transition label; an output value computed by the final step of the recipe is reported as usual.

## Packages and imports

Control programs support Java-like modularity:

- `package p.q` as the first declaration causes all rules and procedures named in the program to be implicitly qualified by `p.q`; the natural counterpart of the hierarchical rule names described in [Graphs and rules](manual_basics.html#rule-names).
- `import p.q.r` declares that the simple name `r` stands for the qualified name `p.q.r` in the remainder of the program.

Rule and procedure names that clash with reserved words of the control language can be escaped with backward quotes, e.g. `` `any` `` or `` `out` ``.

## Example

The following program controls a hypothetical grammar with rules `init`, `grow`, `check` and `finish`:

    package demo;

    // initialise, then grow as long as possible,
    // checking after every step
    init;
    alap {
        grow;
        try check;
    }
    finish;

The `control.gps` grammar among the [samples](samples.html) demonstrates most of the constructs in this chapter.
