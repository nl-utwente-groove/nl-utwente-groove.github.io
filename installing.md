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

There are three ways to get GROOVE: a native installer for your platform, a `zip` archive that you unpack yourself, or the Maven artifacts. All release assets are on the [release page]({{site.groove_url}}/code/releases/latest). The installers and one of the `zip` archives contain the commercial library yFiles and may therefore be used **for academic, non-commercial purposes only**; the other archives and the Maven artifacts are unrestricted. See [With or without yFiles](#yfiles) before you choose.

## Requirements

- The native installers bundle a Java 25 runtime; for those, no Java is needed on your machine.

- To run GROOVE from a `zip` archive you need Java on your machine. The currently required minimum version is Java 21; any newer version works, including the Java 25 that the installers ship.

- MacOS ships no Java of its own, so on a Mac the `zip` route requires you to get the Open JDK.

- For the instructions under [Running](#running) to work, the JVM needs to be on your system path. [Instructions can be found here](https://www.java.com/en/download/help/path.html).

## Native installers

The installers save you the trouble of unpacking an archive, having the right Java version and getting the invocation right: they bundle their own Java runtime and register all [runnable components](../index#runnable) — Simulator, Generator, ModelChecker, Imager and Viewer — with the platform's application menu. The asset names carry the release version (`x_y_z` below):

| Platform | Asset |
| :--- | :--- |
| Windows (x64) | `groove-x_y_z-windows-x64.msi` |
| MacOS (Apple silicon) | `groove-x_y_z-macos-aarch64.dmg` |
| MacOS (Intel) | `groove-x_y_z-macos-x64.dmg` |
| Linux (x64), Debian and Ubuntu | `groove-x_y_z-linux-x64.deb` |
| Linux (x64), Fedora, RHEL and openSUSE | `groove-x_y_z-linux-x64.rpm` |

On Linux, pick the package format of your distribution: `.deb` for Debian-based systems, `.rpm` for RPM-based ones. The installers have few customisation options; among other things, they use a fixed, platform-default installation path. If you want more control, use the stand-alone installation below.

The installers contain yFiles, so **they may be used for academic, non-commercial purposes only**; the restrictions are part of the license that each installer carries (see [With or without yFiles](#yfiles)). If your use does not qualify, take the stand-alone installation without yFiles.

### If Windows or MacOS blocks the installer<a name="blocked"></a>

The installers are not code-signed, so Windows and MacOS treat them as coming from an unknown publisher and block them at first invocation, and some Linux package managers ask before installing the `.rpm`. Nothing is wrong with the file: the installers are built automatically from the public source code.

- **Windows** (*"Windows protected your PC"*): close the dialog, right-click the downloaded `.msi` in File Explorer, choose `Properties`, tick `Unblock` at the bottom of the `General` tab and click `OK`; then run the `.msi` again. From PowerShell, the same is `Unblock-File <path-to-the-msi>`.

- **MacOS** (*"Apple could not verify GROOVE…"*): see [the MacOS page](../mac#blocked).

- **Linux** (*"Package is not signed!"*): `zypper` asks whether to continue; answer yes. `dnf` and `rpm -i` install the `.rpm` without asking; if `dnf` refuses, add `--nogpgcheck`. The `.deb` is not affected, as `apt` and `dpkg` do not check signatures of local package files.

The full instructions are also among the release assets, as `IF-WINDOWS-OR-MACOS-BLOCKS-THE-INSTALLER.txt`. If you cannot get the installer for your platform to work, please [file an issue]({{site.groove_url}}/code/issues) and use the stand-alone installation for the time being.

## Stand-alone installation

- To install GROOVE without an installer, download one of the `zip` archives [here]({{site.groove_url}}/code/releases/latest):

  | Asset | Content | Permitted use |
  | :--- | :--- | :--- |
  | `groove-x_y_z-bin.zip` | GROOVE without yFiles | Unrestricted (Apache License 2.0) |
  | `groove-x_y_z-bin+doc.zip` | The same, with the Javadoc | Unrestricted (Apache License 2.0) |
  | `groove-x_y_z-yfiles-bin.zip` | GROOVE with yFiles | Academic, non-commercial only |

  The difference is explained under [With or without yFiles](#yfiles).

- Unpack the zip in a directory of your choice. This will result in a `bin` subdirectory containing the runnable `jar`s (which are essentially empty, merely containing the metadata to find the runnable in the `groove` jar), and a `lib` subdirectory containing all actual libraries, that is, the `groove` jar together with all required dependencies. Next to these are the change log, the license and installation instructions; the `bin+doc` variant has a further `doc` subdirectory with the API documentation, and the `yfiles-bin` variant a further file `LICENSE-YFILES.txt` with the restrictions on its use.

- This route needs Java 21 or newer on your machine (see [Requirements](#requirements)), but triggers none of the warnings described above.

## Running<a name="running"></a>

All the [runnable components](../index#runnable) of GROOVE are available as JAR files in the `bin` subdirectory of your installation. For instance, `Runnable` can be invoked as follows:

- Go to the `bin` subdirectory, either in a file explorer window or in a command-line window

- From the file explorer, double-click `Runnable.jar` to invoke it

- In command-line mode, type

  `java -jar Runnable.jar`
  
  The components that are meant to be invoked from the command line, in particular the Generator and the Imager, have a number of options, which can be queried by 

  `java -jar Runnable.jar -h`

- On Java 25 or newer, adding `-XX:+UseCompactObjectHeaders` before `-jar` trims the heap of a state-space exploration by roughly a tenth; the launchers of the native installers run with this option.

- Mac users may run into errors due to incompatible accessibility settings; see [this page](../mac)

If you used a native installer instead, the same components are started from the application menu, and the command-line ones from the executables in the installation directory.

## With or without yFiles<a name="yfiles"></a>

GROOVE draws graphs through a visualisation backend, of which there are two: one built on the commercial library [yFiles for Java (Swing)](https://www.yworks.com/products/yfiles-for-java) by yWorks GmbH, which contributes its own layout algorithms, and the JGraph-based one of earlier releases. GROOVE itself is open source, but yFiles is not: it is licensed to the University of Twente for academic, non-commercial use only, and that restriction carries over to every release asset that contains the library. A release therefore comes in two forms:

- **With yFiles**: the native installers and `groove-x_y_z-yfiles-bin.zip`. **You may use these for academic, non-commercial purposes only**, such as research, teaching and study. The precise restrictions are in [`LICENSE-YFILES.txt`]({{site.groove_url}}/code/blob/master/release/yfiles/include/LICENSE-YFILES.txt), which is at the root of the zip and part of the license of each installer. Among other things, the library may not be extracted from the distribution or used for anything other than running GROOVE.

- **Without yFiles**: `groove-x_y_z-bin.zip` and `groove-x_y_z-bin+doc.zip`, as well as the [Maven package](#maven-package). These carry no such restriction: they are under the Apache License 2.0 alone. They draw graphs with the JGraph-based backend, which lacks the yFiles layout algorithms but is otherwise the same tool.

If in doubt about whether your use qualifies as academic and non-commercial, use one of the archives without yFiles.

A distribution with yFiles contains both backends. The Simulator uses yFiles unless you choose otherwise under `View` &rarr; `Options` &rarr; `Graph backend`; a change takes effect at the next start. The layout algorithms of both backends are described in [the user manual](../manual_basics#layout).

## Maven package

You can also download the compiled GROOVE jar (without dependencies and without yFiles), as well as the source files and documentation, from Maven Central: see

- either <https://mvnrepository.com/artifact/nl.utwente.groove>

- or <https://central.sonatype.com/artifact/nl.utwente.groove/groove>.

The artifact is compiled for Java 21. If your application starts the GROOVE Simulator on Java 24 or newer, pass `--enable-native-access=ALL-UNNAMED` to the JVM: the look-and-feel library loads a native library for the window decorations, which such a Java reports with a warning unless native access has been granted. The runnable jars of the `zip` and the installers carry this grant in their manifest, so they need no flag.

## Source code

GROOVE is open source, under the Apache License 2.0; the development version can be found (formatted as Eclipse Maven project) at <{{site.groove_url}}/code>. The source includes the part of GROOVE that calls yFiles, but not the yFiles library itself, which is not open source: building that part requires a yFiles license of your own.

## Prior versions

Prior to release 6.8.0, GROOVE was hosted on [Sourceforge](https://sf.net). Older versions of the tool can be found [there](https://sf.net/projects/groove).
 


