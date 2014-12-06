// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-core
//             Class: CapController
//              File: CapController.java
//        changed by: christian.fritz
//       change date: 06.12.14 22:01
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

/**
 * @author christian.fritz
 */
public class CapController extends AbstractController {

    /**
     * Handles the request processing.
     *
     * @return The response thats fullfill the request.
     * @throws Exception In case of any error executing the request.
     */
    @Override
    protected String handle() throws Exception {
        return "cap multigraph dirtyconfig";
    }

    /**
     * Get a short help message.
     *
     * @return The help message.
     */
    @Override
    public String getHelpMessage() {
        return "Get the capabilities of this server.";
    }
}
