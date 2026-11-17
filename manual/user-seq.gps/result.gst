<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
    <graph role="graph" edgeids="false" edgemode="directed" id="result">
        <attr name="$version">
            <string>curly</string>
        </attr>
        <node id="n">
            <attr name="layout">
                <string>190 40 60 30</string>
            </attr>
        </node>
        <node id="a">
            <attr name="layout">
                <string>50 150 60 30</string>
            </attr>
        </node>
        <node id="b">
            <attr name="layout">
                <string>190 150 60 30</string>
            </attr>
        </node>
        <node id="c">
            <attr name="layout">
                <string>330 150 60 30</string>
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
        <edge from="a" to="a">
            <attr name="label">
                <string>type:Term</string>
            </attr>
        </edge>
        <edge from="a" to="a">
            <attr name="label">
                <string>let:degree=0</string>
            </attr>
        </edge>
        <edge from="a" to="a">
            <attr name="label">
                <string>let:coeff=5</string>
            </attr>
        </edge>
        <edge from="b" to="b">
            <attr name="label">
                <string>type:Term</string>
            </attr>
        </edge>
        <edge from="b" to="b">
            <attr name="label">
                <string>let:degree=1</string>
            </attr>
        </edge>
        <edge from="b" to="b">
            <attr name="label">
                <string>let:coeff=6</string>
            </attr>
        </edge>
        <edge from="c" to="c">
            <attr name="label">
                <string>type:Term</string>
            </attr>
        </edge>
        <edge from="c" to="c">
            <attr name="label">
                <string>let:degree=2</string>
            </attr>
        </edge>
        <edge from="c" to="c">
            <attr name="label">
                <string>let:coeff=5</string>
            </attr>
        </edge>
        <edge from="n" to="a">
            <attr name="label">
                <string>term</string>
            </attr>
        </edge>
        <edge from="n" to="b">
            <attr name="label">
                <string>term</string>
            </attr>
        </edge>
        <edge from="n" to="c">
            <attr name="label">
                <string>term</string>
            </attr>
        </edge>
    </graph>
</gxl>
