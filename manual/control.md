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
    false
last_updated:
    false
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
tags:      # need to be included in _data/tags_doc.yml and have a page in tags/
keywords:  # used in metadata for findability
    control, program, recipe, function, sequencing

usermanual_suffix:
  /usermanual/blob/master
---
{% capture usermanual_url %}
  {{site.groove_url}}{{page.usermanual_suffix}}
{% endcapture %}

This chapter will describe the control language: an imperative language with sequencing, choice, loops, rule parameters, and function and recipe definitions, used to constrain the order in which rules are applied during exploration.

*This chapter has not yet been migrated to the web manual. Meanwhile, refer to the corresponding chapters of the [legacy PDF manual]({{usermanual_url}}/usermanual.pdf), keeping in mind that it is outdated in places. The control editor in the Simulator displays the current, authoritative grammar of the language in its right-hand help panel.*
