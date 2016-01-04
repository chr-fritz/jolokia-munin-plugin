// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: ShutdownThreadTest
//              File: ShutdownThreadTest.java
//        changed by: christian.fritz
//       change date: 29.12.15 15:51
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test for {@link ShutdownThread}
 *
 * @author christian.fritz
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class ShutdownThreadTest {

    private ShutdownThread thread = ShutdownThread.getInstance();
    @Mock
    private Server server;

    @Before
    public void setUp() throws Exception {
        cleanUp();
    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }

    private void cleanUp() {
        List<Server> servers = (List<Server>) Whitebox.getInternalState(thread, "servers");
        for (int i = servers.size() - 1; i > 0; i--) {
            servers.remove(i);
        }
        if (servers.size() == 1) {
            ShutdownThread.unregister(servers.get(0));
        }
    }

    @Test
    public void testUnRegister() throws Exception {
        assertThat((List<Server>) Whitebox.getInternalState(thread, "servers"), hasSize(0));
        assertThat(Whitebox.getInternalState(thread, "hooked"), is(false));
        ShutdownThread.register(server);
        assertThat((List<Server>) Whitebox.getInternalState(thread, "servers"), hasSize(1));
        assertThat(Whitebox.getInternalState(thread, "hooked"), is(true));
        ShutdownThread.unregister(server);
        assertThat((List<Server>) Whitebox.getInternalState(thread, "servers"), hasSize(0));
        assertThat(Whitebox.getInternalState(thread, "hooked"), is(false));
    }

    @Test
    public void testRun() throws Exception {
        ShutdownThread.register(server);
        assertThat((List<Server>) Whitebox.getInternalState(thread, "servers"), contains(server));
        thread.run();
        Mockito.verify(server).close();
    }
}
