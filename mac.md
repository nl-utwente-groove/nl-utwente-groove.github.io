---
title:
    "MacOS issues and workarounds"
permalink:
    mac.html
summary:
    "A summary of known problems on MacOS"
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

## Accessibility errors

When running GROOVE on a Mac, you might experience crashes resulting in a stack trace starting with (something like)

```
Exception in GROOVE java.lang.NullPointerException: Cannot invoke "javax.accessibility.AccessibleContext.getAccessibleRole()" because "nvAC" is null
at java.desktop/sun.lwawt.macosx.CAccessibility.propertyChange(CAccessibility.java:114)
at java.desktop/java.beans.PropertyChangeSupport.fire(PropertyChangeSupport.java:343)
```

This is caused by certain conflicting Mac-specific accessibility settings, and can (at least in some cases) be resolved by disabling one or more applications in

```
System Preferences > Security & Privacy > Accessibility
```

Known cases are:

- `Magnet`, an application for automatic resizing and positioning windows on the screen
- `Voice Over`, a screen reader available on MacOS

## A blocked installer<a name="blocked"></a>

The GROOVE installers are not code-signed, so MacOS blocks the `.dmg` the first time it is run, with the message *"Apple could not verify "GROOVE" is free of malware"* or *"GROOVE is damaged and can't be opened"*. Nothing is wrong with the file: the installers are built automatically from the public source code.

1. Open the `.dmg` and drag GROOVE to Applications as usual.
2. Try to open GROOVE once, and dismiss the warning with `Done` (not `Move to Trash`).
3. Open `System Settings` &rarr; `Privacy & Security`, scroll down to the message about GROOVE, click `Open Anyway` and confirm with your password.

If MacOS says the app is damaged, or no `Open Anyway` button appears, run this in a terminal instead, and then open GROOVE normally:

```
xattr -dr com.apple.quarantine /Applications/GROOVE.app
```

The `zip` archives (see [the installation page](installing.html)) need no installer and trigger none of these warnings, but they require Java 21 or newer.
