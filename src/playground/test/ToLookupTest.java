package playground.test;

import blaq.core.Enumerable;
import blaq.util.IGrouping;
import blaq.util.ILookup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ToLookupTest {

    @Test(expected = UnsupportedOperationException.class)
    public void srcSequenceIsReadEagerly(){
        Iterable<Integer> i = new ThrowingIterable();
        Enumerable.toLookup(i, x -> x);
    }

    @Test
    public void changesToSrcSequenceAfterToLookupAreNotNoticed(){
        List<String> src = new ArrayList<>();
        src.add("abc");
        ILookup<Integer, String> lookup = Enumerable.toLookup(src, String::length);
        Assert.assertEquals(1, lookup.count());

        // Potential new key is ignored
        src.add("x");
        Assert.assertEquals(1, lookup.count());

        // Potential new value for existing key is ignored
        src.add("xyz");
        Assert.assertEquals(Enumerable.toList(Arrays.asList("abc")), Enumerable.toList(lookup.getItem(3)));
    }

    @Test
    public void lookupWithNoCompOrSelector(){
        Iterable<String> src = Arrays.asList("abc", "def", "x", "y", "ghi", "z", "00");
        // Use the length as the key selector, and the first character as the element
        ILookup<Integer, String> lookup = Enumerable.toLookup(src, String::length);

        Assert.assertEquals(Arrays.asList("abc", "def", "ghi"), Enumerable.toList(lookup.getItem(3)));
        Assert.assertEquals(Arrays.asList("x", "y", "z"), Enumerable.toList(lookup.getItem(1)));
        Assert.assertEquals(Enumerable.toList(Arrays.asList("00")), Enumerable.toList(lookup.getItem(2)));

        Assert.assertEquals(3, lookup.count());

        // Unknown key should return an empty sequence
        Assert.assertEquals(Collections.emptyList(), Enumerable.toList(lookup.getItem(100)));
    }

    @Test
    public void findByNullKeyNonePresent(){
        Iterable<String> src = Arrays.asList("first", "second");
        ILookup<String, String> lookup = Enumerable.toLookup(src, x -> x);
        Assert.assertEquals(Collections.emptyList(), Enumerable.toList(lookup.getItem(null)));
    }

    @Test
    public void findByNullKeyWhenPresent(){
        Iterable<String> src = Arrays.asList("first", "null", "nothing", "second");
        ILookup<String, String> lookup = Enumerable.toLookup(src, x -> x.startsWith("n") ? null : x);
        Assert.assertEquals(Enumerable.toList(Arrays.asList("null", "nothing")), Enumerable.toList(lookup.getItem(null)));
        Iterable<String> i = Enumerable.project(lookup, IGrouping::getKey);
        Assert.assertEquals(Enumerable.toList(Arrays.asList("first", null, "second")), i);
        Assert.assertEquals(3, lookup.count());
        Assert.assertEquals(Enumerable.toList(Arrays.asList("null", "nothing")), Enumerable.toList(lookup.getItem(null)));
    }
}
