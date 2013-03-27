// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: AppTest
//              File: AppTest.java
//        changed by: christian
//       change date: 27.03.13 10:35
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin;

import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.ConfigurationFactory;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
