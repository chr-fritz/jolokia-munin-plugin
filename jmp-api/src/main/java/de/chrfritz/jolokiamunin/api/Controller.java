// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: Controller
//              File: Controller.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:47
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api;

import java.util.List;

/**
 * A controller which handles incoming requests.
 * <p>
 * Every request starts with the controller name and may followed by some arguments. The dispatcher decides which
 * controller must be used to handle this request and calls the controllers {@link Controller#execute(String)} method
 * and send the return value to the client.
 * <p>
 * Additionally a controller can return a short help message which should contain a short description what are valid
 * arguments and how they influence the controllers work.
 *
 * @author christian.fritz
 */
public interface Controller {

    /**
     * Get a list with all command names that the controller is responsible for.
     *
     * @return A list with all handled commands.
     */
    List<String> getHandledCommands();

    /**
     * Process the request.
     *
     * @param arguments The arguments to execute the request processing.
     * @return The response.
     */
    String execute(String arguments);

    /**
     * Get a short help message.
     *
     * @return The help message.
     */
    String getHelpMessage();
}
