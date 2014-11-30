// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ListControllerTest
//              File: ListControllerTest.java
//        changed by: christian
//       change date: 31.12.13 17:04
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

/**
 * List controller tests
 */
@RunWith(MockitoJUnitRunner.class)
public class ListControllerTest {
    @Mock
    private MuninProvider provider;

    @Mock
    private Configuration configuration;

    private Controller controller = new ListController();

    @Before
    public void setUp() throws Exception {
        controller.setMuninProvider(provider);
        controller.setConfiguration(configuration);
    }

    @Test
    public void testHandleListSingle() throws Exception {
        when(configuration.isSingleFetchAllowed()).thenReturn(true);
        List<String> graphs = new ArrayList<>();
        graphs.add("graph1");
        graphs.add("graph2");
        when(provider.getGraphNames(anyListOf(Category.class))).thenReturn(graphs);
        String expected = "graph1 graph2";
        String actual = controller.execute("");
        assertEquals(expected, actual);
    }

    @Test
    public void testHandleListNoSingle() throws Exception {
        when(configuration.isSingleFetchAllowed()).thenReturn(false);
        assertEquals("jolokia", controller.execute(""));
    }
}
