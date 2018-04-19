package playground.test;

import blaq.core.Enumerable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GroupJoinTest {

    @Test
    public void simpleGroupJoin()
    {
        // We're going to join on the first character in the outer sequence item
        // being equal to the second character in the inner sequence item
        Iterable<String> outer = Arrays.asList("first", "second", "third" );
        Iterable<String> inner = Arrays.asList("essence", "offer", "eating", "psalm");

        Iterable<String> query = Enumerable.groupJoin(outer, inner,
                outerElement -> outerElement.charAt(0),
                innerElement -> innerElement.charAt(1),
            (outerElement, innerElements) -> outerElement + ":" + innerElements);

        Assert.assertEquals(Arrays.asList("first:[offer]", "second:[essence,psalm]", "third:[]"),  Enumerable.toList(query));
    }
}
