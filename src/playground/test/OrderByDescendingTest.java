package playground.test;

import blaq.core.Enumerable;
import blaq.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class OrderByDescendingTest {

    @Test
    public void orderingIsStable(){
        Iterable<Pair<Integer, Integer>> src = Arrays.asList(new Pair<>(10, 1), new Pair<>(11, 2), new Pair<>(11, 3), new Pair<>(10, 4));
        Iterable<Pair<Integer, Integer>> sorted = Enumerable.orderByDescending(src, x -> x.getKey());
        Assert.assertEquals(Arrays.asList(2, 3, 1, 4), Enumerable.toList(Enumerable.project(sorted, x -> x.getValue())));
    }

    @Test
    public void extremeTesting(){
        Iterable<Integer> vals = Arrays.asList(1, 3, 2, 4, 8, 5, 7, 6);
        Iterable<Integer> query = Enumerable.orderByDescending(vals, x -> x, new ExtremeComparator());
        Assert.assertEquals(Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1), Enumerable.toList(query));
    }

    private class ExtremeComparator implements Comparator<Integer> {

        public int compare(Integer x, Integer y){
            return (x == y || y.equals(x)) ? 0
                    : x < y ? Integer.MIN_VALUE
                    : Integer.MAX_VALUE;
        }
    }
}
