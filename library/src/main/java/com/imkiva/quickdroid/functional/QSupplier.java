package com.imkiva.quickdroid.functional;

/**
 * Represents a supplier of results.
 * <p>
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 * @author kiva
 */
@FunctionalInterface
public interface QSupplier<T> {
    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
