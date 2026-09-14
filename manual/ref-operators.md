---
title:
    "Reference: data operations"
permalink:
    manual_ref_operators.html
summary:
    "All built-in operations on the data sorts"
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
    attribute, operation, sort, reference
---

*This page is generated from the GROOVE 7.5.4-SNAPSHOT source code by `manual/make-refs.sh`; do not edit it by hand.*

The built-in operations per data sort, usable in the attribute expressions described in the [Advanced rule features](manual_advanced.html) chapter. Operations with a symbol can be written in prefix or infix notation; all operations can be called by name, in functional style.

## Sort `bool`

| Operation | Symbol | Description |
| :--- | :---: | :--- |
| <code>and(bool, bool): bool</code> | <code>&amp;</code> | Conjunction |
| <code>bigand(bool): bool</code> |  | Collective conjunction |
| <code>bigor(bool): bool</code> |  | Collective disjunction |
| <code>eq(bool, bool): bool</code> | <code>==</code> | Equality test |
| <code>neq(bool, bool): bool</code> | <code>!=</code> | Inequality test |
| <code>not(bool): bool</code> | <code>!</code> | Inversion |
| <code>or(bool, bool): bool</code> | <code>&#124;</code> | Disjunction |

## Sort `int`

| Operation | Symbol | Description |
| :--- | :---: | :--- |
| <code>abs(int): int</code> |  | Absolute value |
| <code>add(int, int): int</code> | <code>+</code> | Addition |
| <code>bigmax(int): int</code> |  | Collective maximum |
| <code>bigmin(int): int</code> |  | Collective minimum |
| <code>div(int, int): int</code> | <code>/</code> | Division |
| <code>eq(int, int): bool</code> | <code>==</code> | Equality test |
| <code>ge(int, int): bool</code> | <code>&gt;=</code> | Greater-or-equal test |
| <code>gt(int, int): bool</code> | <code>&gt;</code> | Greater-than test |
| <code>ite(bool, int, int): int</code> |  | If-then-else |
| <code>le(int, int): bool</code> | <code>&lt;=</code> | Lesser-or-equal test |
| <code>lt(int, int): bool</code> | <code>&lt;</code> | Lesser-than test |
| <code>max(int, int): int</code> |  | Maximum |
| <code>min(int, int): int</code> |  | Minimum |
| <code>mod(int, int): int</code> | <code>%</code> | Modulo |
| <code>mul(int, int): int</code> | <code>*</code> | Multiplication |
| <code>neg(int): int</code> | <code>-</code> | Inversion |
| <code>neq(int, int): bool</code> | <code>!=</code> | Inequality test |
| <code>prod(int): int</code> |  | Product |
| <code>sub(int, int): int</code> | <code>-</code> | Subtraction |
| <code>sum(int): int</code> |  | Summation |
| <code>toReal(int): real</code> | <code>(real)</code> | Real number conversion |
| <code>toString(int): string</code> | <code>(string)</code> | Conversion to STRING |

## Sort `real`

| Operation | Symbol | Description |
| :--- | :---: | :--- |
| <code>abs(real): real</code> |  | Absolute value |
| <code>add(real, real): real</code> | <code>+</code> | Addition |
| <code>bigmax(real): real</code> |  | Collective maximum |
| <code>bigmin(real): real</code> |  | Collective minimum |
| <code>div(real, real): real</code> | <code>/</code> | Division |
| <code>eq(real, real): bool</code> | <code>==</code> | Equality test |
| <code>ge(real, real): bool</code> | <code>&gt;=</code> | Greater-or-equal test |
| <code>gt(real, real): bool</code> | <code>&gt;</code> | Greater-than test |
| <code>ite(bool, real, real): real</code> |  | If-then-else |
| <code>le(real, real): bool</code> | <code>&lt;=</code> | Lesser-or-equal test |
| <code>lt(real, real): bool</code> | <code>&lt;</code> | Lesser-than test |
| <code>max(real, real): real</code> |  | Maximum |
| <code>min(real, real): real</code> |  | Minimum |
| <code>mul(real, real): real</code> | <code>*</code> | Multiplication |
| <code>neg(real): real</code> | <code>-</code> | Inversion |
| <code>neq(real, real): bool</code> | <code>!=</code> | Inequality test |
| <code>prod(real): real</code> |  | Product |
| <code>sub(real, real): real</code> | <code>-</code> | Subtraction |
| <code>sum(real): real</code> |  | Summation |
| <code>toInt(real): int</code> | <code>(int)</code> | Conversion to INT |
| <code>toString(real): string</code> | <code>(string)</code> | Conversion to STRING |

## Sort `string`

| Operation | Symbol | Description |
| :--- | :---: | :--- |
| <code>concat(string, string): string</code> | <code>+</code> | String concatenation |
| <code>eq(string, string): bool</code> | <code>==</code> | Equality test |
| <code>ge(string, string): bool</code> | <code>&gt;=</code> | Greater-or-equal test |
| <code>gt(string, string): bool</code> | <code>&gt;</code> | Greater-than test |
| <code>isBool(string): bool</code> |  | Boolean representation test |
| <code>isInt(string): bool</code> |  | Integer representation test |
| <code>isReal(string): bool</code> |  | Real number representation test |
| <code>ite(bool, string, string): string</code> |  | If-then-else |
| <code>le(string, string): bool</code> | <code>&lt;=</code> | Lesser-or-equal test |
| <code>length(string): int</code> |  | Length |
| <code>lookup(string, string): int</code> |  | Substring matching |
| <code>lt(string, string): bool</code> | <code>&lt;</code> | Lesser-than test |
| <code>neq(string, string): bool</code> | <code>!=</code> | Inequality test |
| <code>substring(string, int, int): string</code> |  | Substring |
| <code>suffix(string, int): string</code> |  | Suffix |
| <code>toBool(string): bool</code> |  | Boolean conversion |
| <code>toInt(string): int</code> | <code>(int)</code> | Conversion to INT |
| <code>toReal(string): real</code> | <code>(real)</code> | Real number conversion |

