// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: StartCliControllerTest
//              File: StartCliControllerTest.java
//        changed by: christian.fritz
//       change date: 03.01.16 15:03
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon.controller;

import de.chrfritz.jolokiamunin.api.Dispatcher;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import de.chrfritz.jolokiamunin.common.lookup.LookupStrategy;
import de.chrfritz.jolokiamunin.daemon.ShutdownMonitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for the {@link StartCliController} and {@link StopCliController}.
 *
 * @author christian.fritz
 */
@RunWith(MockitoJUnitRunner.class)
public class StartStopCliControllerTest {
    @Mock
    private LookupStrategy strategy;

    @Before
    public void setUp() throws Exception {
        Lookup.init(strategy);
        when(strategy.lookup(Dispatcher.class)).thenReturn(mock(Dispatcher.class));
        when(strategy.lookup(ConfigurationLoader.class)).thenReturn(mock(ConfigurationLoader.class));
        System.setProperty(ShutdownMonitor.STOP_KEY_PROPERTY, "testStopKey");
        System.setProperty(StartCliController.BIND_PORT_PROPERTY, "0");
        Thread.interrupted(); // clear interrupted
    }

    @Test
    public void testExecute() throws Exception {
        String start = new StartCliController().execute("");
        assertThat(start, startsWith("Daemon successfully started on port"));

        String stop = new StopCliController().execute("");
        assertThat(stop, is(equalTo("Stopping successful")));
    }
}
