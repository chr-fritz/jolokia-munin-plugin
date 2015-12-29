// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: Client
//              File: Client.java
//        changed by: christian.fritz
//       change date: 13.09.14 16:54
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.daemon;

import de.chrfritz.jolokiamunin.api.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

public class Client implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private static final int SOCKET_TIMEOUT = 1000 * 90;

    private final Socket clientSocket;
    private final Charset charset = Charset.forName("UTF-8");
    private Dispatcher dispatcher;


    public Client(Socket clientSocket, Dispatcher dispatcher) {
        this.clientSocket = clientSocket;
        this.dispatcher = dispatcher;
        LOGGER.info("Connection accepted from {}", clientSocket.getRemoteSocketAddress());
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        LOGGER.debug("Start processing client communication");
        try (
                Socket socket = clientSocket;
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset))
        ) {
            socket.setSoTimeout(SOCKET_TIMEOUT);
            sendHelloBanner(writer);
            while (!Thread.interrupted()) {
                String command = reader.readLine();
                LOGGER.debug("Received command \"{}\" from client {}", command, clientSocket.getRemoteSocketAddress());
                if (command != null) {
                    String response = handleCommands(command.trim()) + ".\n";
                    LOGGER.debug("Finished processing of command \"{}\".", command);
                    writer.write(response);
                    writer.flush();
                }
            }
        }
        catch (SocketTimeoutException e) {
            LOGGER.info("Closing connection because of read timeout");
        }
        catch (IOException e) {
            LOGGER.error("Can not open streams", e);
        }
    }

    private void sendHelloBanner(BufferedWriter writer) throws IOException {
        LOGGER.debug("Send hello banner with hostname {}", dispatcher.getConfiguration().getBannerHostname());
        writer.write("# munin node at " + dispatcher.getConfiguration().getBannerHostname() + "\n");
        writer.flush();
    }

    protected String handleCommands(String commandLine) {
        String[] parts = commandLine.split("[\n ]+", 2);
        String command = parts[0];
        switch (command) {
            case "quit":
            case "exit":
                Thread.currentThread().interrupt();
                return "";
            default:
                return dispatcher.handleRequest(commandLine) + "\n";
        }
    }
}
