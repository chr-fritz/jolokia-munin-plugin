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

/**
 * A handler that thats get a configuration and produces either the configuration for munin or fetches the values and
 * generate the munin value string.
 */
public interface MuninProvider {

    /**
     * Get the munin compatible configuration for a list of categories.
     *
     * @param categories The list of configurations
     * @return The produced munin configuration.
     */
    String getConfig(List<Category> categories);

    /**
     * Get the munin compatible configuration for a single category.
     *
     * @param category The category.
     * @return The produced munin configuration.
     */
    String getConfig(Category category);

    /**
     * Fetches all the values of a list of categories and generates the munin values string.
     *
     * @param categories The list of categories.
     * @return The fetched values as munin compatible string.
     * @throws FetcherException In case of all errors when try to fetching the values.
     */
    String getValues(List<Category> categories) throws FetcherException;

    /**
     * Fetch all values within a single category and generates the munin value string.
     *
     * @param category The category
     * @return The fetched values as munin compatible string.
     * @throws FetcherException In case of all errors when try to fetching the values.
     */
    String getValues(Category category) throws FetcherException;
}
