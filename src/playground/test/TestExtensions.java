package playground.test;

import org.junit.Assert;
import playground.Enumerable;

import java.util.ArrayList;

public class TestExtensions {

    @SafeVarargs
    public static <T> void assertSequenceEqual(Iterable<T> actual, T... expected){
        ArrayList<T> copy = new ArrayList<>();
        Enumerable.addToCollection(copy,actual);
        Assert.assertEquals(expected.length, copy.size());

        for(int i = 0; i < copy.size(); i++)
            if(!copy.get(i).equals(expected[i]))
                Assert.fail("Expected sequences differ at index " + 1 + ": expected "
                                + expected[i] + "; was " + copy.get(i));
    }
}
