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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author christian.fritz
 */
public class ConnectionIntegrationTest {

    private Socket socket;
    private Writer writer;
    private BufferedReader reader;

    @Before
    public void setUp() throws Exception {
        int port = Integer.parseInt(System.getProperty("de.chrfritz.jolokiamunin.bindPort", "65049"));
        socket = new Socket("127.0.0.2", port);
        writer = new OutputStreamWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @After
    public void tearDown() throws Exception {
        writer.write("quit\n");
        writer.flush();
        reader.close();
        writer.close();
        socket.close();
    }

    @Test
    public void testVersion() throws Exception {
        assertThat(reader.readLine(), is(equalTo("# munin node at localhost")));
        writer.write("version\n");
        writer.flush();
        assertThat(reader.readLine(), containsString("Jolokia-Munin Plugin by Christian Fritz"));
    }

    @Test
    public void testHelp() throws Exception {
        assertThat(reader.readLine(), is(equalTo("# munin node at localhost")));
        writer.write("help\n");
        writer.flush();
        assertThat(reader.readLine(), is(equalTo("Usage: jolokia [command]")));
        assertThat(reader.readLine(), is(equalTo("Available Commands:")));
    }

    @Test
    public void testConfig() throws Exception {
        assertThat(reader.readLine(), is(equalTo("# munin node at localhost")));
        writer.write("config\n");
        writer.flush();
        assertThat(reader.readLine(), is(equalTo("multigraph ServletContainer_heapMem")));
        assertThat(reader.readLine(), is(equalTo("graph_title HeapMemoryUsage")));
        assertThat(reader.readLine(), is(equalTo("graph_args --base 1024")));
        assertThat(reader.readLine(), is(equalTo("graph_category ServletContainer")));
    }

    @Test
    public void testList() throws Exception {
        assertThat(reader.readLine(), is(equalTo("# munin node at localhost")));
        writer.write("list\n");
        writer.flush();
        assertThat(reader.readLine(), is(equalTo("ServletContainer_heapMem Tomcat_noneHeapMem")));
    }

    @Test
    public void testInvalidCommand() throws Exception {
        assertThat(reader.readLine(), is(equalTo("# munin node at localhost")));
        writer.write("invalidCommand\n");
        writer.flush();
        assertThat(reader.readLine(), is(equalTo("ERROR: Can not handle request. Invalid command")));
    }
}
