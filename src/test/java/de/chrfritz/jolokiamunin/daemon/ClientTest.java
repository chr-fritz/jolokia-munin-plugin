// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ClientTest
//              File: ClientTest.java
//        changed by: christian
//       change date: 15.05.13 14:56
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.daemon;

import de.chrfritz.jolokiamunin.controller.Dispatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.Socket;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    private Client client;

    @Mock
    private Dispatcher dispatcher;
    @Mock
    private Socket clientSocket;

    @Before
    public void setUp() throws Exception {
        client = new Client(clientSocket, dispatcher);
    }

    @After
    public void tearDown() throws Exception {
        client = null;
    }

    @Test
    public void testHandleCommandsQuit() throws Exception {
        assertThat(Thread.currentThread().isInterrupted(), is(false));
        client.handleCommands("quit");
        assertThat(Thread.currentThread().isInterrupted(), is(true));
    }

    @Test
    public void testHandleCommandsInvalid() throws Exception {
        client.handleCommands("unspecified");
        verify(dispatcher).handleRequest("unspecified");
    }
}
