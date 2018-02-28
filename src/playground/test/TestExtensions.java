package playground.test;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import blaq.core.Enumerable;
import blaq.core.InvalidOperationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

    public static class NonIterableList<T> extends ArrayList<T> implements Iterable<T>{

        public NonIterableList(Collection<? extends T> c) {
            super(c);
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            throw new InvalidOperationException();
        }
    }
}
