package playground.test;

import org.junit.jupiter.api.Test;
import playground.Enumerable;

class WhereTest {

    @Test //(where = playground.NullArgumentException.class)
    void NullSourceThrowsNullArgumentException(){
        Iterable<Integer> source = null;
        Enumerable.where(source, x -> x > 5);
    }
}
