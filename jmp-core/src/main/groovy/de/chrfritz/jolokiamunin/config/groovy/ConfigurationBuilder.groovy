// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigurationBuilder.groovy
//              File: ConfigurationBuilder.groovy
//        changed by: christian.fritz
//       change date: 14.11.14 23:10
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.groovy

import de.chrfritz.jolokiamunin.config.Category
import de.chrfritz.jolokiamunin.config.Configuration
import de.chrfritz.jolokiamunin.config.Field
import de.chrfritz.jolokiamunin.config.Graph
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Groovy Builder to build the configuration instance with groovy.
 *
 * @see BuilderSupport
 * @author christian.fritz
 */
public class ConfigurationBuilder extends BuilderSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationBuilder.class)

    @Override
    protected void setParent(Object parent, Object child) {
    }

    @Override
    protected Object createNode(Object name) {
        return createNode(name, [:])
    }

    @Override
    protected Object createNode(Object name, Object value) {
        if (current.hasProperty("$name")) {
            current."$name" = value
        }
        return null
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        Object node = null;
        attributes << [name: name]

        if (current == null) {
            node = new Configuration(name: name)
        }
        else if (current instanceof Configuration) {
            node = new Category()
            current.configuration << node
        }
        else if (current instanceof Category) {
            node = new Graph()
            current.graphs << node
        }
        else if (current instanceof Graph) {
            node = new Field()
            current.fields << node
        }

        attributes.each { k, v ->
            if (node.hasProperty("$k")) {
                node."$k" = v
            }
            else {
                LOGGER.warn('Can not add property {} to {}', k, node?.class?.name)
            }
        }
        return node
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        return null
    }
}

