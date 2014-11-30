// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FetcherFactory
//              File: FetcherFactory.java
//        changed by: christian
//       change date: 02.03.13 13:48
//       description: Fetcher Factory interface
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.jolokia;

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
