---
title: # required
    "Import and export"
permalink: # required, must match filename.html
    manual_io.html
summary:
    "Exchanging graphs and grammars with other formats and tools"
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
    import, export, GXL, Ecore, EMF, image

usermanual_suffix:
  /usermanual/blob/master
---
{% capture usermanual_url %}
  {{site.groove_url}}{{page.usermanual_suffix}}
{% endcapture %}

This chapter will describe how to exchange models between GROOVE and other tools: the native GXL graph format, import and export of Ecore/EMF models, and export of graphs and rules to image and document formats via the Imager.

*This chapter has not yet been migrated to the web manual. Meanwhile, refer to the corresponding chapters of the [legacy PDF manual]({{usermanual_url}}/usermanual.pdf), keeping in mind that it is outdated in places.*
