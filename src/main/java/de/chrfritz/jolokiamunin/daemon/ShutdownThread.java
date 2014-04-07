// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ShutdownThread
//              File: ShutdownThread.java
//        changed by: christian.fritz
//       change date: 07.04.14 12:01
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The shutdown tread. This thread holds a list of all started servers and will be executed when the jvm stopped.
 *
 * @author christian.fritz
 */
public final class ShutdownThread extends Thread {

    private static final ShutdownThread THREAD = new ShutdownThread();
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownThread.class);
    private final List<Server> servers = new CopyOnWriteArrayList<>();
    private boolean hooked;

    private ShutdownThread() {
    }

    /**
     * Get the shutdown thread instance.
     *
     * @return The shutdown thread instance.
     */
    public static ShutdownThread getInstance() {
        return THREAD;
    }

    /**
     * Register a server instance for shutdown.
     *
     * @param server The server instance which should be stopped on termination.
     */
    public static synchronized void register(Server server) {
        getInstance().servers.add(server);
        if (THREAD.servers.size() > 0) {
            THREAD.hook();
        }
    }

    /**
     * Unregister a server instance from shutdown.
     *
     * @param server The instance to unregister.
     */
    public static synchronized void unregister(Server server) {
        getInstance().servers.remove(server);
        if (THREAD.servers.size() == 0) {
            THREAD.unhook();
        }
    }

    /**
     * Run the shutdown of all servers.
     */
    @Override
    public void run() {
        for (Server server : servers) {
            try {
                server.close();
            }
            catch (IOException e) {
                LOGGER.info("Can not shutdown server", e);
            }
        }
    }

    /**
     * Register the shutdown thread as jvm shutdown hook
     */
    private synchronized void hook() {
        if (!hooked) {
            Runtime.getRuntime().addShutdownHook(this);
        }
        hooked = true;
    }

    /**
     * Unregister the shutdown thread as jvm shutdown hook
     */
    private synchronized void unhook() {
        hooked = false;
        Runtime.getRuntime().removeShutdownHook(this);
    }
}
