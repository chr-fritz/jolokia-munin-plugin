# Configuration
The Jolokia Munin Plugin is easy to configure through a XML configuration file. It supports
to configure the categories, the graphs and the fields thats a graph contains.

# Example Configuration
This example configuration shows all memory based values and the number of threads of the jvm.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<config xmlns="http://chrfritz.de/jolokia-munin-plugin/config">
    <category name="Jetty" src="http://localhost:8080/jolokia/">
        <graph name="heapMemUsage" title="Heap Memory Usage" vlabel="bytes">
            <info>Heap Memory Usage of JVM</info>
            <args>--base 1024 -l 0</args>
            <mbean>java.lang:type=Memory</mbean>
            <attribute>HeapMemoryUsage</attribute>
            <field name="committed" label="Committed" type="GAUGE" draw="AREA" color="0078c5">
                <path>committed</path>
            </field>
            <field name="used" label="Used" type="GAUGE" draw="AREA" color="ffd700">
                <path>used</path>
            </field>
            <field name="max" label="Maximum" type="GAUGE" draw="LINE1" color="00de00">
                <path>max</path>
            </field>
            <field name="init" label="Initial" type="GAUGE" draw="LINE1" color="ff8d00">
                <path>init</path>
            </field>
        </graph>
        <graph name="nonHeapMemUsage" title="Non Heap Memory Usage" vlabel="bytes">
            <info>Non Heap Memory Usage of JVM</info>
            <args>--base 1024 -l 0</args>
            <mbean>java.lang:type=Memory</mbean>
            <attribute>NonHeapMemoryUsage</attribute>
            <field name="committed" label="Committed" type="GAUGE" draw="AREA" color="0078c5">
                <path>committed</path>
            </field>
            <field name="used" label="Used" type="GAUGE" draw="AREA" color="ffd700">
                <path>used</path>
            </field>
            <field name="max" label="Maximum" type="GAUGE" draw="LINE1" color="00de00">
                <path>max</path>
            </field>
            <field name="init" label="Initial" type="GAUGE" draw="LINE1" color="ff8d00">
                <path>init</path>
            </field>
        </graph>
        <graph name="permGenUsage" title="PermGen Usage" vlabel="bytes">
            <info>PermGen Usage of JVM</info>
            <args>--base 1024 -l 0</args>
            <mbean>java.lang:name=Perm Gen,type=MemoryPool</mbean>
            <attribute>Usage</attribute>
            <field name="committed" label="Committed" type="GAUGE" draw="AREA" color="0078c5">
                <path>committed</path>
            </field>
            <field name="used" label="Used" type="GAUGE" draw="AREA" color="ffd700">
                <path>used</path>
            </field>
            <field name="max" label="Maximum" type="GAUGE" draw="LINE1" color="00de00">
                <path>max</path>
            </field>
            <field name="init" label="Initial" type="GAUGE" draw="LINE1" color="ff8d00">
                <path>init</path>
            </field>
        </graph>
        <graph name="permGenPeakUsage" title="PermGen Peak Usage" vlabel="bytes">
            <info>PermGen PeakUsage of JVM</info>
            <args>--base 1024 -l 0</args>
            <mbean>java.lang:name=Perm Gen,type=MemoryPool</mbean>
            <attribute>PeakUsage</attribute>
            <field name="committed" label="Committed" type="GAUGE" draw="AREA" color="0078c5">
                <path>committed</path>
            </field>
            <field name="used" label="Used" type="GAUGE" draw="AREA" color="ffd700">
                <path>used</path>
            </field>
            <field name="max" label="Maximum" type="GAUGE" draw="LINE1" color="00de00">
                <path>max</path>
            </field>
            <field name="init" label="Initial" type="GAUGE" draw="LINE1" color="ff8d00">
                <path>init</path>
            </field>
        </graph>
        <graph name="permGenCollUsage" title="PermGen Collection Usage" vlabel="bytes">
            <info>PermGen CollectionUsage of JVM</info>
            <args>--base 1024 -l 0</args>
            <mbean>java.lang:name=Perm Gen,type=MemoryPool</mbean>
            <attribute>CollectionUsage</attribute>
            <field name="committed" label="Committed" type="GAUGE" draw="AREA" color="0078c5">
                <path>committed</path>
            </field>
            <field name="used" label="Used" type="GAUGE" draw="AREA" color="ffd700">
                <path>used</path>
            </field>
            <field name="max" label="Maximum" type="GAUGE" draw="LINE1" color="00de00">
                <path>max</path>
            </field>
            <field name="init" label="Initial" type="GAUGE" draw="LINE1" color="ff8d00">
                <path>init</path>
            </field>
        </graph>
        <graph name="threads" title="Threads" vlabel="bytes">
            <info>Number of Threads</info>
            <args>-l 0</args>
            <mbean>java.lang:type=Threading</mbean>
            <field name="ThreadCount" label="Count" type="GAUGE" draw="LINE1">
                <attribute>ThreadCount</attribute>
            </field>
            <field name="PeakThreadCount" label="PeakThreadCount" type="GAUGE" draw="LINE1">
                <attribute>PeakThreadCount</attribute>
            </field>
            <field name="DaemonThreadCount" label="DaemonThreadCount" type="GAUGE" draw="LINE1">
                <attribute>DaemonThreadCount</attribute>
            </field>
        </graph>
    </category>
</config>
```
