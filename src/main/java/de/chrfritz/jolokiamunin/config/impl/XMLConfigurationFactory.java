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
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Initalize a new xml configuration.
 */
public class XMLConfigurationFactory implements ConfigurationFactory {

    /**
     * Get a new configuration from file.
     *
     * @param file The configuration file.
     * @return The new configuration.
     */
    @Override
    public Configuration getInstance(File file) throws ConfigurationException {
        try {
            return new XMLConfiguration(file.toURI().toURL());
        }
        catch (MalformedURLException e) {
            throw new ConfigurationException(e);
        }
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

    /**
     * Get a configuration from given url.
     *
     * @param url The url for the new configuration
     * @return The new configuration.
     */
    @Override
    public Configuration getInstance(URL url) {
        return new XMLConfiguration(url);
    }
}
