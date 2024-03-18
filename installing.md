---
title: # required
    "GROOVE installation"
# tags: # optional, need to be included in _data/tags_doc.yml and have a page in tags/
# 	[overview]
# keywords: # optional, used in metadata for findability
# 	groove home
# last_updated: # optional, appears in footer
sidebar: # required
    home_sidebar
# summary: # optional
permalink: # required, must match filename.html
    installing.html
datatable: # optional, true for jQueries, see https://www.datatables.net/
    false
toc: # optional, false to exclude from initial table of contents
    false
---

Prior to github, GROOVE was hosted on SourceForge: <https://sf.net/projects/groove>. For now, the installation still uses their service - this will eventually move to github.

## Requirements

- You need Java on your machine to run GROOVE. The currently required minimum version is Java 17.

- MAC users need to get the Open JDK.

- For the instructions under [Running][#running] to work, the JVM needs to be on your system path. [Instructions can be found here](https://www.java.com/en/download/help/path.html).

## Stand-alone installation

- To install GROOVE, download the library (as a `zip`-file) [here](https://sourceforge.net/projects/groove/files/latest/download)

- Unpack the zip in a directory of your choice. This will result in a `bin` subdirectory containing the runnable `jar`s (which are essentially empty, merely containing the metadata to find the runnable in the `groove` jar), and a `lib` subdirectory containing all actual libraries, that is, the `groove` jar together with all required dependencies.

## <a name="running"></a> Running

Invoke the `Simulator.jar` by going to the `bin` subdirectory, either in a GUI-based file explorer or in a command-line window. In the GUI-based explorer, double-click `Simulator.jar` to invoke it; in command-line mode, type

  `java -jar Simulator.jar`

## Maven package

You can also download the GROOVE jar (without dependencies) from Maven Central: see

- either <https://mvnrepository.com/artifact/nl.utwente.groove>

- or <https://central.sonatype.com/artifact/nl.utwente.groove/groove>.


