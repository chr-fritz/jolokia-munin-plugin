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

import com.google.common.base.Strings;
import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.ConfigurationException;
import de.chrfritz.jolokiamunin.config.ConfigurationFactory;
import de.chrfritz.jolokiamunin.config.impl.XMLConfigurationFactory;
import de.chrfritz.jolokiamunin.controller.VersionController;
import de.chrfritz.jolokiamunin.daemon.Server;
import de.chrfritz.jolokiamunin.jolokia.impl.JolokiaFetcherFactory;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import de.chrfritz.jolokiamunin.munin.impl.MuninProviderImpl;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * The Main Application Class
 */
public class App {

    private MuninProvider muninProvider;
    private ConfigurationFactory configFactory;

    /**
     * Main Entry Point to run the application.
     *
     * @param args The commandline arguments
     * @throws IOException
     * @throws ConfigurationException
     */
    public static void main(String[] args) throws IOException, ConfigurationException {
        MuninProvider provider = new MuninProviderImpl(new JolokiaFetcherFactory());
        ConfigurationFactory configurationFactory = new XMLConfigurationFactory();
        System.out.print(new App(provider, configurationFactory).run(args));
    }

    /**
     * Initialize a new application instance
     *
     * @param muninProvider The MuninProvider for fetching and configuring the application.
     */
    public App(MuninProvider muninProvider, ConfigurationFactory configFactory) {
        this.muninProvider = muninProvider;
        this.configFactory = configFactory;
    }

    /**
     * Run the application.
     *
     * @param args The commandline arguments.
     */
    protected String run(String[] args) throws IOException, ConfigurationException {
        // Fetch the values
        if (args.length == 0) {
            return fetch();
        }
        else {
            String command = args[0];
            switch (command) {
                case "daemon":
                    return daemon();
                case "config":
                    return config();
                case "fetch":
                    return fetch();
                case "version":
                    return version();
                case "help":
                default:
                    return help();
            }
        }
    }

    /**
     * Run the Jolokia Munin Plugin as Munin-Deamon.
     */
    private String daemon() throws IOException, ConfigurationException {
        new Thread(new Server(muninProvider, configFactory)).start();
        return "Daemon successfully started";
    }

    /**
     * Load and show the configuration.
     *
     * @throws MalformedURLException
     * @throws ConfigurationException
     */
    private String config() throws MalformedURLException, ConfigurationException {
        Configuration config = getConfiguration();
        return muninProvider.getConfig(config.getConfiguration());
    }

    /**
     * Fetch the current values.
     *
     * @throws MalformedURLException
     * @throws ConfigurationException
     */
    private String fetch() throws MalformedURLException, ConfigurationException {
        Configuration config = getConfiguration();
        return muninProvider.getValues(config.getConfiguration());
    }

    /**
     * Print the current version string on system out.
     *
     * @throws IOException In case of there are some io errors.
     */
    public static String version() throws IOException {
        return new VersionController().execute("");
    }

    /**
     * Prints the help.
     */
    private String help() throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("Usage: jolokia [command]\n")
                .append("Available Commands:\n")
                .append("    config:    Get the configuration for munin\n")
                .append("    daemon:    Run it as daemon\n")
                .append("     fetch:    Fetch all values\n")
                .append("   version:    Print the version string\n")
                .append("      help:    Print this help\n")
                .append("------------------------------------------------------------\n")
                .append(version())
                .append('\n');
        return builder.toString();
    }

    /**
     * Get and load the current configuration.
     * The configuration is searched by the following algorithm:
     * <ul>
     * <li>System-Property "configFile"</li>
     * <li>Enviorment Variable "JOLOKIAMUNIN_CONFIG"</li>
     * <li>In the current working directory a file named "jolokiamunin.xml"</li>
     * </ul>
     *
     * @return The loaded configuration.
     * @throws MalformedURLException
     * @throws ConfigurationException
     */
    protected Configuration getConfiguration() throws MalformedURLException, ConfigurationException {
        String configName;
        configName = System.getProperty("configFile", System.getenv("JOLOKIAMUNIN_CONFIG"));
        if (Strings.isNullOrEmpty(configName)) {
            configName = System.getenv("PWD") + "jolokiamunin.xml";
        }
        Configuration config = configFactory.getInstance(configName);
        config.load();
        return config;
    }
}

