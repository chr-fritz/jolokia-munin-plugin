// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Controller
//              File: Controller.java
//        changed by: christian.fritz
//       change date: 31.03.14 14:42
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.munin.MuninProvider;

/**
 * A controller which handles incoming requests.
 * <p/>
 * Every request starts with the controller name and may followed by some arguments. The dispatcher decides which
 * controller must be used to handle this request and calls the controllers {@link Controller#execute(String)} method
 * and send the return value to the client.
 * <p/>
 * Additionally a controller can return a short help message which should contain a short description what are valid
 * arguments and how they influence the controllers work.
 *
 * @author christian.fritz
 */
public interface Controller {

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

    /**
     * Get the munin provider thats build the response.
     *
     * @return The munin porvider instance.
     */
    MuninProvider getMuninProvider();

    /**
     * Set a new munin provider instance.
     *
     * @param provider The new instance.
     */
    void setMuninProvider(MuninProvider provider);

    /**
     * Get the configuration thats used to execute the request.
     *
     * @return The current configuration.
     */
    Configuration getConfiguration();

    /**
     * Set a new configuration for executing further requests.
     *
     * @param configuration the new configuration
     */
    void setConfiguration(Configuration configuration);

    /**
     * Set the dispatcher which delegates the request to the controller.
     *
     * @param dispatcher The dispatcher instance
     */
    void setDispatcher(Dispatcher dispatcher);
}
