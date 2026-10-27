<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
    <graph role="graph" edgeids="false" edgemode="directed" id="final">
        <attr name="$version">
            <string>curly</string>
        </attr>
        <node id="n19">
            <attr name="layout">
                <string>105 91 19 19</string>
            </attr>
        </node>
        <node id="n20">
            <attr name="layout">
                <string>105 22 19 19</string>
            </attr>
        </node>
        <node id="n21">
            <attr name="layout">
                <string>105 180 19 19</string>
            </attr>
        </node>
        <edge from="n19" to="n19">
            <attr name="label">
                <string>type:Person</string>
            </attr>
        </edge>
        <edge from="n19" to="n19">
            <attr name="label">
                <string>let:birthday=Date(28,2,2000)</string>
            </attr>
        </edge>
        <edge from="n19" to="n21">
            <attr name="label">
                <string>child</string>
            </attr>
        </edge>
        <edge from="n20" to="n20">
            <attr name="label">
                <string>type:Person</string>
            </attr>
        </edge>
        <edge from="n20" to="n20">
            <attr name="label">
                <string>let:birthday=Date(6,6,1966)</string>
            </attr>
        </edge>
        <edge from="n21" to="n21">
            <attr name="label">
                <string>type:Person</string>
            </attr>
        </edge>
        <edge from="n21" to="n21">
            <attr name="label">
                <string>let:birthday=Date(3,3,2026)</string>
            </attr>
        </edge>
    </graph>
</gxl>
