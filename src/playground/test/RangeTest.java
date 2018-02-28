package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.ArgumentOutOfRangeException;
import blaq.core.Enumerable;

import java.util.Arrays;
import java.util.Collections;

public class RangeTest {

    @Test(expected = ArgumentOutOfRangeException.class)
    public void negativeCount(){
        Enumerable.range(10, -1);
    }

    @Test(expected = ArgumentOutOfRangeException.class)
    public void countTooLarge1(){
        Enumerable.range(Integer.MAX_VALUE, 2);
    }

    @Test(expected = ArgumentOutOfRangeException.class)
    public void countTooLarge2(){
        Enumerable.range(2, Integer.MAX_VALUE);
    }

    @Test(expected = ArgumentOutOfRangeException.class)
    public void countTooLarge3(){
        Enumerable.range(Integer.MAX_VALUE/2, (Integer.MAX_VALUE/2) + 3);
    }

    @Test
    public void largeBoundariesCount(){
        Enumerable.range(Integer.MAX_VALUE, 1);
        Enumerable.range(1, Integer.MAX_VALUE);
        Enumerable.range(Integer.MAX_VALUE / 2, (Integer.MAX_VALUE / 2) + 2);
    }

    @Test
    public void validRange(){
        Assert.assertEquals(Enumerable.toList(Enumerable.range(5, 3)), Arrays.asList(5, 6, 7));
    }

    @Test
    public void negativeStart(){
        Assert.assertEquals(Enumerable.toList(Enumerable.range(-2, 5)), Arrays.asList(-2, -1, 0, 1, 2));
    }

    @Test
    public void emptyRange(){
        Assert.assertEquals(Enumerable.toList(Enumerable.range(100, 0)), Collections.emptyList());
    }

    @Test
    public void singleValueOfMaxInt(){
        Assert.assertEquals(Enumerable.toList(Enumerable.range(Integer.MAX_VALUE, 1)), Collections.singletonList(Integer.MAX_VALUE));
    }

    @Test
    public void emptyRangeStartingAtMinInt(){
        Assert.assertEquals(Enumerable.toList(Enumerable.range(Integer.MIN_VALUE, 0)), Collections.emptyList());
    }
}
