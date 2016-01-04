// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-daemon
//             Class: ITdaemon.groovy
//              File: ITdaemon.groovy
//        changed by: christian.fritz
//       change date: 04.01.16 18:44
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

itdaemon {
    bindAddress '127.0.0.1'
    port 65049
    singleFetchAllowed true
    bannerHostname 'localhost'

    ServletContainer(sourceUrl: 'http://localhost:8080/jolokia') {
        heapMem(title: "HeapMemoryUsage", vlabel: "bytes") {
            info 'Heap Memory Usage of JVM'
            args '--base 1024'
            mbean 'java.lang:type=Memory'
            attribute 'HeapMemoryUsage'
            max(label: 'Maximum') {
                type 'GAUGE'
                draw 'LINE3'
                path 'max'
            }
        }
    }
    Tomcat(sourceUrl: 'http://localhost:8080/jolokia') {
        noneHeapMem(title: 'NonHeapMemoryUsage', vlabel: 'bytes') {
            info 'Non Heap Memory Usage of JVM'
            args '--base 1024'
            hostname 'localhost'
            mbean 'java.lang:type=Memory'
            attribute 'NonHeapMemoryUsage'
            max(label: 'Maximum') {
                type 'GAUGE'
                draw 'LINE3'
                path 'max'
            }
            committed(label: 'Committed') {
                type 'GAUGE'
                draw 'AREA'
                path 'committed'
            }
            init(label: 'Inital') {
                type 'GAUGE'
                draw 'LINE1'
                path 'init'
            }
            used(label: 'Used') {
                type 'GAUGE'
                draw 'AREA'
                path 'used'
            }
        }
    }
}
