// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: JolokiaFetcherFactory
//              File: JolokiaFetcherFactory.java
//        changed by: christian
//       change date: 02.03.13 13:49
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.jolokia.impl;

import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.FetcherFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class JolokiaFetcherFactory implements FetcherFactory {

    @Override
    public Fetcher getInstance(URL url) {
        return new JolokiaFetcher(url);
    }

    @Override
    public Fetcher getInstance(String url) throws MalformedURLException {
        return new JolokiaFetcher(new URL(url));
    }
}
