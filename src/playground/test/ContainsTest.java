package playground.test;

import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ContainsTest {

    @Test(expected = NullArgumentException.class)
    public void nullSrcNoComp(){
        Iterable<String> src = null;
        Enumerable.contains(src, "x");
    }

    @Test
    public void noMatchNoComp(){
        Iterable<String> src = Arrays.asList("foo", "bar", "baz");
        Assert.assertFalse(Enumerable.contains(src, "BAZ"));
    }

    @Test
    public void immediateReturnWhenMatchIsFound(){
        Iterable<Integer> src = Arrays.asList(10, 1, 5, 0);
        Iterable<Integer> query = Enumerable.project(src, x -> 10/x);
        Assert.assertTrue(Enumerable.contains(query, 2));
    }
}
