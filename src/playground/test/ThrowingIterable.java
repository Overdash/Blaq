package playground.test;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import playground.util.ClosableIterator;
import playground.FillerKeywords.Yield;

import java.util.Iterator;
import java.util.function.Function;

public final class ThrowingIterable implements Iterable<Integer> {
    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        throw new UnsupportedOperationException();
    }

    public static <T> void AssertDeferred(Function<Iterable<Integer>, Iterable<T>> deferredExecution){
        ThrowingIterable source = new ThrowingIterable();
        Yield<T> result = (Yield<T>)deferredExecution.apply(source);
        try(ClosableIterator<T> it = result.iterator()) {
            Assertions.assertThrows(UnsupportedOperationException.class, it::next);
        }
    }
}
