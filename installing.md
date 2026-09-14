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

There are three ways to get GROOVE: a native installer for your platform, a `zip` archive that you unpack yourself, or the Maven artifacts. All release assets are on the [release page]({{site.groove_url}}/code/releases/latest).

## Requirements

- The native installers bundle a Java runtime; for those, no Java is needed on your machine.

- To run GROOVE from a `zip` archive you need Java on your machine. The currently required minimum version is Java 21.

- MacOS ships no Java of its own, so on a Mac the `zip` route requires you to get the Open JDK.

- For the instructions under [Running](#running) to work, the JVM needs to be on your system path. [Instructions can be found here](https://www.java.com/en/download/help/path.html).

## Native installers

The installers save you the trouble of unpacking an archive, having the right Java version and getting the invocation right: they bundle their own Java runtime and register all [runnable components](../index#runnable) — Simulator, Generator, ModelChecker, Imager and Viewer — with the platform's application menu. The asset names carry the release version (`x_y_z` below):

| Platform | Asset |
| :--- | :--- |
| Windows (x64) | `groove-x_y_z-windows-x64.msi` |
| MacOS (Apple silicon) | `groove-x_y_z-macos-aarch64.dmg` |
| MacOS (Intel) | `groove-x_y_z-macos-x64.dmg` |
| Linux (x64) | `groove-x_y_z-linux-x64.deb` |

The installers have few customisation options; among other things, they use a fixed, platform-default installation path. If you want more control, use the stand-alone installation below.

### If Windows or MacOS blocks the installer<a name="blocked"></a>

The installers are not code-signed, so Windows and MacOS treat them as coming from an unknown publisher and block them at first invocation. Nothing is wrong with the file: the installers are built automatically from the public source code.

- **Windows** (*"Windows protected your PC"*): close the dialog, right-click the downloaded `.msi` in File Explorer, choose `Properties`, tick `Unblock` at the bottom of the `General` tab and click `OK`; then run the `.msi` again. From PowerShell, the same is `Unblock-File <path-to-the-msi>`.

- **MacOS** (*"Apple could not verify GROOVE…"*): see [the MacOS page](../mac#blocked).

- **Linux**: not affected.

The full instructions are also among the release assets, as `IF-WINDOWS-OR-MACOS-BLOCKS-THE-INSTALLER.txt`. If you cannot get the installer for your platform to work, please [file an issue]({{site.groove_url}}/code/issues) and use the stand-alone installation for the time being.

## Stand-alone installation

- To install GROOVE without an installer, download the library (as a `zip`-file) [here]({{site.groove_url}}/code/releases/latest): `groove-x_y_z-bin.zip`, or `groove-x_y_z-bin+doc.zip` if you also want the Javadoc.

- Unpack the zip in a directory of your choice. This will result in a `bin` subdirectory containing the runnable `jar`s (which are essentially empty, merely containing the metadata to find the runnable in the `groove` jar), and a `lib` subdirectory containing all actual libraries, that is, the `groove` jar together with all required dependencies. Next to these are the change log, the license and installation instructions; the `bin+doc` variant has a further `doc` subdirectory with the API documentation.

- This route needs Java 21 or newer on your machine (see [Requirements](#requirements)), but triggers none of the warnings described above.

## Running<a name="running"></a>

All the [runnable components](../index#runnable) of GROOVE are available as JAR files in the `bin` subdirectory of your installation. For instance, `Runnable` can be invoked as follows:

- Go to the `bin` subdirectory, either in a file explorer window or in a command-line window

- From the file explorer, double-click `Runnable.jar` to invoke it

- In command-line mode, type

  `java -jar Runnable.jar`
  
  The components that are meant to be invoked from the command line, in particular the Generator and the Imager, have a number of options, which can be queried by 

  `java -jar Runnable.jar -h`

- Mac users may run into errors due to incompatible accessibility settings; see [this page](../mac)

If you used a native installer instead, the same components are started from the application menu, and the command-line ones from the executables in the installation directory.

## The yFiles add-on

GROOVE draws graphs through a visualisation backend, of which there are two: the JGraph-based one included in every release, and one built on the commercial library [yFiles for Java (Swing)](https://www.yworks.com/products/yfiles-for-java) by yWorks GmbH, which contributes its own layout algorithms. The yFiles backend is *not* part of the standard release; it is an optional add-on, released as the separate asset `groove-x_y_z-yfiles-addon.zip`.

The add-on comes with license restrictions that GROOVE itself does not have, most importantly that **GROOVE with the add-on installed may be used for non-commercial purposes only**. Read [`YFILES-ADDON.md`]({{site.groove_url}}/code/blob/master/release/yfiles/include/YFILES-ADDON.md) (also included in the zip) before installing it.

Installing and removing the add-on:

- At the first start of a newly released GROOVE version, the Simulator asks once whether to download and install it. If you decline, the JGraph backend is used.

- The same choice remains available afterwards under `View` &rarr; `YFiles add-on`, which also installs the add-on from a downloaded zip and removes an installed one.

- By hand, unpack the zip into GROOVE's extension directory (`%APPDATA%\GROOVE\extensions` on Windows, `~/Library/Application Support/GROOVE/extensions` on MacOS, `~/.groove/extensions` elsewhere); it is picked up at the next start.

An add-on is built for one GROOVE version and is skipped with a warning by any other, so a new release of GROOVE needs the add-on of that release. When both backends are installed, yFiles is the default; the backend to use is chosen under `View` &rarr; `Options` &rarr; `Graph backend` and takes effect at the next start. On Windows, uninstalling or upgrading GROOVE through the installer removes the add-on as well; the new version offers to install its own add-on at its first start.

## Maven package

You can also download the compiled GROOVE jar (without dependencies), as well as the source files and documentation, from Maven Central: see

- either <https://mvnrepository.com/artifact/nl.utwente.groove>

- or <https://central.sonatype.com/artifact/nl.utwente.groove/groove>.

## Source code

GROOVE is open source; the development version can be found (formatted as Eclipse Maven project) at <{{site.groove_url}}/code>. The yFiles add-on is the exception: it is not open source.

## Prior versions

Prior to release 6.8.0, GROOVE was hosted on [Sourceforge](https://sf.net). Older versions of the tool can be found [there](https://sf.net/projects/groove).
 


