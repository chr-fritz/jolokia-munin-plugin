package de.chrfritz.jolokiamunin.common.lookup;

import java.util.List;

/**
 * The {@code LookupStrategy} is used by the {@link Lookup} class to get a concrete instance of
 * of the given {@link java.lang.Class} object. It allows you to use our own registry, service locator or dependency
 * injection container.
 *
 * @author christian.fritz
 */
public interface LookupStrategy {

    /**
     * Lookup an instance from the registry.
     * <p>
     * The returned service is that service that have the highest service ranking.
     *
     * @param clazz The class to search.
     * @param <T>   The type of the class to search.
     * @return A instance of the requested class or null if not found.
     */
    <T> T lookup(Class<T> clazz);

    /**
     * Lookup all services for one class from the registry.
     * <p>
     * The list of services is ordered by the service ranking. The service with the highest ranking is the first.
     *
     * @param clazz The class to search.
     * @param <T>   The type of the class to search.
     * @return A list with all found service instances for the searched class.
     */
    <T> List<T> lookupAll(Class<T> clazz);
}
