package playground.test;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import playground.util.CloseableIterator;
import playground.tools.Yield;

import java.util.Iterator;
import java.util.function.Function;

public final class ThrowingIterable implements Iterable<Integer> {
    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public static <T> void AssertDeferred(Function<Iterable<Integer>, Iterable<T>> deferredExecution){
        ThrowingIterable source = new ThrowingIterable();
        Yield<T> result = (Yield<T>)deferredExecution.apply(source);
        Iterator<T> it = result.iterator();
        it.next();
    }
}
