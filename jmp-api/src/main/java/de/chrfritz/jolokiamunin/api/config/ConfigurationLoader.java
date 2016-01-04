// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: ConfigurationLoader
//              File: ConfigurationLoader.java
//        changed by: christian.fritz
//       change date: 04.01.16 20:08
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api.config;

import java.net.URI;

/**
 * A configuration loader helps the application to load a config.
 *
 * @author christian.fritz
 */
public interface ConfigurationLoader {

    /**
     * Load a configuration.
     *
     * @return A configuration instance.
     * @throws ConfigurationException In case of the configuration can not loaded.
     */
    Configuration loadConfig() throws ConfigurationException;

    /**
     * Get the loaded configuration uri.
     *
     * @return the loaded configuration uri.
     */
    URI getLoadedUri();
}
