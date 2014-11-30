// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: XMLConfigurationFactory
//              File: XMLConfigurationFactory.java
//        changed by: christian
//       change date: 27.03.13 10:21
//       description: Initalize a new xml configuration
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.config.impl;

import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.ConfigurationException;
import de.chrfritz.jolokiamunin.config.ConfigurationFactory;

import java.io.File;

/**
 * Initalize a new xml configuration.
 */
@Deprecated
public class XMLConfigurationFactory implements ConfigurationFactory {

    /**
     * Get a new configuration from file.
     *
     * @param file The configuration file.
     * @return The new configuration.
     */
    @Override
    public Configuration getInstance(File file) throws ConfigurationException {
        return new XMLConfiguration().loadConfig(file);
    }

    /**
     * Get a configuration from a string file path
     *
     * @param filePath The file path
     * @return The new configuration.
     */
    @Override
    public Configuration getInstance(String filePath) throws ConfigurationException {
        return getInstance(new File(filePath));
    }
}
