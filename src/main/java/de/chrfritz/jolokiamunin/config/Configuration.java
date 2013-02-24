// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Configuration
//              File: Configuration.java
//        changed by: christian
//       change date: 19.02.13 18:17
//       description: Configuration interface
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config;

import java.util.List;

/**
 * A handler to read a configuration.
 */
public interface Configuration {

    /**
     * Load the configuration into the handler.
     *
     * @throws ConfigurationException In case of the configuration can not be loaded.
     */
    void load() throws ConfigurationException;

    /**
     * Get the loaded configuration.
     *
     * @return The loaded configuration.
     */
    List<Category> getConfiguration();
}
