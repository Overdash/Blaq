package playground.test;

import blaq.util.BlaqIterable;
import blaq.util.BlaqList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SkipWhileTest {
    // Null tests ran before

    @Test
    public void predicateFailingFirstElement(){
        BlaqIterable<String> elements = new BlaqList<>(Arrays.asList("zero", "one", "two", "three", "four", "five"));
        Assert.assertEquals(Arrays.asList("zero", "one", "two", "three", "four", "five"),
                elements.skipWhile(x -> x.length() > 4).toList());
    }

    @Test
    public void predicateWithIndexFailingFirstElement(){
        BlaqIterable<String> elements = new BlaqList<>(Arrays.asList("zero", "one", "two", "three", "four", "five"));
        Assert.assertEquals(Arrays.asList("zero", "one", "two", "three", "four", "five"),
                elements.skipWhile((x, index) -> index + x.length() > 4).toList());
    }

    @Test
    public void predicateMatchingSome(){
        BlaqIterable<String> elements = new BlaqList<>(Arrays.asList("zero", "one", "two", "three", "four", "five"));
        Assert.assertEquals(Arrays.asList("three", "four", "five"),
                elements.skipWhile(x -> x.length() < 5).toList());
    }

    @Test
    public void predicateWithIndexMatchingSome(){
        BlaqIterable<String> elements = new BlaqList<>(Arrays.asList("zero", "one", "two", "three", "four", "five"));
        Assert.assertEquals(Arrays.asList("four", "five"),
                elements.skipWhile((x, index) -> x.length() > index).toList());
    }
}
