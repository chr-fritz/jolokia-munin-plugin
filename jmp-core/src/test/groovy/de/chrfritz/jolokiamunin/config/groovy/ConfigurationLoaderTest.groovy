// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigurationLoaderTest.groovy
//              File: ConfigurationLoaderTest.groovy
//        changed by: christian.fritz
//       change date: 22.11.14 14:42
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.groovy

import spock.lang.Specification

/**
 *
 * @author christian.fritz
 */
class ConfigurationLoaderTest extends Specification {
    def "LoadConfig"() {
        given:
        def configFile = new File(getClass().getResource("/de/chrfritz/jolokiamunin/config/testConfig.groovy").toURI())
        when:
        def configuration = new ConfigurationLoader().loadConfig(configFile)
        then:
        assert configuration.bannerHostname == 'localhost.localdomain'
        assert configuration.bindAddress == '127.0.0.1'
        assert configuration.port == 4849
        assert configuration.singleFetchAllowed
        assert configuration.name == 'defaultConfig'
        assert configuration.configuration.size() == 1

        def category = configuration.configuration.get(0)
        assert category.name == 'ServletContainer'
        assert category.sourceUrl == 'http://localhost:8080/jolokia'
        assert category.graphs*.name == ['heapMemUsage', 'nonHeapMemUsage']
        assert category.graphs*.title == ['HeapMemoryUsage', 'NonHeapMemoryUsage']
        assert category.graphs*.info == ['Heap Memory Usage of JVM', 'Non Heap Memory Usage of JVM']
        assert category.graphs*.hostname == ['localhost.localdomain', 'localhost.localdomain']
        assert category.graphs*.mbean == ['java.lang:type=Memory', 'java.lang:type=Memory']
        assert category.graphs*.attribute == ['HeapMemoryUsage', 'NonHeapMemoryUsage']
        assert category.graphs*.vlabel == ['bytes', 'bytes']

        assert category.graphs*.fields*.name == [['max', 'commited', 'init', 'used'], ['max']]
        assert category.graphs*.fields*.path == [['max', 'commited', 'init', 'used'], ['max']]
    }
}
