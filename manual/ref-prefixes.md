---
title:
    "Reference: aspect prefixes"
permalink:
    manual_ref_prefixes.html
summary:
    "All node and edge label syntax, per graph role"
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
    aspect, prefix, syntax, reference
---

*This page is generated from the GROOVE 7.5.4-SNAPSHOT source code by `manual/make-refs.sh`; do not edit it by hand.*

The tables below list the complete label syntax for the three kinds of aspect graph, as also shown in the help panel of the graph editor in the Simulator. Optional parts are shown in square brackets.

## Host graphs

### Node labels

| Syntax | Explanation |
| :--- | :--- |
| <code>sort:constant</code> | Constant value node Represents value constant of sort sort ; sort - one of the primitive sorts bool, int, real, user or string; constant - literal value of sort sort |
| <code>color:(rgb&#124;name)</code> | Node type colour Sets the colour of the nodes and outgoing edges upon rule application. ; rgb - comma-seperated list of three colour dimensions, with range 0..255; name - color name |
| <code>id:name</code> | Node identifier Assigns the (graph-local) name name to this node. Nodes with the same identifier will be merged, both within a single graph and across multiple enabled start graphs; their types must coincide. ; name - the declared name for this node |
| <code>let:field=constant</code> | Initialisation Sets the attribute field field to the initial value constant ; field - field name; constant - literal value of a primitive sort (one of the primitive sorts bool, int, real, user or string) |
| <code>rem:</code> | Declares a remark node, to be used for documentation |
| <code>rem:text</code> | Places a remark on an arbitrary node, to be used for documentation |

### Edge labels

| Syntax | Explanation |
| :--- | :--- |
| <code>regexpr</code> | Regular expression path Tests for a path satisfying regexpr. To specify a regular label containing non-standard characters, prefix with ':'. ; regexpr - regular expression; for syntax see the appropriate tab |
| <code>:free</code> | Literal edge label Specifies a free-labelled edge, where free may be an arbitrary string Only for use in untyped rule systems ; free - a string of arbitrary characters |
| <code>mult=count:label</code> | Parallel edge multiplicity Declares the label-edge to stand for count parallel copies. Only allowed if the semantics grammar property is SPO-multi or DPO. ; count - number of parallel copies; a positive constant; label - edge label text; identifier with optional hyphens |
| <code>rem:text</code> | Declares a remark edge with (free-formatted) label text |

## Rules

### Node labels

| Syntax | Explanation |
| :--- | :--- |
| <code>sort:[expr]</code> | Variable or value node Declares a sort node, optionally with value determined by expr ; sort - one of the primitive sorts bool, int, real, user or string; expr - Optional expression of sort sort, determining the value of the node. (For expression syntax see the appropriate tab) |
| <code>ask:nr</code> | Interactive rule parameter Declares interactive rule parameter nr. The value is provided (upon rule application) through a value oracle, set in the system properties. ; nr - the parameter number, ranging from 0. Parameter numbers must be unique and contiguous |
| <code>cnew:</code> | Conditional node creator Tests for the absence of a node; creates it upon application. |
| <code>cnew[=q]:flag:flag</code> | Conditional flag creator Tests for the absence of flag; creates it upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; flag - flag label text; identifier with optional hyphens |
| <code>cnew[=q]:let:field=expr</code> | Conditional attribute field creator Tests for the absence of an attribute field field with current value expr; creates the field upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; field - created field name; expr - arithmetic expression; for syntax see the appropriate tab |
| <code>color:(rgb&#124;name)</code> | Node type colour Sets the initial colour of the nodes and outgoing edges. ; rgb - comma-seperated list of three colour dimensions, with range 0..255; name - color name |
| <code>del:</code> | Node eraser Tests for the presence of a node; deletes it upon application. |
| <code>del[=q]:sort:name</code> | Attribute field eraser Tests for the presence of an attribute field name of type sort and arbitrary value; deletes the field upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; sort - one of the primitive sorts bool, int, real, user or string; name - erased field name |
| <code>del[=q]:flag:flag</code> | Flag eraser Tests for the presence of a flag-flag; deletes it upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; flag - erased flag |
| <code>del[=q]:let:name=expr</code> | Attribute field test and eraser Tests for the presence of an attribute field name with current value expr; deletes the field upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; name - erased field name; expr - arithmetic expression; for syntax see the appropriate tab |
| <code>exists[=q]:</code> | Existential quantier Tests for the mandatory existence of a graph pattern, and transforms it. Pattern nodes must have outgoing @-edges to the quantifier node; pattern edges may be declared by adding the quantifier level name '=q' to their role aspect (one of use, del, new, cnew or not). ; q - (optional) declared name for this quantifier level. (alternatively given through an id-declation) |
| <code>existsx[=q]:</code> | Optional existential quantifier Tests for the optional existence of a graph pattern, and transforms it if found. Pattern nodes must have outgoing @-edges to the quantifier node; pattern edges may be declared by adding the quantifier level name '=q' to their role aspect (one of use, del, new, cnew or not). ; q - (optional) declared name for this quantifier level. (alternatively given through an id-declation) |
| <code>forall[=q]:</code> | Universal quantifier Matches and transforms all occurrences of a graph pattern. The actual number of occurrences is given by an optional outgoing count-edge. Pattern nodes must have outgoing @-edges to the quantifier node; pattern edges may be declared by adding the quantifier level name '=q' to their role aspect (one of use, del, new, cnew or not). ; q - (optional) declared name for this quantifier level. (alternatively given through an id-declation) |
| <code>forallx[=q]:</code> | Non-vacuous universal quantifier Matches and transforms all occurrences of a graph pattern, provided there is at least one. The actual number of occurrences is given by an optional outgoing count-edge. Pattern nodes must have outgoing @-edges to the quantifier node; pattern edges may be declared by adding the quantifier level name '=q' to their role aspect (one of use, del, new, cnew or not). ; q - (optional) declared name for this quantifier level. (alternatively given through an id-declation) |
| <code>id:name</code> | Node identifier Assigns the (graph-local) name name to this node. A node identifier serves the following purpose, depending on the kind of node: • For regular graph nodes, to qualify field names within expressions; • For variable nodes, to refer to the variable in expressions; • For quantifier nodes, to associate edges with the quantifier level. ; name - the declared name for this node; must be unique within the graph |
| <code>let:field=expr</code> | Assignment Assigns the value of expr to the (existing) attribute field field. The previous value of field is lost ; field - field name; expr - arithmetic expression; for syntax see the appropriate tab |
| <code>new:</code> | Node creator Creates a node upon application. |
| <code>new[=q]:flag:flag</code> | Flag creator Creates a flag upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; flag - created flag |
| <code>new[=q]:letfield=expr</code> | Attribute field creator Upon application, creates an attribute field field with value expr. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; field - created field name; expr - arithmetic expression; for syntax see the appropriate tab |
| <code>not:</code> | Node embargo Tests for the absence of a node. |
| <code>not[=q]:sort:field</code> | Attribute field embargo Tests for the absence of an attribute field field of type sort and arbitrary value. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; sort - one of the primitive sorts bool, int, real, user or string; field - erased field name |
| <code>not[=q]:flagflag</code> | Flag embargo Tests for the absence of a flag. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; flag - forbidden flag |
| <code>par:</code> | Anchor node Declares an explicit anchor node. This causes the node to be considered relevant in distinguishing matches even if it is not involved in any deletion, creation or merging. |
| <code>par:nr</code> | Bidirectional rule parameter Declares bidirectional rule parameter nr. In a control program this parameter may be instantiated with a concrete value, or be used as an output parameter, in which case the value is determined by the matching. ; nr - the parameter number, ranging from 0. Parameter numbers must be unique and contiguous |
| <code>parin:nr</code> | Rule input parameter Declares rule input parameter nr. The value must be provided through a control program, or by setting the 'algebra family' in the system properties to point ; nr - the parameter number, ranging from 0. Parameter numbers must be unique and contiguous |
| <code>parout:nr</code> | Rule output parameter Declares rule output parameter nr. ; nr - the parameter number, ranging from 0. Parameter numbers must be unique and contiguous |
| <code>prod:</code> | Product node Declares a product node, corresponding to a tuple of attribute nodes. |
| <code>rem:</code> | Declares a remark node, to be used for documentation |
| <code>rem:text</code> | Places a remark on an arbitrary node, to be used for documentation |
| <code>test:constraint</code> | Predicate expression Tests if the boolean expression constraint holds in the graph. ; constraint - expression of sort bool; see the appropriate type for syntax |
| <code>test:name=expr</code> | Attribute value test Tests if the attribute field name equals the value of expr. |
| <code>use=q:sort:field</code> | Attribute field test Tests for the presence of an attribute field field on quantification level q of type sort and arbitrary value. ; q - associated quantifier level; sort - one of the primitive sorts bool, int, real, user or string; field - tested field name |
| <code>use=q:flag:flag</code> | Quantified reader flag Tests for the presence of a flag-flag on quantification level q. ; q - associated quantifier level; flag - tested flag |

### Edge labels

| Syntax | Explanation |
| :--- | :--- |
| <code>regexpr</code> | Regular expression path Tests for a path satisfying regexpr. To specify a regular label containing non-standard characters, prefix with ':'. ; regexpr - regular expression; for syntax see the appropriate tab |
| <code>sort:op</code> | Primitive operator Applies operation op from the signature sort to the arguments of the source node (which must be a product node). ; sort - sort of the operator; one of the primitive sorts bool, int, real, user or string; op - operator of sort sort |
| <code>:free</code> | Literal edge label Specifies a free-labelled edge, where free may be an arbitrary string Only for use in untyped rule systems ; free - a string of arbitrary characters |
| <code>arg:nr</code> | Argument edge Projects its source product node (prod) onto argument nr. ; nr - argument number, ranging from 0 to the product node arity - 1. Argument numbers from one prod-node must be unique and contiguous |
| <code>cnew[=q]:label</code> | Conditional edge creator Tests for the absence of a label-edge; creates it upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; label - edge label text; identifier with optional hyphens |
| <code>del[=q]:label</code> | Edge eraser Tests for the presence of a label-edge; deletes it upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; label - label of the erased edge |
| <code>new[=q]:label</code> | Edge creator Creates a label-edge upon application. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; label - label of the created edge |
| <code>not[=q]:regexpr</code> | Edge embargo Tests for the absence of a path satisfying regexpr. The optional q denotes the associated quantifier level. ; q - optional associated quantifier level; regexpr - regular expression; for syntax see the appropriate tab |
| <code>or:</code> | Embargo choice Declares a choice between two negative application patterns. Source and target node must be part of two distinct NACs |
| <code>path:regexpr</code> | Regular path expression Tests for a path satisfying the regular expression regexpr. ; regexpr - regular expression; for syntax see the appropriate tab |
| <code>rem:text</code> | Declares a remark edge with (free-formatted) label text |
| <code>use=q:regexpr</code> | Quantified reader edge Tests for a path satisfying regexpr, on quantification level q. ; q - associated quantifier level; regexpr - regular expression; for syntax see the appropriate tab |
| <code>[nested:](@&#124;in&#124;count)</code> | Structural nesting edge Declares quantifier structure (the nested-prefix itself is optional): • in nests one quantifier within another; • @ connects a graph pattern node to a quantifier; • count points to the cardinality of a quantifier. |

## Type graphs

### Node labels

| Syntax | Explanation |
| :--- | :--- |
| <code>sort:field</code> | Attribute field Declares field to be attribute field of sort sort ; sort - one of the primitive sorts bool, int, real, user or string; field - attribute field name |
| <code>abs:</code> | Abstract node type Declares a node type to be abstract. Only nodes of concrete subtypes can actually exist. |
| <code>abs:flag</code> | Abstract flag type Declares an abstract flag for a node type. The flag can only occur on subtypes where it is redeclared concretely. ; flag - flag label text; identifier with optional hyphens |
| <code>color:(rgb&#124;name)</code> | Node type colour Declares the color of all nodes and outgoing edges of a node type. ; rgb - comma-seperated list of three colour dimensions, with range 0..255; name - color name |
| <code>edge:"format"[,field+]</code> | Nodifier edge pattern Declares the node type to be a nodified edge, meaning that it will not be displayed as a node. Instead, the incoming edges will be labelled by expanding format with string representations of the concrete values of the field list ; format - Label format string, with parameter syntax as in String.format; field - Comma-separated list of attribute field names |
| <code>import:</code> | Imported node type Indicates that the type is imported from another type graph. This affects the behaviour of hiding (all elements of) a type graph. |
| <code>rem:</code> | Declares a remark node, to be used for documentation |
| <code>rem:text</code> | Places a remark on an arbitrary node, to be used for documentation |

### Edge labels

| Syntax | Explanation |
| :--- | :--- |
| <code>regexpr</code> | Regular expression path Tests for a path satisfying regexpr. To specify a regular label containing non-standard characters, prefix with ':'. ; regexpr - regular expression; for syntax see the appropriate tab |
| <code>abs:label</code> | Abstract edge type Declares an abstract label-edge between node types. The edge can only occur between subtypes where it is redeclared concretely. ; label - edge label text; identifier with optional hyphens |
| <code>in=[lo..]hi:label</code> | Incoming edge multiplicity. Constrains the number of incoming label-edges for every node to at least lo (if specified) and at most hi ; lo - optional lower bound; hi - mandatory upper bound ('*' for unbounded); label - label of the incoming edge |
| <code>out=[lo..]hi:label</code> | Outgoing edge multiplicity. Constrains the number of outgoing label-edges for every node to at least lo (if specified) and at most hi ; lo - optional lower bound; hi - mandatory upper bound ('*' for unbounded); label - label of the outgoing edge |
| <code>part:label</code> | Composite edge property Declares an edge type to be composite. Composite edge types implicitly have incoming edge multiplicity 0..1 and their instances may not form a cycle in a graph. ; label - label of the composite edge type |
| <code>rem:text</code> | Declares a remark edge with (free-formatted) label text |
| <code>sub:</code> | Subtype declaration Declares the source type node to be a subtype of the target type node |

