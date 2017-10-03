package playground.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import playground.Enumerable;
import playground.NullArgumentException;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

class WhereTest {

    @Test
    void nullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Assertions.assertThrows(NullArgumentException.class,
                () -> Enumerable.where(source, x -> x > 5));
    }

    @Test
    void nullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Assertions.assertThrows(NullArgumentException.class,
                ()-> Enumerable.where(source, (Predicate<Integer>) null));
    }

    @Test
    void withIndexNullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Assertions.assertThrows(NullArgumentException.class,
                () -> Enumerable.where(source, (x, index) -> x > 5));
    }

    @Test
    void withIndexNullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Assertions.assertThrows(NullArgumentException.class,
                ()-> Enumerable.where(source, (BiPredicate<Integer,Integer>) null));
    }

    @Test
    void simpleFiltering(){
        Iterable<Integer> src = Arrays.asList(1,3,4,2,8,1);
        Iterable<Integer> filteredList = Enumerable.where(src, x -> x < 4);
        Assertions.assertEquals(Enumerable.toList(filteredList), Arrays.asList(1,3,2,1));
    }

    @Test
    void withIndexSimpleFiltering(){
        Iterable<Integer> src = Arrays.asList(1,3,4,2,8,1);
        Iterable<Integer> filteredList = Enumerable.where(src, (x, index) -> x < index);
        Assertions.assertEquals(Enumerable.toList(filteredList), Arrays.asList(2,1));
    }

    @Test
    void emptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.where(src, x -> x<4);
        Assertions.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

    @Test
    void withIndexEmptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.where(src, (x, index) -> x<4);
        Assertions.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

//    @Test // -> Fails atm. Java doesn't do deferred execution.
//    void WithIndexExecutionIsDeferred(){
//        ThrowingIterable.AssertDeferred(src -> Enumerable.where(src, x -> x > 0));
//    }
}
