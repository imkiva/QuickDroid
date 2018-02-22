package com.imkiva.quickdroid.util;

/**
 * The instance is usually stored as a private static variable
 * and created when the variable is initialized.
 *
 * @author kiva
 */

public final class ImmediateSingleton<T> implements Singleton<T> {
    /**
     * The instance, initialized when this object created.
     */
    private final T instance;

    /**
     * Create an instance holder
     *
     * @param instance The instance
     */
    public ImmediateSingleton(T instance) {
        this.instance = instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get() {
        return instance;
    }
}
