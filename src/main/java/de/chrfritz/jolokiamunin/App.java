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
import de.chrfritz.jolokiamunin.config.impl.XMLConfiguration;
import de.chrfritz.jolokiamunin.jolokia.FetcherException;
import de.chrfritz.jolokiamunin.jolokia.impl.JolokiaFetcherFactory;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import de.chrfritz.jolokiamunin.munin.impl.MuninProviderImpl;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

/**
 * The Main Application Class
 */
public class App {

    private MuninProvider muninProvider;

    /**
     * Main Entry Point to run the application.
     *
     * @param args The commandline arguments
     * @throws Exception
     */
    public static void main(String[] args) throws IOException, FetcherException, ConfigurationException {

        MuninProvider provider = new MuninProviderImpl(new JolokiaFetcherFactory());
        System.out.print(new App(provider).run(args));
    }

    /**
     * Initialize a new application instance
     *
     * @param muninProvider The MuninProvider for fetching and configuring the application.
     */
    public App(MuninProvider muninProvider) {
        this.muninProvider = muninProvider;
    }

    /**
     * Run the application.
     *
     * @param args The commandline arguments.
     */
    protected String run(String[] args) throws IOException, FetcherException, ConfigurationException {

        // Fetch the values
        if (args.length == 0) {
            return fetch();
        } else {
            String command = args[0];
            if (StringUtils.equals(command, "config")) {
                return config();
            } else if (StringUtils.equals(command, "fetch")) {
                return fetch();
            } else if (StringUtils.equals(command, "version")) {
                return version();
            } else {
                return help();
            }
        }
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
     * @throws FetcherException
     */
    private String fetch() throws MalformedURLException, ConfigurationException, FetcherException {
        Configuration config = getConfiguration();
        return muninProvider.getValues(config.getConfiguration());
    }

    /**
     * Print the current version string on system out.
     *
     * @throws IOException In case of there are some io errors.
     */
    private String version() throws IOException {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.properties"));

        StringBuilder buffer = new StringBuilder();
        buffer.append("Jolokia-Munin Plugin by Christian Fritz 2013 \n")
                .append("Version: ")
                .append(props.getProperty("jmp.version"))
                .append("\n")
                .append("Build #")
                .append(props.getProperty("jmp.buildNumber"))
                .append(" from git commit ")
                .append(props.getProperty("jmp.buildCommit"))
                .append(" on git branch ")
                .append(props.getProperty("jmp.buildBranch"))
                .append("\n");

        return buffer.toString();
    }

    /**
     * Prints the help.
     */
    private String help() throws IOException {
        return version();
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
     * @return
     * @throws MalformedURLException
     * @throws ConfigurationException
     */
    protected Configuration getConfiguration() throws MalformedURLException, ConfigurationException {
        String configName;
        configName = System.getProperty("configFile", System.getenv("JOLOKIAMUNIN_CONFIG"));
        if (Strings.isNullOrEmpty(configName)) {
            configName = System.getenv("PWD") + "jolokiamunin.xml";
        }
        File file = new File(configName);
        Configuration config = new XMLConfiguration(file.toURI().toURL());
        config.load();
        return config;
    }
}

