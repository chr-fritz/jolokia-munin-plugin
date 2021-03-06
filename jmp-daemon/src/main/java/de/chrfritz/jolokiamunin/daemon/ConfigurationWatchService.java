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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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

    @SuppressWarnings("findbugs:NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    private void init() throws IOException {
        loadConfiguration();
        Path configPath = configFilePath.getParent();
        watchService = configPath.getFileSystem().newWatchService();
        configPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
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
            config.set(configurationLoader.loadConfig());
            if (configFilePath == null) {
                configFilePath = new File(configurationLoader.getLoadedUri()).toPath();
            }
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
