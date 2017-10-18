package playground;

import org.jetbrains.annotations.NotNull;
import playground.Collections.ClosableIterator;
import playground.Collections.ILookup;
import playground.Collections.Lookup;

import java.io.Closeable;
import java.util.*;

import playground.FillerKeywords.Yield;

import java.util.function.*;


/**
 * Class filled with helper methods. Equivalent of IEnumerable in Java is Iterable... however, for convenience
 * Iterable and Collection interfaces will be used where sensible.
 * Uses a Java implementation of Yield to allow a Collection/ Iterable to be used before all values are returned.
 * Methods are decoupled to prevent bugs/ errors in one implementation from contaminating other implementations.
 * @since BLAQ v0
 */
public class Enumerable {
    private Enumerable(){} //prevent creating instances of this class

    //TODO Might create a one-argument constructor that takes an iterable and wraps it with Enumerable methods
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
    // ----------------------------- Might be useless for Java... but !For SQL count(*) -----------------------------

    /**
     * Returns the number of elements in a sequence.
     * @param src A sequence that contains elements to be counted.
     * @param <T> The type of the elements of source.
     * @return The number of elements in the input sequence.
     * @throws NullArgumentException Thrown when {@code src} is null.
     */
    public static <T> int count(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        Collection<T> c = (Collection<T>) src;

        return c.size();
    }

