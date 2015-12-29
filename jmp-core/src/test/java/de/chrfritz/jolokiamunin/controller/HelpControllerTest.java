// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: HelpControllerTest
//              File: HelpControllerTest.java
//        changed by: christian
//       change date: 01.04.14 17:49
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.api.Controller;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * @author christian.fritz
 */
@RunWith(MockitoJUnitRunner.class)
public class HelpControllerTest {
    @Mock
    private Dispatcher dispatcher;
    private AbstractController controller = new HelpController();

    @Before
    public void setUp() throws Exception {
        controller.setDispatcher(dispatcher);
        List<Controller> controllers = new ArrayList<>();
        controllers.add(controller);
        when(dispatcher.getControllers()).thenReturn(controllers);
        when(dispatcher.handleRequest("version")).thenReturn("Version");
    }

    @Test
    public void testHandle() throws Exception {
        assertThat(controller.execute(""),
                   is                             (equalTo                    ("Usage: jolokia [command]\n" +
                                      "Available Commands:\n" +
                                      "           help : Print this help message\n" +
                                      "------------------------------------------------------------\n" +
                                      "Version\n"))
                  );
    }

    @Test
    public void testGetHelpMessage() throws Exception {
        assertThat(controller.getHelpMessage(), is(equalTo("Print this help message")));
    }
}
