// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigurationLoader
//              File: ConfigurationLoader.java
//        changed by: christian.fritz
//       change date: 29.11.14 16:26
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config;

import java.io.File;

/**
 * A configuration loader helps the application to load or reload a specific configuration file.
 *
 * @author christian.fritz
 */
public interface ConfigurationLoader {

    /**
     * Load a configuration from a specific file.
     *
     * @param configFile Load the configuration from this file.
     * @return A configuration instance.
     */
    Configuration loadConfig(File configFile);
}
