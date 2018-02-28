package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.InvalidOperationException;
import blaq.core.NullArgumentException;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceNoPred(){
        Enumerable.first(null);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSourceWithPred(){
        Enumerable.first(null, x -> x.equals(3));
    }

    @Test(expected = NullArgumentException.class)
    public void nullPred(){
        Enumerable.first(new ArrayList<>(3), null);
    }

    @Test(expected = InvalidOperationException.class)
    public void emptySeqNoPred(){
        Enumerable.first(new ArrayList<>());
    }

    @Test
    public void singleElementNoPred(){
        Assert.assertEquals(5, (int)Enumerable.first(Arrays.asList(5)));
    }

    @Test
    public void multiElementNoPred(){
        Assert.assertEquals(5, (int)Enumerable.first(Arrays.asList(5, 10)));
    }

    @Test(expected = InvalidOperationException.class)
    public void emptyWithPred(){
        Enumerable.first(new ArrayList<>(), x -> x.equals(3));
    }

    @Test
    public void singleEleSeqMatch(){
        Assert.assertEquals(5, (int)Enumerable.first(Arrays.asList(5), x->x > 3));
    }

    @Test(expected = InvalidOperationException.class)
    public void singleEleSeqNoMatch(){
        Enumerable.first(Arrays.asList(5), x -> x > 6);
    }

    @Test(expected = InvalidOperationException.class)
    public void multiEleSeqNoMatch(){
        Enumerable.first(Arrays.asList(1, 2, 2, 1), x->x>3);
    }

    @Test
    public void multiEleWithSingleMatch(){
        Assert.assertEquals(5, (int)Enumerable.first(Arrays.asList(1, 2, 5, 2, 1), x->x > 3));
    }

    @Test
    public void multiEleMultiMatch(){
        Assert.assertEquals(5, (int)Enumerable.first(Arrays.asList(1, 2, 5, 10, 2, 1), x->x > 3));
    }

    @Test
    public void earlyOutAfterFirstEleNoPred(){
        Iterable<Integer> a = Enumerable.project(Arrays.asList(11, 1, 0, 3), x -> 10/x);
        // We finish before getting as far as dividing by 0
        Assert.assertEquals(0, (int)Enumerable.first(a));
    }

    @Test
    public void earlyOutAfterFirstEle(){
        Iterable<Integer> a = Enumerable.project(Arrays.asList(11, 1, 0, 3), x -> 10/x);
        // We finish before getting as far as dividing by 0
        Assert.assertEquals(10, (int)Enumerable.first(a, y -> y > 5));
    }
}
