package playground.test;

import blaq.core.Enumerable;
import blaq.core.InvalidOperationException;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AggregateTest {

    @Test(expected = NullArgumentException.class)
    public void nullSrcUnseeded(){
        List<Integer> list = null;
        Enumerable.aggregate(list, (x, y) -> x + y);
    }

    @Test(expected = NullArgumentException.class)
    public void nullFuncUnseeded(){
        List<Integer> list = Arrays.asList(1 ,3);
        Enumerable.aggregate(list, null);
    }

    @Test
    public void unseededAggregation(){
        List<Integer> list = Arrays.asList(1 ,4, 5);
        Assert.assertEquals(17, (int)Enumerable.aggregate(list, (current, val) -> current * 2 + val));
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcSeeded(){
        List<Integer> list = null;
        Enumerable.aggregate(list, 3, (x,y) -> x + y);
    }

    @Test(expected = NullArgumentException.class)
    public void nullFuncSeeded(){
        List<Integer> list = Arrays.asList(1 ,3);
        Enumerable.aggregate(list, 5, null);
    }

    @Test
    public void seededAggregation(){
        List<Integer> list = Arrays.asList(1, 4, 5);
        int seed = 5;
        BiFunction<Integer, Integer, Integer> func = (current, val) -> current * 2 + val;
        // First iteration: 5 * 2 + 1 = 11
        // Second iteration: 11 * 2 + 4 = 26
        // Third iteration: 26 * 2 + 5 = 57
        Assert.assertEquals(57, (int)Enumerable.aggregate(list, seed, func));
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcSeededWithSelector(){
        List<Integer> list = null;
        Enumerable.aggregate(list, 3, (x, y) -> x+y, res -> res.toString());
    }

    @Test(expected = NullArgumentException.class)
    public void nullFuncSeededWithSelector(){
        List<Integer> list = Arrays.asList(1, 3);
        Function<Integer, String> func = null;
        Enumerable.aggregate(list, 5, (x, y) -> x+y, func);
    }

    @Test(expected = NullArgumentException.class)
    public void nullProjectionSeededWithSelector(){
        List<Integer> list = Arrays.asList(1, 3);
        Function<Integer, String> selector = null;
        Enumerable.aggregate(list, 5, (x, y) -> x + y, selector);
    }

    @Test
    public void seededWithSelector(){
        List<Integer> list = Arrays.asList(1, 4, 5);
        BiFunction<Integer, Integer, Integer> func = (current, val) -> current * 2 + val;
        Function<Integer, String> selector = res -> res.toString();
        // First iteration: 5 * 2 + 1 = 11
        // Second iteration: 11 * 2 + 4 = 26
        // Third iteration: 26 * 2 + 5 = 57
        // Result projection: 57.toString() = "57"
        Assert.assertEquals("57", Enumerable.aggregate(list, 5, func, selector));
    }

    @Test
    public void differentSrcAndAccumulatorType(){
        int largeVal = 2000000000;
        List<Integer> list = Arrays.asList(largeVal, largeVal, largeVal);
        long sum = Enumerable.aggregate(list, 0L, (acc, val) -> acc + val);
        Assert.assertEquals(6000000000L, sum);
        // check we didn't miss a 0
        Assert.assertTrue(sum > Integer.MAX_VALUE);
    }

    @Test(expected = InvalidOperationException.class)
    public void emptySeqUnseeded(){
        Enumerable.aggregate(Collections.emptyList(), (x, y) -> x.toString());
    }

    @Test
    public void emptySeqSeeded(){
        List<Integer> list = new ArrayList<>();
        Assert.assertEquals(5, (int)Enumerable.aggregate(list, 5, (x, y) -> x + y));
    }

    @Test
    public void emptySeqSeededWithSelector(){
        List<Integer> list = new ArrayList<>();
        Assert.assertEquals("5", Enumerable.aggregate(list, 5, (x, y) -> x + y, x -> x.toString()));
    }

    // Originally I'd thought it was the default value of T which was used as the seed...
    @Test
    public void firstElementOfInputIsUsedAsSeedForUnseededOverload(){
        List<Integer> list = Arrays.asList(5, 3, 2);
        Assert.assertEquals(30, (int)Enumerable.aggregate(list, (acc, val) -> acc * val));
    }
}
