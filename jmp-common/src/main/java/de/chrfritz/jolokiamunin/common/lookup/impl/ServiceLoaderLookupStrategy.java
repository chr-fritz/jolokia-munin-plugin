package de.chrfritz.jolokiamunin.common.lookup.impl;

import com.google.common.collect.ArrayListMultimap;
import de.chrfritz.jolokiamunin.common.lookup.LookupStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Use the javas {@link java.util.ServiceLoader} to lookup the actual instances.
 *
 * @author christian.fritz
 */
public class ServiceLoaderLookupStrategy implements LookupStrategy {

    private ArrayListMultimap<Class, Object> lookupCache = ArrayListMultimap.create();

    /**
     * Initializes the internal structure to be able to lookup services.
     *
     * @param o the 2d-Array with the classes mapped to services
     */
    @SuppressWarnings("unchecked")
    public void init(Object[][] o) {
        for (Object[] objects : o) {
            Class clazz = (Class) objects[0];
            Object value = objects[1];
            init(clazz, value);
        }
    }

    /**
     * Add a new instance to the lookup. Existing instances are not overridden.
     *
     * @param clazz    The target class.
     * @param instance The added instance.
     * @param <T>      Type of the target instance.
     */
    public <T> void init(Class<T> clazz, T instance) {
        init(clazz, instance, false);
    }

    /**
     * Add a new instance to the lookup. If override is true, all existing instances are removed previous.
     *
     * @param clazz    The target class.
     * @param instance The added instance.
     * @param override True if existing instances should be removed previous.
     * @param <T>      Type of the target instance.
     */
    public <T> void init(Class<T> clazz, T instance, boolean override) {
        if (override) {
            lookupCache.removeAll(clazz);
        }
        lookupCache.put(clazz, instance);
    }

    /**
     * Add a new producer for the given class. Existing instances are not overridden.
     *
     * @param clazz    The target class.
     * @param producer The producer instance.
     * @param <T>      Type of the target instance.
     */
    public <T> void init(Class<T> clazz, Producer<T> producer) {
        init(clazz, producer, false);
    }

    /**
     * Add a new producer for the given class. If override is true, all existing instances are removed previous.
     *
     * @param clazz    The target class.
     * @param producer The producer instance.
     * @param override True if existing instances should be removed previous.
     * @param <T>      Type of the target instance.
     */
    public <T> void init(Class<T> clazz, Producer<T> producer, boolean override) {
        if (override) {
            lookupCache.removeAll(clazz);
        }
        lookupCache.put(clazz, producer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lookup(Class<T> clazz) {
        if (!lookupCache.containsKey(clazz)) {
            loadInstances(clazz);
        }
        List<T> objects = (List<T>) lookupCache.get(clazz);
        if (objects.size() <= 0) {
            return null;
        }
        if (objects.get(0) instanceof Producer) {
            return ((Producer<T>) objects.get(0)).getInstance();
        }
        return objects.get(0);

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> lookupAll(Class<T> clazz) {
        if (!lookupCache.containsKey(clazz)) {
            loadInstances(clazz);
        }
        List<T> instances = new ArrayList<>();
        for (Object object : lookupCache.get(clazz)) {
            T instance;
            if (object instanceof Producer) {
                instance = ((Producer<T>) object).getInstance();
            }
            else {
                instance = (T) object;
            }
            instances.add(instance);
        }
        return instances;
    }

    /**
     * Load the instances with the jdk's {@link java.util.ServiceLoader}.
     *
     * @param clazz The target class.
     * @param <T>   The type of the target class.
     */
    private <T> void loadInstances(Class<T> clazz) {
        ServiceLoader<T> load = ServiceLoader.load(clazz);
        for (T instance : load) {
            lookupCache.put(clazz, instance);
        }
    }

    /**
     * Producer to create the instance created by the lookup strategy.
     * <p>
     * This producers can only be used with the {@link ServiceLoaderLookupStrategy}.
     *
     * @param <T> The type of the created instance.
     */
    public interface Producer<T> {

        /**
         * Produces the instance.
         *
         * @return The created instance.
         */
        T getInstance();
    }
}
