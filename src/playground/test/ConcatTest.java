package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import blaq.util.BlaqList;

import java.util.Arrays;

public class ConcatTest {

    @Test
    public void simpleConcatenation(){
        BlaqList<String> b = new BlaqList<>((Iterable<String>)Arrays.asList("a", "b"));
        Assert.assertEquals(Arrays.asList("a","b","c","d"), b
                .concat(Arrays.asList("c", "d"))
                .toList());
    }

    @Test(expected = NullArgumentException.class)
    public void nullFirstThrowsNullArgs(){
        Enumerable.concat(null, Arrays.asList("hello", "bye"));
    }

    @Test(expected = NullArgumentException.class)
    public void nullSecondThrowsNullArgs(){
        Enumerable.concat(Arrays.asList("hello", "bye"), null);
    }

    //@Test
    public void firstSequenceIsntAccessedBeforeFirstUse(){
        // fix  throwable iterator for this.
    }
}
