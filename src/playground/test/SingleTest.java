package playground.test;

import blaq.core.Enumerable;
import blaq.core.InvalidOperationException;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class SingleTest {
    @Test(expected = NullArgumentException.class)
    public void nullSrcIntNoSelector(){
        Iterable<Integer> src = null;
        Enumerable.single(src);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcIntSelector(){
        Iterable<Integer> src = null;
        Enumerable.single(src, x -> x > 1);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSelectorInt(){
        Iterable<Integer> src = Arrays.asList(1, 3, 5);
        Enumerable.single(src, null);
    }

    @Test(expected = InvalidOperationException.class)
    public void emptySeqNoPred(){
        Iterable<Integer> src = new ArrayList<>();
        Enumerable.single(src);
    }

    @Test
    public void singleElementSeqNoPred(){
        Iterable<Integer> src = Arrays.asList(5);
        Assert.assertEquals(5, (int)Enumerable.single(src));
    }

    @Test(expected = InvalidOperationException.class)
    public void multipleEleSeqNoPred(){
        Iterable<Integer> src = Arrays.asList(5, 10);
        Enumerable.single(src);
    }

    @Test(expected = InvalidOperationException.class)
    public void emptySeqWithPred(){
        Iterable<Integer> src = new ArrayList<>();
        Enumerable.single(src, x-> x > 1);
    }

    @Test
    public void singleEleSeqWithMatch(){
        Iterable<Integer> src = Arrays.asList(5);
        Assert.assertEquals(5, (int)Enumerable.single(src, x -> x > 3));
    }

    @Test(expected = InvalidOperationException.class)
    public void singleElementSeqNoMatching(){
        Iterable<Integer> src = Arrays.asList(2);
        Assert.assertEquals(5, (int)Enumerable.single(src, x -> x > 3));
    }

    @Test(expected = InvalidOperationException.class)
    public void multiEleSeqWithNoPredMatches(){
        Iterable<Integer> src = Arrays.asList(1, 2, 2, 1);
        Enumerable.single(src, x -> x > 1);
    }

    @Test
    public void multiEleSeqWithSinglePredMatch(){
        Iterable<Integer> src = Arrays.asList(1, 2, 5, 2, 1);
        Assert.assertEquals(5, (int)Enumerable.single(src, x -> x > 3));
    }

    @Test(expected = InvalidOperationException.class)
    public void multiEleSequenceWithMultiPredMatch(){
        Iterable<Integer> src = Arrays.asList(1, 2, 5, 10, 2, 1);
        Enumerable.single(src);
    }

    @Test(expected = InvalidOperationException.class)
    public void earlyOutWithoutPred(){
        Iterable<Integer> src = Arrays.asList(1, 2, 0);
        Iterable<Integer> query = Enumerable.project(src, x -> 10/x);
        Enumerable.single(query);
    }

    @Test(expected = InvalidOperationException.class)
    public void earlyOutWithPred(){
        Iterable<Integer> src = Arrays.asList(1, 2, 0);
        Iterable<Integer> query = Enumerable.project(src, x -> 10/x);
        Enumerable.single(query, x -> true);
    }
}
