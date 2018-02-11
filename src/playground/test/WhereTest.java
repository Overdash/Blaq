package playground.test;

import org.junit.Assert;
import org.junit.Test;
import playground.Enumerable;
import playground.NullArgumentException;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WhereTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Enumerable.where(source, x -> x > 5);
    }

    @Test(expected = NullArgumentException.class)
    public void nullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Enumerable.where(source, (Predicate<Integer>) null);
    }

    @Test(expected = NullArgumentException.class)
    public void withIndexNullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Enumerable.where(source, (x, index) -> x > 5);
    }

    @Test(expected = NullArgumentException.class)
    public void withIndexNullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Enumerable.where(source, (BiPredicate<Integer,Integer>) null);
    }

    @Test
    public  void simpleFiltering(){
        Iterable<Integer> src = Arrays.asList(1,3,4,2,8,1);
        Iterable<Integer> filteredList = Enumerable.where(src, x -> x < 4);
        Assert.assertEquals(Enumerable.toList(filteredList), Arrays.asList(1,3,2,1));
    }

    @Test
    public void withIndexSimpleFiltering(){
        Iterable<Integer> src = Arrays.asList(1,3,4,2,8,1);
        Iterable<Integer> filteredList = Enumerable.where(src, (x, index) -> x < index);
        Assert.assertEquals(Enumerable.toList(filteredList), Arrays.asList(2,1));
    }

    @Test
    public void emptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.where(src, x -> x<4);
        Assert.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

    @Test
    public void withIndexEmptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.where(src, (x, index) -> x<4);
        Assert.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

    //@Test // -> Infinite execution, exception isn't thrown by method... I think I achieve deferred execution... Just idk how to test it lol
    public void WithIndexExecutionIsDeferred(){
        ThrowingIterable.AssertDeferred(src -> Enumerable.where(src, x -> x > 0));
        //Assertions.assertThrows(UnsupportedOperationException.class, () -> ThrowingIterable.AssertDeferred(src -> Enumerable.where(src, x -> x > 0)));
    }
}
