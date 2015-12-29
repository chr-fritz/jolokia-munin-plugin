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

import de.chrfritz.jolokiamunin.api.MuninProvider;
import de.chrfritz.jolokiamunin.api.config.Category;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
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
    private ConfigurationLoader configLoader;

    @Mock
    private Configuration configuration;

    private App application;

    @Before
    public void setUp() throws Exception {
        application = new App(provider, configLoader);
        when(configLoader.loadConfig(any())).thenReturn(configuration);
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
    public void testHelp() throws Exception {
        String actual = application.run(new String[]{"help"});
        assertTrue(actual.contains("Usage: jolokia [command]\nAvailable Commands:"));
    }

    @Test
    public void testVersion() throws Exception {
        String actual = application.run(new String[]{"version"});
        assertTrue(actual.contains("Jolokia-Munin Plugin by Christian Fritz"));
    }

    @Test
    public void testGetConfiguration() throws Exception {

        Configuration actual = application.getConfiguration();
        assertSame(configuration, actual);
    }

    @Test
    public void testGetConfigurationWithConfig() throws Exception {

        String configPath = "configfile.xml";
        System.setProperty("configFile", configPath);
        Configuration actual = application.getConfiguration();
        verify(configLoader).loadConfig(new File(configPath));
        assertSame(configuration, actual);
    }
}
