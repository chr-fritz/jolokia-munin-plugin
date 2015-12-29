// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FileEndingConfigurationLoaderTest
//              File: FileEndingConfigurationLoaderTest.java
//        changed by: christian.fritz
//       change date: 29.11.14 17:25
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config;

import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Unit test for {@link de.chrfritz.jolokiamunin.config.FileEndingConfigurationLoader}
 *
 * @author christian.fritz
 */
public class FileEndingConfigurationLoaderTest {
    private ConfigurationLoader loader;

    @Before
    public void setUp() throws Exception {
        loader = new FileEndingConfigurationLoader();
    }

    @Test(expected = ConfigurationException.class)
    public void testLoadConfigInvalidExtension() throws Exception {
        loader.loadConfig(new File("invalid.extension"));
    }

    @Test
    public void testLoadConfig() throws Exception {
        File configFile = new File(
                getClass().getResource("/de/chrfritz/jolokiamunin/config/testConfig.groovy").toURI());
        Configuration actual = loader.loadConfig(configFile);
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void testGetAssignedFileExtensions() throws Exception {
        List<String> actual = loader.getAssignedFileExtensions();
        assertThat(actual, contains("groovy", "xml"));
    }
}
