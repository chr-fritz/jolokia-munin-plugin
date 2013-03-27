//______________________________________________________________________________
//
//          Project:
//             File: $Id: ${FILE_NAME} $
//     last changed: $Rev$
//______________________________________________________________________________
//
//       created by: Christian Fritz
//    creation date: 27.03.13
//       changed by: $Author$
//      change date: $LastChangedDate$
//      description: Factory for new configuration instances
//______________________________________________________________________________
//
//        Copyright: (c) Christian Fritz, all rights reserved
//______________________________________________________________________________
package de.chrfritz.jolokiamunin.config;

import java.io.File;
import java.net.URL;

/**
 * Factory for new configuration instances.
 */
public interface ConfigurationFactory {

    /**
     * Get a new configuration from file.
     *
     * @param file The configuration file.
     * @return The new configuration.
     * @throws ConfigurationException In case of the configuration can not be created.
     */
    Configuration getInstance(File file) throws ConfigurationException;

    /**
     * Get a configuration from a string file path
     *
     * @param filePath The file path
     * @return The new configuration.
     * @throws ConfigurationException In case of the configuration can not be created.
     */
    Configuration getInstance(String filePath) throws ConfigurationException;

    /**
     * Get a configuration from given url.
     *
     * @param url The url for the new configuration
     * @return The new configuration.
     * @throws ConfigurationException In case of the configuration can not be created.
     */
    Configuration getInstance(URL url) throws ConfigurationException;
}
