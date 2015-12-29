// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ListController
//              File: ListController.java
//        changed by: christian.fritz
//       change date: 31.12.13 16:40
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.api.config.Configuration;

import java.util.List;

/**
 * List the names of all available graphs.
 *
 * @author christian.fritz
 */
public class ListController extends AbstractController {

    @Override
    protected String handle() {
        Configuration configuration = getConfiguration();

        if (configuration.isSingleFetchAllowed()) {
            List<String> graphNames = getMuninProvider().getGraphNames(configuration.getConfiguration());
            StringBuilder sb = new StringBuilder();
            for (String name : graphNames) {
                sb.append(name).append(" ");
            }
            return sb.toString().trim();
        }
        else {
            return "jolokia";
        }
    }

    @Override
    public String getHelpMessage() {
        return "List the names of all available graphs";
    }
}
