// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: MuninProvider
//              File: MuninProvider.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:38
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.api;


import de.chrfritz.jolokiamunin.api.config.Category;
import de.chrfritz.jolokiamunin.api.fetcher.FetcherException;

import java.util.List;

/**
 * A handler that thats get a configuration and produces either the configuration for munin or fetches the values and
 * generate the munin value string.
 */
public interface MuninProvider {

    /**
     * Get a list with all graph names which are contained in the given categories.
     *
     * @param categories The category list.
     * @return A list with all graph names
     */
    List<String> getGraphNames(List<Category> categories);

    /**
     * Get a list with all names of the graphs contained in the given {@param category}.
     *
     * @param category The requested category.
     * @return A list with all graph names contained in {@param category}.
     */
    List<String> getGraphNames(Category category);

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
     */
    String getValues(List<Category> categories);

    /**
     * Fetch all values within a single category and generates the munin value string.
     *
     * @param category The category
     * @return The fetched values as munin compatible string.
     * @throws FetcherException In case of all errors when try to fetching the values.
     */
    String getValues(Category category) throws FetcherException;
}
