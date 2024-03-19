---
title: # required
    "GROOVE user manual"
permalink: # required, must match filename.html
    manual.html
summary:
sidebar:
    home_sidebar
toc: 
    false
last_updated:
    false
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
tags:      # need to be included in _data/tags_doc.yml and have a page in tags/
keywords:  # used in metadata for findability
---
{% capture usermanual_url %}{{site.data.defs.groove_url}}/usermanual/blob/master{% endcapture %}

- {% data defs.groove_url %}

The user manual currently consists of two pdf documents, unfortunately both quite outdated with respect to the current feature set:

- [Full user manual]({{usermanual_url}}/usermanual.pdf)
- [Quick reference chart]({{usermanual_url}}/quick-reference.pdf)

Further help is available within the Simulator tool: when editing graphs or control programs, the right-hand side panel displays a range of options. Hovering on the options gives you further information about how to use them. Here are two example screenshots:

| Graph editor prefixes | Control language grammar |
|    :-------------------:     |  :---:  |
| &emsp; ![](images/graph-help.png) &emsp; | &emsp; ![](images/control-help.png) &emsp; |
