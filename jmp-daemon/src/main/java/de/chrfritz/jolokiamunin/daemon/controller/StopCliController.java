// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: StopCliController
//              File: StopCliController.java
//        changed by: christian.fritz
//       change date: 03.01.16 14:18
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon.controller;

import de.chrfritz.jolokiamunin.api.CliController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import static de.chrfritz.jolokiamunin.daemon.ShutdownMonitor.STOP_KEY_PROPERTY;
import static de.chrfritz.jolokiamunin.daemon.ShutdownMonitor.STOP_PORT_POPERTY;
import static java.util.Collections.singletonList;

/**
 * CLI Controller to stop the daemon.
 *
 * @author christian.fritz
 */
public class StopCliController implements CliController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartCliController.class);

    /**
     * Get a list with all command names that the controller is responsible for.
     *
     * @return A list with all handled commands.
     */
    @Override
    public List<String> getHandledCommands() {
        return singletonList("stop");
    }

    /**
     * Process the request.
     *
     * @param arguments The arguments to execute the request processing.
     * @return The response.
     */
    @Override
    public String execute(String arguments) {
        return stop();
    }

    /**
     * Get a short help message.
     *
     * @return The help message.
     */
    @Override
    public String getHelpMessage() {
        return "Stops the daemon.";
    }

    /**
     * Stop the daemon.
     *
     * @return The answer from the daemon
     * @throws IOException In case of some connection errors.
     */
    private String stop() {
        LOGGER.info("Stop daemon");
        Charset charset = Charset.forName("UTF-8");
        try (Socket socket = new Socket("127.0.0.1", Integer.parseInt(System.getProperty(STOP_PORT_POPERTY, "49049")));
             Writer writer = new OutputStreamWriter(socket.getOutputStream(), charset);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset))) {
            LOGGER.debug("Send stop command");
            writer.write(System.getProperty(STOP_KEY_PROPERTY));
            writer.write("\n");
            writer.write("stop\n");
            writer.flush();
            return reader.readLine();
        }
        catch (IOException e) {
            LOGGER.error("Can not connect to daemon.", e);
            return "ERROR: Can not stop daemon.";
        }
    }
}
