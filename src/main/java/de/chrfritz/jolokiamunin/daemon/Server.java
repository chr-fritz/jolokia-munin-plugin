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


import de.chrfritz.jolokiamunin.config.ConfigurationFactory;
import de.chrfritz.jolokiamunin.controller.Dispatcher;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static final int DEFAULT_PORT = 4949;

    private final ServerSocket server;

    private Thread serverThread;
    private Thread configurationWatchService;
    private Dispatcher dispatcher;

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private SocketAddress socketAddress;

    public Server(MuninProvider muninProvider, ConfigurationFactory configurationFactory) throws IOException {
        this(muninProvider, configurationFactory, new InetSocketAddress(DEFAULT_PORT));
    }

    public Server(MuninProvider muninProvider, ConfigurationFactory configurationFactory,
            SocketAddress socketAddress) throws IOException {
        server = new ServerSocket();
        dispatcher = new Dispatcher(muninProvider);
        configurationWatchService = new Thread(new ConfigurationWatchService(configurationFactory, dispatcher));
        configurationWatchService.setName("configurationWatchService");
        configurationWatchService.setDaemon(true);
        ShutdownThread.register(this);
        this.socketAddress = socketAddress;
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        configurationWatchService.start();
        try {
            dispatcher.resolveControllers();
        }
        catch (IOException e) {
            LOGGER.error("Can not resolve controllers.", e);
            return;
        }

        serverThread = Thread.currentThread();
        serverThread.setName("Munin-Node-Server-Thread");
        LOGGER.info("Server successfully started at {}", server.getLocalSocketAddress());
        try {
            server.bind(socketAddress);
            while (!Thread.interrupted()) {
                Socket clientSocket = server.accept();
                threadPool.execute(new Client(clientSocket, dispatcher));
            }
        }
        catch (IOException e) {
            LOGGER.debug("Can not handle client connection", e);
        }
    }

    @Override
    public void close() throws IOException {
        serverThread.interrupt();
        configurationWatchService.interrupt();
        threadPool.shutdown();
        synchronized (server) {
            server.close();
        }
    }
}
