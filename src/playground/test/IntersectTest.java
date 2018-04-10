package playground.test;

import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IntersectTest {

    @Test(expected = NullArgumentException.class)
    public void nullFirstNoComp(){
        List<String> first = null;
        List<String> second = new ArrayList<>();
        Enumerable.intersect(first, second);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSecondNoComp(){
        List<String> second = null;
        List<String> first = new ArrayList<>();
        Enumerable.intersect(first, second);
    }

    @Test
    public void noComparerSpecified(){
        List<String> first = Arrays.asList("A", "a", "b", "c", "b");
        List<String> second = Arrays.asList("b", "a", "d", "a");

        Assert.assertEquals(Arrays.asList("a", "b"),
                Enumerable.toList(Enumerable.intersect(first, second)));
    }

    // Passes but Yield doesn't return the exceptions to this class.
    @Test(expected = ArithmeticException.class)
    public void secondSequenceReadFullyOnFirstResultIteration(){
        Iterable<Integer> first = Arrays.asList(1);
        Iterable<Integer> secondQuery = Enumerable.project(Arrays.asList(10, 2, 0), x -> 10/x);

        Iterable<Integer> query = Enumerable.intersect(first, secondQuery);
        Iterator<Integer> it = query.iterator();
        it.next();
    }

    // Passes but Yield doesn't return the exceptions to this class.
    @Test(expected = ArithmeticException.class)
    public void firstSequenceOnlyReadAsResultsAreRead(){
        Iterable<Integer> firstQuery = Enumerable.project(Arrays.asList(10, 2, 0, 2), x -> 10/x);
        Iterable<Integer> second = Arrays.asList(1);

        Iterable<Integer> query = Enumerable.intersect(firstQuery, second);
        Iterator<Integer> it = query.iterator();

        // Should be able to use the first value with no issues
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(1, (int)it.next());

        // Getting at the *second* value of the result sequence requires
        // reading from the first input sequence until the "bad" division
        it.next();
    }
}
