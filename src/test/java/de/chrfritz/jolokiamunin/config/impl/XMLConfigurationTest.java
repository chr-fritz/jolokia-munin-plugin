// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: XMLConfigurationTest
//              File: XMLConfigurationTest.java
//        changed by: christian
//       change date: 18.02.13 19:46
//       description: Test for xml configuration
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.impl;

import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the xml configuration system.
 */
public class XMLConfigurationTest {

    private Configuration config;

    @Before
    public void setUp() throws Exception {
        URL configFile = Thread.currentThread()
                .getContextClassLoader()
                .getResource("de/chrfritz/jolokiamunin/config/impl/xmltest.xml");

        config = new XMLConfiguration(configFile);
    }

    @After
    public void tearDown() throws Exception {
        config = null;
    }

    @Test
    public void testLoad() throws Exception {
        config.load();
        List<Category> categories = config.getConfiguration();

        assertEquals(1, categories.size());
        Category category = categories.get(0);
        assertEquals(2, category.getGraphs().size());
        assertEquals("ServletContainer", category.getName());
        assertEquals("http://localhost:8080/jolokia", category.getSourceUrl());

        Graph graph = category.getGraphs().get(0);
        assertEquals("HeapMemoryUsage", graph.getTitle());
        assertEquals("bytes", graph.getVlabel());
        assertEquals("java.lang:type=Memory", graph.getMbean());
        assertEquals(4, graph.getFields().size());
    }
}
