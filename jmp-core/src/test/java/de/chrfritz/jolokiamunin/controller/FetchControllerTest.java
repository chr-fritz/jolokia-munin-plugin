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
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import de.chrfritz.jolokiamunin.common.lookup.LookupStrategy;
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
    private LookupStrategy strategy;
    @Mock
    private MuninProvider provider;

    @Mock
    private Configuration configuration;

    private Controller controller = new FetchController();

    @Before
    public void setUp() throws Exception {
        Lookup.init(strategy);
        when(strategy.lookup(Configuration.class)).thenReturn(configuration);
        when(strategy.lookup(MuninProvider.class)).thenReturn(provider);
    }

    @Test
    public void testHandle() throws Exception {
        String expected = "expected values";
        when(provider.getValues(anyListOf(Category.class))).thenReturn(expected);
        assertEquals(expected, controller.execute(""));
    }
}
