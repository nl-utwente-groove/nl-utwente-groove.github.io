---
title: # required
    "GROOVE user manual"
permalink: # required, must match filename.html
    manual.html
summary:
    "Entry point of the web edition of the user manual"
sidebar:
    manual_sidebar
toc: 
    false
last_updated:
    false
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
tags:      # need to be included in _data/tags_doc.yml and have a page in tags/
keywords:  # used in metadata for findability
    manual, documentation, reference

usermanual_suffix:
  /usermanual/blob/master
---
{% capture usermanual_url %}
  {{site.groove_url}}{{page.usermanual_suffix}}
{% endcapture %}

This is the web edition of the GROOVE user manual, which supersedes the legacy PDF manual. All chapters have been rewritten against the current feature set of the tool; the status column below records the tool version against which each chapter was last checked.

## Chapters

| Chapter | Contents | Checked against |
| :--- | :--- | :--- |
| [Introduction](manual_introduction.html) | What GROOVE is, the concepts behind it, and the components of the tool set | 7.5.3 |
| [Graphs and rules](manual_basics.html) | Editing host graphs and transformation rules | 7.5.3 |
| [Advanced rule features](manual_advanced.html) | Wildcards, regular expressions, attributes, parameters, quantification, typing and system properties | 7.5.3 |
| [Control language](manual_control.html) | Programming the order of rule applications | 7.5.3 |
| [Exploration and verification](manual_verification.html) | Exploration strategies and model checking | 7.5.3 |
| [Import and export](manual_io.html) | Storage formats, and exchanging graphs and grammars with other tools | 7.5.3 |

## Reference pages

In addition to the chapters, the manual contains reference pages that are *generated from the GROOVE source code*, so they cannot drift out of date:

- [Aspect prefixes](manual_ref_prefixes.html) — the complete label syntax for host graphs, rules and type graphs
- [Data operations](manual_ref_operators.html) — all built-in operations on the data sorts
- [Control grammar](manual_ref_control.html) — the full grammar of the control language
- [Strategies and acceptors](manual_ref_exploration.html) — the named vocabulary of the legacy exploration options

## Legacy PDF documents

The previous edition of the user manual is a PDF document, quite outdated with respect to the current feature set:

- [Full user manual]({{usermanual_url}}/usermanual.pdf)

## Help within the tool

Further help is available within the Simulator tool: when editing graphs or control programs, the right-hand side panel displays a range of options. Hovering on the options gives you further information about how to use them. Here are two example screenshots:

| Graph editor prefixes | Control language grammar |
|    :-------------------:     |  :---:  |
| &emsp; ![](images/graph-help.png) &emsp; | &emsp; ![](images/control-help.png) &emsp; |
