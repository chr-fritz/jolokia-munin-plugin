// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: Server
//              File: Server.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:55
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.daemon;


import com.google.common.util.concurrent.SettableFuture;
import de.chrfritz.jolokiamunin.api.Controller;
import de.chrfritz.jolokiamunin.api.Dispatcher;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.*;

import static java.util.Collections.singletonList;

public class Server implements RunnableFuture<Integer>, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static final int DEFAULT_PORT = 4949;

    private final ServerSocket server;

    private Thread serverThread;
    private Thread configurationWatchService;
    private Dispatcher dispatcher = Lookup.lookup(Dispatcher.class);

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private SocketAddress socketAddress;
    private SettableFuture<Integer> started = SettableFuture.create();

    public Server() throws IOException {
        this(new InetSocketAddress(DEFAULT_PORT));
    }

    public Server(SocketAddress socketAddress) throws IOException {
        server = new ServerSocket();
        configurationWatchService = new Thread(new ConfigurationWatchService());
        configurationWatchService.setName("configuration-watch-thread");
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
        dispatcher.init(singletonList(Controller.class));

        serverThread = Thread.currentThread();
        serverThread.setName("Munin-Node-Server-Thread");
        LOGGER.info("Server successfully started at {}", server.getLocalSocketAddress());
        try {
            server.bind(socketAddress);
            started.set(server.getLocalPort());
            while (!Thread.interrupted()) {
                Socket clientSocket = server.accept();
                threadPool.execute(new Client(clientSocket, dispatcher));
            }
        }
        catch (IOException e) {
            LOGGER.debug("Can not handle client connection", e);
            started.setException(e);
        }
    }

    @Override
    public void close() throws IOException {
        LOGGER.warn("Shutdown server");
        serverThread.interrupt();
        configurationWatchService.interrupt();
        threadPool.shutdown();
        synchronized (server) {
            server.close();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return started.isDone();
    }

    @Override
    public Integer get() throws InterruptedException, ExecutionException {
        return started.get();
    }

    @Override
    public Integer get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return started.get(timeout, unit);
    }
}
