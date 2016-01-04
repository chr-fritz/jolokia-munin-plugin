// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: AbstractController
//              File: AbstractController.java
//        changed by: christian.fritz
//       change date: 31.03.14 13:53
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.api.Controller;
import de.chrfritz.jolokiamunin.api.MuninProvider;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Base class for all controllers. It implements the most methods of the {@link Controller} interface and provides the
 * exception handling for executing the request.
 *
 * @author christian.fritz
 */
public abstract class AbstractController implements Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    private List<String> arguments;

    /**
     * Process the request.
     *
     * @param arguments The arguments to execute the request processing.
     * @return The response.
     */
    @Override
    public String execute(String arguments) {
        try {
            this.arguments = Arrays.asList(arguments.split("\\s"));
            return handle();
        }
        catch (Exception e) {
            LOGGER.error("Can not handle request.", e);
            return "ERROR: " + e.getMessage();
        }
    }

    protected List<String> getArguments() {
        return arguments;
    }

    protected MuninProvider getMuninProvider() {
        return Lookup.lookup(MuninProvider.class);
    }

    protected Configuration getConfiguration() {
        return Lookup.lookup(Configuration.class);
    }

    /**
     * Handles the request processing.
     *
     * @return The response thats fullfill the request.
     * @throws Exception In case of any error executing the request.
     */
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    protected abstract String handle() throws Exception;
}
