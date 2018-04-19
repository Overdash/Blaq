package playground.test;

import blaq.core.Enumerable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AverageTest {

    @Test
    public void simpleAverageIntNoSelector(){
        Iterable<Integer> src = Arrays.asList(5, 10, 0, 15);
        Assert.assertEquals(7.5d, Enumerable.average(src), 0);
    }
}
