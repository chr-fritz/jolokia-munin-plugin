// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigurationBuilder.groovy
//              File: ConfigurationBuilder.groovy
//        changed by: christian.fritz
//       change date: 19.09.14 22:59
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config

/**
 *
 * @author christian.fritz
 */
class ConfigurationBuilder extends BuilderSupport {
    public static ConfigurationBuilder newInstance() {
        return new ConfigurationBuilder();
    }

    protected void setParent(Object parent, Object child) {
        println parent + "--" + child
    }

    protected Object createNode(Object name) {
        println "currentNode(" + name + ") - c:" + current
        return "ret:" + name
    }

    protected Object createNode(Object name, Object value) {
        println "currentNode(n:" + name + ",v:" + value + ") - c:" + current

        return "ret:(n:" + name + ",v:" + value + ")"
    }

    protected Object createNode(Object name, Map attributes) {
        println "currentNode(n:" + name + ",a:" + attributes ") - c:" +current
        return "ret:(n:" + name + ",a:" + attributes + ")"
    }

    protected Object createNode(Object name, Map attributes, Object value) {
        println "currentNode(n:" + name + ",a:" + attributes ",v:" +value + ") - c:" + current
        return "ret:(n:" + name + ",a:" + attributes + ",v:" + value + ")"
    }
}
