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

import de.chrfritz.jolokiamunin.api.config.Configuration;

import java.util.List;

import static java.util.Collections.singletonList;

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

    /**
     * Get a list with all command names that the controller is responsible for.
     *
     * @return A list with all handled commands.
     */
    @Override
    public List<String> getHandledCommands() {
        return singletonList("config");
    }

    @Override
    public String getHelpMessage() {
        return "Get the configuration for munin";
    }
}
