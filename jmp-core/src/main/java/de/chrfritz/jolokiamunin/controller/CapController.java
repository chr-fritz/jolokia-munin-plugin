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

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author christian.fritz
 */
public class CapController extends AbstractController {

    /**
     * Handles the request processing.
     *
     * @return The response thats fullfill the request.
     */
    @Override
    protected String handle() {
        return "cap multigraph dirtyconfig";
    }

    /**
     * Get a list with all command names that the controller is responsible for.
     *
     * @return A list with all handled commands.
     */
    @Override
    public List<String> getHandledCommands() {
        return singletonList("cap");
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
