package com.imkiva.quickdroid.functional;

import com.imkiva.quickdroid.util.QObjects;

/**
 * @author kiva
 */

public interface QBiConsumer<T, U> {
    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u);

    /**
     * Returns a composed {@code QBiConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code QBiConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default QBiConsumer<T, U> andThen(QBiConsumer<? super T, ? super U> after) {
        QObjects.requireNonNull(after);
        return (T t, U u) -> {
            accept(t, u);
            after.accept(t, u);
        };
    }
}
