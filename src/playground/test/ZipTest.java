package playground.test;

import blaq.core.Enumerable;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ZipTest {

    // Null tests passed before -- not gonna retest

    @Test
    public void shortFirst(){
        Iterable<String> first = Arrays.asList("a", "b", "c");
        Iterable<Integer> second = Enumerable.range(5, 10);
        Iterable<?> query = Enumerable.zip(first, second, (x, y) -> x + ":" + y);
        Assert.assertEquals(Arrays.asList("a:5", "b:6", "c:7"), Enumerable.toList(query));
    }

    @Test
    public void sequencesEqualLength(){
        Iterable<String> first = Arrays.asList("a", "b", "c");
        Iterable<Integer> second = Enumerable.range(5, 3);
        Iterable<?> query = Enumerable.zip(first, second, (x, y) -> x + ":" + y);
        Assert.assertEquals(Arrays.asList("a:5", "b:6", "c:7"), Enumerable.toList(query));
    }

    @Test
    public void adjacentElements(){
        BlaqIterable<String> elements = new BlaqList<>(Arrays.asList("a", "b", "c", "d", "e"));
//        System.out.println(elements.skip(1).toList());
        Assert.assertEquals(Arrays.asList("ab", "bc", "cd", "de"),
                elements.zip(elements.skip(1), (x, y) -> x + y).toList());
    }
}
