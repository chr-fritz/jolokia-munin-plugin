// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigurationBuilderBinding.groovy
//              File: ConfigurationBuilderBinding.groovy
//        changed by: christian.fritz
//       change date: 15.11.14 23:30
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.groovy

/**
 * Script engine binding to avoid the need of creating a new Configuration builder within the configuration file.
 *
 * @author christian.fritz
 */
class ConfigurationBuilderBinding extends Binding {
    ConfigurationBuilder builder

    Object getVariable(String name) {
        return { Object... args -> builder.invokeMethod(name, args) }
    }
}
