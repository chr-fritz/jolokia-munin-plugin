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

import com.google.common.reflect.ClassPath;
import de.chrfritz.jolokiamunin.api.Controller;
import de.chrfritz.jolokiamunin.api.MuninProvider;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Handles the loading of all command controllers and dispatches the incomming command requests to the controllers.
 * They will handle the request.
 *
 * @author christian.fritz
 */
public class Dispatcher {

    public static final String DEFAULT_SEARCH_PACKAGE = "de.chrfritz.jolokiamunin.controller";

    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    private List<String> searchPackages;

    private Map<String, Class<? extends Controller>> controllerClasses;

    private Configuration configuration;

    private MuninProvider proivder;

    /**
     * Initialize the dispatcher.
     *
     * @param provider Use this provider to generate the munin compatible answers.
     */
    public Dispatcher(MuninProvider provider) {
        this(Arrays.asList(DEFAULT_SEARCH_PACKAGE), provider);
    }

    public Dispatcher(List<String> searchPackages, MuninProvider provider) {
        if (!searchPackages.contains(DEFAULT_SEARCH_PACKAGE)) {
            searchPackages.add(0, DEFAULT_SEARCH_PACKAGE);
        }
        this.searchPackages = searchPackages;
        this.proivder = provider;
    }

    public synchronized Configuration getConfiguration() {
        return configuration;
    }

    public synchronized void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Handle a unique request.
     * The request begins with the command name followed by zero or more arguments.
     *
     * @param request The request string.
     * @return The response.
     */
    public String handleRequest(String request) {
        LOGGER.info("Handle request: {}", request);
        String[] requestArray = request.split("\\s+", 2);
        Controller controller = getControllerForCommand(requestArray[0]);

        String arguments = requestArray.length > 1 ? requestArray[1] : "";
        if (controller != null) {
            controller.setMuninProvider(proivder);
            controller.setConfiguration(getConfiguration());

            String response = controller.execute(arguments);
            LOGGER.info("Finished handling request (command: {})", requestArray[0]);
            return response;
        }
        else {
            return "ERROR: Can not handle request. Invalid command";
        }
    }

    /**
     * Get the controller for a given command name.
     *
     * @param command The command name.
     * @return The controller matching this command.
     */
    public Controller getControllerForCommand(String command) {
        Class controllerClass = controllerClasses.get(command);
        return initializeController(controllerClass);
    }

    /**
     * Get a list of all controlles.
     *
     * @return A list of all controllers.
     */
    public List<Controller> getControllers() {
        List<Controller> controllers = new ArrayList<>();
        for (Class<? extends Controller> controllerClass : controllerClasses.values()) {
            controllers.add(initializeController(controllerClass));
        }
        return controllers;
    }

    /**
     * Resolve all controllers form classpath.
     *
     * @throws IOException In case of the classpath can not be fully read.
     */
    public void resolveControllers() throws IOException {
        if (controllerClasses != null) {
            return;
        }
        controllerClasses = new HashMap<>();

        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        for (String searchPackage : searchPackages) {
            for (ClassPath.ClassInfo info : classPath.getTopLevelClassesRecursive(searchPackage)) {
                processController(info);
            }
        }
    }

    /**
     * Process the given class info.
     * <p>
     * It checks if the given class info would produce a valid controller and add the class info to the the controller
     * classes.
     *
     * @param info The class information info.
     */
    private void processController(ClassPath.ClassInfo info) {
        String className = info.getSimpleName();
        if (!className.endsWith("Controller")) {
            return;
        }
        Class<?> clazz = info.load();
        int modifiers = clazz.getModifiers();

        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers) ||
                !Controller.class.isAssignableFrom(clazz)) {
            return;
        }

        String command = className.substring(0, className.lastIndexOf("Controller")).toLowerCase();
        controllerClasses.put(command, (Class<? extends Controller>) clazz);
    }

    /**
     * Initialize the a new controller by the given class.
     *
     * @param controllerClass Initialize a new controller instance for this class.
     * @return The fully initialized controller instance.
     */
    private Controller initializeController(Class<? extends Controller> controllerClass) {
        if (controllerClass == null) {
            return null;
        }
        try {
            Object controller = controllerClass.newInstance();
            if (controller instanceof AbstractController) {
                ((AbstractController) controller).setDispatcher(this);
            }
            return (Controller) controller;
        }
        catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Can not init controller class", e);
        }
        return null;
    }
}
