// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Configuration.groovy
//              File: Configuration.groovy
//        changed by: christian.fritz
//       change date: 22.09.14 22:21
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config

/**
 *
 * @author christian.fritz
 */
class GroovyConfiguration implements Configuration {

    List<Category> categories = []

    /**
     * Load the configuration into the handler.
     *
     * @throws ConfigurationException In case of the configuration can not be loaded.
     */
    @Override
    void load() throws ConfigurationException {

    }

    /**
     * Get the loaded configuration.
     *
     * @return The loaded configuration.
     */
    @Override
    List<Category> getConfiguration() {
        return null
    }

    /**
     * Get the ip address for that interface where the daemon should be bind.
     * <p/>
     * Default is 0.0.0.0 (also known as all interfaces)
     *
     * @return The bind ip address.
     */
    @Override
    String getBindAddress() {
        return null
    }

    /**
     * Get the port to listen for incomming connections when run in daemon mode.
     * <p/>
     * Default is 4949.
     *
     * @return The listen port for daemon mode.
     */
    @Override
    int getPort() {
        return 0
    }

    /**
     * Is it allowed to fetch a single graph when the daemon mode is used?
     *
     * @return Is the single fetch mode allowed.
     */
    @Override
    boolean isSingleFetchAllowed() {
        return false
    }

    /**
     * Get the hostname for the hello banner when using the daemon mode.
     *
     * @return the hostname
     */
    @Override
    String getBannerHostname() {
        return null
    }
}
