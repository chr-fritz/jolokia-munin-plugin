// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: Dispatcher
//              File: Dispatcher.java
//        changed by: christian.fritz
//       change date: 29.12.15 15:42
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api;

import de.chrfritz.jolokiamunin.api.config.Configuration;

import java.io.IOException;

/**
 * @author christian.fritz
 */
public interface Dispatcher {
    /**
     * Handle a unique request.
     * The request begins with the command name followed by zero or more arguments.
     *
     * @param request The request string.
     * @return The response.
     */
    String handleRequest(String request);

    /**
     * Resolve all controllers form classpath.
     *
     * @throws IOException In case of the classpath can not be fully read.
     */
    void resolveControllers();

    Configuration getConfiguration();

    void setConfiguration(Configuration configuration);
}
