---
title: # required
    "Graphs and rules"
permalink: # required, must match filename.html
    manual_basics.html
summary:
    "Editing host graphs and transformation rules"
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
    graph, rule, editing, aspect, prefix

usermanual_suffix:
  /usermanual/blob/master
---
{% capture usermanual_url %}
  {{site.groove_url}}{{page.usermanual_suffix}}
{% endcapture %}

This chapter will explain how to read and edit host graphs and transformation rules: the visual notation (colours and shapes of the rule roles — readers, erasers, creators and embargoes), the label prefixes behind that notation, and the workflow of the graph editor in the Simulator.

*This chapter has not yet been migrated to the web manual. Meanwhile, refer to the corresponding chapters of the [legacy PDF manual]({{usermanual_url}}/usermanual.pdf), keeping in mind that it is outdated in places.*
