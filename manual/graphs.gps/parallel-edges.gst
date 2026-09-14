<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
    <graph edgemode="directed" edgeids="false" role="graph" id="parallel-edges">
        <attr name="$version">
            <string>curly</string>
        </attr>
        <node id="n1">
            <attr name="layout">
                <string>40 40 50 30</string>
            </attr>
        </node>
        <node id="n2">
            <attr name="layout">
                <string>220 40 50 30</string>
            </attr>
        </node>
        <edge to="n1" from="n1">
            <attr name="label">
                <string>Buffer</string>
            </attr>
        </edge>
        <edge to="n2" from="n2">
            <attr name="label">
                <string>Item</string>
            </attr>
        </edge>
        <edge to="n2" from="n1">
            <attr name="label">
                <string>mult=2:holds</string>
            </attr>
            <attr name="layout">
                <string>500 0 65 55 155 15 245 55 12</string>
            </attr>
        </edge>
        <edge to="n2" from="n1">
            <attr name="label">
                <string>first</string>
            </attr>
        </edge>
    </graph>
</gxl>
