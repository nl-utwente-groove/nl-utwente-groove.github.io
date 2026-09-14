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

<style>
.dl-legend { text-align: center; font-size: 12px; color: #666; line-height: 1.8; }
.dl-legend-item { cursor: pointer; margin: 0 8px; white-space: nowrap; }
.dl-legend-item.dl-hidden { text-decoration: line-through; opacity: 0.6; }
.dl-swatch-line, .dl-swatch-box { display: inline-block; width: 32px; margin-right: 5px; vertical-align: middle; }
.dl-swatch-line { border-top: 2px solid #333; }
.dl-swatch-box { height: 12px; }
</style>

<div id="downloads" markdown="1">

<p class="dl-summary">Loading the download statistics…</p>

<div class="dl-legend"></div>
<div style="position: relative; height: 400px;"><canvas></canvas></div>

<p class="dl-controls">Period:
<select class="dl-period"><option value="10" selected>the last 10 years</option><option value="all">since 2007</option></select>
&nbsp; Series shown separately: the
<select class="dl-recent"><option>3</option><option selected>5</option><option>8</option><option>12</option><option value="all">all</option></select>
most recent minor versions.</p>

Downloads of the releases per month, from SourceForge and GitHub together, stacked by
minor version series: the most recent series each have their own colour, all older
versions are grey. The line is the cumulative total since 2007. Hovering over a month
gives the numbers per series and per site; clicking a legend entry hides or shows a
series. The last month is still incomplete. Downloads of the documentation, the sample
grammars and other files that belong to no release are not included.

The releases moved from [SourceForge](https://sourceforge.net/projects/groove/files/stats/timeline)
to [GitHub](https://github.com/nl-utwente-groove/code/releases) with version 6.8.0
(first dashed line); the releases since are on both sites. GitHub keeps only a cumulative
download counter per file, so the GitHub numbers are reconstructed by sampling the
counters daily and differencing the samples. The downloads before the first sample
(second dashed line) cannot be placed in time; they are included in the cumulative line
from that day and in the table below. The GitHub counters also count every automated
fetch of a file (continuous-integration runs, mirrors, scanners); nothing is filtered.
SourceForge additionally records the country and operating system of each download; that
breakdown is on [SourceForge's own statistics page](https://sourceforge.net/projects/groove/files/stats/timeline).

The data are collected in the repository
[nl-utwente-groove/download-stats](https://github.com/nl-utwente-groove/download-stats),
whose README describes the files and their caveats.

## Totals per release

<table class="dl-table"></table>

<p><a href="#" class="dl-expand"></a></p>

</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script>
<script src="js/downloads.js"></script>
