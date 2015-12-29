// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigurationLoader.groovy
//              File: ConfigurationLoader.groovy
//        changed by: christian.fritz
//       change date: 15.11.14 23:27
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.groovy

import de.chrfritz.jolokiamunin.api.config.Configuration

/**
 * Load a groovy configuration.
 *
 * @author christian.fritz
 */
class ConfigurationLoader implements de.chrfritz.jolokiamunin.api.config.ConfigurationLoader {

    private ConfigurationBuilder builder = new ConfigurationBuilder()

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration loadConfig(File configFile) {
        Script s = (Script) new GroovyClassLoader().parseClass(configFile).newInstance()
        s.binding = new ConfigurationBuilderBinding(builder: builder)
        return (Configuration) s.run()
    }

    /**
     * Get a list of all file extensions which can be read by the implementing configuration loader.
     *
     * @return A list of all readable file extensions.
     */
    @Override
    List<String> getAssignedFileExtensions() {
        return ['groovy']
    }
}
