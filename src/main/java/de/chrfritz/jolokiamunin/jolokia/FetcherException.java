// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: FetcherException
//              File: FetcherException.java
//        changed by: christian
//       change date: 20.02.13 21:59
//       description: Fetcher Exception
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.jolokia;

/**
 * The FetcherException will be thrown in cases of no or some values can't be fetched.
 */
public class FetcherException extends Exception {

    public FetcherException() {
    }

    public FetcherException(String s) {
        super(s);
    }

    public FetcherException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FetcherException(Throwable throwable) {
        super(throwable);
    }
}
