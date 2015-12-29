// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: ConfigurationBuilderTest.groovy
//              File: ConfigurationBuilderTest.groovy
//        changed by: christian.fritz
//       change date: 15.11.14 22:49
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config.groovy

import de.chrfritz.jolokiamunin.api.config.Configuration
import spock.lang.Specification

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

/**
 * Unit test for the {@link de.chrfritz.jolokiamunin.config.groovy.ConfigurationBuilder}
 *
 * @author christian.fritz
 */
class ConfigurationBuilderTest extends Specification {

    private static final ScriptEngine GROOVY_ENGINE = new ScriptEngineManager().getEngineByName("groovy")

    private Configuration loadConfiguration(String configFile) {
        def reader = new InputStreamReader(getClass().getResourceAsStream("/${configFile}.groovy"))

        GROOVY_ENGINE.eval(reader)
    }


    def "test load full config"() {
        when:
        def configuration = loadConfiguration("configBuilderTest")

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
