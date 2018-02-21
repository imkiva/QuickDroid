package com.imkiva.quickdroid.functional;

/**
 * @author kiva
 */

public final class When<T> {
    private Object object;

    private boolean matched = false;
    private T result;
    private QSupplier<T> defaultAction;

    private When(Object object) {
        this.object = object;
    }

    public When<T> meet(Object check, QSupplier<T> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        if (!matched && (object == null || object.equals(check))) {
            result = action.get();
            matched = true;
        }
        return this;
    }

    public When<T> otherwise(QSupplier<T> defaultAction) {
        this.defaultAction = defaultAction;
        return this;
    }

    public T get() {
        if (matched) {
            return result;
        }
        if (defaultAction != null) {
            return defaultAction.get();
        }
        return null;
    }

    public static <T> When<T> when(Object object) {
        return new When<>(object);
    }
}
