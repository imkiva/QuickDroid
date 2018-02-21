package com.imkiva.quickdroid.util;

/**
 * @author kiva
 */

public final class ImmediateSingleton<T> implements Singleton<T> {
    private final T instance;

    public ImmediateSingleton(T instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        return instance;
    }
}
