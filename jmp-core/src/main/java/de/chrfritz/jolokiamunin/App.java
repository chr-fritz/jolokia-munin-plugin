// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: App
//              File: App.java
//        changed by: christian
//       change date: 02.03.13 19:47
//       description: The Main Application class
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin;

import de.chrfritz.jolokiamunin.api.CliController;
import de.chrfritz.jolokiamunin.api.Controller;
import de.chrfritz.jolokiamunin.api.Dispatcher;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import de.chrfritz.jolokiamunin.common.lookup.impl.ServiceLoaderLookupStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * The Main Application Class
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private ServiceLoaderLookupStrategy lookupStrategy;

    /**
     * Main Entry Point to run the application.
     *
     * @param args The commandline arguments
     */
    public static void main(String[] args) {
        System.out.print(new App(new ServiceLoaderLookupStrategy()).run(args));
    }

    public App(ServiceLoaderLookupStrategy lookupStrategy) {
        this.lookupStrategy = lookupStrategy;
        Lookup.init(lookupStrategy);
    }

    protected String run(String[] args) {
        try {
            loadConfiguration();
            String[] arguments = args.length != 0 ? args : new String[]{"fetch"};
            return dispatch(arguments);
        }
        catch (MalformedURLException | ConfigurationException e) {
            LOGGER.error("Unable to load configuration", e);
            return "ERROR: Can not load configuration.";
        }
        catch (Exception e) {
            LOGGER.error("Can not execute command", e);
            return "ERROR: " + e.getMessage();
        }
    }

    private Dispatcher getDispatcher() {
        try {
            Dispatcher dispatcher = Lookup.lookup(Dispatcher.class);
            lookupStrategy.initInstance(Dispatcher.class, dispatcher.getClass().newInstance(), true);
            return dispatcher;
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Dispatch the requests.
     *
     * @param args The request args
     * @return The response for the dispatched request
     */
    private String dispatch(String[] args) {
        Dispatcher dispatcher = getDispatcher();
        dispatcher.init(Arrays.asList(CliController.class, Controller.class));
        return dispatcher.handleRequest(StringUtils.join(args, ' '));
    }

    /**
     * Get and load the current configuration.
     * The configuration is searched by the following algorithm:
     * <ul>
     * <li>System-Property "configFile"</li>
     * <li>Enviorment Variable "JOLOKIAMUNIN_CONFIG"</li>
     * <li>In the current working directory a file named "jolokiamunin.groovy"</li>
     * </ul>
     *
     * @throws MalformedURLException
     * @throws ConfigurationException
     */
    protected void loadConfiguration() throws MalformedURLException, ConfigurationException {
        String configName;
        configName = System.getProperty("configFile", System.getenv("JOLOKIAMUNIN_CONFIG"));
        if (StringUtils.isBlank(configName)) {
            configName = System.getenv("PWD") + "/jolokiamunin.groovy";
        }
        Configuration configuration = Lookup.lookup(ConfigurationLoader.class).loadConfig(new File(configName));
        lookupStrategy.initInstance(Configuration.class, configuration);
    }
}

