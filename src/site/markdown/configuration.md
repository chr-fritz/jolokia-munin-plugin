# Configuration
The Jolokia Munin Plugin is easy to configure through a XML configuration file. It supports
to configure the categories, the graphs and the fields thats a graph contains.

# Meaning of the Configuration tags
This chapter will explain the meaning of all tags which are can used within the configuration file.

For more technical details please take a view into the `src/main/xsd/config.xsd`-file.
## Tag: config
The `<config>`-Tag is the root container object for a configuration.
### Attributes
The `<config>` tag do not define any attributes.
### Subtags
Every `<config>` tag must contain at least one `<category>` tag.
## Tag: category
The `<category>`-tag describes the general information about the category in which are the containing graphs be placed
by munin.
### Attributes
#### name
The attribute 'name' specifies the name of the category viewed in munin.
#### src
The 'src' attribute defines the source url of the jolokia agent. This is in most cases simply a http or https url.
### Subtags
The `<category>` tag must contain at least one `<graph>` tag.

## Tag: graph
The tag `<graph>` defines a graph which is shown in munin.
### Attributes
#### name
Defines the name for this graph. It should not contain any whitespaces.
#### title
Defines the shown name for this graph.
#### vlabel
Defines the label for the vertical scale.
#### scale
TODO: please refer the munin documentation for further details.
### Subtags
The `<graph>` tag can contain the following tags:

- Never or exactly once
    -  info
    -  args
    -  mbean
    -  attribute
- At least once
    - field

## Tag: info
The tag `<info>` defines the viewed information in munin for this graph. It has no attributes and no subtags.

## Tag: args
The tag `<args>` is used to define additional rdd-tool arguments. It do not define any attributes or subtags. Please refer
the documentation of rdd-tool for more information about this tag.

## Tag: mbean
The `<mbean>` defines the java bean that should be fetched by jolokia. It can be defined for the whole graph or for a
single field. If it is defined on both, the graph and field, the field value will override the graph value. There are no
subtags or attributes.

## Tag: attribute
The `<attribute>` tag defines the jolokia attribute. It can be defined for the whole graph or a single field.
If it is defined on both, the graph and field, the field value will override the graph value. There are no subtags or attributes.

## Tag: field
The tag `<field>` defines a field that is shown within a graph. It is the equivalent to the munin field.
### Subtags
A field can have the following attributes. Every subtag expect is optional. They can maximum occur once.

- info
- mbean
- attribute
- path
### Attributes


# Example Configuration
This example configuration shows all memory based values and the number of threads of the jvm.

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
