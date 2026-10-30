#!/bin/bash
# Regenerates the reference pages of the web manual (manual/ref-*.md) from the
# GROOVE source code, by running MakeRefs.java against a GROOVE build.
#
# Requires java (21+) on the PATH and the environment variables
#   GROOVE_CLASSPATH - a GROOVE build: groove jar including dependencies, or
#                      classes directory plus dependency jars
#   GROOVE_SRC       - the root of the GROOVE source tree of the SAME build
#                      (its pom.xml supplies the version number)
#
# The build should correspond to the released GROOVE version that the manual
# documents -- not to a development branch.
#
# Usage: ./make-refs.sh   (from the manual/ directory, in a bash shell;
# from PowerShell, run "bash ./make-refs.sh" so that output and exit code
# stay in the console)

set -e

if [ -z "$GROOVE_CLASSPATH" ] || [ -z "$GROOVE_SRC" ]; then
    echo "Error: set GROOVE_CLASSPATH and GROOVE_SRC first" >&2
    exit 1
fi

# The version is the pom's revision property; the GROOVE_VERSION resource in
# the source tree only holds the placeholder that the build fills in.
VERSION=$(sed -n 's/.*<revision>\(.*\)<\/revision>.*/\1/p' "$GROOVE_SRC/pom.xml")
if [ -z "$VERSION" ]; then
    echo "Error: no revision property found in $GROOVE_SRC/pom.xml" >&2
    exit 1
fi

java -cp "$GROOVE_CLASSPATH" MakeRefs.java "$VERSION" .
