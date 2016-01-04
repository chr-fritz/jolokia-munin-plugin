// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Dispatcher
//              File: Dispatcher.java
//        changed by: christian.fritz
//       change date: 05.04.14 14:55
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.api.Controller;
import de.chrfritz.jolokiamunin.api.Dispatcher;
import de.chrfritz.jolokiamunin.api.DispatcherAwareController;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles the loading of all command controllers and dispatches the incomming command requests to the controllers.
 * They will handle the request.
 *
 * @author christian.fritz
 */
public class SimpleDispatcher implements Dispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDispatcher.class);

    private Map<String, Class<? extends Controller>> controllerClasses;
    private Map<List<String>, String> helpTexts = new HashMap<>();

    /**
     * Initialize the dispatcher.
     *
     * @param searchTypes The controller interface types to search.
     */
    @Override
    public void init(List<Class<? extends Controller>> searchTypes) {
        if (controllerClasses != null) {
            throw new IllegalStateException("Dispatcher is already initialized");
        }
        controllerClasses = searchTypes.stream()
                .flatMap(t -> Lookup.lookupAll(t).stream())
                .sorted((c1, c2) -> c1.getClass().getSimpleName().compareTo(c2.getClass().getSimpleName()))
                .peek(c -> helpTexts.put(c.getHandledCommands(), c.getHelpMessage()))
                .flatMap(c -> c.getHandledCommands().stream().map(cmd -> Pair.of(cmd, (Class) c.getClass())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }


    /**
     * Handle a unique request.
     * The request begins with the command name followed by zero or more arguments.
     *
     * @param request The request string.
     * @return The response.
     */
    @Override
    public String handleRequest(String request) {
        LOGGER.info("Start handling request: {}", request);
        String[] requestArray = request.split("\\s+", 2);
        Controller controller = getControllerForCommand(requestArray[0]);

        String arguments = requestArray.length > 1 ? requestArray[1] : "";
        if (controller != null) {
            String response = controller.execute(arguments);
            LOGGER.info("Finished handling request (command: {})", requestArray[0]);
            return response;
        }
        else {
            LOGGER.error("Unable to handle request: Controller not found.");
            return "ERROR: Can not handle request. Invalid command";
        }
    }

    private Controller getControllerForCommand(String command) {
        if (!controllerClasses.containsKey(command)) {
            return null;
        }
        Controller lookup = Lookup.lookup(controllerClasses.get(command));
        if (lookup instanceof DispatcherAwareController) {
            ((DispatcherAwareController) lookup).setDispatcher(this);
        }
        return lookup;
    }

    /**
     * Build a short help message that contains the command name and a short description of all installed controllers.
     *
     * @author christian.fritz
     */
    public static class HelpController implements DispatcherAwareController {
        private static final int PREFIX_LENGTH = 25;
        private SimpleDispatcher dispatcher;

        /**
         * Get a list with all command names that the controller is responsible for.
         *
         * @return A list with all handled commands.
         */
        @Override
        public List<String> getHandledCommands() {
            return Arrays.asList("help", "h");
        }

        /**
         * Process the request.
         *
         * @param arguments The arguments to execute the request processing.
         * @return The response.
         */
        @Override
        public String execute(String arguments) {
            StringBuilder builder = new StringBuilder();
            builder.append("Usage: jolokia [command]\n")
                    .append("Available Commands:\n");

            Integer prefixLength = dispatcher.helpTexts.keySet()
                    .stream()
                    .map(strings -> strings.stream()
                            .map((s) -> s.length() + 2)
                            .collect(Collectors.summingInt(i -> i)) - 2
                    )
                    .max(Integer::compare)
                    .orElse(PREFIX_LENGTH);

            for (Map.Entry<List<String>, String> entry : dispatcher.helpTexts.entrySet()) {
                String command = entry.getKey().stream().collect(Collectors.joining(", "));
                builder.append(StringUtils.leftPad(command, prefixLength))
                        .append(" : ")
                        .append(entry.getValue())
                        .append('\n');
            }
            builder.append("------------------------------------------------------------\n")
                    .append(dispatcher.handleRequest("version"))
                    .append('\n');
            return builder.toString();
        }

        /**
         * Get a short help message.
         *
         * @return The help message.
         */
        @Override
        public String getHelpMessage() {
            return "Print this help message";
        }

        @Override
        public void setDispatcher(Dispatcher dispatcher) {
            this.dispatcher = (SimpleDispatcher) dispatcher;
        }
    }
}
