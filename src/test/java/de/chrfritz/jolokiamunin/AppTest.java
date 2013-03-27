// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: AppTest
//              File: AppTest.java
//        changed by: christian
//       change date: 27.03.13 10:35
//       description: Test for the main application class
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin;

import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.ConfigurationFactory;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the main application class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AppTest {

    @Mock
    private MuninProvider provider;

    @Mock
    private ConfigurationFactory configFactory;

    @Mock
    private Configuration configuration;

    private App application;

    @Before
    public void setUp() throws Exception {
        application = new App(provider, configFactory);
        when(configFactory.getInstance(anyString())).thenReturn(configuration);
    }

    @Test
    public void testConfig() throws Exception {
        String configReturn = "Munin Config";
        when(provider.getConfig((List<Category>) anyCollectionOf(Category.class))).thenReturn(configReturn);
        String actual = application.run(new String[]{"config"});
        assertEquals(configReturn, actual);
    }

    @Test
    public void testFetch() throws Exception {
        String valuesReturn = "Munin Values";
        when(provider.getValues((List<Category>) anyCollectionOf(Category.class))).thenReturn(valuesReturn);
        String actual = application.run(new String[]{});
        assertEquals(valuesReturn, actual);

        actual = application.run(new String[]{"fetch"});
        assertEquals(valuesReturn, actual);
    }

    @Test
    @Ignore
    public void testHelp() throws Exception {
        String actual = application.run(new String[]{"Help"});
        assertTrue(actual.contains("Usage:"));
    }

    @Test
    public void testVersion() throws Exception {
        String actual = application.run(new String[]{"version"});
        assertTrue(actual.contains("Jolokia-Munin Plugin by Christian Fritz"));
    }

    @Test
    public void testGetConfiguration() throws Exception {

        Configuration actual = application.getConfiguration();
        verify(configuration).load();
        assertSame(configuration, actual);
    }

    @Test
    public void testGetConfigurationWithConfig() throws Exception {

        String configPath = "configfile.xml";
        System.setProperty("configFile", configPath);
        Configuration actual = application.getConfiguration();
        verify(configFactory).getInstance(configPath);
        verify(configuration).load();
        assertSame(configuration, actual);
    }
}
