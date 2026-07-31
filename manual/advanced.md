---
title: # required
    "Advanced rule features"
permalink: # required, must match filename.html
    manual_advanced.html
summary:
    "Attributes, quantification, typing and rule properties"
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
    attribute, quantifier, type graph, rule properties, regular expression

usermanual_suffix:
  /usermanual/blob/master
---
{% capture usermanual_url %}
  {{site.groove_url}}{{page.usermanual_suffix}}
{% endcapture %}

This chapter will cover the features that go beyond plain structural rules: data attributes and the operations on them, universally quantified (nested) rules, regular expressions over edge labels, type graphs with inheritance and multiplicities, and the properties that can be set on individual rules.

*This chapter has not yet been migrated to the web manual. Meanwhile, refer to the corresponding chapters of the [legacy PDF manual]({{usermanual_url}}/usermanual.pdf), keeping in mind that it is outdated in places.*
