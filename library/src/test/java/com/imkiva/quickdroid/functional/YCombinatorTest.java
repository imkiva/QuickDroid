package com.imkiva.quickdroid.functional;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author kiva
 */
public class YCombinatorTest {
    private int commonFact(int n) {
        return n == 1 ? 1 : n * commonFact(n - 1);
    }

    private int commonFib(int n) {
        return n <= 2 ? 1 : commonFib(n - 1) + commonFib(n - 2);
    }

    @Test
    public void testYc() {
        QFunction<Integer, Integer> fact = Combinators.Y(f -> n ->
                n == 1 ? 1 : n * f.apply(n - 1));
        Assert.assertEquals(commonFact(10), fact.apply(10).intValue());

        QFunction<Integer, Integer> fib = Combinators.Y(f -> n ->
                n <= 2 ? 1 : f.apply(n - 1) + f.apply(n - 2));
        Assert.assertEquals(commonFib(10), fib.apply(10).intValue());
    }
}
