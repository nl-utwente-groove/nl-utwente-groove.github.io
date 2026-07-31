---
title:
    "Reference: control grammar"
permalink:
    manual_ref_control.html
summary:
    "The full grammar of the control language"
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
    control, grammar, syntax, reference
---

*This page is generated from the GROOVE 7.5.3 source code by `manual/make-refs.sh`; do not edit it by hand.*

The grammar of the control language (see the [Control language](manual_control.html) chapter), one nonterminal per section, as also shown in the help panel of the control editor in the Simulator. The first alternative of every nonterminal appears in the heading of its section.

## program

| Syntax | Explanation |
| :--- | :--- |
| <code>[package] import* ( function &#124; recipe &#124; stat )*</code> | Main program, consisting of a sequence top-level statements, control function definitions and recipe definitions. Java-like packages and imports are provided for modularity. |

## package_decl

| Syntax | Explanation |
| :--- | :--- |
| <code>package qual_name</code> | Causes all rules and procedures to be implicitly qualified by qual_name |

## import_decl

| Syntax | Explanation |
| :--- | :--- |
| <code>import qual_name</code> | Declares the last part of qual_name to stand for the entire name |

## qual_name

| Syntax | Explanation |
| :--- | :--- |
| <code>[qual_name .] name</code> | Name name in namespace qual_name. ; qual_name - optional namespace; name - sub-name; use backward quotes for reserved words, e.g., `any` or `out` |

## recipe

| Syntax | Explanation |
| :--- | :--- |
| <code>recipe name par_list [priority int] block</code> | Declares an atomic rule name, with parameters par_list and body block. The optional priority int assigns preference in a group call. ; name - name of the declared recipe; par_list - parameter list for the recipe; int - optional non-negative priority; block - recipe body |

## function

| Syntax | Explanation |
| :--- | :--- |
| <code>function name par_list block</code> | Declares the function name, with parameters par_list and body block. ; name - name of the declared function; par_list - parameter list for the function; block - function body |

## par_list

| Syntax | Explanation |
| :--- | :--- |
| <code>[ par (, par)* ]</code> | Possibly empty, comma-separated list of parameters |

## par

| Syntax | Explanation |
| :--- | :--- |
| <code>out var_type id</code> | Output parameter Variable id will receive a value in the course of the function or recipe. |
| <code>var_type id</code> | Input parameter Variable id is initialised by the argument passed into the call. |

## block

| Syntax | Explanation |
| :--- | :--- |
| <code>{ stat* }</code> | Possibly empty sequence of statements, surrounded by curly braces. |

## stat

| Syntax | Explanation |
| :--- | :--- |
| <code>var_decl ;</code> | A variable declaration. |
| <code>block</code> |  |
| <code>alap stat</code> | The body stat is repeated as long as it remains enabled. Enabledness is determined by the first rule of the statement. |
| <code>&lt; stat* &gt;</code> | Atomically evaluated sequence of statements, surrounded by angle brackets. The transitions in the body are only added to the transition system if they complete successfully |
| <code>while ( cond ) stat</code> | As long as the condition cond is successfully applied, the body stat is repeated. This is equivalent to "alap { cond ; stat }". |
| <code>until ( cond ) stat</code> | As long as the condition cond fails, the body stat is repeated. Note that if this terminates, the last action is an application of cond. |
| <code>do stat while ( cond )</code> | Statement stat is executed repeatedly, as long as afterwards the condition cond is enabled. If enabled, cond is also executed. Equivalent to "stat while ( cond ) stat" |
| <code>do stat until ( cond )</code> | Statement stat is executed repeatedly, as long as afterwards the condition cond is not enabled. Note that if this terminates, the last action is an application of cond. Equivalent to "stat until ( cond ) stat" |
| <code>if ( cond ) stat1 [else stat2]</code> | If condition cond is enabled, it is executed and next stat1 is executed; otherwise, the optional stat2 is executed. |
| <code>try stat1 [else stat2]</code> | Statement stat1 is executed if it is enabled, otherwise the (optional) stat2 is executed. |
| <code>choice stat (or stat)+</code> | Nondeterministic choice of statements. |
| <code>expr ;</code> | An expression used as a statement. |
| <code>halt</code> | Stops exploration, without marking the state as final |

## var_decl

| Syntax | Explanation |
| :--- | :--- |
| <code>var_type id (, id)* [ := call ]</code> | Declares a list of variables, all of the same var_type. Optionally simultaneously initialises the declared variables through an assignment call. |

## cond

| Syntax | Explanation |
| :--- | :--- |
| <code>cond1 &#124; cond2</code> | Nondeterministic choice between cond1 and cond2. |
| <code>true</code> | Condition that always succeeds. |
| <code>call</code> | Tests the enabledness of a given function or rule. Note that the function or rule is in fact executed if enabled. |

## expr

| Syntax | Explanation |
| :--- | :--- |
| <code>expr1 &#124; expr2</code> | Nondeterministic choice between expr1 and expr2. Equivalent to "choice expr1 ; or expr2 ;", except that this is an expression and choice is a statement. |
| <code>expr +</code> | Nondeterministically executes expr one or more times. Equivalent to "expr ; expr *". |
| <code>expr *</code> | Nondeterministically executes expr zero or more times. Note that this is not equivalent to "expr #" or "alap expr ;". |
| <code># expr</code> | Executes expr as long as possible. Equivalent to "alap expr ;", except that this is an expression and alap is a statement. |
| <code>( expr )</code> | Bracketed expression. |
| <code>assign</code> | Invokes a function or rule, assigning the output parameters to target variables |
| <code>call</code> | Invokes a function or rule. |

## assign

| Syntax | Explanation |
| :--- | :--- |
| <code>id1 (, id2)* := call</code> | Rule call of call with assignment syntax for output parameters. The argument list of call contains only non-output parameters. The id1/id2-list corresponds to the output parameters of the call. ; id1 - variable serving as output parameter ; id2 - optional further output parameters; call - the call, with only (optionally) non-output arguments |

## call

| Syntax | Explanation |
| :--- | :--- |
| <code>rule_name [ ( arg_list ) ]</code> | Invokes a rule, procedure or group rule_name, with optional arguments arg_list. ; rule_name - the rule, procedure or group name; arg_list - optional comma-separated list of arguments |

## rule_name

| Syntax | Explanation |
| :--- | :--- |
| <code>[ qual_name . ] name</code> | Explicit rule or procedure (i.e., recipe or function) call of name, optionally qualified with qual_name ; qual_name - optional (qualified) package name; name - rule or procedure name; use backward quotes for reserved words, e.g., `any` or `out` |
| <code>[ qual_name . ] [ * . ] group</code> | Invokes all (if group is any) or all not explicitly invoked (if group is other) actions in qual_name (including all subpackages if group is preceded by *) or in the current scope if qual_name and * are absent ; qual_name - optional (qualified) package name; group - any or other |

## arg_list

| Syntax | Explanation |
| :--- | :--- |
| <code>[ arg (, arg)* ]</code> | Possibly empty, comma-separated list of arguments |

## arg

| Syntax | Explanation |
| :--- | :--- |
| <code>out id</code> | Output argument Variable id will receive a value through the call. |
| <code>DONT_CARE</code> | Don't-care argument The parameter does not affect the match or the control state. |
| <code>true</code> | Boolean value for truth. |
| <code>false</code> | Boolean value for falsehood. |
| <code>"text"</code> | String constant with value text. |
| <code>number</code> | Integer constant with value number. |
| <code>number . number</code> | Real number constant. |

## in_arg

| Syntax | Explanation |
| :--- | :--- |
| <code>unary in_arg</code> | Unary operator expression as input argument Applies unary operator unary to in_arg and passes the result into the call ; unary - unary operator symbol; in_arg - expression to which unary is applied |
| <code>in_arg1 binary in_arg2</code> | Binary operator expression as input argument Applies binary operator in_arg1 to binary and in_arg2 and passes the result into the call ; in_arg1 - first argument for in_arg1; binary - binary operator symbol; in_arg2 - second argument for in_arg1 |
| <code>id</code> | Variable input argument Variable id must be bound to a value, which will be passed into the call. |
| <code>op(in_arg (, in_arg)*)</code> | Operator invocation as input argument Applies operator op to the comma-separated list of in_arg, and passes the result into the call ; op - operator name; in_arg - first argument for op; in_arg - optional further arguments for op |
| <code>( in_arg )</code> | Parenthesised input argument |

## var_type

| Syntax | Explanation |
| :--- | :--- |
| <code>node</code> | The type of all non-value nodes. |
| <code>bool</code> | The type of boolean values. |
| <code>string</code> | The type of string values. |
| <code>int</code> | The type of integer values. |
| <code>real</code> | The type of real number values. |
| <code>user</code> | User-defined type. |

