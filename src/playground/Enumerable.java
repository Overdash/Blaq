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
    private Enumerable(){} //prevent creating instances of this class

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
//        Supplier<Iterable<T>> res = () -> whereImp(source, predicate); // For deferred Exe, this should be skipped when Ln 21 in ThrowingIterable is executed
//        return res.get();
        return whereImp(source, predicate);
    }

    private static <T> Iterable<T> whereImp(Iterable<T> source, Predicate<T> predicate) {
        return (Yield<T>) yield -> {
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

    // ----------------------------- Range -----------------------------
    public static <T> Iterable<Integer> range(Iterable<T> src, int start, int count){
        if(count < 0)
            throw new ArgumentOutOfRangeException("count");

        // Convert everything to long (avoid overflows)
        if((long)start + (long)count - 1L > Integer.MAX_VALUE)
            throw new ArgumentOutOfRangeException("count");

        return rangeImp(src, start, count);
    }

    private static <T> Yield<Integer> rangeImp(Iterable<T> src, int start, int count){
        return yield -> {
          for (int i = 0; i < count; i++)
              yield.returning(start+i);
        };
    }

    // ----------------------------- Empty. Caches (hence special class) -----------------------------
    //TODO: Empty -- Static generic fields are illegal in java (From JavaDoc: We cannot declare static fields whose types are type parameters)
    public static <TResult> Iterable<TResult> empty(){
        return null; // Need to implement this...
    }

    // ----------------------------- Repeat -----------------------------
    public static <E> Iterable<E> repeat(E e, int count){
        if(count < 0)
            throw new ArgumentOutOfRangeException("count");
        return repeatImp(e,count);
    }

    private static <E> Iterable<E> repeatImp(E e, int count) {
        return (Yield<E>) yield -> {
          for(int i=0; i < count; i++)
              yield.returning(e);
        };
    }

    // ----------------------------- Count & LongCount (Use immediate exec) -----------------------------
    // ----------------------------- Might be useless for Java... -----------------------------
    public static <T> int count(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        Collection<T> c = (Collection<T>) src;

        return c.size();
    }

    public static <T> int count(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        int count = 0;
        for(T item : src){
            if(predicate.test(item)){
                if(count >= Integer.MAX_VALUE)
                    throw new ArithmeticException("count overflow");
                else
                    count++;
            }
        }
        return count;
    }

    public static <T> long longCount(Iterable<T> src){
        return 0;

    }

    public static <T> long longCount(Iterable<T> src, Predicate<T> predicate){
        return 0;

    }

    // ----------------------------- Concat -----------------------------
    // TODO Prepend/ Append (MoreLINQ)
    public static <T> Iterable<T> concat(Iterable<T> first, Iterable<T> second){
        if(first == null)
            throw new NullArgumentException("first");
        if(second == null)
            throw new NullArgumentException("second");
        return concatImp(first,second);
    }

    private static <T> Iterable<T> concatImp(Iterable<T> first, Iterable<T> second) {
        return (Yield<T>) yield -> {
            for(T item: first)
                yield.returning(item);
            // if possible, make first null to allow GC to collect it.
            for(T item: second)
                yield.returning(item);
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
    public static <TSource, TSub, TResult> Iterable<TResult> projectMany(Iterable<TSource> source,
                                                                         Function<TSource, Iterable<TSub>> projector,
                                                                         BiFunction<TSource, TSub, TResult> resultProjector){
        if(source == null)
            throw new NullArgumentException("source");
        if(projector == null)
            throw new NullArgumentException("projector");
        if(resultProjector == null)
            throw new NullArgumentException("result projector");
        return projectManyImp(source, projector, resultProjector);
    }

    private static <TSource, TSub, TResult> Iterable<TResult> projectManyImp(Iterable<TSource> source,
                                                                             Function<TSource, Iterable<TSub>> projector,
                                                                             BiFunction<TSource, TSub, TResult> resultProjector){
        return (Yield<TResult>) yield -> {
            for(TSource item : source)
                for(TSub subItem : projector.apply(item))
                    yield.returning(resultProjector.apply(item, subItem));
        };
    }

    // ----------------------------- ToLookup -----------------------------
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

    // ----------------------------- GroupJoin -----------------------------
    public static <TOuter, TInner, TKey, TResult> Iterable<TResult> GroupJoin(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                              Function<TOuter, TKey> outerKeySelector,
                                                                              Function<TInner, TKey> innerKeySelector,
                                                                              BiFunction<TOuter, Iterable<TInner>, TResult> resultSelector){
        return null; // Implement later
    }

    /* --------------------Nested Classes---------------------- */

    private static class EmptyIterable<T> implements Iterable<T>, Iterator<T>{

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return null;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }

    }
}
