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

import de.chrfritz.jolokiamunin.api.Dispatcher;
import de.chrfritz.jolokiamunin.api.MuninProvider;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import de.chrfritz.jolokiamunin.common.lookup.impl.ServiceLoaderLookupStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyList;
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
    @Mock
    private Dispatcher dispatcher;
    @Mock
    private ServiceLoaderLookupStrategy strategy;

    private App application;

    @Before
    public void setUp() throws Exception {
        application = new App(strategy);
        when(strategy.lookup(ConfigurationLoader.class)).thenReturn(configLoader);
        when(strategy.lookup(Dispatcher.class)).thenReturn(dispatcher);
        when(configLoader.loadConfig()).thenReturn(configuration);
        when(dispatcher.handleRequest("help")).thenReturn("help");
        when(dispatcher.handleRequest("fetch")).thenReturn("init: 1");
    }

    @Test
    public void testDispatchWithoutArgs() throws Exception {
        String result = application.run(new String[]{});
        verify(dispatcher).init(anyList());
        verify(dispatcher).handleRequest("fetch");
        assertThat(result, is(equalTo("init: 1")));
    }

    @Test
    public void testDispatchWithArgs() throws Exception {

        String result = application.run(new String[]{"help"});
        verify(dispatcher).init(anyList());
        verify(dispatcher).handleRequest("help");
        assertThat(result, is(equalTo("help")));
    }

    @Test
    public void testGetConfigurationWithConfig() throws Exception {
        String configPath = "configfile.xml";
        System.setProperty("configFile", configPath);
        application.loadConfiguration();
        verify(configLoader).loadConfig();
    }
}
