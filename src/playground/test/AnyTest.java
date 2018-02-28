package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.NullArgumentException;

import java.util.ArrayList;
import java.util.Arrays;

public class AnyTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceWithoutPred(){
        Enumerable.any(null);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSourceWithPred(){
        Enumerable.any(null, x -> x.equals(1));
    }

    @Test(expected = NullArgumentException.class)
    public void nullPred(){
        Enumerable.any(Arrays.asList(2, 3, 4), null);
    }

    @Test
    public void emptySeqWithoutPred(){
        Assert.assertFalse(Enumerable.any(new ArrayList<>()));
    }

    @Test
    public void emptySeqWithPred(){
        Assert.assertFalse(Enumerable.any(new ArrayList<Integer>(), x -> x > 3));
    }

    @Test
    public void nonEmptySeq(){
        Assert.assertTrue(Enumerable.any(Arrays.asList(3)));
    }

    @Test
    public void nonEmptySeqWithPredWithMatch(){
        Assert.assertTrue(Enumerable.any(Arrays.asList(3, 6, 9), x -> x > 2));
    }

    @Test
    public void nonEmptySeqWithPredNoMatch(){
        Assert.assertFalse(Enumerable.any(Arrays.asList(3, 4), x -> x < 2));
    }

    @Test
    public void sequenceIsNotEvaluatedAfterFirstMatch(){
        Iterable<Integer> a = Enumerable.project(Arrays.asList(10, 2, 0, 3), x -> 10/x);
        Assert.assertTrue(Enumerable.any(a));
    }
}
