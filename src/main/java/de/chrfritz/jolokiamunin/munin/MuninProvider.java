// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: MuninProvider
//              File: MuninProvider.java
//        changed by: christian
//       change date: 19.02.13 18:32
//       description: Munin Provider Interface
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.munin;

import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.jolokia.FetcherException;

import java.util.List;

public interface MuninProvider {

    String getConfig(List<Category> categories);

    String getConfig(Category category);

    String getValues(List<Category> categories) throws FetcherException;

    String getValues(Category category) throws FetcherException;
}
