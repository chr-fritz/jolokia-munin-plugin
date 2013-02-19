// ______________________________________________________________________________
//
//           Project:
//              File: $Id$
//      last changed: $Rev$
// ______________________________________________________________________________
//
//        created by: ${USER}
//     creation date: ${DATE}
//        changed by: $Author$
//       change date: $LastChangedDate$
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.jolokia;

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
