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
import java.util.List;

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
     * @throws ConfigurationException In case of the configuration can not loaded.
     */
    Configuration loadConfig(File configFile) throws ConfigurationException;


    /**
     * Get a list of all file extensions which can be read by the implementing configuration loader.
     *
     * @return A list of all readable file extensions.
     */
    List<String> getAssignedFileExtensions();
}
