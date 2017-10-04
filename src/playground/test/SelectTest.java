package playground.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import playground.Enumerable;
import playground.NullArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

class SelectTest {

    @Test
    void nullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Assertions.assertThrows(NullArgumentException.class,
                () -> Enumerable.project(source, x -> x + 5));
    }

    @Test
    void nullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Assertions.assertThrows(NullArgumentException.class,
                ()-> Enumerable.project(source, (Function<Integer, Integer>) null));
    }

    @Test
    void withIndexNullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Assertions.assertThrows(NullArgumentException.class,
                () -> Enumerable.project(source, (x, index) -> x + index));
    }

    @Test
    void withIndexNullPredicateThrowsNullArgumentException(){
        Iterable<Integer> source = Arrays.asList(1,3,7,9,10);
        Assertions.assertThrows(NullArgumentException.class,
                ()-> Enumerable.project(source, (BiFunction<Integer, Integer,Integer>) null));
    }

    @Test
    void simpleProjection(){
        Iterable<Integer> src = Arrays.asList(1,5,2);
        Iterable<Integer> projectedList = Enumerable.project(src, x->x*2);
        Assertions.assertEquals(Enumerable.toList(projectedList), Arrays.asList(2,5,4));
    }

    @Test
    void simpleProjectionToDifferentType(){
        Iterable<Integer> src = Arrays.asList(1,5,2);
        Iterable<String> projectedList = Enumerable.project(src, Object::toString);
        Assertions.assertEquals(Enumerable.toList(projectedList), Arrays.asList("2","5","4"));
    }

    @Test
    void emptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.project(src, x -> x*4);
        Assertions.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

    @Test
    void withIndexEmptySource(){
        Iterable<Integer> src = Collections.emptyList();
        Iterable<Integer> filteredList = Enumerable.project(src, (x, index) -> x*4);
        Assertions.assertEquals(Enumerable.toList(filteredList), Collections.emptyList());
    }

//    @Test // This test may not be possible because of the way Java handles lambda expressions (values must be new or final)
//    void sideEffectsInProjection(){
//        Iterable<Integer> src = new ArrayList<>(3);
//        int count = 0;
//        Iterable<Integer> filteredList = Enumerable.project(src, x -> count++);
//
//    }
}
