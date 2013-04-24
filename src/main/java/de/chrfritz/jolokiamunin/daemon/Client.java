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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final Socket clientSocket;

    public Client(Socket clientSocket) {
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
            while (!Thread.interrupted()) {
                String command = reader.readLine().trim();
                String response = handleCommands(command);
                writer.write(response);
                writer.flush();
            }
        }
        catch (IOException e) {
            LOGGER.error("Can not open streams", e);
        }
    }

    private String handleCommands(String command) {
        String[] parts=command.split("[\n ]+");
        switch (parts[0].toLowerCase()) {
            case "list":
            case "fetch":
            case "config":
            case "version":
                try {
                    return App.version();
                }
                catch (IOException e) {
                    LOGGER.error("Can not fetch version information", e);
                    return "Unknown to get version info\n";
                }
            case "quit":
            case "exit":
                Thread.currentThread().interrupt();
                return "";
            default:
                return "INVALID COMMAND\n";
        }
    }
}
