import de.chrfritz.jolokiamunin.config.GroovyConfiguration

// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: tst.groovy
//              File: tst.groovy
//        changed by: christian.fritz
//       change date: 20.09.14 00:02
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

/**
 *
 * @author christian.fritz
 */

/*ConfigurationBuilder.newInstance().Configuration {
    "ServeletContainer" {
        agent new URI("http://localhost:8080/jolokia")
        graph "heapMemUsage" {
            title "HeapMemoryUsage"
            vlabel "bytes"
            info "Heap Memory Usage of JVM"
            args "--base 1024"
            mbean "java.lang:type=Memory"
            attribute "HeapMemoryUsage"
            field "max" {
                label "Maximum"
                type "GAUGE"
                draw "LINE3"
                path "max"
            }
            field "committed" {
                label "Committed"
                type "GAUGE"
                draw "AREA"
                path "committed"
            }
            field "init" {
                label "Initial"
                type "GAUGE"
                draw "LINE1"
                path "init"
            }
            field "used" {
                label "Used"
                type "GAUGE"
                draw "AREA"
                path "used"
            }
        }
    }
}*/


new GroovyConfiguration() {

    categories {

    }
}
