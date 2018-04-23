package playground.test;

import blaq.core.Enumerable;
import blaq.core.InvalidOperationException;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class SingleOrNullTest {

    @Test(expected = NullArgumentException.class)
    public void nullSrcIntNoSelector(){
        Iterable<Integer> src = null;
        Enumerable.singleOrNull(src);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcIntSelector(){
        Iterable<Integer> src = null;
        Enumerable.singleOrNull(src, x -> x > 1);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSelectorInt(){
        Iterable<Integer> src = Arrays.asList(1, 3, 5);
        Enumerable.singleOrNull(src, null);
    }

    @Test
    public void emptySeqNoPred(){
        Iterable<Integer> src = new ArrayList<>();
        Assert.assertNull(Enumerable.singleOrNull(src));
    }

    @Test
    public void singleOrNullElementSeqNoPred(){
        Iterable<Integer> src = Arrays.asList(5);
        Assert.assertEquals(5, (int)Enumerable.singleOrNull(src));
    }

    @Test(expected = InvalidOperationException.class)
    public void multipleEleSeqNoPred(){
        Iterable<Integer> src = Arrays.asList(5, 10);
        Assert.assertNull(Enumerable.singleOrNull(src));
    }

    @Test
    public void emptySeqWithPred(){
        Iterable<Integer> src = new ArrayList<>();
        Assert.assertNull(Enumerable.singleOrNull(src, x-> x > 1));
    }

    @Test
    public void singleOrNullEleSeqWithMatch(){
        Iterable<Integer> src = Arrays.asList(5);
        Assert.assertEquals(5, (int)Enumerable.singleOrNull(src, x -> x > 3));
    }

    @Test
    public void singleOrNullElementSeqNoMatching(){
        Iterable<Integer> src = Arrays.asList(2);
        Assert.assertNull(Enumerable.singleOrNull(src, x -> x > 3));
    }

    @Test(expected = InvalidOperationException.class)
    public void multiEleSeqWithNoPredMatches(){
        Iterable<Integer> src = Arrays.asList(1, 2, 2, 1);
        Enumerable.singleOrNull(src, x -> x > 1);
    }

    @Test
    public void multiEleSeqWithsingleOrNullPredMatch(){
        Iterable<Integer> src = Arrays.asList(1, 2, 5, 2, 1);
        Assert.assertEquals(5, (int)Enumerable.singleOrNull(src, x -> x > 3));
    }

    @Test(expected = InvalidOperationException.class)
    public void multiEleSequenceWithMultiPredMatch(){
        Iterable<Integer> src = Arrays.asList(1, 2, 5, 10, 2, 1);
        Enumerable.singleOrNull(src);
    }

    @Test(expected = InvalidOperationException.class)
    public void earlyOutWithoutPred(){
        Iterable<Integer> src = Arrays.asList(1, 2, 0);
        Iterable<Integer> query = Enumerable.project(src, x -> 10/x);
        Enumerable.singleOrNull(query);
    }

    @Test(expected = InvalidOperationException.class)
    public void earlyOutWithPred(){
        Iterable<Integer> src = Arrays.asList(1, 2, 0);
        Iterable<Integer> query = Enumerable.project(src, x -> 10/x);
        Enumerable.singleOrNull(query, x -> true);
    }
}
