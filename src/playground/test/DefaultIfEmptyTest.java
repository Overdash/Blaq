package playground.test;

import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultIfEmptyTest {

    @Test(expected = NullArgumentException.class)
    public void nullSourceNoDefaultVal(){
        ArrayList<Integer> a = null;
        Enumerable.defaultIfEmpty(a);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSourceWithDefaultVal(){
        ArrayList<Integer> a = null;
        Enumerable.defaultIfEmpty(a, 5);
    }

    @Test
    public void emptySequenceNoDefault(){
        Iterable<?> res = Enumerable.defaultIfEmpty(new ArrayList<>());
        Assert.assertEquals(null, res);
    }

    @Test
    public void emptySequenceWithDefault(){
        Assert.assertEquals(Arrays.asList(5),
                new BlaqList<>(Enumerable.defaultIfEmpty(Collections.emptyList(), 5)));
    }

    @Test
    public void nonEmptySeqNoDefault(){
        List<Integer> a = Arrays.asList(3, 1, 4);
        BlaqIterable<Integer> res = new BlaqList<>(Enumerable.defaultIfEmpty(a));
        Assert.assertEquals(a, res.toList());
    }

    @Test
    public void nonEmptySeqWithDefault(){
        List<Integer> a = Arrays.asList(3, 1, 4);
        BlaqIterable<Integer> res = new BlaqList<>(Enumerable.defaultIfEmpty(a, 5));
        Assert.assertEquals(a, res.toList());
    }

}
