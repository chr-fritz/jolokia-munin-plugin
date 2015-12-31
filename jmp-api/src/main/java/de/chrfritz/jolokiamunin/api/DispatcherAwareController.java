// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: DispatcherAwareController
//              File: DispatcherAwareController.java
//        changed by: christian.fritz
//       change date: 31.12.15 15:56
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api;

/**
 * Controller that shows that it needs the dispatched dispatcher.
 *
 * @author christian.fritz
 */
public interface DispatcherAwareController extends Controller {
    /**
     * The dispatcher that dispatched the current request.
     *
     * @param dispatcher the dispatcher.
     */
    void setDispatcher(Dispatcher dispatcher);
}
