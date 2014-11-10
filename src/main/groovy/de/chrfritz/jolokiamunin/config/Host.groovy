// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Host.groovy
//              File: Host.groovy
//        changed by: christian.fritz
//       change date: 22.09.14 22:27
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config

/**
 *
 * @author christian.fritz
 */
class Host {
    String hostName
    URI agent
    List<Category> category = []

    Host(String hostName) {
        this.hostName = hostName
    }

    URI getAgent() {
        return agent
    }

    void setAgent(URI agent) {
        this.agent = agent
    }

    List<Category> getCategory() {
        return category
    }

}
