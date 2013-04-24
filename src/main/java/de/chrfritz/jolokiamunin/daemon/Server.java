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
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class Server implements Runnable, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final ServerSocket server;

    private Thread serverThread;

    private Configuration configuration;

    private MuninProvider muninProvider;

    public Server(MuninProvider muninProvider, Configuration configuration) {
        this.configuration = configuration;
        this.muninProvider = muninProvider;
        try {
            server = new ServerSocket(4949);
        }
        catch (IOException e) {
            LOGGER.error("Can not bind to address", e);
            throw new RuntimeException(e);
        }
    }

    public Server(MuninProvider muninProvider, Configuration configuration, SocketAddress socketAddress) {
        this.configuration = configuration;
        this.muninProvider = muninProvider;
        try {
            server = new ServerSocket();
            server.bind(socketAddress);
        }
        catch (IOException e) {
            LOGGER.error("Can not bind to address", e);
            throw new RuntimeException(e);
        }
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

        serverThread = Thread.currentThread();
        serverThread.setName("Munin-Node-Server-Thread");
        LOGGER.info("Server successfully started at {}", server.getLocalSocketAddress());
        while (!Thread.interrupted()) {
            try {
                Socket clientSocket = server.accept();
                new Thread(new Client(clientSocket, muninProvider, configuration)).start();
            }
            catch (IOException e) {
                LOGGER.error("Can not handle client connection", e);
                throw new RuntimeException(e);
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void close() throws Exception {
        serverThread.interrupt();
        synchronized (server) {
            server.close();
        }
    }
}
