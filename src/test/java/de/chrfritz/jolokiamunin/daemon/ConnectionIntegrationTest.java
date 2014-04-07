// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConnectionIntegrationTest
//              File: ConnectionIntegrationTest.java
//        changed by: christian.fritz
//       change date: 07.04.14 14:03
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author christian.fritz
 */
public class ConnectionIntegrationTest {

    private Socket socket;

    @Before
    public void setUp() throws Exception {
        int port = Integer.parseInt(System.getProperty("de.chrfritz.jolokiamunin.bindPort", "4949"));
        socket = new Socket("127.0.0.1", port);
    }

    @Test
    public void testVersion() throws Exception {
        try (Writer writer = new OutputStreamWriter(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            writer.write("version\n");
            writer.flush();
            assertThat(reader.readLine(), containsString("Jolokia-Munin Plugin by Christian Fritz"));
        }
    }
}
