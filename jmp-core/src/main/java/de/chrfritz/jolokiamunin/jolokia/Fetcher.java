// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Fetcher
//              File: Fetcher.java
//        changed by: christian
//       change date: 24.02.13 00:46
//       description: The fetcher interface
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.jolokia;

import java.util.List;
import java.util.Map;

/**
 * A handler thats fetches a list of values from an external source. This might be for
 * example a jolokia agent or other jmx sources.
 */
public interface Fetcher {

    /**
     * Fetch the values for the given requests. If a request is not specific all sub
     * attributes are be fetched and returned.
     * e.g. a request with only set mbean=java.lang:type=Memory will return 9 values
     *
     * @param requests The requested values.
     * @return A map that maps the specific request to a value.
     * @throws FetcherException
     */
    Map<Request, Number> fetchValues(List<Request> requests) throws FetcherException;
}
