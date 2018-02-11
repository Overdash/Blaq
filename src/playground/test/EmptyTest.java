package playground.test;

import org.junit.Assert;
import org.junit.Test;
import playground.Enumerable;

import java.util.Iterator;

// FLOPPED TEST need to work around Java Generics.
public class EmptyTest {

    @Test
    void emptyContainsNoElements(){
        Iterator<? super Integer> it = Enumerable.empty().iterator();
        Assert.assertFalse(it.hasNext());
    }

    @Test
    void emptyIsASingletonPerElementType(){

    }
}
