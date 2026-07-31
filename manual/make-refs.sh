#!/bin/bash
# Regenerates the reference pages of the web manual (manual/ref-*.md) from the
# GROOVE source code, by running MakeRefs.java against a GROOVE build.
#
# Requires java (21+) on the PATH and the environment variables
#   GROOVE_CLASSPATH - a GROOVE build: groove jar including dependencies, or
#                      classes directory plus dependency jars
#   GROOVE_SRC       - the root of the GROOVE source tree of the SAME build
#                      (used to read the version number)
#
# The build should correspond to the released GROOVE version that the manual
# documents -- not to a development branch.
#
# Usage: ./make-refs.sh   (from the manual/ directory)

set -e

if [ -z "$GROOVE_CLASSPATH" ] || [ -z "$GROOVE_SRC" ]; then
    echo "Error: set GROOVE_CLASSPATH and GROOVE_SRC first" >&2
    exit 1
fi

VERSION=$(cat "$GROOVE_SRC/src/main/resources/nl/utwente/groove/resource/version/GROOVE_VERSION")

java -cp "$GROOVE_CLASSPATH" MakeRefs.java "$VERSION" .
