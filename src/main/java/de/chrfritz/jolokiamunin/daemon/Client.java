// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Client
//              File: Client.java
//        changed by: christian
//       change date: 23.04.13 19:06
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.daemon;

import de.chrfritz.jolokiamunin.App;
import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.jolokia.FetcherException;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

public class Client implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private static final int SOCKET_TIMEOUT = 1000 * 15;

    private final Socket clientSocket;

    private final Configuration configuration;

    private final MuninProvider muninProvider;

    public Client(Socket clientSocket, MuninProvider muninProvider, Configuration configuration) {
        this.muninProvider = muninProvider;
        this.configuration = configuration;
        this.clientSocket = clientSocket;
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        try (
                Socket socket = clientSocket;
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            socket.setSoTimeout(SOCKET_TIMEOUT);
            while (!Thread.interrupted()) {
                String command = reader.readLine().trim();
                String response = handleCommands(command);
                writer.write(response);
                writer.flush();
            }
        }
        catch (SocketTimeoutException e) {
            LOGGER.info("Closing connection because of read timeout");
        }
        catch (IOException e) {
            LOGGER.error("Can not open streams", e);
        }
    }

    protected String handleCommands(String commandLine) {
        String[] parts = commandLine.split("[\n ]+", 2);
        String command = parts[0];
        String arg = null;
        if (parts.length == 2) {
            arg = parts[1];
        }
        switch (command) {
            case "list":
                return handleList();
            case "fetch":
                return handleFetch(arg);
            case "config":
                return handleConfig(arg);
            case "version":
                return handleVersion();
            case "quit":
            case "exit":
                Thread.currentThread().interrupt();
                return "";
            default:
                return "ERROR: Invalid Command\n";
        }
    }

    protected String handleVersion() {
        try {
            return App.version();
        }
        catch (IOException e) {
            LOGGER.error("Can not fetch version information", e);
            return "ERROR: Unknown to get version info\n";
        }
    }

    protected String handleConfig(String graph) {
        return muninProvider.getConfig(configuration.getConfiguration());
    }

    protected String handleFetch(String graph) {
        try {
            return muninProvider.getValues(configuration.getConfiguration());
        }
        catch (FetcherException e) {
            LOGGER.error("Can not fetch values.", e);
            return "ERROR: Can not fetch values";
        }
    }

    protected String handleList() {
        if (configuration.isSingleFetchAllowed()) {
            List<String> graphNames = muninProvider.getGraphNames(configuration.getConfiguration());
            StringBuilder sb = new StringBuilder();
            for (String name : graphNames) {
                sb.append(name).append(" ");
            }
            return sb.toString().trim();
        } else {
            return "jolokia";
        }
    }
}
