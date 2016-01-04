// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: ConfigurationWatchService
//              File: ConfigurationWatchService.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:46
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.daemon;

import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import de.chrfritz.jolokiamunin.common.lookup.LookupStrategy;
import de.chrfritz.jolokiamunin.common.lookup.impl.ServiceLoaderLookupStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

/**
 * This is a watch service for the current configuration file. It is notified when the configuration file is changed and
 * reload it automatically.
 *
 * @author christian.fritz
 */
public class ConfigurationWatchService implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationWatchService.class);
    private final AtomicReference<Configuration> config = new AtomicReference<>();
    private ConfigurationLoader configurationLoader = Lookup.lookup(ConfigurationLoader.class);
    private WatchService watchService;
    private Path configFilePath;

    /**
     * Initialize a new configuration watch service.
     *
     * @throws IOException In case of the first configuration loading failed.
     */
    public ConfigurationWatchService() throws IOException {
        init();
    }

    private void init() throws IOException {
        String configName = System.getProperty("configFile", System.getenv("JOLOKIAMUNIN_CONFIG"));
        if (StringUtils.isBlank(configName)) {
            configName = System.getenv("PWD") + "jolokiamunin.groovy";
        }
        configFilePath = Paths.get(configName).toAbsolutePath();
        Path configPath = configFilePath.getParent();
        FileSystem fileSystem = requireNonNull(configPath.getFileSystem(), "FileSystem of configpath must not be null");
        watchService = fileSystem.newWatchService();
        configPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        loadConfiguration();
        initLookup();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            while (!Thread.interrupted()) {
                WatchKey watchKey = watchService.take();
                WatchEvent<Path> watchEvent = (WatchEvent<Path>) watchKey.pollEvents().get(0);
                if (Objects.equals(watchEvent.kind(), StandardWatchEventKinds.ENTRY_MODIFY)
                        && Objects.equals(configFilePath.getFileName(), watchEvent.context())) {

                    LOGGER.warn("Configuration changed");
                    loadConfiguration();
                }
                watchKey.reset();
            }
        }
        catch (InterruptedException ignored) {
            LOGGER.debug("Watchservice interrupted");
        }
    }

    private void loadConfiguration() {
        try {
            config.set(configurationLoader.loadConfig(configFilePath.toFile()));
            LOGGER.info("Configuration successfully loaded");
        }
        catch (ConfigurationException e) {
            LOGGER.error("Can not reaload configuration", e);
        }
    }

    private void initLookup() {
        LookupStrategy strategy = Lookup.getLookupStrategy();
        if (strategy instanceof ServiceLoaderLookupStrategy) {
            ((ServiceLoaderLookupStrategy) strategy).init(Configuration.class, config::get, true);
        }
    }
}
