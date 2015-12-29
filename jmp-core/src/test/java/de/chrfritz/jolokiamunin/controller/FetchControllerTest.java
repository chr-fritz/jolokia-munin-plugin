// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FetchControllerTest
//              File: FetchControllerTest.java
//        changed by: christian
//       change date: 31.12.13 16:55
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.api.Controller;
import de.chrfritz.jolokiamunin.api.MuninProvider;
import de.chrfritz.jolokiamunin.api.config.Category;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

/**
 * fetch controller tests
 */
@RunWith(MockitoJUnitRunner.class)
public class FetchControllerTest {
    @Mock
    private MuninProvider provider;

    @Mock
    private Configuration configuration;

    private Controller controller = new FetchController();

    @Before
    public void setUp() throws Exception {
        controller.setMuninProvider(provider);
        controller.setConfiguration(configuration);
    }

    @Test
    public void testHandle() throws Exception {
        String expected = "expected values";
        when(provider.getValues(anyListOf(Category.class))).thenReturn(expected);
        assertEquals(expected, controller.execute(""));
    }
}
