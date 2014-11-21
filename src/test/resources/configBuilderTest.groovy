import de.chrfritz.jolokiamunin.config.groovy.ConfigurationBuilder

// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: de.chrfritz.jolokiamunin.testconfig.groovy
//              File: de.chrfritz.jolokiamunin.testconfig.groovy
//        changed by: christian.fritz
//       change date: 14.11.14 22:58
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

/**
 *
 * @author christian.fritz
 */
new ConfigurationBuilder().defaultConfig {
    bindAddress "127.0.0.1"
    port 4849
    singleFetchAllowed true
    bannerHostname "localhost.localdomain"

    ServletContainer(sourceUrl: "http://localhost:8080/jolokia") {
        heapMemUsage(title: "HeapMemoryUsage", vlabel: "bytes") {
            info 'Heap Memory Usage of JVM'
            args '--bytes 1024'
            hostname 'localhost.localdomain'
            mbean 'java.lang:type=Memory'
            attribute 'HeapMemoryUsage'
            max(label: 'Maximum') {
                type 'GAUGE'
                draw 'LINE3'
                path 'max'
            }
            commited(label: 'Commited') {
                type 'GAUGE'
                draw 'AREA'
                path 'commited'
            }
            init(label: 'Initial') {
                type 'GAUGE'
                draw 'LINE1'
                path 'init'
            }
            used(label: 'USED') {
                type 'GAUGE'
                draw 'AREA'
                path 'used'
            }
        }
        nonHeapMemUsage {
            title 'NonHeapMemoryUsage'
            vlabel 'bytes'
            info 'Non Heap Memory Usage of JVM'
            args '--bytes 1024'
            hostname 'localhost.localdomain'
            mbean 'java.lang:type=Memory'
            attribute 'NonHeapMemoryUsage'
            max(label: 'Maximum') {
                type 'GAUGE'
                draw 'LINE3'
                path 'max'
            }
        }
    }
}
