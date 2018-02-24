package com.imkiva.quickdroid.util;

import android.support.annotation.Nullable;

import com.imkiva.quickdroid.functional.QBiConsumer;
import com.imkiva.quickdroid.functional.QConsumer;

import java.util.Collection;
import java.util.Map;

/**
 * @author kiva
 */

public final class QCollections {
    private QCollections() {
        throw new RuntimeException();
    }

    public static <K, V> void forEach(@Nullable Map<K, V> map, @Nullable QBiConsumer<K, V> action) {
        if (map != null && action != null) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                try {
                    K k = entry.getKey();
                    V v = entry.getValue();
                    action.accept(k, v);
                } catch (IllegalStateException ignore) {
                }
            }
        }
    }

    public static <T> void forEach(@Nullable T[] elements, @Nullable QConsumer<T> action) {
        if (elements != null && action != null) {
            for (T element : elements) {
                action.accept(element);
            }
        }
    }

    public static <T> void forEach(@Nullable Collection<T> collection, @Nullable QConsumer<T> action) {
        if (collection != null && action != null) {
            for (T t : collection) {
                action.accept(t);
            }
        }
    }
}
