package playground.test;

import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UnionTest {

   @Test(expected = NullArgumentException.class)
   public void nullFirstNoComp(){
       List<String> first = null;
       List<String> second = new ArrayList<>();
       Enumerable.union(first, second);
   }

    @Test(expected = NullArgumentException.class)
    public void nullSecondNoComp(){
        List<String> second = null;
        List<String> first = new ArrayList<>();
        Enumerable.union(first, second);
    }

    @Test
    public void unionNoComparer(){
        List<String> first = Arrays.asList("a", "b", "B", "c", "b");
        List<String> second = Arrays.asList("d", "e", "d", "a");

        Assert.assertEquals(Arrays.asList("a", "b", "B", "c", "d", "e"),
                Enumerable.toList(Enumerable.union(first, second)));
    }

    @Test
    public void unionWithEmptyFirstSequence(){
        List<String> first = new ArrayList<>();
        List<String> second = Arrays.asList("d", "e", "d", "a");

        Assert.assertEquals(Arrays.asList("d", "e", "a"),
                Enumerable.toList(Enumerable.union(first, second)));
    }

    @Test
    public void unionWithEmptySecondSequence(){
       List<String> first = Arrays.asList("a", "b", "B", "c", "b");
       List<String> second = new ArrayList<>();

        Assert.assertEquals(Arrays.asList("a", "b", "B", "c"),
                Enumerable.toList(Enumerable.union(first, second)));
    }

    @Test
    public void unionWithTwoEmptySeq(){
       List<String> first = new ArrayList<>();
       List<String> second = new ArrayList<>();

        Assert.assertEquals(Collections.emptyList(),
                Enumerable.toList(Enumerable.union(first, second)));
    }

    @Test
    public void testDeferredExec(){
       List<Integer> first = new ArrayList<>();
       first.add(2);
       List<Integer> second = new ArrayList<>();
       second.add(6);
       Iterable<Integer> i = Enumerable.union(first, second);
       first.add(10);
       second.add(7);
       first.add(2);
       second.add(10);
        Assert.assertEquals(Arrays.asList(2, 10, 6, 7),
                Enumerable.toList(i));
    }
}
