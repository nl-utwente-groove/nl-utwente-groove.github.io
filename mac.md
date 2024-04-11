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
