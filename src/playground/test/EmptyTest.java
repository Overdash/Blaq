package playground.test;

import org.junit.Assert;
import org.junit.Test;
import blaq.core.Enumerable;

import java.util.Iterator;

/**
 * Test:
 *  - Return Sequence is empty
 *  - (Optional) Return Sequence is cached for types (singleton) -- May not work due to Type Erasure and Java generic handling.
 */
public class EmptyTest {

    @Test
    public void emptyContainsNoElements(){
        Iterator<? super Integer> it = Enumerable.empty().iterator();
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void emptyIsASingletonPerElementType(){
        Assert.assertSame(Enumerable.<Integer>empty(), Enumerable.<Integer>empty());
        Assert.assertSame(Enumerable.<Long>empty(), Enumerable.<Long>empty());
        Assert.assertSame(Enumerable.<String>empty(), Enumerable.<String>empty());
        Assert.assertSame(Enumerable.empty(), Enumerable.empty());

        /* These will always Fail! (due to type Erasure) */
        // Assert.assertNotSame(Enumerable.empty(), Enumerable.<String>empty());
        //Assert.assertNotSame(Enumerable.<Integer>empty(), Enumerable.<Long>empty());
    }
}
