// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: Dispatcher
//              File: Dispatcher.java
//        changed by: christian.fritz
//       change date: 30.12.15 19:10
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api;

import java.util.List;

/**
 * The main interface for a dispatcher that redirects calls to the controllers.
 *
 * @author christian.fritz
 */
public interface Dispatcher {

    /**
     * Initialize the dispatcher.
     *
     * @param searchTypes The controller interface types to search.
     */
    void init(List<Class<? extends Controller>> searchTypes);

    /**
     * Handle a unique request.
     * The request begins with the command name followed by zero or more arguments.
     *
     * @param request The request string.
     * @return The response.
     */
    String handleRequest(String request);
}
