//______________________________________________________________________________
//
//          Project:
//             File: $Id: ${FILE_NAME} $
//     last changed: $Rev$
//______________________________________________________________________________
//
//       created by: Christian Fritz
//    creation date: 23.04.13
//       changed by: $Author$
//      change date: $LastChangedDate$
//      description:
//______________________________________________________________________________
//
//        Copyright: (c) Christian Fritz, all rights reserved
//______________________________________________________________________________
package de.chrfritz.jolokiamunin.daemon;


import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.controller.Dispatcher;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class Server implements Runnable, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static final int DEFAULT_PORT = 4949;

    private final ServerSocket server;

    private Thread serverThread;

    private Configuration configuration;

    private MuninProvider muninProvider;

    public Server(MuninProvider muninProvider, Configuration configuration) throws IOException {
        this.configuration = configuration;
        this.muninProvider = muninProvider;
        server = new ServerSocket(DEFAULT_PORT);
    }

    public Server(MuninProvider muninProvider, Configuration configuration, SocketAddress socketAddress) throws
            IOException {
        this.configuration = configuration;
        this.muninProvider = muninProvider;
        server = new ServerSocket();
        server.bind(socketAddress);
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        if (configuration == null) {
            LOGGER.error("Can not start daemon becuase there is no valid configuration");
            return;
        }
        Dispatcher dispatcher = new Dispatcher(configuration, muninProvider);
        serverThread = Thread.currentThread();
        serverThread.setName("Munin-Node-Server-Thread");
        LOGGER.info("Server successfully started at {}", server.getLocalSocketAddress());
        try {
            while (!Thread.interrupted()) {

                Socket clientSocket = server.accept();
                new Thread(new Client(clientSocket, dispatcher)).start();
            }
        }
        catch (IOException e) {
            LOGGER.error("Can not handle client connection", e);
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void close() throws IOException {
        serverThread.interrupt();
        synchronized (server) {
            server.close();
        }
    }
}
