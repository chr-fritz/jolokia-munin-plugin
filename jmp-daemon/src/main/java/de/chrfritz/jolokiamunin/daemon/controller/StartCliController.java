// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: StartCliController
//              File: StartCliController.java
//        changed by: christian.fritz
//       change date: 03.01.16 14:18
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon.controller;

import de.chrfritz.jolokiamunin.api.CliController;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.daemon.Server;
import de.chrfritz.jolokiamunin.daemon.ShutdownMonitor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * CLI Controller to start the daemon.
 *
 * @author christian.fritz
 */
public class StartCliController implements CliController {
    public static final String BIND_IP_PROPERTY = "de.chrfritz.jolokiamunin.bindIp";
    public static final String BIND_PORT_PROPERTY = "de.chrfritz.jolokiamunin.bindPort";
    private static final Logger LOGGER = LoggerFactory.getLogger(StartCliController.class);
    public static final int TIMEOUT = 25;

    /**
     * Get a list with all command names that the controller is responsible for.
     *
     * @return A list with all handled commands.
     */
    @Override
    public List<String> getHandledCommands() {
        return Arrays.asList("start", "daemon");
    }

    /**
     * Process the request.
     *
     * @param arguments The arguments to execute the request processing.
     * @return The response.
     */
    @Override
    public String execute(String arguments) {
        try {
            return daemon();
        }
        catch (IOException | ConfigurationException | InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Can not start jolokia munin plugin as daemon.", e);
            return "ERROR: Can not start jolokia munin plugin as daemon. Take a look into the Logfile for more details.";
        }
    }

    /**
     * Get a short help message.
     *
     * @return The help message.
     */
    @Override
    public String getHelpMessage() {
        return "Start the jolokia munin plugin as daemon. Acts as a munin-node.";
    }

    /**
     * Run the Jolokia Munin Plugin as Munin-Deamon.
     */
    private String daemon() throws IOException, ConfigurationException, InterruptedException, ExecutionException,
            TimeoutException {
        ShutdownMonitor.getInstance().start();
        Server server;
        SocketAddress bindTo = getBindAddress();
        if (bindTo == null) {
            server = new Server();
        }
        else {
            server = new Server(bindTo);
        }
        new Thread(server).start();
        Integer port = server.get(TIMEOUT, TimeUnit.SECONDS);
        return "Daemon successfully started on port " + port + "\n";
    }

    private static SocketAddress getBindAddress() {
        String bindIp = System.getProperty(BIND_IP_PROPERTY);
        int bindPort = Integer.parseInt(System.getProperty(BIND_PORT_PROPERTY, "-1"));
        if (StringUtils.isNotBlank(bindIp) && bindPort >= 0) {
            return new InetSocketAddress(bindIp, bindPort);
        }
        else if (StringUtils.isBlank(bindIp) && bindPort >= 0) {
            return new InetSocketAddress(bindPort);
        }
        else if (StringUtils.isNotBlank(bindIp) && bindPort < 0) {
            return new InetSocketAddress(bindIp, Server.DEFAULT_PORT);
        }
        else {
            return null;
        }
    }
}
