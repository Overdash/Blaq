package playground.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import playground.Enumerable;
import playground.NullArgumentException;

class WhereTest {

    @Test
    void NullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Assertions.assertThrows(NullArgumentException.class,
                () -> {Iterable<Integer> filteredList = Enumerable.where(source, x -> x > 5);
        });
    }

    @Test // -> Fails atm. Java doesn't do deferred execution.
    void WithIndexExecutionIsDeferred(){
        ThrowingIterable.AssertDeferred(src -> Enumerable.where(src, (x, index) -> x > 0));
    }
}
