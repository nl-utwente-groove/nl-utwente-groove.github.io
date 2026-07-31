#!/bin/bash
# Regenerates the SVG figures of the web manual (in images/manual/) from the
# example grammar manual/graphs.gps, using the GROOVE Imager.
#
# Every rule (.gpr), host graph (.gst) and type graph (.gty) in graphs.gps is
# rendered twice: as <name>-display.svg (the Simulator's display view) and as
# <name>-edit.svg (the editor view, showing the aspect prefixes literally).
#
# Requires java (21+) on the PATH and the environment variable GROOVE_CLASSPATH
# pointing to a GROOVE build: either a groove jar including dependencies (e.g.
# lib/groove-X.Y.Z.jar of a release installation) or a classes directory plus
# dependency jars, separated by the platform path separator.
#
# Usage: ./make-figures.sh   (from the manual/ directory)

set -e

if [ -z "$GROOVE_CLASSPATH" ]; then
    echo "Error: set GROOVE_CLASSPATH to a GROOVE jar or classpath first" >&2
    exit 1
fi

GPS=graphs.gps
OUT=../images/manual
IMAGER="java -cp $GROOVE_CLASSPATH nl.utwente.groove.Imager -v 0 -f svg"

mkdir -p "$OUT"
for f in "$GPS"/*.gpr "$GPS"/*.gst "$GPS"/*.gty; do
    name=$(basename "$f")
    name=${name%.*}
    echo "$name"
    $IMAGER "$f" "$OUT/$name-display.svg"
    $IMAGER -e "$f" "$OUT/$name-edit.svg"
done
