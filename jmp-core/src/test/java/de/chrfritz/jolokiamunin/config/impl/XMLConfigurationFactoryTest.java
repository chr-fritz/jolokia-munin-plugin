// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: XMLConfigurationFactoryTest
//              File: XMLConfigurationFactoryTest.java
//        changed by: christian
//       change date: 27.03.13 10:31
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.config.impl;

import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.ConfigurationFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class XMLConfigurationFactoryTest {

    private ConfigurationFactory factory;
    private URL configFile;

    @Before
    public void setUp() throws Exception {
        factory = new XMLConfigurationFactory();
        configFile = Thread.currentThread()
                .getContextClassLoader()
                .getResource("de/chrfritz/jolokiamunin/config/impl/xmltest.xml");
    }

    @Test
    public void testGetInstanceFile() throws Exception {
        Configuration fetcher = factory.getInstance(new File(configFile.getFile()));
        assertThat(fetcher, is(notNullValue()));
    }

    @Test
    public void testGetInstanceString() throws Exception {
        Configuration fetcher = factory.getInstance(configFile.getFile());
        assertThat(fetcher, is(notNullValue()));
    }
}
