// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FetchController
//              File: FetchController.java
//        changed by: christian.fritz
//       change date: 31.12.13 16:39
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.jolokia.FetcherException;

/**
 * Fetches the values for all defined graphs from the jolokia agents and build a valid munin fetch response.
 *
 * @author christian.fritz
 */
public class FetchController extends AbstractController {

    @Override
    protected String handle() throws FetcherException {
        return getMuninProvider().getValues(getConfiguration().getConfiguration());
    }

    @Override
    public String getHelpMessage() {
        return "Fetch all values";
    }
}
