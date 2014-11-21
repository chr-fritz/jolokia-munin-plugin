// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: GroovyConfiguration.groovy
//              File: GroovyConfiguration.groovy
//        changed by: christian.fritz
//       change date: 14.11.14 23:25
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.groovy

import de.chrfritz.jolokiamunin.config.Category
import groovy.transform.Canonical

/**
 * Implementation of a generic configuration which is loaded through a groovy builder.
 *
 * @author christian.fritz
 */
@Canonical
class Configuration implements de.chrfritz.jolokiamunin.config.Configuration {

    String name
    String bindAddress
    int port
    boolean singleFetchAllowed
    String bannerHostname
    List<Category> configuration = new ArrayList<Category>()

    /**
     * {@inheritDoc}
     */
    public void load() {}
}
