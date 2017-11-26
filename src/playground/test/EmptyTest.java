package playground.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import playground.Enumerable;
import playground.util.ClosableIterator;

import java.util.Collections;
import java.util.Iterator;

// FLOPPED TEST need to work around Java Generics.
public class EmptyTest {

    @Test
    void emptyContainsNoElements(){
        Iterator<? super Integer> it = Enumerable.empty().iterator();
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    void emptyIsASingletonPerElementType(){

    }
}
