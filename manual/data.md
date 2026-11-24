---
title: # required
    "Using data"
permalink: # required, must match filename.html
    manual_data.html
summary:
    "Data attributes, expressions, algebras, and user-defined types and operations"
release:   # GROOVE release this chapter describes; see manual.md
    8.0.0
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
    attribute, expression, data, sort, algebra, operation, user operation, user type, user interface, record, display method, indeterminate, random, enumerator, range
---

GROOVE supports data values — integers, reals, booleans and strings — in the form of *attributes*: named fields of graph nodes, formally represented as edges to special *value nodes*. The four built-in data sorts are `int`, `real`, `bool` and `string`. A fifth sort, `user`, holds values of *user-defined types*: Java records that you make available to GROOVE, together with your own operations on them. These are the subject of the [second half](#user-defined-types-and-operations) of this chapter; the first half covers the built-in data support.

## Attributes in host graphs

In a host graph, an attribute can be specified in either of two ways:

- By an ordinary edge to a constant node: a node labelled `int:0`, `real:100.0`, `bool:true` or `string:"John"` (note the double quotes) stands for the corresponding data value. The edge label is the field name. Within the double quotes, a backslash escapes a double quote or another backslash, so `"say \"hi\""` and `"C:\\"` stand for `say "hi"` and `C:\`; escapes are processed left to right, and a backslash before any other character is taken literally (since release 7.6.0).
- By an assignment `let:field = const` on the node itself.

In the display view, attributes are not shown as separate nodes and edges, but in the familiar record-like notation, as `field = value` equations inside the node. (The Simulator has an option to switch this off and show value nodes as ellipses.) The following shows an attributed host graph:

| Edit view | Display view |
| :---: | :---: |
| ![](images/manual/attribute-graph-edit.svg) | ![](images/manual/attribute-graph-display.svg) |

## Tests and assignments in rules

Within rules, attributes are matched, tested and modified using *expressions*:

- **`test:`** followed by a boolean expression makes the rule applicable only if the expression holds. Field names in the expression refer to attributes of the node the test is on (optionally written `self.field`); fields of other nodes can be referenced by declaring a node identifier `id:name` on the other node and writing `name.field`.

- **`let:`** followed by an assignment `field = expr` sets the value of the field upon rule application, replacing any previous value. The expression is evaluated over the values *before* application.

- **Variable nodes** are value nodes with unknown value: a node labelled just with a sort, such as `int:` or `real:`, matches any value of that sort. With an `id:x` label, the value is available in expressions as `x`. Variable nodes are connected by ordinary attribute edges, and can be used to bind, compare or consume values.

Value nodes can never be created or deleted — every value exists exactly once, at least conceptually — so they only ever occur as readers; attribute *edges* however can be erased and created like any other edge.

The following rule expresses a bank withdrawal: the requested `amount`-edge from the `ATM` to the value `x` is consumed, and the `Account`'s balance is reduced by `x`, provided the balance is sufficient:

| Edit view | Display view |
| :---: | :---: |
| ![](images/manual/attribute-rule-edit.svg) | ![](images/manual/attribute-rule-display.svg) |

Note the display notation: the test is shown as a plain condition (`balance >= x`) and the assignment as a value change (`± balance → balance - x`).

## Expression syntax

Expressions use conventional infix syntax:

| Sort | Operators |
| :--- | :--- |
| `int`, `real` | `+` `-` `*` `/` (and `%` for `int`), unary `-`; comparisons `<` `<=` `>` `>=` `==` `!=` |
| `bool` | `!` (not), `&` (and), <code>&#124;</code> (or), `==` `!=` |
| `string` | `+` (concatenation), lexicographic `<` `<=` `>` `>=`, `==` `!=` |

Beyond the operator symbols, named operations are called in functional style, e.g. `max(x,y)`, `abs(x)`, `length(s)`, `substring(s,i,j)`, `ite(b,x,y)` (if-then-else), and conversions such as `toInt(s)` or casts `(real) x` (the `(real)` and `(string)` casts since release 6.3.0). The complete list of operations per sort is given in the generated [data operations reference](manual_ref_operators.html), and in the editor's syntax help panel. User-defined operations (see [below](#user-defined-types-and-operations)) are called in the same functional style.

An operation whose arguments are not valid for it — division by zero, say, or a substring index out of range — yields an *error value* of its result sort (since release 6.7.0): `NaI` for `int`, `NaR` for `real`, `NaB` for `bool`, `NaS` for `string` and `NaU` for `user`. Error values are values like any other: they are shown as such in the state, and a rule can test for them.

## The underlying product-node notation

Expressions are in fact a convenient front-end for a lower-level graph notation that remains available: *product nodes* (labelled `prod:`) stand for tuples of values, with `arg:0`, `arg:1`, … edges to their argument value nodes and operator edges (such as `real:sub` or `real:ge`) to result value nodes. The following is the same withdrawal rule in this notation:

| Edit view | Display view |
| :---: | :---: |
| ![](images/manual/attribute-rule-legacy-edit.svg) | ![](images/manual/attribute-rule-legacy-display.svg) |

The expression syntax is strongly preferred for readability; the product-node notation is chiefly of interest because the display view of complex expressions decomposes into it, and for grammars dating from before the expression syntax existed.

## Algebras

Formally, the data sorts and operations form a *signature*, which is interpreted by an *algebra* defining the actual values and functions. Through the `algebraFamily` system property (see [Advanced rule features](manual_advanced.html#system-properties)), the interpretation can be changed. Supported values:

- `default`: Java-based values (`int`, `double`, `boolean`, `String`);
- `point`: a single value for every sort — all constants and operations collapse to that value (useful for abstraction);
- `big`: high-precision values — arbitrary-precision integers and 34-digit decimal reals (`BigInteger`/`BigDecimal`);
- `term`: symbolic term representations rather than computed values.

In the `default` algebra, comparison of reals has a *tolerance* of 10⁻⁷: values whose relative difference is below the tolerance are considered equal, to avoid artificial differences due to rounding errors. In the `big` algebra, the tolerance is 10⁻³⁰.

The `user` sort is only implemented in the `default` algebra: a grammar that uses user-defined types or operations must leave the `algebraFamily` property at its default value.

Among the [samples](samples.html), `attributed-graphs` demonstrates the use of attributes.

## Enumerators

Most operations yield a single value. An *enumerator* (since release 7.7.0) is an operation that yields a set of values of its result sort; a rule that calls it is matched once for every value in that set. There is one enumerator: `range(n)` yields the integers from 0 up to but not including `n`, which is the empty set if `n` is zero or negative.

What "once for every value" amounts to depends on the [nesting level](manual_advanced.html#nested-rules) of the node on which the call stands, just as it does for any other part of a rule that can be matched in more than one way:

- **On an existential level, one of the values is chosen.** A top-level node with the assignment `let:x = range(a.n)` gives a rule with one match for every value: for `a.n = 3`, three matches, which assign 0, 1 and 2 to `x`.
- **On a universal level, all values are taken.** A `new:` node on the level of a `forall:` node, with the assignment `let:idx = range(t.n)`, is created once for every value: the rule creates `t.n` nodes, numbered from 0 by their `idx` attribute. This is the way to create a number of nodes that is itself given as data.
- **In a negative application condition, no value may qualify.** The condition is violated as soon as it can be satisfied with one of the values.
- **Compared to a value that is known otherwise, the call tests for membership.** An attribute edge `x` from an existing node to a value node `int:range(a.n)` is matched if the value of `x` is one of the enumerated values, since one equal value is enough to match the rule. The test `test:x == range(a.n)` is the same condition, written as a label of the node that has the attribute.

An enumerator call may be part of a larger expression: `a.lo + range(a.hi - a.lo + 1)` yields the integers from `a.lo` up to and including `a.hi`. Any expression that calls an enumerator is an *enumeration*. An expression may call at most one enumerator: `range(a.n) + range(a.m)` is an error. To combine two enumerations, give each a value node of its own, with the labels `id:i` and `int:range(a.n)` on the one and `id:j` and `int:range(a.m)` on the other; `i + j` then ranges over all pairs of values, so that an assignment `let:x = i + j` is matched once for every pair.

The display view sets an enumeration apart by an element-of sign, in place of the equals sign or the assignment arrow: the assignment and the membership test above are both shown as `x ∈ range(a.n)`, and a value node with identifier `i` and value `range(a.n)` as `i : int ∈ range(a.n)`.

An enumeration that is read stands for *some* one of its values, which limits the ways in which it can be tested or negated:

- **A test may call an enumerator in one form only**: the equality `test:x == range(a.n)`, with a field of the node itself on the left. Negated, as `not:test:x == range(a.n)`, it holds if the value of `x` is none of the enumerated values, which is displayed as `! x ∈ range(a.n)`. A node that has no `x` at all fails the membership test and satisfies its negation. Any other test that calls an enumerator, such as `test:x < range(a.n)`, is an error: it would hold as soon as it holds for one of the values, and its text does not convey that. To impose a condition on the values, name them: a value node with the labels `id:i` and `int:range(a.n)` stands for one of the values, and `test:x < i` is then an ordinary test of that value.
- **An embargo edge may not point to an enumeration.** A `not:` on the attribute edge `x` to the value node `int:range(a.n)` is an error, since it would mean that some enumerated value differs from `x`. The `not:` belongs on the value node: the condition then is that `x` is none of the values, as in the negated test.

Some things to be aware of:

- **The result is a set, without an order.** Every value is matched once, and no value is the first or the next one. If the order of the values matters, it has to be present in the values themselves, as with the `idx` attribute above.
- **Enumerators can only be called in rules.** They are an error in the attribute expressions of other graphs, and cannot be evaluated in the arguments of a rule call in a [control program](manual_control.html).
- **The algebra matters** (see [Algebras](#algebras)). In the `default` and `big` algebras the values are enumerated as described. In the `point` algebra the set collapses to the single value of the sort, so that the number of matches no longer follows the data. The `term` algebra cannot enumerate a symbolic set: a rule that calls an enumerator is an error there.
- **An error is a value.** If an argument of the call is an error value, the result is the error value of the result sort, as the only value.

## User-defined types and operations

The built-in sorts and operations are deliberately minimal, which leaves three things awkward or impossible to model:

- **Computations beyond the built-in operations.** Trigonometry, statistics, elaborate string processing: anything not in the [data operations reference](manual_ref_operators.html) has to be encoded in rules and graph structure, if it can be encoded at all.
- **Randomness.** All built-in operations are *deterministic*: the result is fixed by the arguments. The only way to model the roll of a die is to enumerate all its outcomes as separate transitions, so that every possible outcome becomes part of the state space.
- **Structured data.** A date, say, has to be modelled as a node with three `int` attributes. That blurs the line between data and graph structure, it leaves it to the rules to keep such nodes immutable and unique (two nodes with the same day, month and year ought to be *one* value), and it offers no way to attach operations such as "the next date" to the structure.

GROOVE addresses all three through *user-defined types and operations* (since release 7.5.0; operations alone since release 6.7.0, and their use in rule parameters and control programs since release 7.5.1): Java records and methods that you annotate, compile against GROOVE, and list in the system properties of your grammar. From then on they are used in rules exactly like the built-in sorts and operations. The design and the theory behind the feature are described in the paper *User-defined Types and Operations in GROOVE* (Arend Rensink, 2026); the example in this section is taken from that paper, and its code is also available in the paper's [GitHub repository](https://github.com/nl-utwente-groove/userops).

### Defining operations

A user-defined operation is a `public static` method of a `public` class, annotated with `@UserOperation` (from the package `nl.utwente.groove.annotation`):

```java
import nl.utwente.groove.annotation.UserOperation;

public class MyOps {
    /** Returns the square root of a number. */
    @UserOperation
    public static double sqrt(double x) {
        return Math.sqrt(x);
    }

    /** Tests if one string is a prefix of another. */
    @UserOperation
    public static boolean isPrefix(String prefix, String total) {
        return total.startsWith(prefix);
    }
}
```

The parameter types and the return type must be among the Java types that correspond to the GROOVE sorts, or a user-defined type or user interface (see below); the last parameter may also be a sequence of such values (see [Sequence components](#sequence-components)):

| Java type | GROOVE sort |
| :--- | :--- |
| `boolean` | `bool` |
| `int` | `int` |
| `double` | `real` |
| `String` | `string` |
| a `@UserType` record | `user` |
| a `@UserInterface` interface | `user` |

The name of the operation is the name of the method, and it is by this name that the operation is called in expressions: `sqrt(x)`, `isPrefix(s, t)`. Names must be unique across all classes that a grammar lists, since GROOVE does not support overloading; a duplicate is reported as an error. The one exception is an operation that several user types share through a [user interface](#user-interfaces). Methods without the annotation are ignored, so a class may contain any number of helper methods. If a method throws an exception, the operation yields the error value of its result sort (see [Expression syntax](#expression-syntax)).

### Defining types

A user-defined type is a `public` Java `record`, annotated with `@UserType`, whose components are all of the four Java types above (not of another user type; the last component may be a sequence, see [below](#sequence-components)):

```java
import nl.utwente.groove.annotation.UserType;

@UserType
public record Date(int day, int month, int year) {
}
```

Such a record contributes several operations at once:

- a *constructor* operation, named after the record, with the record components as parameters and a result of sort `user`: here `Date(int, int, int)`;
- an *accessor* operation for every component, named after the component, taking a `user` value and returning the component: here `day(user)`, `month(user)` and `year(user)`.

In addition, a user type may declare *instance methods* annotated with `@UserOperation`: public, but not static. These get an implicit first parameter of sort `user`, the value the method is invoked on, so that a method `int daysSince()` of `Date` becomes the operation `daysSince(user)` and is called in a rule as `daysSince(d)`. Instance methods have access to the record's components and may construct new values, but they should not *change* anything (and a record cannot be changed anyway): a user value is a *value*, a tuple of its components, in the same way that `3` is a value and not an object. Two values with the same components are the same value, and GROOVE relies on the `equals` and `hashCode` that Java generates for records to recognise this.

A user type and its operations are listed in the syntax help panel of the graph editor as soon as the grammar declares them (see [below](#making-the-classes-available)), with the class they are declared in.

### Sequence components

The last component of a user type may hold a sequence of values rather than a single one (since release 7.7.0). Its type is then one of four classes that GROOVE provides, in the same package as the annotations:

| Java type | Elements | GROOVE sort of the elements |
| :--- | :--- | :--- |
| `BoolSeq` | `boolean` | `bool` |
| `IntSeq` | `int` | `int` |
| `RealSeq` | `double` | `real` |
| `StringSeq` | `String` | `string` |

The following type is a named polynomial, given by its coefficients in the order of increasing degree:

```java
{% include_relative user-ops-src/user/Poly.java %}
```

The sequence classes are immutable, and two sequences are equal if they hold the same elements in the same order. A record with a sequence component is therefore a value like any other user value: two `Poly` values with the same name and coefficients are the same value, however they were arrived at. In Java code, `IntSeq.of(1, 2, 3)` makes a sequence, `size()` and `get(i)` read it, `with(i, v)` and `append(v)` return a changed copy, and `toArray()` returns the elements as an array; the other three classes have the same methods.

In a rule, a sequence is not a value by itself: there is no sort for it, so no node holds one and no expression yields one. The record instead contributes the following operations:

- the *constructor* takes the elements as trailing arguments, any number of them: `Poly(n, a, b)` is a polynomial with two coefficients and `Poly(n)` is one with none. A constant is written in the same way, `user:Poly("p", 1, 2, 3)`, and so is a value in the display view;
- `coeffsAt(p, i)` is the element at position `i`, counting from 0. If the sequence has no such position, the result is the error value of the element sort;
- `coeffsSize(p)` is the number of elements.

The two names are derived from the name of the component. There is no accessor called `coeffs`, and that name is reserved: an operation with the name of a sequence component is an error.

Anything that concerns the sequence as a whole is an instance method of the record, where the Java code has the sequence at hand: `append(p, c)` above is an example.

**Sequence parameters.** The last parameter of a `@UserOperation` method may be a sequence as well. The operation is then called with the elements as trailing arguments, like the constructor:

```java
/** Evaluates the polynomial with given coefficients, in the order of increasing degree. */
@UserOperation
public static int horner(int x, IntSeq coeffs) {
    int result = 0;
    for (int i = coeffs.size() - 1; i >= 0; i--) {
        result = result * x + coeffs.get(i);
    }
    return result;
}
```

is called as `horner(x, 1, 2, 3)`, or with any other number of arguments after `x`. The elements are always listed one by one: the sequence component of a user value cannot be passed on as a whole. For a variable number of user values there is a fifth class, `UserSeq<T>` with `T` a user type or a [user interface](#user-interfaces), which is allowed as a parameter but not as a component: `static int countCoeffs(UserSeq<Poly> polys)` is called as `countCoeffs(p, q)`. An argument of another user type than `T` is an error in the grammar if its type is known when the grammar is loaded (see [Checking calls](#checking-calls)), and otherwise makes the result an error value. User classes that use `UserSeq` and were compiled against release 7.7.0 or 7.7.1 have to be recompiled for release 7.8.0.

**Ranging over the elements.** The [enumerator](#enumerators) `range` yields the positions of a sequence, and `coeffsAt` the element at each of them. The following rule unfolds a polynomial, the attribute `p` of the node `t`, into one `Term` node for each of its coefficients. (The rule and the graphs below are in the grammar `user-seq.gps` in the [source of this manual]({{site.groove_url}}/nl-utwente-groove.github.io/tree/main/manual).)

| Edit view | Display view |
| :---: | :---: |
| ![](images/manual/user-seq-terms-edit.svg) | ![](images/manual/user-seq-terms-display.svg) |

The value node `i` is on the level of the `forall:` node and has the enumeration `range(coeffsSize(t.p))` as its value, so on that level the rule is matched once for every position of the sequence; the display view marks this by the element-of sign. The `Term` node is created on the same level, hence once for every position, with the position as its `degree` and the element at that position as its `coeff`. Applied to the graph on the left, the rule yields the graph on the right:

| Before | After |
| :---: | :---: |
| ![](images/manual/user-seq-start-display.svg) | ![](images/manual/user-seq-result-display.svg) |

It is the positions that are enumerated, not the elements, so an element that occurs twice counts twice: the coefficient 5 gives two `Term` nodes.

Two variations on this rule:

- Where the position is not needed as data, the enumeration can be part of the expression: the assignment `let:coeff = coeffsAt(t.p, range(coeffsSize(t.p)))` on the `new:` node does without the node `i`.
- To combine the elements instead of creating something for each, name them: a value node on the level of the `forall:` node, with the labels `id:e` and `int:coeffsAt(t.p, range(coeffsSize(t.p)))`, stands for every element in turn, so that `sum(e)` on the level above is the sum of the elements, 16 for the polynomial above. The same goes for the other operations that take all values of a nested level, such as `prod` (see the [data operations reference](manual_ref_operators.html)).

### User interfaces

An operation name can belong to one user type only: two user types cannot both have a component `name`, say, or both an instance method `size`. A *user interface* (since release 7.8.0) lets several user types share operations. It is a `public` Java interface annotated with `@UserInterface`, whose non-static methods annotated with `@UserOperation`, abstract or with a default implementation, are the shared operations. The following interface describes containers of integers:

```java
import nl.utwente.groove.annotation.IntSeq;
import nl.utwente.groove.annotation.UserInterface;
import nl.utwente.groove.annotation.UserOperation;

@UserInterface
public interface Container {
    /** The elements of the container. */
    @UserOperation
    IntSeq items();

    /** Returns the element that is next in line. */
    @UserOperation
    int peek();

    /** Returns this container extended by a given element. */
    @UserOperation
    Container push(int item);

    /** Tests if the container holds no elements. */
    @UserOperation
    default boolean isEmpty() {
        return items().size() == 0;
    }
}
```

and the following two user types, each in a file of its own (with the imports of `IntSeq` and `UserType`), implement it:

```java
@UserType
public record Stack(IntSeq items) implements Container {
    @Override
    public int peek() {
        return items().get(items().size() - 1);
    }

    @Override
    public Stack push(int item) {
        return new Stack(items().append(item));
    }
}

@UserType
public record Queue(IntSeq items) implements Container {
    @Override
    public int peek() {
        return items().get(0);
    }

    @Override
    public Queue push(int item) {
        return new Queue(items().append(item));
    }
}
```

An operation of a user interface takes a value of any of the user types that implement it as its first argument, in the same way as an instance method of a user type, and is applied as implemented by the user type of that value: Java dispatches the call. So `peek(Stack(1, 2))` is 2, `peek(Queue(1, 2))` is 1, and `push(Queue(1), 2)` is `Queue(1, 2)`. A user type implements an operation by a component or a method of that name: `items` above is implemented by the record components, `peek` and `push` by methods, which need not be annotated themselves (if one is, it must agree with the interface on being `indeterminate`). An implementing method may have a narrower return type, as `push` has here. A default method, such as `isEmpty`, is an operation of every user type that implements the interface.

A method of a user interface that has no parameters and returns one of the sequence classes of the [previous section](#sequence-components), such as `items()` above, declares a sequence-valued field: like a sequence component, it stands for the operations `itemsAt` and `itemsSize`, and the name `items` itself is reserved. This is the only way for two user types to have a sequence component of the same name: without `items()` in `Container`, the components of `Stack` and `Queue` would be a duplicate.

Furthermore:

- A user interface may extend other user interfaces. An operation belongs to the topmost interface that declares it.
- A user interface may be the type of a parameter and the return type of a user operation, in which case it stands for the sort `user`, like a user type. The last parameter may be a `UserSeq` of a user interface, which takes the values of all user types that implement it: `static int countItems(UserSeq<Container> containers)` is called as `countItems(Stack(1, 2), Queue(3))`. For a generic interface `I` the parameter is declared as `UserSeq<I<?>>`.
- A user interface need not be listed in the `userOperations` property (see [below](#making-the-classes-available)): it is found through the `implements` and `extends` clauses of the listed classes. Its simple name, `Container` here, must differ from those of all other user interfaces and user types of the grammar.

### The `user` sort

All user-defined types share the single sort `user`. In the graph editor this is the sort of variable nodes, expression nodes and constants of any user type:

- a variable node `user:` matches any user value;
- an expression node `user:Date(d.day, d.month, d.year)` stands for the value of the expression;
- in a host graph, a user value is written in the same way as the other constants, by its constructor applied to constant components: `user:Date(6,6,1966)`, or `let:birthday = Date(6,6,1966)` as an assignment on the node. A `string` component is written in double quotes, as elsewhere.

The display view shows user values in the same constructor notation: `Date(6, 6, 1966)`. Saved host graphs use the `let:` form.

**Display methods.** For a type with many components the constructor term gets wide. A `@UserType` record can therefore have a *display method* (since release 7.8.0): a public, non-static method without parameters with return type `String`, annotated with `@UserDisplay` (from the same package as the other annotations). A user type has at most one. The state display of the Simulator then shows a value of the type by the string that the method returns, with the constructor term in the tooltip of the node; the constructor term is also shown whenever aspect prefixes are shown. For instance, the method

```java
@UserDisplay
public String display() {
    return day() + "/" + month() + "/" + year();
}
```

added to `Date` would make the state display show `6/6/1966`. The display string is shown only: the value itself, and the way it is written, saved and parsed, do not change, so editors, saved graphs and transition labels keep showing `Date(6, 6, 1966)`. If the method throws an exception or returns `null` or the empty string, the constructor term is shown instead. A display method may be a `@UserOperation` as well.

#### Checking calls

The flip side of the uniformity of the `user` sort is that it does not distinguish between user types. Applying an accessor or an operation to a value of the wrong user type — `day(p)` where `p` happens to be a `Point` — yields the error value `NaU` at run time, in the same way that dividing by zero yields `NaI`. Rule parameters, control variables and attribute fields of sort `user` likewise accept a value of any user type.

Where the type of an argument is known when the grammar is loaded, however, a call is checked against the Java types of the operation (since release 7.8.0; up to release 7.7.1 such a call loaded and yielded `NaU`). The type of an argument is known if it is a user constant or a constructor call, or a call of a user operation, whose type is then the return type of the method that implements it. A call that cannot succeed is an error in the grammar: in a grammar that lists `Poly` and the [containers](#user-interfaces), `coeffsSize(Stack(1, 2))` gives

    User operation 'coeffsSize' is not implemented for a value of type Stack, only for Poly

and `countCoeffs(Poly("p", 1), Stack(1))` gives

    Parameter 1 of user operation 'countCoeffs' should have type Poly rather than Stack

(the parameters counted from 0). Since `push(s, 1)` is a `Container` whatever `s` is, `coeffsSize(push(s, 1))` is an error as well, while `peek(push(s, 1))` is accepted. A variable or a field, such as `p` in `day(p)`, has no known type, so a call on it is never rejected and a mismatch still yields `NaU` at run time.

### Making the classes available

Two things are needed for a grammar to use your classes.

- **The `userOperations` system property** (see [Advanced rule features](manual_advanced.html#system-properties)) lists the qualified names of the annotated classes, separated by spaces: `user.Date` or `user.Date org.example.MyOps`. A name of the form `package.*` (since release 7.8.0) stands for all top-level classes of that package, not of its subpackages, that are a `@UserType` or a `@UserInterface` or declare a `@UserOperation`: `user.*` lists `Stack`, `Queue` and any other user class of the package `user`. The other classes of the package, such as helper classes, are left alone. It is an error if the package has no such class, or if the `*` is not the last part of the name; a class of the package that cannot be loaded is skipped with a warning. Every user type that occurs as a parameter or return type of a listed operation must itself be listed, whereas a [user interface](#user-interfaces) need not be. Every listed class is checked when the grammar is loaded: a class that cannot be found, an annotated method that is not public, or that is not static although it is not in a user type or user interface, a parameter of an unsupported type, a duplicate name, or a user interface with the simple name of another user interface or user type is reported as an error on the property. Changing the property reloads the grammar, so the operations become available in the editor right away.

- **The classes must be where GROOVE can load them.** Compile them against the GROOVE jar — `lib/groove-x.y.z.jar` of a stand-alone installation, or the Maven artifact `nl.utwente.groove:groove`. GROOVE then looks for each named class in four places (since release 7.6.0; earlier releases only search the first), in this order, and takes the first that has it:

  - *On the class path* of the Java virtual machine that runs GROOVE, as described below. This comes first because the other three are searched by a class loader whose parent is GROOVE's own, and Java consults the parent first.
  - *In the grammar directory*: the subdirectory `classes` of the `.gps` directory, holding compiled `.class` files in their package structure, followed by the jars in the subdirectory `lib`, sorted by file name without regard to case, so in the same order on every platform. The classes travel with the grammar, also when it is zipped or saved elsewhere, which makes this the form for distributing a grammar.
  - *On the grammar class path*: the entries of the `userClassPath` system property, directories or jars separated by spaces, resolved relative to the grammar directory. This is the form for a grammar that lives inside the code base that defines its operations, as in the paper's repository: `userClassPath = ../target/classes`. An entry that does not exist is reported as an error on the property. Since the entries are relative, they do not survive zipping the grammar.
  - *On the user class path* of the installation: directories and jars that are set once per installation and apply to every grammar, for an implementation that lives somewhere the grammar should not know about. In the Simulator it is edited under Options &gt; User class path...; the command-line tools take it from the same setting, and override it for a single run with `--user-classpath entries` (entries separated by the platform's path separator) or the system property `groove.user.classpath`. An override replaces the stored value rather than extending it.

  The three GROOVE-managed forms work however GROOVE is started, including the runnable jars and the native installers. Jars are read from temporary copies, so a jar can be rebuilt while the grammar is open; the new version is picked up when the grammar is reloaded, which the Simulator does on a change of the `userOperations` or `userClassPath` property. If a named class is found nowhere, the error on the `userOperations` property lists the places that were searched; if it is found in more than one place, a warning on the property lists them all, the one in use first, so that a copy on the JVM class path silently taking precedence over the grammar's own does not go unnoticed.

  For the first form, start the component you want with an explicit class path that includes both the GROOVE jar and your classes:

    ```
    java -cp "lib/*:/path/to/your/classes" nl.utwente.groove.Simulator
    ```

    from the directory of a stand-alone installation (on Windows, separate the entries with `;` instead of `:`). The Generator, Imager and the other [runnable components](installing.html#running) are started in the same way, by replacing `Simulator` with their name. The runnable jars in the `bin` directory and the native installers run with a fixed class path and cannot pick up classes this way. On Java 24 or newer, add `--enable-native-access=ALL-UNNAMED` before `-cp` to avoid a warning from the Simulator's look-and-feel library.

If your classes are a Maven project, the dependency is

```xml
<dependency>
    <groupId>nl.utwente.groove</groupId>
    <artifactId>groove</artifactId>
    <version>8.0.0</version>
</dependency>
```

and if the project is modular, its `module-info.java` should declare `requires nl.utwente.groove;`.

### Using them in rules

Once declared, user-defined operations are called by name in expressions, in the functional style of the built-in named operations, and they combine freely with those:

- `isLeapYear(y)`, `sqrt(x)` — a static operation;
- `Date(1, m, y)` — a constructor;
- `year(d)`, `daysSince(d)`, `next(d)` — an accessor or instance operation, with the `user` value as its first argument;
- `let:birthday = today()` — an assignment of a user value to an attribute;
- `test:daysSince(birthday) > 7000` — a test involving one.

Note that a user value is *not* a graph node, so its components cannot be reached by the `name.field` notation; that is what the accessors are for. In the example rules below, `d.day` refers to the `day` attribute of a graph node `d` (of node type `Date`), whereas `day(date)` would retrieve the `day` component of the user value `date`.

### Indeterminate operations

An operation can be declared *indeterminate*:

```java
@UserOperation(indeterminate = true)
public static int roll() {
    return 1 + random.nextInt(6);
}
```

This tells GROOVE that the result is *not* determined by the arguments: the prototypical examples are a random generator and the current date or time. The consequences for a rule that calls such an operation are:

- The operation is evaluated afresh for every application of the rule, and its outcome is part of the identity of the resulting transition: two applications of the same rule at the same match may lead to different target states.
- The matches of the rule are recomputed in every state, rather than inherited from the previous state as GROOVE normally does where possible, since they may depend on the outcome.
- Exploring the same grammar twice may give different state spaces — which is exactly what randomness means. Note that this also affects reproducibility: a model checking result obtained on one run need not hold on the next.
- An indeterminate operation should **not** be used on a quantified level of a [nested rule](manual_advanced.html#nested-rules): the matches of the nested levels cannot be reliably reconstructed from an outcome that is not fixed. Releases 7.5.3 to 7.8.0 still accept such a rule; release 8.0.0 rejects it as an error (see [issue 911]({{site.groove_url}}/code/issues/911)). The way around it, shown in the `create` rule below, is to evaluate the operation on the top level, bind the result to a variable node, and use that variable in the nested conditions.

Whether or not an operation is indeterminate, GROOVE gives no guarantee about *when* or *how often* it invokes an operation during matching and transformation. Operations should therefore be free of side effects, or at least of side effects that GROOVE could notice: the correctness of the state space depends on it.

### Example: dates

The following class defines a user type `Date` with a number of operations, two of them indeterminate. (This is the example of the paper mentioned above, lightly edited; it is also included, together with the rules below, in the [source of this manual]({{site.groove_url}}/nl-utwente-groove.github.io/tree/main/manual).)

```java
{% include_relative user-ops-src/user/Date.java %}
```

Together, the record and its annotated methods define the following operations:

| Operation | Signature | Kind |
| :--- | :--- | :--- |
| `Date` | `(int, int, int): user` | constructor |
| `day`, `month`, `year` | `(user): int` | accessors |
| `toString` | `(user): string` | instance operation |
| `daysSince` | `(user): int` | instance operation, indeterminate |
| `next` | `(user): user` | instance operation |
| `isLeapYear` | `(int): bool` | static operation |
| `today` | `(): user` | static operation, indeterminate |

The grammar that uses it has `userOperations = user.Date` in its system properties, and starts from a graph of two `Date`-typed nodes with `day`, `month` and `year` attributes — the kind of structure one would have to use *without* user types:

![](images/manual/user-ops-start-display.svg)

The rule `create` turns such a node into a `Person` whose `birthday` is a `Date` *value*, constructed from the node's attributes. Moreover, the created `Person` is `pregnant` on the condition that they are young enough, which is expressed by the optional existential level `young` (see [Nested rules](manual_advanced.html#named-nesting-levels)): the level matches if the number of days since the birthday lies between 7000 and 15000. Because `daysSince` is indeterminate it should not be called on the `young` level itself, so the rule computes it on the top level, into the variable `age`, and tests `age` on the nested level. The test is carried by a `bool:true` node connected to the quantifier by an `at`-edge: such a constant node matches exactly once, so it serves as a neutral anchor for a condition that involves no graph node of its own.

| Edit view | Display view |
| :---: | :---: |
| ![](images/manual/user-ops-create-edit.svg) | ![](images/manual/user-ops-create-display.svg) |

The `Date` value is also declared as an output parameter of the rule (`parout:0`), so that it appears in the transition label and can be bound to a control variable. The rule `birth` lets a `pregnant` person give birth: the new `Person` is their `child`, with `today()` as their `birthday`; the parameter here is the birthday of the parent.

| Edit view | Display view |
| :---: | :---: |
| ![](images/manual/user-ops-birth-edit.svg) | ![](images/manual/user-ops-birth-display.svg) |

Finally, the rule `age` shows an instance operation applied to an attribute value, to store a person's age in days:

| Edit view | Display view |
| :---: | :---: |
| ![](images/manual/user-ops-age-edit.svg) | ![](images/manual/user-ops-age-display.svg) |

The grammar is driven by a [control program](manual_control.html) that uses a variable of sort `user` to pass the date from one rule to the other:

```
alap {
  user person := create();
  try { birth(person); }
}
```

Exploring this grammar from the start graph above yields transitions labelled `create(Date(6,6,1966))`, `create(Date(28,2,2000))` and `birth(Date(28,2,2000))` — the `Date` values appearing as transition arguments — in the possible orders. Whether `birth` is applicable at all depends on the day of the exploration, since only a person between roughly 19 and 41 years old is created `pregnant`; and the final graph contains a person born *today*:

![](images/manual/user-ops-final-display.svg)

(This final graph was obtained on 3 March 2026; an exploration on another day gives another birthday, and since `today()` is indeterminate, that is exactly what the state space records.)

### Limitations

The current implementation has the following restrictions:

- User-defined types and operations are only supported in the `default` algebra (see [Algebras](#algebras)).
- User types must be Java `record`s, and their components must be of the four primitive Java types; a user type cannot have a component of another user type, nor of an array or list type. The one exception is a [sequence](#sequence-components) as the last component.
- A sequence is not a sort: a node cannot hold a sequence, an operation cannot return one (only a user value that contains one), and a sequence parameter is filled by listing the elements. A user type has at most one sequence component, and the names derived from it (`coeffsAt`, `coeffsSize`) must be distinct from all other operation names, like any other.
- All user types share the sort `user`, so mismatches between user types are detected when the grammar is loaded only where the type of an argument is known then (see [Checking calls](#checking-calls)), and otherwise at run time, as the error value `NaU`.
- Operation names must be distinct: there is no overloading, and component names must be distinct across all user types as well, since they become accessor operations. The exception are the operations of a [user interface](#user-interfaces), which the user types that implement it share.
- Indeterminate operations should not be used on quantified levels of nested rules; release 8.0.0 rejects this.
- A user-defined operation yields a single value: a `Set`, `List` or other collection as return type is an error.
