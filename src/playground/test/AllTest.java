package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.NullArgumentException;

import java.util.ArrayList;
import java.util.Arrays;

public class AllTest {

    @Test(expected = NullArgumentException.class)
    public void nullSource(){
        Enumerable.all(null, x -> x.equals(1));
    }

    @Test(expected = NullArgumentException.class)
    public void nullPred(){
        Enumerable.all(Arrays.asList(2, 3, 4), null);
    }

    @Test
    public void emptySeqReturnTrue(){
        Assert.assertTrue(Enumerable.all(new ArrayList<Integer>(), x -> x > 0));
    }

    @Test
    public void predMatchAll(){
        Assert.assertTrue(Enumerable.all(Arrays.asList(3, 6, 9), x -> x > 2));
    }

    @Test
    public void predNoMatch(){
        Assert.assertFalse(Enumerable.all(Arrays.asList(3, 4), x -> x < 2));
    }

    @Test
    public void predMatchSome(){
        Assert.assertFalse(Enumerable.all(Arrays.asList(3, 4), x -> x > 3));
    }

    @Test
    public void sequenceIsNotEvaluatedAfterFirstMatch(){
        Iterable<Integer> a = Enumerable.project(Arrays.asList(2, 10, 0, 3), x -> 10/x);
        Assert.assertFalse(Enumerable.all(a, y -> y > 2));
    }
}
