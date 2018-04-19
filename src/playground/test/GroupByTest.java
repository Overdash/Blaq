package playground.test;

import blaq.core.Enumerable;
import blaq.util.IGrouping;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GroupByTest {

    @Test
    public void simpleGroupBy()
    {
        Iterable<String> source = Arrays.asList( "abc", "hello", "def", "there", "four" );
        Iterable<IGrouping<Integer, String>> groups = Enumerable.groupBy(source, String::length);

        List<IGrouping<Integer, String>> list = Enumerable.toList(groups);
        Assert.assertEquals(3, list.size());

        Assert.assertEquals(Arrays.asList("abc", "def"), list.get(0).toList());
        Assert.assertEquals(3, (int)list.get(0).getKey());

        Assert.assertEquals(Arrays.asList("hello", "there"), list.get(1).toList());
        Assert.assertEquals(5, (int)list.get(1).getKey());

        Assert.assertEquals(Arrays.asList("four"), list.get(2).toList());
        Assert.assertEquals(4, (int)list.get(2).getKey());
    }

    @Test
    public void groupByWithElementProjection()
    {
        Iterable<String> source = Arrays.asList( "abc", "hello", "def", "there", "four" );
        Iterable<IGrouping<Integer, String>> groups = Enumerable.groupBy(source, String::length, x -> String.valueOf(x.charAt(0)));

        List<IGrouping<Integer, String>> list = Enumerable.toList(groups);
        Assert.assertEquals(3, list.size());

        Assert.assertEquals(Arrays.asList("a", "d"), list.get(0).toList());
        Assert.assertEquals(3, (int)list.get(0).getKey());

        Assert.assertEquals(Arrays.asList("h", "t"), list.get(1).toList());
        Assert.assertEquals(5, (int)list.get(1).getKey());

        Assert.assertEquals(Arrays.asList("f"), list.get(2).toList());
        Assert.assertEquals(4, (int)list.get(2).getKey());
    }
}
