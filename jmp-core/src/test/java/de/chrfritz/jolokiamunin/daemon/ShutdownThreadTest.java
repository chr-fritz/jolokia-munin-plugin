// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ShutdownThreadTest
//              File: ShutdownThreadTest.java
//        changed by: christian.fritz
//       change date: 07.04.14 13:23
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link ShutdownThread}
 *
 * @author christian.fritz
 */
@RunWith(MockitoJUnitRunner.class)
public class ShutdownThreadTest {

    private ShutdownThread thread = ShutdownThread.getInstance();
    @Mock
    private Server server;

    @After
    public void tearDown() throws Exception {
        List<Server> servers = getHiddenServerList();
        for (int i = servers.size() - 1; i > 0; i--) {
            servers.remove(i);
        }
        if (servers.size() == 1) {
            ShutdownThread.unregister(servers.get(0));
        }
    }

    @Test
    public void testUnRegister() throws Exception {
        assertThat(getHiddenServerList(), hasSize(0));
        assertThat(getHiddenHooked(), is(false));
        ShutdownThread.register(server);
        assertThat(getHiddenServerList(), hasSize(1));
        assertThat(getHiddenHooked(), is(true));
        ShutdownThread.unregister(server);
        assertThat(getHiddenServerList(), hasSize(0));
        assertThat(getHiddenHooked(), is(false));
    }

    @Test
    public void testRun() throws Exception {
        ShutdownThread.register(server);
        assertThat(getHiddenServerList(), contains(server));
        thread.run();
        verify(server).close();
    }

    private List<Server> getHiddenServerList() throws IllegalAccessException, NoSuchFieldException {
        Field serversField = ShutdownThread.class.getDeclaredField("servers");
        serversField.setAccessible(true);
        return (List<Server>) serversField.get(thread);
    }

    private boolean getHiddenHooked() throws IllegalAccessException, NoSuchFieldException {
        Field serversField = ShutdownThread.class.getDeclaredField("hooked");
        serversField.setAccessible(true);
        return (boolean) serversField.get(thread);
    }
}
