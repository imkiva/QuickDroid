package com.imkiva.quickdroid.util;

/**
 * @author kiva
 */

public abstract class LazySingleton<T> implements Singleton<T> {
    private T instance;

    protected abstract T createInstance();

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
