package playground.test;

import blaq.core.Enumerable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SkipTest {
    // Null tests passed

    @Test
    public void zeroCount(){
        Iterable<?> it = Enumerable.skip(Enumerable.range(0, 5), 0);
        Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4),
                Enumerable.toList(it));
    }

    @Test
    public void negativeCount(){
        Iterable<?> it = Enumerable.skip(Enumerable.range(0, 5), -5);
        Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4),
                Enumerable.toList(it));
    }

    @Test
    public void countShorterThanSrc(){
        Iterable<?> it = Enumerable.skip(Enumerable.range(0, 5), 3);
        Assert.assertEquals(Arrays.asList(3, 4),
                Enumerable.toList(it));
    }

    @Test
    public void countEqualToSrcLength(){
        Iterable<?> it = Enumerable.skip(Enumerable.range(0, 5), 5);
        Assert.assertEquals(Arrays.asList(),
                Enumerable.toList(it));
    }

    @Test
    public void countGreaterThanSrcLength(){
        Iterable<?> it = Enumerable.skip(Enumerable.range(0, 5), 100);
        Assert.assertEquals(Arrays.asList(),
                Enumerable.toList(it));
    }
}
