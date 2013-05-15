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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
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
        clientMock.handleCommands("list");
        clientMock.handleCommands("fetch");
        clientMock.handleCommands("fetch main");
        clientMock.handleCommands("config");
        clientMock.handleCommands("config main");
        clientMock.handleCommands("version");
        clientMock.handleCommands("quit");

        verify(clientMock).handleList();
        verify(clientMock, times(2)).handleConfig();
        verify(clientMock, times(2)).handleFetch();
        verify(clientMock).handleVersion();

        assertTrue(Thread.currentThread().isInterrupted());

        assertEquals("ERROR: Invalid Command\n", clientMock.handleCommands("unspecified"));
    }

    @Test
    public void testHandleVersion() throws Exception {
        String actual = client.handleVersion();
        assertTrue(actual.contains("Jolokia-Munin Plugin by Christian Fritz"));
    }

    @Test
    @Ignore
    public void testHandleConfig() throws Exception {

    }

    @Test
    @Ignore
    public void testHandleFetch() throws Exception {

    }

    @Test
    @Ignore
    public void testHandleList() throws Exception {

    }
}
