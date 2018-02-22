package com.imkiva.quickdroid.util;

/**
 * A singleton implementation may use lazy initialization,
 * where the instance is created when the static method is first invoked.
 * If the static method might be called from multiple threads simultaneously,
 * measures may need to be taken to prevent race conditions
 * that could result in the creation of multiple instances of the class.
 * This is a thread-safe implementation,
 * using lazy initialization with double-checked locking.
 *
 * @author kiva
 */

public abstract class LazySingleton<T> implements Singleton<T> {
    /**
     * The instance, not initialized until {@code get()} is called.
     */
    private T instance;

    /**
     * Factory method that returns an instance of {@code T}.
     * This method will only be called once.
     *
     * @return The instance
     */
    protected abstract T createInstance();

    /**
     * {@inheritDoc}
     */
    @Override
    public final T get() {
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    instance = createInstance();
                }
            }
        }
        return instance;
    }
}
