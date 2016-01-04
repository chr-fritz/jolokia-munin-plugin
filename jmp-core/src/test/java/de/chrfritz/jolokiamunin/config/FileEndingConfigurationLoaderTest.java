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
import de.chrfritz.jolokiamunin.api.config.FileConfigurationLoader;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import de.chrfritz.jolokiamunin.common.lookup.LookupStrategy;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link de.chrfritz.jolokiamunin.config.FileEndingConfigurationLoader}
 *
 * @author christian.fritz
 */
public class FileEndingConfigurationLoaderTest {
    private FileConfigurationLoader loader;

    @Before
    public void setUp() throws Exception {
        LookupStrategy strategy = mock(LookupStrategy.class);
        List<FileConfigurationLoader> configurationLoaders = Arrays.asList(mockLoader("xml"), mockLoader("groovy"));
        when(strategy.lookupAll(FileConfigurationLoader.class)).thenReturn(configurationLoaders);
        Lookup.init(strategy);

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

    private FileConfigurationLoader mockLoader(String... extensions) throws ConfigurationException {
        FileConfigurationLoader configurationLoader = mock(FileConfigurationLoader.class);
        when(configurationLoader.loadConfig(any())).thenReturn(mock(Configuration.class));
        when(configurationLoader.getAssignedFileExtensions()).thenReturn(Arrays.asList(extensions));
        return configurationLoader;
    }
}
