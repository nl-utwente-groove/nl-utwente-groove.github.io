<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
    <graph role="graph" edgeids="false" edgemode="directed" id="start">
        <attr name="$version">
            <string>curly</string>
        </attr>
        <node id="n1">
            <attr name="layout">
                <string>347 137 19 19</string>
            </attr>
        </node>
        <node id="n2">
            <attr name="layout">
                <string>320 208 19 19</string>
            </attr>
        </node>
        <node id="n3">
            <attr name="layout">
                <string>260 274 19 19</string>
            </attr>
        </node>
        <node id="n0">
            <attr name="layout">
                <string>67 50 19 19</string>
            </attr>
        </node>
        <node id="n4">
            <attr name="layout">
                <string>67 150 19 19</string>
            </attr>
        </node>
        <edge from="n1" to="n1">
            <attr name="label">
                <string>int:1966</string>
            </attr>
        </edge>
        <edge from="n2" to="n2">
            <attr name="label">
                <string>int:6</string>
            </attr>
        </edge>
        <edge from="n3" to="n3">
            <attr name="label">
                <string>int:6</string>
            </attr>
        </edge>
        <edge from="n0" to="n0">
            <attr name="label">
                <string>type:Date</string>
            </attr>
        </edge>
        <edge from="n0" to="n3">
            <attr name="label">
                <string>day</string>
            </attr>
        </edge>
        <edge from="n0" to="n2">
            <attr name="label">
                <string>month</string>
            </attr>
        </edge>
        <edge from="n0" to="n1">
            <attr name="label">
                <string>year</string>
            </attr>
        </edge>
        <edge from="n4" to="n4">
            <attr name="label">
                <string>type:Date</string>
            </attr>
        </edge>
        <edge from="n4" to="n4">
            <attr name="label">
                <string>let:day=28</string>
            </attr>
        </edge>
        <edge from="n4" to="n4">
            <attr name="label">
                <string>let:month=2</string>
            </attr>
        </edge>
        <edge from="n4" to="n4">
            <attr name="label">
                <string>let:year=2000</string>
            </attr>
        </edge>
    </graph>
</gxl>
