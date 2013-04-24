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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final ServerSocket server;

    private Thread serverThread;

    public Server() {

        try {
            server = new ServerSocket(4949);
        }
        catch (IOException e) {
            LOGGER.error("Can not bind to address", e);
            throw new RuntimeException(e);
        }
    }

    public Server(InetAddress bindTo, int port) {

        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(bindTo, port));
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
        serverThread = Thread.currentThread();
        serverThread.setName("Munin-Node-Server-Thread");
        LOGGER.info("Server successfully started at {}", server.getLocalSocketAddress());
        while (!Thread.interrupted()) {
            try {
                Socket clientSocket = server.accept();
                new Thread(new Client(clientSocket)).start();
            }
            catch (IOException e) {
                LOGGER.error("Can not handle client connection", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        serverThread.interrupt();
        synchronized (server) {
            server.close();
        }
    }
}
