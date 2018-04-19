package playground.test;

import blaq.core.Enumerable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MaxTest {

    @Test
    public void simpleSeqNoSelector(){
        Iterable<Integer> src = Arrays.asList(5, 10, 6, 2, 13, 8);
        Assert.assertEquals(13, (int)Enumerable.max(src));
    }
}
