package com.imkiva.quickdroid.util;

import com.imkiva.quickdroid.functional.QSupplier;

/**
 * The Implementation of Singleton Design Pattern.
 * Do not use this class directly,
 * use {@link ImmediateSingleton} or {@link LazySingleton} instead.
 *
 * @author kiva
 * @see ImmediateSingleton
 * @see LazySingleton
 */

public interface Singleton<T> extends QSupplier<T> {
    /**
     * Get the instance.
     * {@inheritDoc}
     *
     * @return The instance
     */
    @Override
    T get();
}
