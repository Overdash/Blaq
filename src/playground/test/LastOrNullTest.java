package playground.test;

import blaq.core.Enumerable;
import blaq.core.InvalidOperationException;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class LastOrNullTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceNoPred(){
        Enumerable.lastOrDefault(null);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSourceWithPred(){
        Enumerable.lastOrDefault(null, x -> x.equals(3));
    }

    @Test(expected = NullArgumentException.class)
    public void nullPred(){
        LinkedList<Integer> ll = new LinkedList<>(Arrays.asList(1, 3, 5));
        Enumerable.lastOrDefault(ll, null);
    }

    @Test
    public void emptySeqNoPred(){
        Assert.assertNull(Enumerable.lastOrDefault(new LinkedList<Integer>()));
    }

    @Test
    public void singleEleSeqNoPred(){
        Assert.assertEquals(5, (int)Enumerable.lastOrDefault(new LinkedList<>(Arrays
                .asList(5))));
    }

    @Test
    public void multiEleSeqNoPred(){
        Assert.assertEquals(10, (int)Enumerable.lastOrDefault(new LinkedList<>(
                Arrays.asList(5,10)
        )));
    }

    @Test
    public void emptySeqWithPred(){
        Assert.assertNull(Enumerable.lastOrDefault(new LinkedList<>(), x -> x.equals(3)));
    }

    @Test
    public void singleEleSeqWithMatch(){
        Assert.assertEquals(5, (int)Enumerable.lastOrDefault(new LinkedList<>(
                Arrays.asList(5)
        ), x -> x > 3));
    }

    @Test
    public void singleEleSeqNoMatchPred(){
        Assert.assertNull(Enumerable.lastOrDefault(Arrays.asList(2), x -> x > 3));
    }

    @Test
    public void multiEleSeqNoPredMatch(){
        Assert.assertNull(Enumerable.lastOrDefault(Arrays.asList(1, 2, 2, 1), x -> x > 3));
    }

    @Test
    public void multiEleSeqWithSinglePredMatch(){
        Assert.assertEquals(5, (int)Enumerable.lastOrDefault(
                new LinkedList<>(Arrays.asList(1, 2, 5, 2, 1)), x->x>3
        ));
    }

    @Test
    public void multiEleWithMultiPredMatch(){
        Assert.assertEquals(10, (int)Enumerable.lastOrDefault(
                new LinkedList<>(Arrays.asList(1, 2, 5, 10, 2, 1)), x->x>3
        ));
    }

    @Test
    public void listNoPredDoesntIterate(){
        Assert.assertEquals(3, (int)Enumerable.lastOrDefault(
                new TestExtensions.NonIterableList<>(Arrays.asList(1, 5, 10, 3))
        ));
    }

    @Test(expected = InvalidOperationException.class)
    public void listWithPredStillIterates(){
        Enumerable.lastOrDefault(new TestExtensions.NonIterableList<>(
                Arrays.asList(1, 5, 10, 3)
        ), x -> x > 3);
    }
}
