// Renders the download statistics page: a monthly chart of the downloads of
// GROOVE from SourceForge (since 2007) and GitHub (sampled since September
// 2026), stacked by minor version series, and a table of totals per release.
// The data are the two CSV files of nl-utwente-groove/download-stats, read
// from raw.githubusercontent.com; that repository's README documents them.
// Needs Chart.js (UMD build) loaded before this script.
(function () {
  'use strict';

  var root = document.getElementById('downloads');
  if (!root) return;
  var BASE = root.getAttribute('data-base') ||
    'https://raw.githubusercontent.com/nl-utwente-groove/download-stats/main/';
  // Okabe-Ito for up to five recent series, oldest first; grey for the rest
  var COLOURS = ['#0072b2', '#56b4e9', '#009e73', '#e69f00', '#d55e00'];
  var EARLIER = '#8c8c8c', OTHER = '#c8c8c8';

  var chart = null;

  Promise.all([fetchCsv('snapshots.csv'), fetchCsv('sourceforge-monthly.csv')])
    .then(function (files) { render(derive(files[0], files[1])); })
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

  function compareVersions(a, b) {
    var x = a.split('.').map(Number), y = b.split('.').map(Number);
    for (var i = 0; i < Math.max(x.length, y.length); i++) {
      if ((x[i] || 0) !== (y[i] || 0)) return (x[i] || 0) - (y[i] || 0);
    }
    return 0;
  }

  // Turns the two files into monthly counts per version and source. GitHub
  // downloads of a month are the differences of consecutive daily counter
  // samples; a counter that dropped was re-uploaded and starts again at zero.
  // The counts of the very first sampling day accumulated since the release
  // was published and cannot be placed in months: they are kept apart as the
  // backlog, dated to the first sampling day.
  function derive(gh, sf) {
    var monthly = {};   // month -> version|'' -> {sf, gh}
    var totals = {};    // version|'' -> {sf, gh, backlog}
    var firstDay = null, lastDay = null;
    function total(version) {
      var v = version || '';
      return totals[v] || (totals[v] = { sf: 0, gh: 0, backlog: 0 });
    }
    function add(month, version, source, n) {
      if (!n) return;
      var v = version || '';
      var m = monthly[month] || (monthly[month] = {});
      var c = m[v] || (m[v] = { sf: 0, gh: 0 });
      c[source] += n;
      total(v)[source] += n;
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

    var months = Object.keys(monthly).sort();
    var minors = [];
    Object.keys(totals).filter(Boolean).sort(compareVersions).forEach(function (v) {
      var m = minorOf(v);
      if (minors.indexOf(m) < 0) minors.push(m);
    });
    return {
      monthly: monthly,
      totals: totals,
      minors: minors,
      months: monthRange(months[0], months[months.length - 1]),
      firstDay: firstDay,
      lastDay: lastDay,
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

  // diagonal hatching in the series colour, for the GitHub part of a stack
  function hatch(colour) {
    var c = document.createElement('canvas');
    c.width = c.height = 8;
    var g = c.getContext('2d');
    g.fillStyle = colour;
    g.fillRect(0, 0, 8, 8);
    g.strokeStyle = 'rgba(255,255,255,0.7)';
    g.lineWidth = 2;
    g.beginPath(); g.moveTo(-2, 6); g.lineTo(6, -2); g.moveTo(2, 10); g.lineTo(10, 2); g.stroke();
    return g.createPattern(c, 'repeat');
  }

  function render(data) {
    var sfTotal = 0, ghTotal = 0;
    Object.keys(data.totals).forEach(function (v) {
      sfTotal += data.totals[v].sf;
      ghTotal += data.totals[v].gh + data.totals[v].backlog;
    });
    root.querySelector('.dl-summary').innerHTML =
      '<b>' + format(sfTotal + ghTotal) + ' downloads</b> since ' + monthName(data.months[0]) +
      ': ' + format(sfTotal) + ' from SourceForge and ' + format(ghTotal) + ' from GitHub' +
      ' (GitHub counters sampled since ' + data.firstDay + ', last sample ' + data.lastDay + ').';

    var select = root.querySelector('.dl-recent');
    function recentCount() {
      var v = select ? select.value : '5';
      return v === 'all' ? data.minors.length : Math.min(+v, data.minors.length);
    }
    drawChart(data, recentCount());
    if (select) select.addEventListener('change', function () { drawChart(data, recentCount()); });

    var versions = Object.keys(data.totals).filter(Boolean).sort(compareVersions).reverse();
    var rows = versions.map(function (v) {
      var t = data.totals[v];
      return [v, t.sf, t.gh + t.backlog];
    });
    var o = data.totals[''] || { sf: 0, gh: 0, backlog: 0 };
    rows.push(['other files', o.sf, o.gh + o.backlog]);
    var html = '<thead><tr><th>Release</th><th>SourceForge</th><th>GitHub</th><th>total</th></tr></thead><tbody>';
    rows.forEach(function (r) {
      html += '<tr><td>' + r[0] + '</td><td>' + format(r[1]) + '</td><td>' + format(r[2]) +
        '</td><td><b>' + format(r[1] + r[2]) + '</b></td></tr>';
    });
    html += '<tr><td><b>all</b></td><td>' + format(sfTotal) + '</td><td>' + format(ghTotal) +
      '</td><td><b>' + format(sfTotal + ghTotal) + '</b></td></tr></tbody>';
    root.querySelector('table').innerHTML = html;
  }

  // Draws the monthly chart with the `recent` most recent minor series as
  // separate segments, the older versions lumped, the non-release files as a
  // segment of their own, and the cumulative total as a line.
  function drawChart(data, recent) {
    var months = data.months;
    var recentMinors = data.minors.slice(data.minors.length - recent);
    // segments from the bottom of the stack up
    var segments = [];
    if (recentMinors.length < data.minors.length) {
      segments.push({ key: 'earlier', label: 'earlier versions', colour: EARLIER });
    }
    recentMinors.forEach(function (m, i) {
      segments.push({ key: m, label: 'version ' + m + '.x', colour: colour(i, recentMinors.length) });
    });
    segments.push({ key: 'other', label: 'documentation, samples, other files', colour: OTHER });
    function segmentOf(version) {
      if (!version) return 'other';
      var m = minorOf(version);
      return recentMinors.indexOf(m) < 0 ? 'earlier' : m;
    }

    var series = {};
    segments.forEach(function (s) {
      series[s.key] = { sf: months.map(function () { return 0; }), gh: months.map(function () { return 0; }) };
    });
    var cumulative = [], running = 0;
    var backlogMonth = data.firstDay ? data.firstDay.slice(0, 7) : null;
    months.forEach(function (month, i) {
      var m = data.monthly[month] || {};
      Object.keys(m).forEach(function (v) {
        var s = series[segmentOf(v)];
        s.sf[i] += m[v].sf;
        s.gh[i] += m[v].gh;
        running += m[v].sf + m[v].gh;
      });
      if (month === backlogMonth) running += data.backlog;
      cumulative.push(running);
    });

    var datasets = [];
    segments.forEach(function (s) {
      datasets.push({ label: s.label, segment: s.key, source: 'SourceForge',
        data: series[s.key].sf, backgroundColor: s.colour, stack: 'downloads', order: 2 });
      if (series[s.key].gh.some(Boolean)) {
        datasets.push({ label: s.label, segment: s.key, source: 'GitHub',
          data: series[s.key].gh, backgroundColor: hatch(s.colour), stack: 'downloads', order: 2 });
      }
    });
    datasets.push({ type: 'line', label: 'cumulative total', segment: 'total', yAxisID: 'y2',
      data: cumulative, borderColor: '#333', borderWidth: 1.5, pointRadius: 0, order: 1 });

    var samplingIndex = backlogMonth ? months.indexOf(backlogMonth) : -1;
    var marker = {
      id: 'samplingMarker',
      afterDatasetsDraw: function (c) {
        if (samplingIndex < 1) return;
        // between the bar of the month before and the bar of the first month
        var x = (c.scales.x.getPixelForValue(samplingIndex - 1) + c.scales.x.getPixelForValue(samplingIndex)) / 2;
        var area = c.chartArea, g = c.ctx;
        g.save();
        g.strokeStyle = '#333'; g.setLineDash([4, 3]); g.lineWidth = 1;
        g.beginPath(); g.moveTo(x, area.top); g.lineTo(x, area.bottom); g.stroke();
        g.setLineDash([]);
        g.fillStyle = '#333'; g.font = '11px sans-serif'; g.textAlign = 'right';
        g.fillText('GitHub sampling', x - 4, area.top + 12);
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
          legend: {
            // one entry per segment; toggling it hides both sources
            labels: { filter: function (item, d) { return d.datasets[item.datasetIndex].source !== 'GitHub'; } },
            onClick: function (e, item, legend) {
              var c = legend.chart, key = c.data.datasets[item.datasetIndex].segment;
              c.data.datasets.forEach(function (d, i) {
                if (d.segment === key) c.setDatasetVisibility(i, !c.isDatasetVisible(i));
              });
              c.update();
            }
          },
          tooltip: {
            filter: function (item) { return item.raw > 0; },
            callbacks: {
              title: function (items) {
                var l = items[0].label;
                return monthName(l) + (l === months[months.length - 1] ? ' (incomplete)' : '');
              },
              label: function (item) {
                var d = item.dataset;
                return d.label + (d.source ? ' (' + d.source + ')' : '') + ': ' + format(item.raw);
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
  }
})();
