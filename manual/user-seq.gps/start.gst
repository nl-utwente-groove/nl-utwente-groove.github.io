<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
    <graph role="graph" edgeids="false" edgemode="directed" id="start">
        <attr name="$version">
            <string>curly</string>
        </attr>
        <node id="n">
            <attr name="layout">
                <string>60 60 60 30</string>
            </attr>
        </node>
        <edge from="n" to="n">
            <attr name="label">
                <string>type:Polynomial</string>
            </attr>
        </edge>
        <edge from="n" to="n">
            <attr name="label">
                <string>let:p=Poly("p",5,6,5)</string>
            </attr>
        </edge>
    </graph>
</gxl>
