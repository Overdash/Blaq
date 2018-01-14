package playground.test;

import org.junit.jupiter.api.Assertions;
import playground.Enumerable;

import java.util.ArrayList;

public class TestExtensions {

    @SafeVarargs
    public static <T> void assertSequenceEqual(Iterable<T> actual, T... expected){
        ArrayList<T> copy = new ArrayList<>();
        Enumerable.addToCollection(copy,actual);
        Assertions.assertEquals(expected.length, copy.size(), "Expected sizes to be equal");

        for(int i = 0; i < copy.size(); i++)
            if(!copy.get(i).equals(expected[i]))
                Assertions.fail("Expected sequences differ at index " + 1 + ": expected "
                                + expected[i] + "; was " + copy.get(i));
    }
}
