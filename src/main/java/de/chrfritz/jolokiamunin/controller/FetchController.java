// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FetchController
//              File: FetchController.java
//        changed by: christian
//       change date: 31.12.13 16:39
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.jolokia.FetcherException;

/**
 * Fetch all values
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
