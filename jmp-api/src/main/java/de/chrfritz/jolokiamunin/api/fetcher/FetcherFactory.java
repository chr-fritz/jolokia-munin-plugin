// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: FetcherFactory
//              File: FetcherFactory.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:39
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.api.fetcher;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Handles the creation of new fetchers with the given url.
 */
public interface FetcherFactory {

    /**
     * Creates a new fetcher instance form the given url.
     *
     * @param url The url to connect.
     * @return The new fetcher thats use the given url.
     */
    Fetcher getInstance(URL url);

    /**
     * Creates a new fetcher instance form the given url.
     *
     * @param url The url to connect.
     * @return The new fetcher thats use the given url.
     */
    Fetcher getInstance(String url) throws MalformedURLException;
}