    /**
     * Returns a number that represents how many elements in the specified sequence satisfy a condition.
     * @param src A sequence that contains elements to be tested and counted.
     * @param predicate A function to test each element for a condition.
     * @param <T> The type of the elements of source.
     * @return A number that represents how many elements in the sequence satisfy the condition in the predicate function.
     * @throws NullArgumentException Thrown when {@code src} or {@code predicate} is null.
     * @throws ArithmeticException When the count of {@code src} is greater than {@code Integer.MAX_VALUE}
     */
    public static <T> int count(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        int count = 0;
        for(T item : src){
            if(predicate.test(item)){
                if(count >= Integer.MAX_VALUE)
                    throw new ArithmeticException("Integer overflow");
                else count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of elements in a sequence as a {@link Long}.
     * @param src A sequence that contains elements to be counted.
     * @param <T> The type of the elements of source.
     * @return The {@link Long} number of elements in the input sequence.
     * @throws NullArgumentException Thrown when {@code src} is null.
     */
    public static <T> long longCount(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        Collection<T> c = (Collection<T>) src;

        return c.size();
    }

    /**
     * Returns a number that represents how many elements in the specified sequence satisfy a condition, as a {@link Long}.
     * @param src A sequence that contains elements to be tested and counted.
     * @param predicate A function to test each element for a condition.
     * @param <T> The type of the elements of source.
     * @return A {@link Long} number that represents how many elements in the sequence satisfy the condition in the predicate function.
     * @throws NullArgumentException Thrown when {@code src} or {@code predicate} is null.
     * @throws ArithmeticException When the count of {@code src} is greater than {@code Long.MAX_VALUE}
     */
    public static <T> long longCount(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        long count = 0;
        for(T item : src){
            if(predicate.test(item)){
                if(count >= Long.MAX_VALUE)
                    throw new ArithmeticException("Long overflow");
                else count++;
            }
        }
        return count;
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

    // ----------------------------- SelectMany - O(n^2) -> T S U -----------------------------
    // Very important method, must understand it

    /**
     * Projects each element of a sequence to an {@link Iterable}, flattens the resulting sequences into one sequence,
     * and invokes a result selector function on each element therein.
     * @param source A sequence of values to project.
     * @param colProjector A transform function to apply to each element of the input sequence.
     * @param resultProjector A transform function to apply to each element of the intermediate sequence.
     * @param <TSource> The type of the elements of source.
     * @param <TSub> The type of the intermediate elements collected by colProjector.
     * @param <TResult> The type of the elements of the resulting sequence.
     * @return An {@link Iterable} whose elements are the result of invoking the one-to-many transform function
     * {@code collectionSelector} on each element of source and then mapping each of those sequence elements and
     * their corresponding {@code source} element to a result element.
     * @throws NullArgumentException Thrown when {@code src}, {@code colProjector} or {@code resultProjector} is null.
     */
    public static <TSource, TSub, TResult> Iterable<TResult> projectMany(Iterable<TSource> source,
                                                                         Function<TSource, Iterable<TSub>> colProjector,
                                                                         BiFunction<TSource, TSub, TResult> resultProjector){
        if(source == null)
            throw new NullArgumentException("source");
        if(colProjector == null)
            throw new NullArgumentException("projector");
        if(resultProjector == null)
            throw new NullArgumentException("result projector");
        return projectManyImp(source, colProjector, resultProjector);
    }

    private static <TSource, TSub, TResult> Iterable<TResult> projectManyImp(Iterable<TSource> source,
                                                                             Function<TSource, Iterable<TSub>> colProjector,
                                                                             BiFunction<TSource, TSub, TResult> resultProjector){
        return (Yield<TResult>) yield -> {
            for(TSource item : source)
                for(TSub subItem : colProjector.apply(item))
                    yield.returning(resultProjector.apply(item, subItem));
        };
    }

    /**
     * Projects each element of a sequence to an {@link Iterable}, flattens the resulting sequences into one sequence,
     * and invokes a result selector function on each element therein.
     * The index of each source element is used in the intermediate projected form of that element.
     * @param src A sequence of values to project.
     * @param colProjector A transform function to apply to each source element;
     *                     the second parameter of the function represents the index of the source element.
     * @param resultProjector A transform function to apply to each element of the intermediate sequence.
     * @param <TSource> The type of the elements of source.
     * @param <TSub> The type of the intermediate elements collected by colProjector.
     * @param <TResult> The type of the elements of the resulting sequence.
     * @return An {@link Iterable} whose elements are the result of invoking the one-to-many transform function
     * {@code collectionSelector} on each element of source and then mapping each of those sequence elements and
     * their corresponding {@code source} element to a result element.
     * @throws NullArgumentException Thrown when {@code src}, {@code colProjector} or {@code resultProjector} is null.
     */
    public static <TSource, TSub, TResult> Iterable<TResult> projectMany(Iterable<TSource> src,
                                                                         BiFunction<TSource, Integer, Iterable<TSub>> colProjector,
                                                                         BiFunction<TSource, TSub, TResult> resultProjector){
        if(src == null)
            throw new NullArgumentException("source");
        if(colProjector == null)
            throw new NullArgumentException("colProjector");
        if(resultProjector == null)
            throw new NullArgumentException("result colProjector");
        return projectManyImp(src, colProjector, resultProjector);
    }

    private static <TResult, TSub, TSource> Iterable<TResult> projectManyImp(Iterable<TSource> src,
                                                                             BiFunction<TSource, Integer, Iterable<TSub>> colProjector,
                                                                             BiFunction<TSource, TSub, TResult> resultProjector) {
        return (Yield<TResult>) yield -> {
            int i = 0;
            for(TSource item : src)
                for(TSub colItem : colProjector.apply(item, i++))
                    yield.returning(resultProjector.apply(item, colItem));
        };
    }

    /**
     * Projects each element of a sequence to an {@link Iterable} and flattens the resulting sequences into one sequence.
     * @param src A sequence of values to project.
     * @param projector A transform function to apply to each element.
     * @param <TSource> The type of the elements of source.
     * @param <TResult> The type of the elements of the sequence returned by selector.
     * @return An {@link Iterable} whose elements are the result of invoking the one-to-many
     * transform function on each element of the input sequence.
     * @throws NullArgumentException Thrown when {@code src} or {@code projector} is null.
     */
    public static <TSource, TResult> Iterable<TResult> projectMany(Iterable<TSource> src,
                                                                   Function<TSource, Iterable<TResult>> projector){
        if(src == null)
            throw new NullArgumentException("src");
        if(projector == null)
            throw new NullArgumentException("projector");
        return projectManyImp(src, projector);
    }

    private static <TResult, TSource> Iterable<TResult> projectManyImp(Iterable<TSource> src,
                                                                       Function<TSource, Iterable<TResult>> projector) {
        return (Yield<TResult>) yield -> {
          for(TSource item : src)
              for(TResult result : projector.apply(item))
                  yield.returning(result);
        };
    }

    /**
     * Projects each element of a sequence to an {@link Iterable} and flattens the resulting sequences into one sequence.
     * The index of each source element is used in the projected form of that element.
     * @param src A sequence of values to project.
     * @param projector A transform function to apply to each source element; the second parameter of the function represents the index of the source element.
     * @param <TSource> The type of the elements of source.
     * @param <TResult> The type of the elements of the sequence returned by selector.
     * @return An {@link Iterable} whose elements are the result of invoking the one-to-many
     * transform function on each element of the input sequence.
     * @throws NullArgumentException Thrown when {@code src} or {@code projector} is null.
     */
    public static <TSource, TResult> Iterable<TResult> projectMany(Iterable<TSource> src,
                                                                   BiFunction<TSource, Integer, Iterable<TResult>> projector){
        if(src == null)
            throw new NullArgumentException("src");
        if(projector == null)
            throw new NullArgumentException("projector");
        return projectManyImp(src, projector);
    }

    private static <TResult, TSource> Iterable<TResult> projectManyImp(Iterable<TSource> src, BiFunction<TSource, Integer, Iterable<TResult>> projector) {
        return (Yield<TResult>) yield -> {
          int i = 0;
          for(TSource item : src)
              for(TResult result : projector.apply(item, i++))
                  yield.returning(result);
        };
    }

    // ----------------------------- Any -----------------------------

    /**
     * Determines whether a sequence contains any elements.
     * @param src The {@link Iterable} to check for emptiness.
     * @param <T> The type of the elements of source.
     * @return {@code true} if the source sequence contains any elements; otherwise, {@code false}.
     */
    public static <T> boolean any(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try(ClosableIterator<T> iterator = (ClosableIterator<T>) src.iterator()){
            return iterator.hasNext();
        }
    }

    /**
     * Determines whether any element of a sequence satisfies a condition.
     * @param src An {@link Iterable} whose elements to apply the predicate to.
     * @param predicate A function to test each element for a condition.
     * @param <T> The type of the elements of source.
     * @return {@code true} if any elements in the source sequence pass the test in the specified predicate; otherwise, {@code false}.
     */
    public static <T> boolean any(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        for(T item : src)
            if(predicate.test(item))
                return true;
        return false;
    }

    // ----------------------------- All -----------------------------

    /**
     * Determines whether all elements of a sequence satisfy a condition.
     * @param src An {@link Iterable} that contains the elements to apply the predicate to.
     * @param predicate A function to test each element for a condition.
     * @param <T> The type of the elements of source.
     * @return {@code true} if every element of the source sequence passes the test in the specified predicate, or if the sequence is empty; otherwise, {@code false}.
     */
    public static <T> boolean all(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        for(T item : src)
            if(!predicate.test(item))
                return false;
        return true;
    }

    // ----------------------------- First -----------------------------

    public static <T> T first(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try(ClosableIterator<T> it = (ClosableIterator<T>)src.iterator()){
            if(it.hasNext())
                return it.next();
            throw new InvalidOperationException("Empty sequence");
        }
    }

    public static <T> T first(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        for(T item : src)
            if(predicate.test(item))
                return item;
        throw new InvalidOperationException("No items match the predicate");
    }

    // ----------------------------- FirstOrDefault -----------------------------

    public static <T> T firstOrNull(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try(ClosableIterator<T> it = (ClosableIterator<T>)src.iterator()){
            return it.hasNext() ? it.next() : null;
        }
    }

    public static <T> T firstOrNull(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        for(T item : src)
            if(predicate.test(item))
                return item;
        return null;
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
