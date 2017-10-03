package playground.test;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Iterable<T> result = deferredExecution.apply(source);
        Iterator<T> it = result.iterator();
        Assertions.assertThrows(UnsupportedOperationException.class, it::next);
    }
}
