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

import de.chrfritz.jolokiamunin.api.Dispatcher;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.api.config.ConfigurationLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

/**
 * This is a watch service for the current configuration file. It is notified when the configuration file is changed and
 * reload it automatically.
 *
 * @author christian.fritz
 */
public class ConfigurationWatchService implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationWatchService.class);
    private ConfigurationLoader configurationLoader;
    private Dispatcher dispatcher;
    private WatchService watchService;
    private Path configFilePath;

    /**
     * Initialize a new configuration watch service.
     *
     * @param configurationLoader Use this configurationLoader to initialize the configuration.
     * @param dispatcher          The controller dispatcher thats use the new configuration.
     * @throws IOException In case of the first configuration loading failed.
     */
    public ConfigurationWatchService(ConfigurationLoader configurationLoader, Dispatcher dispatcher) throws
            IOException {
        this.configurationLoader = configurationLoader;
        this.dispatcher = dispatcher;
        init();
    }

    private void init() throws IOException {
        String configName = System.getProperty("configFile", System.getenv("JOLOKIAMUNIN_CONFIG"));
        if (StringUtils.isBlank(configName)) {
            configName = System.getenv("PWD") + "jolokiamunin.xml";
        }
        configFilePath = Paths.get(configName).toAbsolutePath();
        Path configPath = configFilePath.getParent();
        watchService = configPath.getFileSystem().newWatchService();
        configPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        loadConfiguration();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                WatchKey watchKey = watchService.take();
                WatchEvent<Path> watchEvent = (WatchEvent<Path>) watchKey.pollEvents().get(0);
                if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY
                        && configFilePath.getFileName().equals(watchEvent.context())) {

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
            Configuration config = configurationLoader.loadConfig(configFilePath.toFile());
            dispatcher.setConfiguration(config);
            LOGGER.info("Configuration successfully loaded");
        }
        catch (ConfigurationException e) {
            LOGGER.error("Can not reaload configuration", e);
        }
    }
}
