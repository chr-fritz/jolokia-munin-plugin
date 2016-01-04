// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FileEndingConfigurationLoader
//              File: FileEndingConfigurationLoader.java
//        changed by: christian.fritz
//       change date: 29.11.14 16:47
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config;

import com.google.common.collect.Lists;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import de.chrfritz.jolokiamunin.api.config.FileConfigurationLoader;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load a configuration using a configuration loader based by the file ending of the given config file.
 * <p>
 * It uses the {@link java.util.ServiceLoader} to find the available configuration loader. If you want to implement your
 * own configuration loader just implement the {@link FileConfigurationLoader} interface and
 * register it through the {@code /META-INF/services/de.chrfritz.jolokiamunin.config.ConfigurationLoader} in your own jar
 * within the classpath.
 *
 * @author christian.fritz
 * @see java.util.ServiceLoader
 */
public class FileEndingConfigurationLoader implements ConfigurationLoader, FileConfigurationLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileEndingConfigurationLoader.class);
    private Map<String, FileConfigurationLoader> configurationLoaderMap = new HashMap<>();

    /**
     * Initialize the configuraton loader.
     */
    public FileEndingConfigurationLoader() {
        List<FileConfigurationLoader> serviceLoader = Lookup.lookupAll(FileConfigurationLoader.class);
        for (FileConfigurationLoader configurationLoader : serviceLoader) {
            configurationLoader.getAssignedFileExtensions()
                    .stream()
                    .filter(fileEnding -> !configurationLoaderMap.containsKey(fileEnding))
                    .forEach(fileEnding -> configurationLoaderMap.put(fileEnding, configurationLoader));
        }
    }

    /**
     * Load a configuration from a specific file.
     *
     * @param configFile Load the configuration from this file.
     * @return A configuration instance.
     * @throws ConfigurationException In case of the configuration can not loaded.
     */
    @Override
    public Configuration loadConfig(File configFile) throws ConfigurationException {
        String fileName = configFile.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (!configurationLoaderMap.containsKey(extension)) {
            throw new ConfigurationException("Can not load configuration file '" + configFile +
                            "'. There is no matching configuration loader available.\n" +
                    "The file extensions can be loaded:\n" + StringUtils.join(getAssignedFileExtensions(), "\n  - ")
            );
        }
        FileConfigurationLoader configurationLoader = configurationLoaderMap.get(extension);
        LOGGER.debug("Start loading config file {} using {}", configFile, configurationLoader.getClass().getSimpleName());
        return configurationLoader.loadConfig(configFile);
    }

    /**
     * Get a list of all file extensions which can be read by the implementing configuration loader.
     *
     * @return A list of all readable file extensions.
     */
    @Override
    public List<String> getAssignedFileExtensions() {
        return Lists.newArrayList(configurationLoaderMap.keySet());
    }

    /**
     * Load a configuration.
     *
     * @return A configuration instance.
     * @throws ConfigurationException In case of the configuration can not loaded.
     */
    @Override
    public Configuration loadConfig() throws ConfigurationException {
    }

    /**
     * Get the loaded configuration uri.
     *
     * @return the loaded configuration uri.
     */
    @Override
    public URI getLoadedUri() {
    }
}
