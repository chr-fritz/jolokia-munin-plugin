// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: Configuration.groovy
//              File: Configuration.groovy
//        changed by: christian.fritz
//       change date: 29.12.15 14:46
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api.config

import groovy.transform.Canonical

/**
 * Implementation of a generic configuration which is loaded through a groovy builder.
 *
 * @author christian.fritz
 */
@Canonical
class Configuration {

    String name
    String bindAddress
    int port
    boolean singleFetchAllowed
    String bannerHostname
    private List<Category> configuration = new ArrayList<>()

    /**
     * {@inheritDoc}
     */
    List<Category> getConfiguration() {
        return configuration
    }
}
