// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FetcherFactory
//              File: FetcherFactory.java
//        changed by: christian
//       change date: 02.03.13 13:48
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.jolokia;

import java.net.MalformedURLException;
import java.net.URL;

public interface FetcherFactory {

    Fetcher getInstance(URL url);

    Fetcher getInstance(String url) throws MalformedURLException;
}
