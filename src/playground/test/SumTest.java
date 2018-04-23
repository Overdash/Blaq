package playground.test;

import blaq.core.Enumerable;
import blaq.core.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Note: This test suite omits the testing of "Nullable types" found in C#
 * These types are replaced with the Box versions of primitives, i.e :
 *  int (in C#) == Integer (in Java).
 *  int? (in C#) == Integer (in Java).
 * For this reason, tests that use Boxed primitives immediately count for 2 tests:
 * The un-boxed primitive test and the nullable un-boxed primitive test.
 */
public class SumTest {

    /** Int Sum tests **/

    @Test(expected = NullArgumentException.class)
    public void nullSrcIntNoSelector(){
        Iterable<Integer> src = null;
        Enumerable.sum(src);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcIntSelector(){
        Iterable<String> src = null;
        Enumerable.sum(src, x -> x.length());
    }

    @Test(expected = NullArgumentException.class)
    public void nullSelectorInt(){
        Iterable<String> src = new ArrayList<>();
        Function<String, Integer> selector = null;
        Enumerable.sum(src, selector);
    }

    @Test
    public void emptySequenceInt(){
        Iterable<Integer> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.sum(src));
    }

    @Test
    public void seqOfNullsNullableInt(){
        Iterable<Integer> src = Arrays.asList(null, null);
        Assert.assertEquals(0, Enumerable.sum(src));
    }

    @Test
    public void emptySequenceIntSelector(){
        Iterable<String> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.sum(src, x -> x.length()));
    }

    @Test
    public void seqOfNullsNullableIntSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Assert.assertEquals(0, Enumerable.sum(src, x -> null));
    }

    @Test
    public void simpleSumInt(){
        Iterable<Integer> src = Arrays.asList(1, 3, 2);
        Assert.assertEquals(6, Enumerable.sum(src));
    }

    @Test
    public void simpleSumNullableIntIncludingNulls(){
        Iterable<Integer> src = Arrays.asList(1, null, 3, null, 2);
        Assert.assertEquals(6, Enumerable.sum(src));
    }

    @Test
    public void simpleSumIntWithSelector(){
        Iterable<String> src = Arrays.asList("x", "abc", "de");
        Assert.assertEquals(6, Enumerable.sum(src, x -> x.length()));
    }

    @Test
    public void simpleSumNullableIntWithSelectorIncludingNulls(){
        Iterable<String> src = Arrays.asList("x", "abc", "null", "de");
        Assert.assertEquals(6, Enumerable.sum(src, x -> x.equals("null") ? null : x.length()));
    }

    @Test
    public void negativeOverflowInt(){
        // Only test this once per type - the other overflow tests should be enough
        // for different method calls
        Iterable<Integer> src = Arrays.asList(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Assert.assertEquals(0, Enumerable.sum(src));
    }

    @Test(expected = ArithmeticException.class)
    public void overflowInt(){
        Iterable<Integer> src = Arrays.asList(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Enumerable.sum(src);
    }

    @Test(expected = ArithmeticException.class)
    public void overflowIntWithSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Enumerable.sum(src, x -> Integer.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void overflowComputableSumInt(){
        Iterable<Integer> src = Arrays.asList(Integer.MAX_VALUE, 1, -1, -Integer.MAX_VALUE);
        // In a world where we summed using a long accumulator, the
        // result would be 0.
        Enumerable.sum(src);
    }

    /** Long Sum Tests **/

    @Test(expected = NullArgumentException.class)
    public void nullSrcLongNoSelector(){
        Iterable<Long> src = null;
        Enumerable.longSum(src);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcLongSelector(){
        Iterable<String> src = null;
        Enumerable.longSum(src, x -> (long)x.length());
    }

    @Test(expected = NullArgumentException.class)
    public void nullSelectorLong(){
        Iterable<String> src = new ArrayList<>();
        Function<String, Long> selector = null;
        Enumerable.longSum(src, selector);
    }

    @Test
    public void emptySequenceLong(){
        Iterable<Long> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.longSum(src));
    }

    @Test
    public void seqOfNullsNullableLong(){
        Iterable<Long> src = Arrays.asList(null, null);
        Assert.assertEquals(0, Enumerable.longSum(src));
    }

    @Test
    public void emptySequenceLongSelector(){
        Iterable<String> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.longSum(src, x -> (long)x.length()));
    }

    @Test
    public void seqOfNullsNullableLongSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Assert.assertEquals(0, Enumerable.longSum(src, x -> null));
    }

    @Test
    public void simpleSumLong(){
        Iterable<Long> src = Arrays.asList(1L, 3L, 2L);
        Assert.assertEquals(6, Enumerable.longSum(src));
    }

    @Test
    public void simpleSumNullableLongIncludingNulls(){
        Iterable<Long> src = Arrays.asList(1L, null, 3L, null, 2L);
        Assert.assertEquals(6, Enumerable.longSum(src));
    }

    @Test
    public void simpleSumLongWithSelector(){
        Iterable<String> src = Arrays.asList("x", "abc", "de");
        Assert.assertEquals(6, Enumerable.longSum(src, x -> (long) x.length()));
    }

    @Test
    public void simpleSumNullableLongWithSelectorIncludingNulls(){
        Iterable<String> src = Arrays.asList("x", "abc", "null", "de");
        Assert.assertEquals(6, Enumerable.longSum(src, x -> x.equals("null") ? null : (long) x.length()));
    }

    @Test
    public void negativeOverflowLong(){
        // Only test this once per type - the other overflow tests should be enough
        // for different method calls
        Iterable<Long> src = Arrays.asList(Long.MIN_VALUE, Long.MIN_VALUE);
        Assert.assertEquals(0, Enumerable.longSum(src));
    }

    @Test(expected = ArithmeticException.class)
    public void overflowLong(){
        Iterable<Long> src = Arrays.asList(Long.MAX_VALUE, Long.MAX_VALUE);
        Enumerable.longSum(src);
    }

    @Test(expected = ArithmeticException.class)
    public void overflowLongWithSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Enumerable.longSum(src, x -> Long.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void overflowComputableSumLong(){
        Iterable<Long> src = Arrays.asList(Long.MAX_VALUE, 1L, -1L, -Long.MAX_VALUE);
        // In a world where we summed using a long accumulator, the
        // result would be 0.
        Enumerable.longSum(src);
    }

    /** Short Sum Test **/

    @Test(expected = NullArgumentException.class)
    public void nullSrcShortNoSelector(){
        Iterable<Short> src = null;
        Enumerable.shortSum(src);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcShortSelector(){
        Iterable<String> src = null;
        Enumerable.shortSum(src, x -> (short)x.length());
    }

    @Test(expected = NullArgumentException.class)
    public void nullSelectorShort(){
        Iterable<String> src = new ArrayList<>();
        Function<String, Short> selector = null;
        Enumerable.shortSum(src, selector);
    }

    @Test
    public void emptySequenceShort(){
        Iterable<Short> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.shortSum(src));
    }

    @Test
    public void seqOfNullsNullableShort(){
        Iterable<Short> src = Arrays.asList(null, null);
        Assert.assertEquals(0, Enumerable.shortSum(src));
    }

    @Test
    public void emptySequenceShortSelector(){
        Iterable<String> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.shortSum(src, x -> (short)x.length()));
    }

    @Test
    public void seqOfNullsNullableShortSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Assert.assertEquals(0, Enumerable.shortSum(src, x -> null));
    }

    @Test
    public void simpleSumShort(){
        Iterable<Short> src = Arrays.asList((short)1, (short)3, (short)2);
        Assert.assertEquals(6, Enumerable.shortSum(src));
    }

    @Test
    public void simpleSumNullableShortIncludingNulls(){
        Iterable<Short> src = Arrays.asList((short)1, null, (short)3, null, (short)2);
        Assert.assertEquals(6, Enumerable.shortSum(src));
    }

    @Test
    public void simpleSumShortWithSelector(){
        Iterable<String> src = Arrays.asList("x", "abc", "de");
        Assert.assertEquals(6, Enumerable.shortSum(src, x -> (short) x.length()));
    }

    @Test
    public void simpleSumNullableShortWithSelectorIncludingNulls(){
        Iterable<String> src = Arrays.asList("x", "abc", "null", "de");
        Assert.assertEquals(6, Enumerable.shortSum(src, x -> x.equals("null") ? null : (short) x.length()));
    }

    @Test
    public void negativeOverflowShort(){
        // Only test this once per type - the other overflow tests should be enough
        // for different method calls
        Iterable<Short> src = Arrays.asList(Short.MIN_VALUE, Short.MIN_VALUE);
        Assert.assertEquals(0, Enumerable.shortSum(src));
    }

    @Test(expected = ArithmeticException.class)
    public void overflowShort(){
        Iterable<Short> src = Arrays.asList(Short.MAX_VALUE, Short.MAX_VALUE);
        Enumerable.shortSum(src);
    }

    @Test(expected = ArithmeticException.class)
    public void overflowShortWithSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Enumerable.shortSum(src, x -> Short.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void overflowComputableSumShort(){
        Iterable<Short> src = Arrays.asList(Short.MAX_VALUE, (short)1, (short)-1, (short) -Short.MAX_VALUE);
        // In a world where we summed using a long accumulator, the
        // result would be 0.
        Enumerable.shortSum(src);
    }

    /** Double Sum Test **/

    @Test(expected = NullArgumentException.class)
    public void nullSrcDoubleNoSelector(){
        Iterable<Double> src = null;
        Enumerable.doubleSum(src);
    }

    @Test(expected = NullArgumentException.class)
    public void nullSrcDoubleSelector(){
        Iterable<String> src = null;
        Enumerable.doubleSum(src, x -> (double)x.length());
    }

    @Test(expected = NullArgumentException.class)
    public void nullSelectorDouble(){
        Iterable<String> src = new ArrayList<>();
        Function<String, Double> selector = null;
        Enumerable.doubleSum(src, selector);
    }

    @Test
    public void emptySequenceDouble(){
        Iterable<Double> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.doubleSum(src), 0);
    }

    @Test
    public void seqOfNullsNullableDouble(){
        Iterable<Double> src = Arrays.asList(null, null);
        Assert.assertEquals(0, Enumerable.doubleSum(src), 0);
    }

    @Test
    public void emptySequenceDoubleSelector(){
        Iterable<String> src = new ArrayList<>();
        Assert.assertEquals(0, Enumerable.doubleSum(src, x -> (double)x.length()), 0);
    }

    @Test
    public void seqOfNullsNullableDoubleSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Assert.assertEquals(0, Enumerable.doubleSum(src, x -> null), 0);
    }

    @Test
    public void simpleSumDouble(){
        Iterable<Double> src = Arrays.asList((double)1, (double)3, (double)2);
        Assert.assertEquals(6, Enumerable.doubleSum(src), 0);
    }

    @Test
    public void simpleSumNullableDoubleIncludingNulls(){
        Iterable<Double> src = Arrays.asList((double)1, null, (double)3, null, (double)2);
        Assert.assertEquals(6, Enumerable.doubleSum(src), 0);
    }

    @Test
    public void simpleSumDoubleWithSelector(){
        Iterable<String> src = Arrays.asList("x", "abc", "de");
        Assert.assertEquals(6, Enumerable.doubleSum(src, x -> (double) x.length()), 0);
    }

    @Test
    public void simpleSumNullableDoubleWithSelectorIncludingNulls(){
        Iterable<String> src = Arrays.asList("x", "abc", "null", "de");
        Assert.assertEquals(6,
                Enumerable.doubleSum(src, x -> x.equals("null") ? null : (double) x.length()), 0);
    }

    @Test
    public void negativeOverflowDouble(){
        // Only test this once per type - the other overflow tests should be enough
        // for different method calls
        Iterable<Double> src = Arrays.asList(Double.MIN_VALUE, Double.MIN_VALUE);
        Assert.assertEquals(2*Double.MIN_VALUE, Enumerable.doubleSum(src), 0.0);
    }

    @Test
    public void overflowDouble(){
        Iterable<Double> src = Arrays.asList(Double.MAX_VALUE, Double.MAX_VALUE);
        Assert.assertTrue(Double.isInfinite(Enumerable.doubleSum(src)));
    }

    @Test
    public void overflowDoubleWithSelector(){
        Iterable<String> src = Arrays.asList("x", "y");
        Assert.assertTrue(Double.isInfinite(Enumerable.doubleSum(src, x -> Double.MAX_VALUE)));
    }

    @Test
    public void overflowComputableSumDouble(){
        Iterable<Double> src = Arrays.asList(Double.MAX_VALUE, (double)1, (double)-1, -Double.MAX_VALUE);
        Assert.assertEquals(0d, Enumerable.doubleSum(src), 0.0);
    }
}
