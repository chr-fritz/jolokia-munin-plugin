# Jolokia Munin Plugin
The Jolokia Munin Plugin is a plugin for the [Munin](http://munin-monitoring.org/) monitoring
software which helps you to monitor every JVM thats loaded with the [Jolokia](http://jolokia.org/) agent.
## Features
* Multi-Graph Munin Plugin
* Fetch values form one or more agents over http connections
* Configureable through XML
* Only one JVM execution per Munin-Update
* Easy to install
## System Requirements
### On Target
 * Running JVM you want to monitor with installed and running Jolokia Agent.
 * Jolokia needs at least a Java 6 VM.
### On Munin-Node
 * Java Runtime Enviorment (JRE) 1.7 or heigher
 * Execute "java" from path variable
 * And of course a configured Munin-Node (at least version 1.4)
