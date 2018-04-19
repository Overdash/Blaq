package playground.test;

import blaq.core.Enumerable;
import org.junit.Assert;
import org.junit.Test;

public class AsBlaqIterableTest {

    @Test
    public void nullSrcIsAllowed(){
        Iterable<String> src = null;
        Enumerable.asBlaqIterable(src);
    }

    @Test
    public void normalSeq(){
        Iterable<Integer> range = Enumerable.range(0, 10);
        Enumerable.asBlaqIterable(range);
    }

}
