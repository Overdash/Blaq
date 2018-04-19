package playground.test;

import blaq.core.ArgumentOutOfRangeException;
import blaq.core.Enumerable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class ElementAtOrNullTest {
    @Test
    public void negativeIndex(){
        Assert.assertNull(Enumerable.elementAtOrNull(Arrays.asList(0, 1, 2), -1));
    }

    @Test(expected = ArgumentOutOfRangeException.class)
    public void overshootIndexCollection(){
        Iterable<Integer> src = new ArrayDeque<>(Arrays.asList(90, 91, 92));
        Enumerable.elementAtOrNull(src, 3);
    }

    @Test
    public void validIndexOnLazySequence(){
        Iterable<Integer> src = new ArrayList<>(Arrays.asList(100, 56, 93, 22));
        Assert.assertEquals(93, (int)Enumerable.elementAtOrNull(src, 2));
    }
}
