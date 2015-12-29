// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-config-xml
//             Class: XMLConfigurationTest
//              File: XMLConfigurationTest.java
//        changed by: christian.fritz
//       change date: 29.12.15 15:00
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.xml;

import de.chrfritz.jolokiamunin.api.config.Category;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.api.config.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the xml configuration system.
 */
public class XMLConfigurationTest {

    private Configuration config;

    @Before
    public void setUp() throws Exception {
        File configFile = new File(getClass().getResource("/de/chrfritz/jolokiamunin/config/xml/xmltest.xml").toURI());

        config = new XMLConfiguration().loadConfig(configFile);
    }

    @After
    public void tearDown() throws Exception {
        config = null;
    }

    @Test
    public void testLoad() throws Exception {
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
        assertEquals("localhost.localdomain", graph.getHostname());
        assertEquals(4, graph.getFields().size());
    }

    @Test(expected = ConfigurationException.class)
    public void testLoadInvalidFile() throws Exception {
        File configFile = new File(
                getClass().getResource("/de/chrfritz/jolokiamunin/config/xml/xmltestInvalid.xml").toURI());

        config = new XMLConfiguration().loadConfig(configFile);
    }
}
