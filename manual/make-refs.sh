#!/bin/bash
# Regenerates the reference pages of the web manual (manual/ref-*.md) from the
# GROOVE source code, by running MakeRefs.java against a GROOVE build.
#
# Requires java (21+) on the PATH and the environment variable
#   GROOVE_CLASSPATH - a GROOVE build: groove jar including dependencies, or
#                      classes directory plus dependency jars
#
# The version named on the generated pages is that of the build; the build
# should correspond to the released GROOVE version that the manual documents,
# not to a development branch. Pass a version as the first argument to override.
#
# Usage: ./make-refs.sh [version]   (from the manual/ directory)

set -e

if [ -z "$GROOVE_CLASSPATH" ]; then
    echo "Error: set GROOVE_CLASSPATH first" >&2
    exit 1
fi

java -cp "$GROOVE_CLASSPATH" MakeRefs.java . "$@"
