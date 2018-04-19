package playground.test;

import blaq.core.Enumerable;
import blaq.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OrderByTest {

    @Test
    public void orderingIsStable(){
        Iterable<Pair<Integer, Integer>> src = Arrays.asList(new Pair<>(10, 1), new Pair<>(11, 2), new Pair<>(11, 3), new Pair<>(10, 4));
        Iterable<Pair<Integer, Integer>> sorted = Enumerable.orderBy(src, x -> x.getKey());
        Assert.assertEquals(Arrays.asList(1, 4, 2, 3), Enumerable.toList(Enumerable.project(sorted, x -> x.getValue())));
    }
}
