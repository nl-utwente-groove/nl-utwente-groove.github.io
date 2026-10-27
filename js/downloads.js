// Renders the download statistics page: a monthly chart of the downloads of
// the GROOVE releases from SourceForge (since 2007) and GitHub (sampled
// since September 2026), stacked by minor version series, and a table of
// totals per release. The data are the CSV files of
// nl-utwente-groove/download-stats, read from raw.githubusercontent.com;
// that repository's README documents them. Needs Chart.js (UMD build)
// loaded before this script.
(function () {
  'use strict';

  var root = document.getElementById('downloads');
  if (!root) return;
  var BASE = root.getAttribute('data-base') ||
    'https://raw.githubusercontent.com/nl-utwente-groove/download-stats/main/';
  // Okabe-Ito for up to five recent series, oldest first; grey for the rest
  var COLOURS = ['#0072b2', '#56b4e9', '#009e73', '#e69f00', '#d55e00'];
  var EARLIER = '#8c8c8c';
  // rows of the table shown before expanding it
  var ROWS = 10;

  var chart = null;

  Promise.all([fetchCsv('snapshots.csv'), fetchCsv('sourceforge-monthly.csv'), fetchCsv('releases.csv')])
    .then(function (files) { render(derive(files[0], files[1], files[2])); })
    .catch(function (e) {
      root.querySelector('.dl-summary').textContent =
        'The download statistics could not be loaded (' + e.message + ').';
    });

  function fetchCsv(name) {
    return fetch(BASE + name).then(function (r) {
      if (!r.ok) throw new Error(name + ': HTTP ' + r.status);
      return r.text();
    }).then(function (text) {
      // no field contains a comma or a quote; see the README of the data
      return text.trim().split('\n').slice(1).map(function (l) { return l.split(','); });
    });
  }

  // 'release-7_5_3' and 'groove/7.5.3-whatever' give '7.5.3'; anything else
  // (docs, samples, old files, readmes) gives null
  function versionOf(path) {
    var m = /^release-(\d+)_(\d+)_(\d+)$/.exec(path);
    if (m) return m[1] + '.' + m[2] + '.' + m[3];
    m = /^groove\/(\d+\.\d+\.\d+)/.exec(path);
    return m ? m[1] : null;
  }

  function isTest(version) { return /^99\./.test(version); }

  function compareVersions(a, b) {
    var x = a.split('.').map(Number), y = b.split('.').map(Number);
    for (var i = 0; i < Math.max(x.length, y.length); i++) {
      if ((x[i] || 0) !== (y[i] || 0)) return (x[i] || 0) - (y[i] || 0);
    }
    return 0;
  }

  // Turns the files into monthly counts per version and source. GitHub
  // downloads of a month are the differences of consecutive daily counter
  // samples; a counter that dropped was re-uploaded and starts again at zero.
  // The counts of the very first sampling day accumulated since the release
  // was published and cannot be placed in months: they are kept apart as the
  // backlog, dated to the first sampling day. Files that belong to no
  // release only count towards `other`.
  function derive(gh, sf, rel) {
    var monthly = {};   // month -> version -> {sf, gh}
    var totals = {};    // version -> {sf, gh, backlog}
    var other = 0;
    var firstDay = null, lastDay = null;
    function total(version) {
      return totals[version] || (totals[version] = { sf: 0, gh: 0, backlog: 0 });
    }
    function add(month, version, source, n) {
      if (!n) return;
      if (!version) { other += n; return; }
      var m = monthly[month] || (monthly[month] = {});
      var c = m[version] || (m[version] = { sf: 0, gh: 0 });
      c[source] += n;
      total(version)[source] += n;
    }

    sf.forEach(function (r) { add(r[0], versionOf(r[1]), 'sf', +r[2]); });

    var byAsset = {};
    gh.forEach(function (r) {
      var date = r[0], tag = r[1], asset = r[2], count = +r[3];
      if (/^release-99_/.test(tag) || /read-?me/i.test(asset)) return;
      if (!firstDay || date < firstDay) firstDay = date;
      if (!lastDay || date > lastDay) lastDay = date;
      (byAsset[tag + '/' + asset] || (byAsset[tag + '/' + asset] = []))
        .push({ date: date, version: versionOf(tag), count: count });
    });
    var backlog = 0;
    Object.keys(byAsset).forEach(function (key) {
      var rows = byAsset[key].sort(function (a, b) { return a.date < b.date ? -1 : 1; });
      var prev = null;
      rows.forEach(function (r) {
        var n = prev === null ? r.count : r.count - prev;
        if (n < 0) n = r.count;
        if (prev === null && r.date === firstDay) {
          total(r.version).backlog += n;
          backlog += n;
        } else {
          add(r.date.slice(0, 7), r.version, 'gh', n);
        }
        prev = r.count;
      });
    });

    var releases = {};   // version -> {date, source}
    var firstGitHub = null;
    rel.forEach(function (r) {
      if (isTest(r[0])) return;
      releases[r[0]] = { date: r[1], source: r[2] };
      if (r[2] === 'github' && (!firstGitHub || r[1] < firstGitHub)) firstGitHub = r[1];
      // a release without downloads yet still gets its row and its series
      total(r[0]);
    });

    var versions = Object.keys(totals).sort(compareVersions);
    var minors = [];
    versions.forEach(function (v) {
      var m = minorOf(v);
      if (minors.indexOf(m) < 0) minors.push(m);
    });
    var months = Object.keys(monthly).sort();
    return {
      monthly: monthly,
      totals: totals,
      other: other,
      versions: versions,
      minors: minors,
      releases: releases,
      months: monthRange(months[0], months[months.length - 1]),
      firstDay: firstDay,
      lastDay: lastDay,
      firstGitHub: firstGitHub,
      backlog: backlog
    };
  }

  function monthRange(from, to) {
    var out = [], y = +from.slice(0, 4), m = +from.slice(5, 7);
    for (;;) {
      var label = y + '-' + (m < 10 ? '0' : '') + m;
      out.push(label);
      if (label >= to) return out;
      if (++m > 12) { m = 1; y++; }
    }
  }

  function minorOf(version) { return version.split('.').slice(0, 2).join('.'); }

  function format(n) { return n.toLocaleString('en-US'); }

  function monthName(month) {
    return new Date(month + '-15').toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
  }

  function colour(i, n) {
    if (n <= COLOURS.length) return COLOURS[i];
    return 'hsl(' + Math.round(300 * i / (n - 1)) + ', 60%, 45%)';
  }

  function render(data) {
    var sfTotal = 0, ghTotal = 0;
    data.versions.forEach(function (v) {
      sfTotal += data.totals[v].sf;
      ghTotal += data.totals[v].gh + data.totals[v].backlog;
    });
    root.querySelector('.dl-summary').innerHTML =
      '<b>' + format(sfTotal + ghTotal) + ' downloads of the releases</b> since ' +
      monthName(data.months[0]) + ': ' + format(sfTotal) + ' from SourceForge and ' +
      format(ghTotal) + ' from GitHub (GitHub counters sampled since ' + data.firstDay +
      ', last sample ' + data.lastDay + '). A further ' + format(data.other) +
      ' downloads of documentation, sample grammars and other files are not counted.';

    var recentSelect = root.querySelector('.dl-recent');
    var periodSelect = root.querySelector('.dl-period');
    function redraw() {
      var r = recentSelect ? recentSelect.value : '5';
      var p = periodSelect ? periodSelect.value : '10';
      drawChart(data,
        r === 'all' ? data.minors.length : Math.min(+r, data.minors.length),
        p === 'all' ? data.months.length : Math.min(12 * +p, data.months.length));
    }
    redraw();
    if (recentSelect) recentSelect.addEventListener('change', redraw);
    if (periodSelect) periodSelect.addEventListener('change', redraw);

    var versions = data.versions.slice().reverse();
    var html = '<thead><tr><th>Release</th><th>Date</th><th>SourceForge</th><th>GitHub</th><th>Total</th></tr></thead><tbody>' +
      '<tr><td><b>All</b></td><td></td><td>' + format(sfTotal) + '</td><td>' + format(ghTotal) +
      '</td><td><b>' + format(sfTotal + ghTotal) + '</b></td></tr>';
    versions.forEach(function (v, i) {
      var t = data.totals[v], r = data.releases[v];
      html += '<tr' + (i >= ROWS ? ' class="dl-more" hidden' : '') + '><td>' + v + '</td><td>' +
        (r ? r.date : '') + '</td><td>' + format(t.sf) + '</td><td>' + format(t.gh + t.backlog) +
        '</td><td><b>' + format(t.sf + t.gh + t.backlog) + '</b></td></tr>';
    });
    root.querySelector('table').innerHTML = html + '</tbody>';

    var expand = root.querySelector('.dl-expand');
    if (expand && versions.length > ROWS) {
      var expanded = false;
      function label() {
        expand.textContent = expanded ? 'Show only the ' + ROWS + ' most recent releases' :
          'Show all ' + versions.length + ' releases';
      }
      label();
      expand.addEventListener('click', function (e) {
        e.preventDefault();
        expanded = !expanded;
        root.querySelectorAll('.dl-more').forEach(function (tr) { tr.hidden = !expanded; });
        label();
      });
    }
  }

  // Draws the chart of the last `span` months with the `recent` most recent
  // minor series as separate segments, the older versions lumped, and the
  // cumulative total (since the beginning, not since the start of the
  // window) as a line.
  function drawChart(data, recent, span) {
    var all = data.months;
    var start = all.length - span;
    var months = all.slice(start);
    var recentMinors = data.minors.slice(data.minors.length - recent);
    // segments from the bottom of the stack up
    var segments = [];
    if (recentMinors.length < data.minors.length) {
      segments.push({ key: 'earlier', label: 'earlier versions', colour: EARLIER });
    }
    recentMinors.forEach(function (m, i) {
      segments.push({ key: m, label: 'version ' + m + '.x', colour: colour(i, recentMinors.length) });
    });
    function segmentOf(version) {
      var m = minorOf(version);
      return recentMinors.indexOf(m) < 0 ? 'earlier' : m;
    }

    var series = {};
    segments.forEach(function (s) {
      series[s.key] = { sf: months.map(function () { return 0; }), gh: months.map(function () { return 0; }) };
    });
    var cumulative = [], running = 0, windowed = [], sinceStart = 0;
    var backlogMonth = data.firstDay ? data.firstDay.slice(0, 7) : null;
    all.forEach(function (month, i) {
      var m = data.monthly[month] || {}, n = 0;
      Object.keys(m).forEach(function (v) {
        n += m[v].sf + m[v].gh;
        if (i >= start) {
          var s = series[segmentOf(v)];
          s.sf[i - start] += m[v].sf;
          s.gh[i - start] += m[v].gh;
        }
      });
      if (month === backlogMonth) n += data.backlog;
      running += n;
      if (i >= start) {
        sinceStart += n;
        cumulative.push(running);
        windowed.push(sinceStart);
      }
    });

    var datasets = segments.map(function (s) {
      return { label: s.label, sf: series[s.key].sf, gh: series[s.key].gh,
        data: series[s.key].sf.map(function (n, i) { return n + series[s.key].gh[i]; }),
        backgroundColor: s.colour, stack: 'downloads', order: 2 };
    });
    datasets.push({ type: 'line', label: 'total since 2007', yAxisID: 'y2',
      data: cumulative, borderColor: '#333', borderWidth: 1.5, pointRadius: 0, order: 1 });
    // a window shorter than the history also gets the total within it
    if (start > 0) {
      datasets.push({ type: 'line', label: 'total since ' + monthName(months[0]), yAxisID: 'y2',
        data: windowed, borderColor: '#333', borderWidth: 1.5, borderDash: [6, 3], pointRadius: 0, order: 1 });
    }

    // dashed lines before the month the releases moved to GitHub and the
    // month the GitHub counters were first sampled
    var marks = [];
    if (data.firstGitHub) marks.push({ month: data.firstGitHub.slice(0, 7), text: 'releases move to GitHub' });
    if (backlogMonth) marks.push({ month: backlogMonth, text: 'GitHub counters sampled' });
    var marker = {
      id: 'marks',
      afterDatasetsDraw: function (c) {
        var area = c.chartArea, g = c.ctx;
        g.save();
        g.strokeStyle = '#333'; g.fillStyle = '#333'; g.lineWidth = 1;
        g.font = '11px sans-serif'; g.textAlign = 'right';
        marks.forEach(function (mark, k) {
          var i = months.indexOf(mark.month);
          if (i < 1) return;
          // between the bar of the month before and the bar of the month
          var x = (c.scales.x.getPixelForValue(i - 1) + c.scales.x.getPixelForValue(i)) / 2;
          g.setLineDash([4, 3]);
          g.beginPath(); g.moveTo(x, area.top); g.lineTo(x, area.bottom); g.stroke();
          g.setLineDash([]);
          g.fillText(mark.text, x - 4, area.top + 12 + 14 * k);
        });
        g.restore();
      }
    };

    if (chart) chart.destroy();
    chart = new Chart(root.querySelector('canvas'), {
      type: 'bar',
      data: { labels: months, datasets: datasets },
      plugins: [marker],
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: false,
        interaction: { mode: 'index' },
        datasets: { bar: { barPercentage: 1, categoryPercentage: 0.85 } },
        scales: {
          x: { stacked: true, grid: { display: false },
            ticks: { autoSkip: false, maxRotation: 0,
              callback: function (v) { var l = months[v]; return l.slice(5) === '01' ? l.slice(0, 4) : ''; } } },
          y: { stacked: true, beginAtZero: true, title: { display: true, text: 'downloads per month' } },
          y2: { position: 'right', beginAtZero: true, grid: { display: false },
            title: { display: true, text: 'cumulative' } }
        },
        plugins: {
          legend: { display: false },
          tooltip: {
            filter: function (item) { return item.raw > 0; },
            callbacks: {
              title: function (items) {
                var l = items[0].label;
                return monthName(l) + (l === all[all.length - 1] ? ' (incomplete)' : '');
              },
              label: function (item) {
                var d = item.dataset, i = item.dataIndex;
                var s = d.label + ': ' + format(item.raw);
                if (d.sf && d.sf[i] && d.gh[i]) {
                  s += ' (SourceForge ' + format(d.sf[i]) + ', GitHub ' + format(d.gh[i]) + ')';
                } else if (d.gh && d.gh[i]) {
                  s += ' (GitHub)';
                }
                return s;
              },
              footer: function (items) {
                var n = 0;
                items.forEach(function (i) { if (i.dataset.type !== 'line') n += i.raw; });
                return 'total: ' + format(n);
              }
            }
          }
        }
      }
    });
    buildLegend(chart);
  }

  // The legend is HTML rather than Chart.js's own, which cannot put the
  // totals on a row of their own or draw them as lines: the totals first,
  // then the version series from newest to oldest, which puts the lumped
  // older versions last. Clicking an item hides or shows its dataset.
  function buildLegend(c) {
    var legend = root.querySelector('.dl-legend');
    if (!legend) return;
    legend.innerHTML = '';
    var lines = [], bars = [];
    c.data.datasets.forEach(function (d, i) {
      (d.type === 'line' ? lines : bars).push(i);
    });
    [lines, bars.reverse()].forEach(function (indices) {
      var row = document.createElement('div');
      row.className = 'dl-legend-row';
      indices.forEach(function (i) {
        var d = c.data.datasets[i];
        var item = document.createElement('span');
        item.className = 'dl-legend-item';
        var swatch = document.createElement('span');
        if (d.type === 'line') {
          swatch.className = 'dl-swatch-line';
          swatch.style.borderTopColor = d.borderColor;
          if (d.borderDash) swatch.style.borderTopStyle = 'dashed';
        } else {
          swatch.className = 'dl-swatch-box';
          swatch.style.backgroundColor = d.backgroundColor;
        }
        item.appendChild(swatch);
        item.appendChild(document.createTextNode(d.label));
        item.addEventListener('click', function () {
          c.setDatasetVisibility(i, !c.isDatasetVisible(i));
          item.classList.toggle('dl-hidden', !c.isDatasetVisible(i));
          c.update();
        });
        row.appendChild(item);
      });
      legend.appendChild(row);
    });
  }
})();
