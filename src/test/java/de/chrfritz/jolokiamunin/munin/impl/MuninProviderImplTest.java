// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: MuninProviderImplTest
//              File: MuninProviderImplTest.java
//        changed by: christian
//       change date: 24.02.13 18:56
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.munin.impl;

import com.google.common.io.Files;
import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.ConfigurationException;
import de.chrfritz.jolokiamunin.config.impl.XMLConfiguration;
import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.FetcherFactory;
import de.chrfritz.jolokiamunin.jolokia.Request;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MuninProviderImplTest {

    @Mock
    private Fetcher fetcher;

    @Mock
    private FetcherFactory factory;

    private MuninProvider provider;


    @Before
    public void setUp() throws Exception {
        when(factory.getInstance((URL) any())).thenReturn(fetcher);
        when(factory.getInstance(anyString())).thenReturn(fetcher);

        provider = new MuninProviderImpl(factory);
    }

    @After
    public void tearDown() throws Exception {
        provider = null;
    }

    @Test
    public void testGetConfig() throws Exception {

        Category category = loadConfig("de/chrfritz/jolokiamunin/munin/singleCategory.xml").getConfiguration().get(0);
        String expected = loadFromClasspath("de/chrfritz/jolokiamunin/munin/singleCategoryConfig.txt");
        String actual = provider.getConfig(category);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetConfigWithoutPath() throws Exception {

        Category category = loadConfig("de/chrfritz/jolokiamunin/munin/withoutPath.xml").getConfiguration().get(0);
        String expected = loadFromClasspath("de/chrfritz/jolokiamunin/munin/withoutPathConfig.txt");
        String actual = provider.getConfig(category);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetConfigsList() throws Exception {
        List<Category> categories = loadConfig("de/chrfritz/jolokiamunin/munin/multiCategory.xml").getConfiguration();
        String expected = loadFromClasspath("de/chrfritz/jolokiamunin/munin/multiCategoryConfig.txt");
        String actual = provider.getConfig(categories);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValues() throws Exception {

        Map<Request, Number> fetcherResult = new HashMap<>();
        when(fetcher.fetchValues(anyListOf(Request.class))).thenReturn(fetcherResult);
        Category category = loadConfig("de/chrfritz/jolokiamunin/munin/singleCategory.xml").getConfiguration().get(0);

        fetcherResult.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "used"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "committed"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "init"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "max"), 1000);

        String actual = provider.getValues(category);

        String expected = loadFromClasspath("de/chrfritz/jolokiamunin/munin/singleCategoryValues.txt");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetValuesList() throws Exception {

        Map<Request, Number> fetcherResult = new HashMap<>();
        when(fetcher.fetchValues(anyListOf(Request.class))).thenReturn(fetcherResult);
        List<Category> categories = loadConfig("de/chrfritz/jolokiamunin/munin/multiCategory.xml").getConfiguration();

        fetcherResult.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "max"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"), 1000);
        fetcherResult.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "init"), 1000);

        String actual = provider.getValues(categories);

        String expected = loadFromClasspath("de/chrfritz/jolokiamunin/munin/multiCategoryValues.txt");

        assertEquals(expected, actual);
    }

    private static Configuration loadConfig(String configFile) throws ConfigurationException {
        URL configUrl = Thread.currentThread()
                .getContextClassLoader()
                .getResource(configFile);

        Configuration config = new XMLConfiguration(configUrl);
        config.load();
        return config;
    }

    private String loadFromClasspath(String filename) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        return Files.toString(new File(url.toURI()), Charset.forName("UTF-8"));
    }
}
