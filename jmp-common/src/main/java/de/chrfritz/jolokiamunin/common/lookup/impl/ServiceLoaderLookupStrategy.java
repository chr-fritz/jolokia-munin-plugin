package de.chrfritz.jolokiamunin.common.lookup.impl;

import com.google.common.collect.ArrayListMultimap;
import de.chrfritz.jolokiamunin.common.lookup.LookupStrategy;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Use the javas {@link java.util.ServiceLoader} to lookup the actual instances.
 *
 * @author christian.fritz
 */
public class ServiceLoaderLookupStrategy implements LookupStrategy {

    private ArrayListMultimap<Class, Supplier> lookupCache = ArrayListMultimap.create();

    /**
     * Initializes the internal structure to be able to lookup services.
     *
     * @param o the 2d-Array with the classes mapped to services
     */
    @SuppressWarnings("unchecked")
    public void initInstance(Object[][] o) {
        for (Object[] objects : o) {
            Class clazz = (Class) objects[0];
            Object value = objects[1];
            if (value instanceof Supplier) {
                init(clazz, (Supplier) value);
            }
            else {
                initInstance(clazz, value);
            }
        }
    }

    /**
     * Add a new instance to the lookup. Existing instances are not overridden.
     *
     * @param clazz    The target class.
     * @param instance The added instance.
     * @param <T>      Type of the target instance.
     */
    public <T> void initInstance(Class<T> clazz, T instance) {
        initInstance(clazz, instance, false);
    }

    /**
     * Add a new instance to the lookup. If override is true, all existing instances are removed previous.
     *
     * @param clazz    The target class.
     * @param instance The added instance.
     * @param override True if existing instances should be removed previous.
     * @param <T>      Type of the target instance.
     */
    public <T> void initInstance(Class<T> clazz, T instance, boolean override) {
        init(clazz, () -> instance, override);
    }

    /**
     * Add a new producer for the given class. Existing instances are not overridden.
     *
     * @param clazz    The target class.
     * @param producer The producer instance.
     * @param <T>      Type of the target instance.
     */
    public <T> void init(Class<T> clazz, Supplier<T> producer) {
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
    public <T> void init(Class<T> clazz, Supplier<T> producer, boolean override) {
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
        List<Supplier> objects = lookupCache.get(clazz);
        if (objects.size() <= 0) {
            return null;
        }
        return (T) objects.get(0).get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> lookupAll(Class<T> clazz) {
        if (!lookupCache.containsKey(clazz)) {
            loadInstances(clazz);
        }
        return lookupCache.get(clazz)
                .stream()
                .map(object -> (T) object.get())
                .collect(Collectors.toList());
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
            Supplier<T> producer = () -> instance;
            lookupCache.put(clazz, producer);
            lookupCache.put(instance.getClass(), producer);
        }
    }
}
