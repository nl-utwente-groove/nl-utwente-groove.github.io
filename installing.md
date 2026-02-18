---
title:
    "GROOVE installation"
permalink:
    installing.html
summary:
    "Instructions for installing and running the tool (components)"
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
    requirements, installing, running, maven
---

## Requirements

- You need Java on your machine to run GROOVE. The currently required minimum version is Java 17.

- MAC users need to get the Open JDK.

- For the instructions under [Running][#running] to work, the JVM needs to be on your system path. [Instructions can be found here](https://www.java.com/en/download/help/path.html).

## Stand-alone installation

- To install GROOVE, download the library (as a `zip`-file) [here](https://github.com/nl-utwente-groove/code/releases/latest)

- Unpack the zip in a directory of your choice. This will result in a `bin` subdirectory containing the runnable `jar`s (which are essentially empty, merely containing the metadata to find the runnable in the `groove` jar), and a `lib` subdirectory containing all actual libraries, that is, the `groove` jar together with all required dependencies.

## Running<a name="running"></a>

All the [runnable components](../index#runnable) of GROOVE are available as JAR files in the `bin` subdirectory of your installation. For instance, `Runnable` can be invoked as follows:

- Go to the `bin` subdirectory, either in a file explorer window or in a command-line window

- From the file explorer, double-click `Runnable.jar` to invoke it

- In command-line mode, type

  `java -jar Runnable.jar`
  
  The components that are meant to be invoked from the command line, in particular the Generator and the Imager, have a number of options, which can be queried by 

  `java -jar Runnable.jar -h`

- Mac users may run into errors due to incompatible accessibility settings; see [this page](../mac)

## Maven package

You can also download the compiled GROOVE jar (without dependencies), as well as the source files and documentation, from Maven Central: see

- either <https://mvnrepository.com/artifact/nl.utwente.groove>

- or <https://central.sonatype.com/artifact/nl.utwente.groove/groove>.

## Source code

GROOVE is open source; the development version can be found (formatted as Eclipse Maven project) at <https://github.com/nl-utwente-groove/code>.

## Prior versions

Prior to release 6.8.0, GROOVE was hosted on [Sourceforge](https://sf.net). Older versions of the tool can be found [there](https://sf.net/projects/groove).
 


