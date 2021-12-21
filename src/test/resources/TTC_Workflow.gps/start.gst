<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<gxl xmlns="http://www.gupro.de/GXL/gxl-1.0.dtd">
    <graph id="TTC_Workflow_start" role="rule" edgeids="false" edgemode="directed">
        <node id="n0">
            <attr name="layout">
                <string>542 1440 0 0</string>
            </attr>
        </node>
        <edge from="n0" to="n0">
            <attr name="label">
                <string>type:InitialNode</string>
            </attr>
        </edge>
        <node id="n1">
            <attr name="layout">
                <string>62 1520 0 0</string>
            </attr>
        </node>
        <edge from="n1" to="n1">
            <attr name="label">
                <string>type:ActivityDiagram</string>
            </attr>
        </edge>
        <node id="n35">
            <attr name="layout">
                <string>542 1600 0 0</string>
            </attr>
        </node>
        <edge from="n35" to="n35">
            <attr name="label">
                <string>bool:false</string>
            </attr>
        </edge>
        <edge from="n1" to="n35">
            <attr name="label">
                <string>running</string>
            </attr>
        </edge>
        <node id="n36">
            <attr name="layout">
                <string>542 1520 0 0</string>
            </attr>
        </node>
        <edge from="n36" to="n36">
            <attr name="label">
                <string>string:"TTC_Workflow"</string>
            </attr>
        </edge>
        <edge from="n1" to="n36">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n2">
            <attr name="layout">
                <string>1422 1217 0 0</string>
            </attr>
        </node>
        <edge from="n2" to="n2">
            <attr name="label">
                <string>type:Variable</string>
            </attr>
        </edge>
        <node id="n37">
            <attr name="layout">
                <string>1637 1145 0 0</string>
            </attr>
        </node>
        <edge from="n37" to="n37">
            <attr name="label">
                <string>string:"not internal"</string>
            </attr>
        </edge>
        <edge from="n2" to="n37">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n3">
            <attr name="layout">
                <string>1667 1225 0 0</string>
            </attr>
        </node>
        <edge from="n3" to="n3">
            <attr name="label">
                <string>type:BooleanValue</string>
            </attr>
        </edge>
        <node id="n38">
            <attr name="layout">
                <string>1972 1225 0 0</string>
            </attr>
        </node>
        <edge from="n38" to="n38">
            <attr name="label">
                <string>bool:false</string>
            </attr>
        </edge>
        <edge from="n3" to="n38">
            <attr name="label">
                <string>value</string>
            </attr>
        </edge>
        <node id="n4">
            <attr name="layout">
                <string>1422 1377 0 0</string>
            </attr>
        </node>
        <edge from="n4" to="n4">
            <attr name="label">
                <string>type:Variable</string>
            </attr>
        </edge>
        <node id="n39">
            <attr name="layout">
                <string>1637 1385 0 0</string>
            </attr>
        </node>
        <edge from="n39" to="n39">
            <attr name="label">
                <string>string:"internal"</string>
            </attr>
        </edge>
        <edge from="n4" to="n39">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n5">
            <attr name="layout">
                <string>1667 1305 0 0</string>
            </attr>
        </node>
        <edge from="n5" to="n5">
            <attr name="label">
                <string>type:BooleanValue</string>
            </attr>
        </edge>
        <node id="n40">
            <attr name="layout">
                <string>1972 1305 0 0</string>
            </attr>
        </node>
        <edge from="n40" to="n40">
            <attr name="label">
                <string>bool:false</string>
            </attr>
        </edge>
        <edge from="n5" to="n40">
            <attr name="label">
                <string>value</string>
            </attr>
        </edge>
        <node id="n6">
            <attr name="layout">
                <string>572 1360 0 0</string>
            </attr>
        </node>
        <edge from="n6" to="n6">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n41">
            <attr name="layout">
                <string>877 1289 0 0</string>
            </attr>
        </node>
        <edge from="n41" to="n41">
            <attr name="label">
                <string>string:"register"</string>
            </attr>
        </edge>
        <edge from="n6" to="n41">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n7">
            <attr name="layout">
                <string>1147 1369 0 0</string>
            </attr>
        </node>
        <edge from="n7" to="n7">
            <attr name="label">
                <string>type:Not</string>
            </attr>
        </edge>
        <node id="n8">
            <attr name="layout">
                <string>542 1272 0 0</string>
            </attr>
        </node>
        <edge from="n8" to="n8">
            <attr name="label">
                <string>type:DecisionNode</string>
            </attr>
        </edge>
        <node id="n9">
            <attr name="layout">
                <string>562 1192 0 0</string>
            </attr>
        </node>
        <edge from="n9" to="n9">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n42">
            <attr name="layout">
                <string>877 1192 0 0</string>
            </attr>
        </node>
        <edge from="n42" to="n42">
            <attr name="label">
                <string>string:"assign_to_project_external"</string>
            </attr>
        </edge>
        <edge from="n9" to="n42">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n10">
            <attr name="layout">
                <string>542 628 0 0</string>
            </attr>
        </node>
        <edge from="n10" to="n10">
            <attr name="label">
                <string>type:MergeNode</string>
            </attr>
        </edge>
        <node id="n11">
            <attr name="layout">
                <string>562 1032 0 0</string>
            </attr>
        </node>
        <edge from="n11" to="n11">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n43">
            <attr name="layout">
                <string>877 1032 0 0</string>
            </attr>
        </node>
        <edge from="n43" to="n43">
            <attr name="label">
                <string>string:"get_welcome_package"</string>
            </attr>
        </edge>
        <edge from="n11" to="n43">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n12">
            <attr name="layout">
                <string>542 868 0 0</string>
            </attr>
        </node>
        <edge from="n12" to="n12">
            <attr name="label">
                <string>type:ForkNode</string>
            </attr>
        </edge>
        <node id="n13">
            <attr name="layout">
                <string>562 1112 0 0</string>
            </attr>
        </node>
        <edge from="n13" to="n13">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n44">
            <attr name="layout">
                <string>877 1112 0 0</string>
            </attr>
        </node>
        <edge from="n44" to="n44">
            <attr name="label">
                <string>string:"add_to_website"</string>
            </attr>
        </edge>
        <edge from="n13" to="n44">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n14">
            <attr name="layout">
                <string>562 788 0 0</string>
            </attr>
        </node>
        <edge from="n14" to="n14">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n45">
            <attr name="layout">
                <string>877 788 0 0</string>
            </attr>
        </node>
        <edge from="n45" to="n45">
            <attr name="label">
                <string>string:"assign_to_project"</string>
            </attr>
        </edge>
        <edge from="n14" to="n45">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n15">
            <attr name="layout">
                <string>542 948 0 0</string>
            </attr>
        </node>
        <edge from="n15" to="n15">
            <attr name="label">
                <string>type:JoinNode</string>
            </attr>
        </edge>
        <node id="n16">
            <attr name="layout">
                <string>562 708 0 0</string>
            </attr>
        </node>
        <edge from="n16" to="n16">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n46">
            <attr name="layout">
                <string>877 708 0 0</string>
            </attr>
        </node>
        <edge from="n46" to="n46">
            <attr name="label">
                <string>string:"manager_interview"</string>
            </attr>
        </edge>
        <edge from="n16" to="n46">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n17">
            <attr name="layout">
                <string>562 312 0 0</string>
            </attr>
        </node>
        <edge from="n17" to="n17">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n47">
            <attr name="layout">
                <string>877 312 0 0</string>
            </attr>
        </node>
        <edge from="n47" to="n47">
            <attr name="label">
                <string>string:"manager_report"</string>
            </attr>
        </edge>
        <edge from="n17" to="n47">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n18">
            <attr name="layout">
                <string>562 152 0 0</string>
            </attr>
        </node>
        <edge from="n18" to="n18">
            <attr name="label">
                <string>type:OpaqueAction</string>
            </attr>
        </edge>
        <node id="n48">
            <attr name="layout">
                <string>877 152 0 0</string>
            </attr>
        </node>
        <edge from="n48" to="n48">
            <attr name="label">
                <string>string:"authorize_payment"</string>
            </attr>
        </edge>
        <edge from="n18" to="n48">
            <attr name="label">
                <string>name</string>
            </attr>
        </edge>
        <node id="n19">
            <attr name="layout">
                <string>542 72 0 0</string>
            </attr>
        </node>
        <edge from="n19" to="n19">
            <attr name="label">
                <string>type:FinalNode</string>
            </attr>
        </edge>
        <node id="n20">
            <attr name="layout">
                <string>122 1424 0 0</string>
            </attr>
        </node>
        <edge from="n20" to="n20">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n21">
            <attr name="layout">
                <string>122 1344 0 0</string>
            </attr>
        </node>
        <edge from="n21" to="n21">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n22">
            <attr name="layout">
                <string>122 1264 0 0</string>
            </attr>
        </node>
        <edge from="n22" to="n22">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n23">
            <attr name="layout">
                <string>122 649 0 0</string>
            </attr>
        </node>
        <edge from="n23" to="n23">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n24">
            <attr name="layout">
                <string>122 1049 0 0</string>
            </attr>
        </node>
        <edge from="n24" to="n24">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n25">
            <attr name="layout">
                <string>122 809 0 0</string>
            </attr>
        </node>
        <edge from="n25" to="n25">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n26">
            <attr name="layout">
                <string>122 889 0 0</string>
            </attr>
        </node>
        <edge from="n26" to="n26">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n27">
            <attr name="layout">
                <string>122 409 0 0</string>
            </attr>
        </node>
        <edge from="n27" to="n27">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n28">
            <attr name="layout">
                <string>122 969 0 0</string>
            </attr>
        </node>
        <edge from="n28" to="n28">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n29">
            <attr name="layout">
                <string>122 569 0 0</string>
            </attr>
        </node>
        <edge from="n29" to="n29">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n30">
            <attr name="layout">
                <string>122 489 0 0</string>
            </attr>
        </node>
        <edge from="n30" to="n30">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n31">
            <attr name="layout">
                <string>122 329 0 0</string>
            </attr>
        </node>
        <edge from="n31" to="n31">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n32">
            <attr name="layout">
                <string>122 249 0 0</string>
            </attr>
        </node>
        <edge from="n32" to="n32">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n33">
            <attr name="layout">
                <string>122 169 0 0</string>
            </attr>
        </node>
        <edge from="n33" to="n33">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <node id="n34">
            <attr name="layout">
                <string>122 80 0 0</string>
            </attr>
        </node>
        <edge from="n34" to="n34">
            <attr name="label">
                <string>type:ControlFlow</string>
            </attr>
        </edge>
        <edge from="n1" to="n0">
            <attr name="label">
                <string>start</string>
            </attr>
        </edge>
        <edge from="n2" to="n3">
            <attr name="label">
                <string>value</string>
            </attr>
        </edge>
        <edge from="n4" to="n5">
            <attr name="label">
                <string>value</string>
            </attr>
        </edge>
        <edge from="n6" to="n7">
            <attr name="label">
                <string>exp</string>
            </attr>
        </edge>
        <edge from="n7" to="n4">
            <attr name="label">
                <string>assignee</string>
            </attr>
        </edge>
        <edge from="n7" to="n2">
            <attr name="label">
                <string>1</string>
            </attr>
        </edge>
        <edge from="n20" to="n0">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n20" to="n6">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n21" to="n6">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n21" to="n8">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n22" to="n8">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n22" to="n9">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n23" to="n9">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n23" to="n10">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n24" to="n8">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n24" to="n11">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n25" to="n11">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n25" to="n12">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n26" to="n12">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n26" to="n13">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n27" to="n12">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n27" to="n14">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n28" to="n13">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n28" to="n15">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n29" to="n14">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n29" to="n15">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n30" to="n15">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n30" to="n16">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n31" to="n16">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n31" to="n17">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n32" to="n17">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n32" to="n10">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n33" to="n10">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n33" to="n18">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
        <edge from="n34" to="n18">
            <attr name="label">
                <string>source</string>
            </attr>
        </edge>
        <edge from="n34" to="n19">
            <attr name="label">
                <string>target</string>
            </attr>
        </edge>
    </graph>
</gxl>
