package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.NullArgumentException;

import java.util.ArrayList;
import java.util.Arrays;

// TODO tests for the OrNull versions
public class FirstOrNullTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceNoPred(){
        Enumerable.firstOrNull(null);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSourceWithPred(){
        Enumerable.firstOrNull(null, x -> x.equals(3));
    }

    @Test(expected = NullArgumentException.class)
    public void nullPred(){
        Enumerable.firstOrNull(new ArrayList<>(3), null);
    }

    @Test
    public void emptySeqNoPred(){
        Assert.assertEquals(null, Enumerable.firstOrNull(new ArrayList<>()));
    }

    @Test
    public void singleElementNoPred(){
        Assert.assertEquals(5, (int)Enumerable.firstOrNull(Arrays.asList(5)));
    }

    @Test
    public void multiElementNoPred(){
        Assert.assertEquals(5, (int)Enumerable.firstOrNull(Arrays.asList(5, 10)));
    }

    @Test
    public void emptyWithPred(){
        Assert.assertEquals(null, Enumerable.firstOrNull(new ArrayList<>(), x -> x.equals(3)));
    }

    @Test
    public void singleEleSeqMatch(){
        Assert.assertEquals(5, (int)Enumerable.firstOrNull(Arrays.asList(5), x->x > 3));
    }

    @Test
    public void singleEleSeqNoMatch(){
        Assert.assertEquals(null, Enumerable.firstOrNull(Arrays.asList(5), x -> x > 6));
    }

    @Test
    public void multiEleSeqNoMatch(){
        Assert.assertEquals(null, Enumerable.firstOrNull(Arrays.asList(1, 2, 2, 1), x->x>3));
    }

    @Test
    public void multiEleWithSingleMatch(){
        Assert.assertEquals(5, (int)Enumerable.firstOrNull(Arrays.asList(1, 2, 5, 2, 1), x->x > 3));
    }

    @Test
    public void multiEleMultiMatch(){
        Assert.assertEquals(5, (int)Enumerable.firstOrNull(Arrays.asList(1, 2, 5, 10, 2, 1), x->x > 3));
    }

    @Test
    public void earlyOutAfterFirstEleNoPred(){
        Iterable<Integer> a = Enumerable.project(Arrays.asList(11, 1, 0, 3), x -> 10/x);
        // We finish before getting as far as dividing by 0
        Assert.assertEquals(0, (int)Enumerable.firstOrNull(a));
    }

    @Test
    public void earlyOutAfterFirstEle(){
        Iterable<Integer> a = Enumerable.project(Arrays.asList(11, 1, 0, 3), x -> 10/x);
        // We finish before getting as far as dividing by 0
        Assert.assertEquals(10, (int)Enumerable.firstOrNull(a, y -> y > 5));
    }
    
}
