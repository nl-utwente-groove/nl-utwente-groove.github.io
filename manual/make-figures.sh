#!/bin/bash
# Regenerates the SVG figures of the web manual (in images/manual/) from the
# example grammars manual/graphs.gps and manual/user-ops.gps, using the GROOVE
# Imager.
#
# Every rule (.gpr), host graph (.gst) and type graph (.gty) is rendered twice:
# as <name>-display.svg (the Simulator's display view) and as <name>-edit.svg
# (the editor view, showing the aspect prefixes literally). The figures of
# user-ops.gps get the prefix user-ops-, to keep them apart from those of
# graphs.gps.
#
# The grammar user-ops.gps relies on the user-defined type in
# manual/user-ops-src/user/Date.java; the script compiles that class against
# the GROOVE build and puts it on the class path of the Imager.
#
# Requires java and javac (21+) on the PATH and the environment variable
# GROOVE_CLASSPATH pointing to a GROOVE build: either a groove jar including
# dependencies (e.g. lib/groove-X.Y.Z.jar of a release installation) or a
# classes directory plus dependency jars, separated by the platform path
# separator.
#
# Usage: ./make-figures.sh   (from the manual/ directory)

set -e

if [ -z "$GROOVE_CLASSPATH" ]; then
    echo "Error: set GROOVE_CLASSPATH to a GROOVE jar or classpath first" >&2
    exit 1
fi

OUT=../images/manual
USER_SRC=user-ops-src
USER_CLASSES=$(mktemp -d)
trap 'rm -rf "$USER_CLASSES"' EXIT

# Platform path separator: ';' on Windows (Git Bash), ':' elsewhere. On
# Windows, the temporary directory also has to be passed to java in Windows
# form, since a ';'-separated class path is not converted by the shell.
case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*) SEP=';'; USER_CLASSES_CP=$(cygpath -m "$USER_CLASSES") ;;
    *) SEP=':'; USER_CLASSES_CP=$USER_CLASSES ;;
esac

echo "Compiling $USER_SRC"
javac -cp "$GROOVE_CLASSPATH" -d "$USER_CLASSES" "$USER_SRC"/user/*.java

IMAGER="java -cp $GROOVE_CLASSPATH$SEP$USER_CLASSES_CP nl.utwente.groove.Imager -v 0 -f svg"

mkdir -p "$OUT"
render() {
    local gps=$1 prefix=$2
    for f in "$gps"/*.gpr "$gps"/*.gst "$gps"/*.gty; do
        [ -e "$f" ] || continue
        name=$(basename "$f")
        name=$prefix${name%.*}
        echo "$name"
        $IMAGER "$f" "$OUT/$name-display.svg"
        $IMAGER -e "$f" "$OUT/$name-edit.svg"
    done
}

render graphs.gps ""
render user-ops.gps "user-ops-"
