package playground;

import org.jetbrains.annotations.NotNull;
import playground.Collections.ILookup;
import playground.Collections.Lookup;

import java.util.*;

import playground.FillerKeywords.Yield;

import java.util.function.*;


/**
 * Class filled with helper methods. Equivalent of IEnumerable in Java is Iterable... however, for convenience
 * Iterable and Collection interfaces will be used where sensible.
 * Uses a Java implementation of Yield to allow a Collection/ Iterable to be used before all values are returned.
 */
public class Enumerable {

    // Refactor Collection to Iterable. -- Check deferred execution
    // Think of adding where List.

    // ----------------------------- Where - O(n) -----------------------------

    /**
     * Filters a sequence based on a predicate.
     * @param source An {@code Iterable} to be filtered.
     * @param predicate Rule to apply on each element.
     * @param <T> Type of elements.
     * @return Output sequence, with elements which conformed to the rule.
     * Output Sequence is the same element type as input
     */
    public static <T> Iterable<T> where(Iterable<T> source, Predicate<T> predicate){
        if(source == null)
            throw new NullArgumentException("source");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        return whereImp(source, predicate);
    }

    private static <T> Yield<T> whereImp(Iterable<T> source, Predicate<T> predicate) {
        return yield -> {
            for(T item: source)
                if(predicate.test(item))
                    yield.returning(item);
        };
    }

    /**
     * Filters a sequence based on a predicate.
     * Each element's index is used in the logic of the predicate function.
     * @param source An {@code Iterable} to be filtered.
     * @param predicate Rule to apply on each element; the second parameter of the function represents the index of the source element.
     * @param <T> Type of elements
     * @return Output sequence, with elements which conformed to the rule.
     */
    public static <T> Iterable<T> where(Iterable<T> source, BiPredicate<T, Integer> predicate){
        if(source == null)
            throw new NullArgumentException("source");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        return whereImp(source, predicate);
    }

    private static <T> Yield<T> whereImp(Iterable<T> source, BiPredicate<T, Integer> predicate){
        return yield -> {
            int i = 0;
            for(T item : source){
                if(predicate.test(item, i))
                    yield.returning(item);
                i++;
            }
        };
    }

    // ----------------------------- Select - O(n) -----------------------------
    public static <TSource, TResult> Iterable<TResult> project(Iterable<TSource> source,
                                                               Function<TSource, TResult> projector){
        if(source == null)
            throw new NullArgumentException("source");
        if(projector == null)
            throw new NullArgumentException("projector");
        return projectionImp(source, projector);
    }

    private static <TSource, TResult> Yield<TResult> projectionImp(Iterable<TSource> source,
                                                                      Function<TSource, TResult> projector){
        return yield -> {
            for(TSource item: source)
                yield.returning(projector.apply(item));
        };
    }

    public static <TSource, TResult> Iterable<TResult> project(Iterable<TSource> source,
                                                               BiFunction<TSource, Integer, TResult> projector){
        if(source == null)
            throw new NullArgumentException("source");
        if(projector == null)
            throw new NullArgumentException("projector");
        return projectionImp(source, projector);
    }

    private static <TSource, TResult> Yield<TResult> projectionImp(Iterable<TSource> source,
                                                                   BiFunction<TSource, Integer, TResult> projector){
        return yield -> {
            int i = 0;
            for(TSource item: source) {
                yield.returning(projector.apply(item, i));
                i++;
            }
        };
    }

    // ----------------------------- ToList - O(n) -----------------------------
    public static <T> List<T> toList(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("source");
        List<T> result = new ArrayList<>();
        for(T item: src)
            result.add(item);
        return result;
    }

    // ----------------------------- toMap - O(n) -----------------------------
    // Can make it so it can take an Iterable of Values and a List/ Iterable for the Indexes

    // SelectMany - O(n^2) -> T S U
    public static <TSource, TSubsequence, TResult> Iterable<TResult> projectMany(Iterable<TSource> source,
                                                                                 Function<TSource, Iterable<TSubsequence>> projector,
                                                                                 BiFunction<TSource, TSubsequence, TResult> resultProjector){
        if(source == null)
            throw new NullArgumentException("source");
        if(projector == null)
            throw new NullArgumentException("projector");
        if(resultProjector == null)
            throw new NullArgumentException("result projector");
        return projectManyImp(source, projector, resultProjector);
    }

    private static <TSource, TSubsequence, TResult> Iterable<TResult> projectManyImp(Iterable<TSource> source,
                                                                                 Function<TSource, Iterable<TSubsequence>> projector,
                                                                                 BiFunction<TSource, TSubsequence, TResult> resultProjector){
        Collection<TResult> result = new ArrayList<>();
        for(TSource item : source)
            for(TSubsequence subItem : projector.apply(item))
                result.add(resultProjector.apply(item, subItem));
        return result;
    }

    // ToLookup -- Conduct testing for all functions before continuing!
    public static <S, K> ILookup<K, S> toLookup(Iterable<S> source, Function<S, K> keySelector){
        return toLookup(source, keySelector, e -> e);
    }

    public static <S, K, V>  ILookup<K, V> toLookup(Iterable<S> source, Function<S, K> keySelector,
                                                    Function<S, V> valSelector){
        Lookup<K, V> result = new Lookup<>();
        for(S item : source)
            result.add(keySelector.apply(item), valSelector.apply(item));

        return result;
    }

    // Empty. Caches (hence special class)
    public static <TResult> Iterable<TResult> empty(){
        return null;
    }

    // GroupJoin
    public static <TOuter, TInner, TKey, TResult> Iterable<TResult> GroupJoin(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                              Function<TOuter, TKey> outerKeySelector,
                                                                              Function<TInner, TKey> innerKeySelector,
                                                                              BiFunction<TOuter, Iterable<TInner>, TResult> resultSelector){
        return null; // Implement later
    }

    /* --------------------Nested Classes---------------------- */

}
