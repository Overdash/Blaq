package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;

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

    /*@Test
    public void usingBlaqIterable(){ // Also test order
        BlaqIterable<Integer> src = new BlaqList<>
                (Arrays.asList(1, 2, 3, 4, 5, 6, 1, 89, 112, 14, 41, 75, 89));
//        src.where(x -> x > 3 ).project()
//        Enumerable.project(Enumerable.where(...,...))
        Assert.assertEquals(Arrays.asList(1, 2, 3, 1, 89, 112, 41, 75, 89), src.where(x -> x < 4 || x > 14).toList());

    }*/
}
