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

import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    private Client client;

    @Mock
    private MuninProvider provider;

    @Mock
    private Configuration config;

    @Mock
    private Socket clientSocket;

    @Before
    public void setUp() throws Exception {
        client = new Client(clientSocket, provider, config);
    }

    @After
    public void tearDown() throws Exception {
        client = null;
    }

    @Test
    public void testHandleCommands() throws Exception {
        Client clientMock = mock(Client.class);
        when(clientMock.handleCommands(anyString())).thenCallRealMethod();

        assertFalse(Thread.currentThread().isInterrupted());
        clientMock.handleCommands("quit");

        assertTrue(Thread.currentThread().isInterrupted());

        assertEquals("ERROR: Can not handle request. Invalid command\n", clientMock.handleCommands("unspecified"));
    }
}
