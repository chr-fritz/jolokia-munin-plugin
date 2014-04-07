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
import de.chrfritz.jolokiamunin.controller.Dispatcher;
import de.chrfritz.jolokiamunin.daemon.Server;
import de.chrfritz.jolokiamunin.daemon.ShutdownMonitor;
import de.chrfritz.jolokiamunin.jolokia.impl.JolokiaFetcherFactory;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import de.chrfritz.jolokiamunin.munin.impl.MuninProviderImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;

/**
 * The Main Application Class
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
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
            return dispatch(new String[]{"fetch"});
        }
        else {
            String command = args[0];
            switch (command) {
                case "daemon":
                    return daemon();
                case "stop":
                    return stop();
                default:
                    return dispatch(args);
            }
        }
    }

    /**
     * Run the Jolokia Munin Plugin as Munin-Deamon.
     */
    private String daemon() throws IOException, ConfigurationException {
        ShutdownMonitor.getInstance().start();
        new Thread(new Server(muninProvider, configFactory)).start();
        return "Daemon successfully started\n";
    }

    /**
     * Stop the daemon.
     *
     * @return The answer from the daemon
     * @throws IOException In case of some connection errors.
     */
    private String stop() throws IOException {
        LOGGER.info("Stop daemon");
        Socket socket = new Socket("127.0.0.1", Integer.parseInt(System.getProperty("STOP.PORT", "49049")));
        try (Writer writer = new OutputStreamWriter(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            LOGGER.debug("Send stop command");
            writer.write(System.getProperty("STOP.KEY"));
            writer.write("\n");
            writer.write("stop\n");
            writer.flush();
            return reader.readLine();
        }
    }

    /**
     * Dispatch the requests.
     *
     * @param args The request args
     * @return The response for the dispatched request
     * @throws IOException            In case of the configuration or values can not be read.
     * @throws ConfigurationException In case of the configuration contains errors.
     */
    private String dispatch(String[] args) throws IOException, ConfigurationException {
        Dispatcher dispatcher = new Dispatcher(muninProvider);
        dispatcher.resolveControllers();
        dispatcher.setConfiguration(getConfiguration());
        return dispatcher.handleRequest(StringUtils.join(args, ' '));
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

