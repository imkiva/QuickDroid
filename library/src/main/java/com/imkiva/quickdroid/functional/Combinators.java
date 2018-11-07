package com.imkiva.quickdroid.functional;

/**
 * @author kiva
 */
public final class Combinators {
    public static <T, R> QFunction<T, R> Y(QFunction<QFunction<T, R>, QFunction<T, R>> ff) {
        return ff.apply(f -> Y(ff).apply(f));
    }
}
