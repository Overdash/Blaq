package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.ArgumentOutOfRangeException;
import blaq.core.Enumerable;

import java.util.Arrays;
import java.util.Collections;

public class RepeatTest {

    @Test
    public void simpleRepeat(){
        Assert.assertEquals(Arrays.asList("blaq", "blaq", "blaq"), Enumerable.toList(Enumerable.repeat("blaq", 3)));
    }

    @Test
    public void emptyRepeat(){
        Assert.assertEquals(Collections.emptyList(), Enumerable.toList(Enumerable.repeat("blaq", 0)));
    }

    /**
     * FAIL: In Yield, the optional calls "requireNonNull" which means you cannot repeat nulls
     * If the sequence was not to be buffered/the implementation of Yield were to change (omit using {@code Optional<T>})
     * then this would pass.
     */
    @Test
    public void nullRepeat(){
        Assert.assertEquals(Arrays.asList(null, null), Enumerable.toList(Enumerable.repeat(null, 2)));
    }

    @Test(expected = ArgumentOutOfRangeException.class)
    public void negativeCount(){
        Enumerable.repeat("blaq", -1);
    }
}
