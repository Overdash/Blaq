package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;

public class CountTest {


    @Test
    public void nonCollectionCount(){
        Assert.assertEquals(5, Enumerable.count(Enumerable.range(2, 5)));
    }

    @Test
    public void collectionCount(){
        Assert.assertEquals(5, new BlaqList<>(Enumerable.range(2, 5)).count());
    }

    @Test(expected = NullArgumentException.class)
    public void nullSourceThrowsNullArgs(){
        Enumerable.count(null);
    }

    @Test(expected = NullArgumentException.class)
    public void predicateNullSourceThrowsNullArgs(){
        Iterable<Integer> c = null;
        Enumerable.count(c, x -> x == 1);
    }

    @Test(expected = NullArgumentException.class)
    public void predicatedNullPredicateThrowsNullArgs(){
        new BlaqList<>(Enumerable.range(0, 1)).count(null);
    }

    @Test
    public void predicateCount(){
        Assert.assertEquals(3, new BlaqList<>(Enumerable.range(2, 5)).count(x -> x % 2 == 0));
    }

    @Test(expected = ArithmeticException.class, timeout = 600)
    public void overflow(){
        BlaqIterable<Integer> b = new BlaqList<>(Enumerable.range(0, Integer.MAX_VALUE))
                .concat(Enumerable.range(0, 1));
        b.count();
    }

    @Test(expected = ArithmeticException.class, timeout = 600) // time out to test rest
    public void overflowWithPredicate(){
        BlaqIterable<Integer> b = new BlaqList<>(Enumerable.range(0, Integer.MAX_VALUE))
                .concat(Enumerable.range(0, 1));
        b.count(x -> x >= 0);
    }

}
