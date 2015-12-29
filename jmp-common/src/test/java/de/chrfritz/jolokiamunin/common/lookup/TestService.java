// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-common
//             Class: TestService
//              File: TestService.java
//        changed by: christian.fritz
//       change date: 29.12.15 16:23
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.common.lookup;

/**
 * Test service for unit testing of {@link de.chrfritz.jolokiamunin.common.lookup.impl.ServiceLoaderLookupStrategy}
 *
 * @author christian.fritz
 */
public interface TestService {

    /**
     * Dummy method that returns a test string.
     *
     * @return A test string.
     */
    String sayHello();
}
