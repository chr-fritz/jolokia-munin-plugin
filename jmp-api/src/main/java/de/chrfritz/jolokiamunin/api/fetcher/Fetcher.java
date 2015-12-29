// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: Fetcher
//              File: Fetcher.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:39
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api.fetcher;

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
