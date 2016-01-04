// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ShutdownMonitor
//              File: ShutdownMonitor.java
//        changed by: christian.fritz
//       change date: 07.04.14 11:25
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Monitors the shutdown process. It listens on localhost on a configureable port for the stop command.
 *
 * @author christian.fritz
 */
public final class ShutdownMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownMonitor.class);
    public static final int MIN_PORT_NUMBER = 1024;
    public static final String STOP_KEY_PROPERTY = "STOP.KEY";
    public static final String STOP_PORT_POPERTY = "STOP.PORT";
    private ShutdownMonitorThread thread;
    private ServerSocket serverSocket;
    private int port;
    private String key;

    /**
     * Get the monitor instance.
     *
     * @return The monitor instance.
     */
    public static ShutdownMonitor getInstance() {
        return Holder.instance;
    }

    private ShutdownMonitor() {
        port = Integer.parseInt(System.getProperty(STOP_PORT_POPERTY, "49049"));
        key = System.getProperty(STOP_KEY_PROPERTY);
    }

    /**
     * Start the monitor
     */
    public synchronized void start() {
        if (thread != null && thread.isAlive()) {
            return;
        }
        thread = new ShutdownMonitorThread();
        thread.start();
    }

    /**
     * The actual monitor thread.
     */
    private class ShutdownMonitorThread extends Thread {
        public ShutdownMonitorThread() {
            setDaemon(true);
            setName("ShutdownMonitor");
        }

        @Override
        public synchronized void start() {
            try {
                if (port < MIN_PORT_NUMBER) {
                    LOGGER.info("Invalid port number {} assigned", port);
                    return;
                }
                serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
            }
            catch (IOException e) {
                LOGGER.warn("Can not start shutdown monitor", e);
            }
            super.start();
        }

        @Override
        @SuppressWarnings("RU_INVOKE_RUN")
        public void run() {
            if (serverSocket == null) {
                return;
            }
            Charset charset = Charset.forName("UTF-8");
            while (serverSocket != null) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader lin = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
                     Writer out = new OutputStreamWriter(socket.getOutputStream(), charset)
                ) {
                    String receivedKey = lin.readLine();
                    if (!key.equals(receivedKey)) {
                        LOGGER.info("wrong key");
                        continue;
                    }
                    String command = lin.readLine();
                    if ("stop".equals(command)) {
                        LOGGER.info("Issuing shutdown...");
                        ShutdownThread.getInstance().run();
                        out.write("Stopping successful");
                        out.flush();
                        socket.close();
                        serverSocket.close();
                        serverSocket = null;
                    }
                }
                catch (IOException e) {
                    LOGGER.warn("Connection Error", e);
                }
            }
        }
    }

    /**
     * Implementation of safe lazy init, using Initialization on Demand Holder technique.
     */
    private static final class Holder {
        private static ShutdownMonitor instance = new ShutdownMonitor();

        private Holder() {
        }
    }
}
