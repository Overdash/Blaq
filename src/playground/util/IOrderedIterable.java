package playground.util;

import playground.Enumerable;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Represents a sorted sequence.
 * Collections have the ability to sort their content in Java but BLAQ is different in various ways:
 *      - It expects the caller wants to sort via projections (ideally using lambdas), rather than "natural" comparison
 *        of the whole value/object (i.e. Specifying if ordering should be achieving using property of the
 *        object - age or height, etc).
 *      - Provides easy specification on ordering in ascending/descending order.
 *      - Multiple ordering criteria can be specified. E.g. Ordering by last name then first name.
 *      - Does not sort in-place. Sorting operators return a new sequence which represents the results of the sort.
 * @param <V> Type of elements of the sequence.
 */
public interface IOrderedIterable<V> extends BlaqIterable<V>{

    /**
     * Performs a subsequent ordering on the elements of an {@link IOrderedIterable} according to a key
     * @param keySelector The function used to extract the key for each element.
     * @param comparator The {@link Comparable} used to compare keys for placement in the returned sequence.
     * @param descending {@code true} to sort the elements in descending order. {@code false} to sort elements in ascending order
     * @param <K> The type of the key produced by {@code keySelector}.
     * @return An {@link IOrderedIterable} whose elements are sorted according to a key.
     */
    <K> IOrderedIterable<V> createOrderedIterable(Function<V, K> keySelector,
                                                  Comparator<K> comparator,
                                                  boolean descending);

    /* Default ops */

    default <K> IOrderedIterable<V> thenBy(Function<V, K> keySelector){
        return Enumerable.thenBy(this, keySelector);
    }

    default <K> IOrderedIterable<V> thenBy(Function<V, K> keySelector, Comparator<K> comparator){
        return Enumerable.thenBy(this, keySelector, comparator);
    }

    default <K> IOrderedIterable<V> thenByDescending(Function<V, K> keySelector){
        return Enumerable.thenByDescending(this, keySelector);
    }

    default <K> IOrderedIterable<V> thenByDescending(Function<V, K> keySelector, Comparator<K> comparator){
        return Enumerable.thenByDescending(this, keySelector, comparator);
    }
}
