package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.NullArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SelectTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Enumerable.project(source, x -> x + 5);
    }

    @Test(expected = NullArgumentException.class)
    public void nullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Enumerable.project(source, (Function<Integer, Integer>) null);
    }

    @Test(expected = NullArgumentException.class)
    public void withIndexNullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Enumerable.project(source, (x, index) -> x + index);
    }

    @Test(expected = NullArgumentException.class)
    public void withIndexNullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Enumerable.project(source, (BiFunction<Integer, Integer,Integer>) null);
    }

    @Test
    public void simpleProjection(){
        Iterable<Integer> src = Arrays.asList(1,5,2);
        Iterable<Integer> projectedList = Enumerable.project(src, x->x*2);
        Assert.assertEquals(Enumerable.toList(projectedList), Arrays.asList(2,10,4));
    }

    @Test
    public void simpleProjectionToDifferentType(){
        Iterable<Integer> src = Arrays.asList(1,5,2);
        Iterable<String> projectedList = Enumerable.project(src, Object::toString);
        Assert.assertEquals(Enumerable.toList(projectedList), Arrays.asList("1","5","2"));
    }

    @Test
    public void emptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.project(src, x -> x*4);
        Assert.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

    @Test
    public void withIndexEmptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.project(src, (x, index) -> x*4);
        Assert.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

//    @Test // This test may not be possible because of the way Java handles lambda expressions (values must be new or final)
//    void sideEffectsInProjection(){
//        Iterable<Integer> src = new ArrayList<>(3);
//        int size = 0;
//        Iterable<Integer> filteredList = Enumerable.project(src, x -> size++);
//
//    }
}
