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

    /**
     * Get the ip address for that interface where the daemon should be bind.
     * <p/>
     * Default is 0.0.0.0 (also known as all interfaces)
     *
     * @return The bind ip address.
     */
    String getBindAddress();

    /**
     * Get the port to listen for incomming connections when run in daemon mode.
     * <p/>
     * Default is 4949.
     *
     * @return The listen port for daemon mode.
     */
    int getPort();

    /**
     * Is it allowed to fetch a single graph when the daemon mode is used?
     *
     * @return Is the single fetch mode allowed.
     */
    boolean isSingleFetchAllowed();

    /**
     * Get the hostname for the hello banner when using the daemon mode.
     *
     * @return the hostname
     */
    String getBannerHostname();
}
