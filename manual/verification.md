---
title: # required
    "Exploration and verification"
permalink: # required, must match filename.html
    manual_verification.html
summary:
    "Exploration strategies and model checking"
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
    exploration, strategy, acceptor, model checking, CTL, LTL

usermanual_suffix:
  /usermanual/blob/master
---
{% capture usermanual_url %}
  {{site.groove_url}}{{page.usermanual_suffix}}
{% endcapture %}

This chapter will cover state space exploration and verification: the available exploration strategies (breadth-first, depth-first, linear, and others) and acceptors, how to configure them in the Simulator and the Generator, and model checking of CTL and LTL properties over the explored state space.

*This chapter has not yet been migrated to the web manual. Meanwhile, refer to the corresponding chapters of the [legacy PDF manual]({{usermanual_url}}/usermanual.pdf), keeping in mind that it is outdated in places.*
