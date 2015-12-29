// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: JolokiaFetcherFactoryTest
//              File: JolokiaFetcherFactoryTest.java
//        changed by: christian
//       change date: 26.03.13 12:21
//       description: Test for the jolokia fetcher factory
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.jolokia.impl;

import de.chrfritz.jolokiamunin.api.fetcher.Fetcher;
import de.chrfritz.jolokiamunin.api.fetcher.FetcherFactory;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertTrue;

/**
 * The jolokia fetcher factory test.
 */
public class JolokiaFetcherFactoryTest {

    private FetcherFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new JolokiaFetcherFactory();
    }

    @Test
    public void testGetInstanceUrl() throws Exception {
        Fetcher fetcher = factory.getInstance(new URL("http://localhost:8080/"));
        assertTrue(fetcher instanceof JolokiaFetcher);
    }

    @Test
    public void testGetInstanceString() throws Exception {
        Fetcher fetcher = factory.getInstance("http://localhost:8080/");
        assertTrue(fetcher instanceof JolokiaFetcher);
    }
}
