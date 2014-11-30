// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigController
//              File: ConfigController.java
//        changed by: christian.fritz
//       change date: 31.03.14 13:34
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.config.Configuration;

/**
 * Get the munin compatible configuration for all graphs defined within the configuration.
 *
 * @author christian.fritz
 */
public class ConfigController extends AbstractController {

    @Override
    protected String handle() {
        Configuration config = getConfiguration();
        return getMuninProvider().getConfig(config.getConfiguration());
    }

    @Override
    public String getHelpMessage() {
        return "Get the configuration for munin";
    }
}
