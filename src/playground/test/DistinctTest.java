package playground.test;

import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DistinctTest {

    // testStr1 and testStr2 are references to different but equal strings
    private static String testStr1 = "test";
    private static String testStr2 = new String(testStr1.toCharArray());

    @Test(expected = NullArgumentException.class)
    public void nullSrcNoComp(){
        List<String> src = null;
        Enumerable.distinct(src);
    }

    //TODO Comparer tests (must implement it first)

    /*@Test(expected = NullArgumentException.class)
    public void nullSrcWithComp(){
        List<String> src = null;
    }*/

    @Test
    public void noComparerSpecifiedUsesDefault(){
        List<String> src = Arrays.asList("xyz", testStr1, "XYZ", testStr2, "def");
        Assert.assertEquals(Arrays.asList("xyz", testStr1, "XYZ", "def"), Enumerable.toList(Enumerable.distinct(src)));
    }
}
