---
title:
    "GROOVE download statistics"
permalink:
    downloads.html
summary:
    "Downloads per month since 2007, from SourceForge and GitHub, and the totals per release"
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
    downloads, statistics, releases
---

<div id="downloads" markdown="1">

<p class="dl-summary">Loading the download statistics…</p>

<div style="position: relative; height: 420px;"><canvas></canvas></div>

<p class="dl-controls">Series shown separately: the
<select class="dl-recent"><option>3</option><option selected>5</option><option>8</option><option>12</option><option value="all">all</option></select>
most recent minor versions.</p>

Downloads per month, stacked by minor version series: the most recent series
each have their own colour, all older versions are grey. Solid parts are downloads
from [SourceForge](https://sourceforge.net/projects/groove/files/stats/timeline),
hatched parts downloads from the [GitHub releases](https://github.com/nl-utwente-groove/code/releases).
The line is the cumulative total. Click a legend entry to hide or show a series; the
last month is still incomplete.

GitHub keeps only a cumulative download counter per file, so the GitHub numbers
are reconstructed by sampling the counters daily and differencing the samples. The
downloads before the first sample (the dashed line) cannot be placed in time; they
are included in the cumulative line from that day and in the table below. The
GitHub counters also count every automated fetch of a file (continuous-integration
runs, mirrors, scanners); nothing is filtered. SourceForge additionally records
the country and operating system of each download; that breakdown is on
[SourceForge's own statistics page](https://sourceforge.net/projects/groove/files/stats/timeline).

The data are collected in the repository
[nl-utwente-groove/download-stats](https://github.com/nl-utwente-groove/download-stats),
whose README describes the files and their caveats.

## Totals per release

Versions 6.8.1 and later are available from both sites; the versions before that only from
SourceForge. The last row counts the files that belong to no release: documentation, sample
grammars and old files.

<table class="dl-table"></table>

</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script>
<script src="js/downloads.js"></script>
