// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: XMLConfiguration
//              File: XMLConfiguration.java
//        changed by: christian
//       change date: 19.02.13 19:12
//       description: Load a configuration form XML
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.impl;

import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.config.ConfigurationException;
import de.chrfritz.jolokiamunin.config.xml.CategoryType;
import de.chrfritz.jolokiamunin.config.xml.Config;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The xml configuration handler. It helps you to load a configuration form a xml file.
 */
public class XMLConfiguration implements Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLConfiguration.class);
    private URL configFile;

    private List<Category> categories;

    private String address;
    private int port;
    private boolean singleFetchAllowed;

    /**
     * Load configuration form the given url.
     *
     * @param configFile The url of the configuration file.
     */
    public XMLConfiguration(URL configFile) {
        this.configFile = configFile;
    }

    /**
     * Load the configuration.
     */
    @Override
    public void load() throws ConfigurationException {

        try {
            JAXBContext jc = JAXBContext.newInstance(Config.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Config config = (Config) unmarshaller.unmarshal(configFile);
            if (config.getDaemon() != null) {
                address = config.getDaemon().getAddress();
                port = config.getDaemon().getPort();
                singleFetchAllowed = config.getDaemon().isAllowSingleFetch();
            }
            Mapper mapper = getMapper();
            categories = new ArrayList<Category>();

            for (CategoryType category : config.getCategory()) {
                categories.add(mapper.map(category, Category.class));
            }
        }
        catch (JAXBException e) {
            LOGGER.error("Can not load configuration", e);
            throw new ConfigurationException(e);
        }
    }

    /**
     * Get the bean mapper to map the internal xml structure to external dto.
     *
     * @return The bean mapper
     */
    protected Mapper getMapper() {

        InputStream mapping = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("de/chrfritz/jolokiamunin/config/xml/mapping-config.xml");
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(mapping);

        return mapper;
    }

    /**
     * Get the loaded configuration.
     *
     * @return The loaded configuration
     */
    @Override
    public List<Category> getConfiguration() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * Get the ip address for that interface where the daemon should be bind.
     * <p/>
     * Default is 0.0.0.0 (also known as all interfaces)
     *
     * @return The bind ip address.
     */
    @Override
    public String getBindAddress() {
        return address;
    }

    /**
     * Get the port to listen for incomming connections when run in daemon mode.
     * <p/>
     * Default is 4949.
     *
     * @return The listen port for daemon mode.
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * Is it allowed to fetch a single graph when the daemon mode is used?
     *
     * @return Is the single fetch mode allowed.
     */
    @Override
    public boolean isSingleFetchAllowed() {
        return singleFetchAllowed;
    }
}
