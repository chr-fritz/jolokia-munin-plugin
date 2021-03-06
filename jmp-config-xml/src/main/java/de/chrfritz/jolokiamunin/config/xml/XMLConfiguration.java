// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-config-xml
//             Class: XMLConfiguration
//              File: XMLConfiguration.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:58
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.xml;

import de.chrfritz.jolokiamunin.api.config.Category;
import de.chrfritz.jolokiamunin.api.config.Configuration;
import de.chrfritz.jolokiamunin.api.config.ConfigurationException;
import de.chrfritz.jolokiamunin.api.config.FileConfigurationLoader;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * The xml configuration handler. It helps you to load a configuration form a xml file.
 */
public class XMLConfiguration implements FileConfigurationLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLConfiguration.class);

    /**
     * Load a configuration from a specific file.
     *
     * @param configFile Load the configuration from this file.
     * @return A configuration instance.
     * @throws ConfigurationException In case of the configuration can not loaded.
     */
    @Override
    public Configuration loadConfig(File configFile) throws ConfigurationException {

        try {
            JAXBContext jc = JAXBContext.newInstance(Config.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            LOGGER.info("Attempt to load configuration from file {} as xml", configFile);
            Config config = (Config) unmarshaller.unmarshal(configFile);
            Configuration loadedConfig = new Configuration();

            if (config.getDaemon() != null) {
                loadedConfig.setBindAddress(config.getDaemon().getAddress());
                loadedConfig.setPort(config.getDaemon().getPort());
                loadedConfig.setSingleFetchAllowed(config.getDaemon().isAllowSingleFetch());
                loadedConfig.setBannerHostname(config.getDaemon().getBannerHostname());
            }
            Mapper mapper = getMapper();

            for (CategoryType category : config.getCategory()) {
                loadedConfig.getConfiguration().add(mapper.map(category, Category.class));
            }
            LOGGER.debug("Finished loading configuration");
            return loadedConfig;
        }
        catch (JAXBException e) {
            LOGGER.error("Can not load configuration", e);
            throw new ConfigurationException(e);
        }
    }

    /**
     * Get a list of all file extensions which can be read by the implementing configuration loader.
     *
     * @return A list of all readable file extensions.
     */
    @Override
    public List<String> getAssignedFileExtensions() {
        return singletonList("xml");
    }

    /**
     * Get the bean mapper to map the internal xml structure to external dto.
     *
     * @return The bean mapper
     */
    protected Mapper getMapper() throws ConfigurationException {

        DozerBeanMapper mapper;
        try (InputStream mapping = getClass().getResourceAsStream("mapping-config.xml")) {
            mapper = new DozerBeanMapper();
            mapper.addMapping(mapping);
            return mapper;
        }
        catch (IOException e) {
            throw new ConfigurationException("Unable to read mapping.", e);
        }
    }
}
