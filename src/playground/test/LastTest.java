package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.InvalidOperationException;
import blaq.core.NullArgumentException;

import java.util.Arrays;
import java.util.LinkedList;

public class LastTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceNoPred(){
        Enumerable.last(null);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSourceWithPred(){
        Enumerable.last(null, x -> x.equals(3));
    }

    @Test(expected = NullArgumentException.class)
    public void nullPred(){
        LinkedList<Integer> ll = new LinkedList<>(Arrays.asList(1, 3, 5));
        Enumerable.last(ll, null);
    }

    @Test(expected = InvalidOperationException.class)
    public void emptySeqNoPred(){
        Enumerable.last(new LinkedList<Integer>());
    }

    @Test
    public void singleEleSeqNoPred(){
        Assert.assertEquals(5, (int)Enumerable.last(new LinkedList<>(Arrays
                .asList(5))));
    }

    @Test
    public void multiEleSeqNoPred(){
        Assert.assertEquals(10, (int)Enumerable.last(new LinkedList<>(
                Arrays.asList(5,10)
        )));
    }

    @Test(expected = InvalidOperationException.class)
    public void emptySeqWithPred(){
        Enumerable.last(new LinkedList<>(), x -> x.equals(3));
    }

    @Test
    public void singleEleSeqWithMatch(){
        Assert.assertEquals(5, (int)Enumerable.last(new LinkedList<>(
                Arrays.asList(5)
        ), x -> x > 3));
    }

    @Test(expected = InvalidOperationException.class)
    public void singleEleSeqNoMatchPred(){
        Enumerable.last(Arrays.asList(2), x -> x > 3);
    }

    @Test(expected = InvalidOperationException.class)
    public void multiEleSeqNoPredMatch(){
        Enumerable.last(Arrays.asList(1, 2, 2, 1), x -> x > 3);
    }

    @Test
    public void multiEleSeqWithSinglePredMatch(){
        Assert.assertEquals(5, (int)Enumerable.last(
                new LinkedList<>(Arrays.asList(1, 2, 5, 2, 1)), x->x>3
        ));
    }

    @Test
    public void multiEleWithMultiPredMatch(){
        Assert.assertEquals(10, (int)Enumerable.last(
                new LinkedList<>(Arrays.asList(1, 2, 5, 10, 2, 1)), x->x>3
        ));
    }

    @Test
    public void listNoPredDoesntIterate(){
        Assert.assertEquals(3, (int)Enumerable.last(
                new TestExtensions.NonIterableList<>(Arrays.asList(1, 5, 10, 3))
        ));
    }

    @Test(expected = InvalidOperationException.class)
    public void listWithPredStillIterates(){
        Enumerable.last(new TestExtensions.NonIterableList<>(
                Arrays.asList(1, 5, 10, 3)
        ), x -> x > 3);
    }

}
