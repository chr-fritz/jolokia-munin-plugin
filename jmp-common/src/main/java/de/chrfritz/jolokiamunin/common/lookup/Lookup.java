package de.chrfritz.jolokiamunin.common.lookup;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The general service lookup for the platform.
 * <p>
 * It can work with the osgi registry or with google guice if this module is not loaded with osgi.
 * <p>
 * This class is thread-safe.
 *
 * @author christian.fritz
 */
public final class Lookup {
    private static final AtomicReference<LookupStrategy> lookupStrategy = new AtomicReference<>();

    private Lookup() {
    }

    /**
     * Initialize {@link Lookup} with the given {@link LookupStrategy}.
     *
     * @param lookupStrategy Use this strategy to lookup for instances.
     */
    public static void init(LookupStrategy lookupStrategy) {
        Lookup.lookupStrategy.set(lookupStrategy);
    }

    /**
     * Lookup a class from the registry.
     * <p>
     * The returned service is that service that have the highest service ranking.
     *
     * @param clazz The class to search.
     * @param <T>   The type of the class to search.
     * @return A instance of the requested class or null if not found.
     */
    public static <T> T lookup(Class<T> clazz) {
        return getLookupStrategy().lookup(clazz);
    }

    /**
     * Lookup all services for one class from the registry.
     * <p>
     * The list of services is ordered by the service ranking. The service with the highest ranking is the first.
     *
     * @param clazz The class to search.
     * @param <T>   The type of the class to search.
     * @return A list with all found service instances for the searched class.
     */
    public static <T> List<T> lookupAll(Class<T> clazz) {
        return getLookupStrategy().lookupAll(clazz);
    }

    /**
     * Get the current {@link LookupStrategy}.
     *
     * @return The current lookup strategy.
     */
    public static LookupStrategy getLookupStrategy() {
        return lookupStrategy.get();
    }
}
