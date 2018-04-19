package playground.test;

import blaq.core.Enumerable;
import org.junit.Assert;
import org.junit.Test;

public class LongCountTest {

    @Test
    public void predicatedCount(){
        Assert.assertEquals(3, Enumerable.longCount(Enumerable.range(2, 5),x -> x % 2 == 0));
    }
}
