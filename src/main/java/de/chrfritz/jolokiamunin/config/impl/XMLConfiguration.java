// ______________________________________________________________________________
//
//           Project:
//              File: $Id$
//      last changed: $Rev$
// ______________________________________________________________________________
//
//        created by: ${USER}
//     creation date: ${DATE}
//        changed by: $Author$
//       change date: $LastChangedDate$
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.config.impl;

import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.config.Configuration;
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

public class XMLConfiguration implements Configuration {

    private static final Logger logger = LoggerFactory.getLogger(XMLConfiguration.class);
    private URL configFile;

    private List<Category> categories;

    public XMLConfiguration(URL configFile) {
        this.configFile = configFile;
    }

    @Override
    public void load() {

        try {
            JAXBContext jc = JAXBContext.newInstance(Config.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Config config = (Config) unmarshaller.unmarshal(configFile);

            Mapper mapper = getMapper();
            categories = new ArrayList<Category>();

            for (CategoryType category : config.getCategory()) {
                categories.add(mapper.map(category, Category.class));
            }
        }
        catch (JAXBException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected Mapper getMapper() {

        InputStream mapping = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("de/chrfritz/jolokiamunin/config/xml/mapping-config.xml");
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(mapping);

        return mapper;
    }

    @Override
    public List<Category> getConfiguration() {
        return Collections.unmodifiableList(categories);
    }
}
