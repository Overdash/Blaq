package playground;

import org.jetbrains.annotations.NotNull;
import playground.util.*;

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
    private Enumerable(){throw new AssertionError("BLAQ doesn't need instances!");} //prevent creating instances of this class

    // TODO Might create a class w/one-argument constructor that takes an iterable and wraps it with Enumerable methods
    // TODO NO NEED FOR CloseableIterator -- Java Iterators are just abstractions, only need to close is closing resource.
    // Refactor Collection to Iterable. -- Check deferred execution
    // Think of adding where List.
    // T - Source/ Primary Param. T - Secondary Param. R - Result/ Tertiary Param.

    // DE - If source contents is changed before iterating through the result, the result should reflect the changes.
    // ... should start iterating through input sequence when src.iterator().next() is called.

    // NOTE: Think of using Futures to achieve DE

    // ----------------------------- Where - O(n) -----------------------------

    /**
     * Filters a sequence based on a predicate.
     * @param source An {@code Iterable} to be filtered.
     * @param predicate Rule to apply on each element.
     * @param <T> Type of elements.
     * @return Output sequence, with elements which conformed to the rule.
     *         Output Sequence is the same element type as input.
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
     * @param predicate Rule to apply on each element;
     *                  the second parameter of the function represents the index of the source element.
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

    // ----------------------------- Select -----------------------------
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
    public static <T> Iterable<Integer> range(int start, int count){
        if(count < 0)
            throw new ArgumentOutOfRangeException("count");

        // Convert everything to long (avoid overflows)
        if((long)start + (long)count - 1L > Integer.MAX_VALUE)
            throw new ArgumentOutOfRangeException("count");

        return rangeImp(start, count);
    }

    private static <T> Yield<Integer> rangeImp(int start, int count){
        return yield -> {
          for (int i = 0; i < count; i++)
              yield.returning(start+i);
        };
    }

    // ----------------------------- Empty. Caches (hence special class) -----------------------------
    //TODO: Empty -- Static generic fields are illegal in java (From JavaDoc: We cannot declare static fields whose types are type parameters)
    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> empty(){
        return (Iterable<T>) EmptyIterable.EMPTY_ITERABLE;
    }

    // ----------------------------- Repeat -----------------------------
    public static <T> Iterable<T> repeat(T e, int count){
        if(count < 0)
            throw new ArgumentOutOfRangeException("count");
        return repeatImp(e,count);
    }

    private static <T> Iterable<T> repeatImp(T e, int count) {
        return (Yield<T>) yield -> {
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
     * @throws ArithmeticException when count is over {@code Integer.MAX_VALUE}
     */
    public static <T> int count(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        if(src instanceof Collection){
            Collection<T> c = (Collection<T>) src;
            return c.size();
        }

        int count = 0;
        for (T i : src) {
            if (count < Integer.MAX_VALUE) count++;
            else throw new ArithmeticException("Overflow.");
        }

        return count;
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

        if(src instanceof Collection){
            Collection<T> c = (Collection<T>) src;
            return c.size();
        }

        long count = 0;
        for(T i : src)
            if(count < Long.MAX_VALUE) count++;

        return count;
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

    // ----------------------------- ToList (IE) - O(n) -----------------------------
    public static <T> List<T> toList(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("source");
        List<T> result;

        // Optimise for when src is a Collection object
        if(src instanceof Collection){
            Collection<T> items = (Collection<T>)src;
            result = new ArrayList<>(items.size());
        }
        else result = new ArrayList<>();

        for(T item: src)
            result.add(item);
        return result;
    }

    // ----------------------------- addToCollection -----------------------------
    public static <T> void addToCollection(Collection<T> src, Iterable<? extends T> it){
        if(src == null)
            throw new NullArgumentException("source");
        if(src == it)
            throw new NullArgumentException("iterable");
        for(T item: it)
            src.add(item);
    }

    public static <T> void addToCollection(Collection<T> src, Iterator<? extends T> it){
        if(src == null)
            throw new NullArgumentException("source");
        if(src == it)
            throw new NullArgumentException("iterator");
        while(it.hasNext())
            src.add(it.next());
    }

    // ----------------------------- SelectMany - O(n^2) -> T T U -----------------------------
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
     *         {@code collectionSelector} on each element of source and then mapping each of those sequence elements and
     *         their corresponding {@code source} element to a result element.
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
     *         {@code colProjector} on each element of source and then mapping each of those sequence elements and
     *         their corresponding {@code source} element to a result element.
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
     *         transform function on each element of the input sequence.
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
     * @param projector A transform function to apply to each source element;
     *                  the second parameter of the function represents the index of the source element.
     * @param <TSource> The type of the elements of source.
     * @param <TResult> The type of the elements of the sequence returned by selector.
     * @return An {@link Iterable} whose elements are the result of invoking the one-to-many
     *         transform function on each element of the input sequence.
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
     * @return {@code true} if every element of the source sequence passes the test in the specified predicate,
     *         or if the sequence is empty; otherwise, {@code false}.
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

    /**
     * Returns the first element of a sequence.
     * @param src The {@link Iterable} to return the first element of.
     * @param <T> The type of the elements of source.
     * @return The first element in the specified sequence.
     * @throws NullArgumentException if src is null
     * @throws InvalidOperationException if source sequence is empty
     */
    public static <T> T first(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try(ClosableIterator<T> it = (ClosableIterator<T>)src.iterator()){
            if(it.hasNext())
                return it.next();
            throw new InvalidOperationException("Empty sequence");
        }
    }

    /**
     * Returns the first element in a sequence that satisfies a specified condition.
     * @param src An {@link Iterable} to return an element from.
     * @param predicate A function to test each element for a condition.
     * @param <T> The type of the elements of source.
     * @return The first element in the sequence that passes the test in the specified predicate function.
     * @throws NullArgumentException if src is null
     * @throws InvalidOperationException if source sequence is empty
     */
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
    // (Rename firstOrNull makes more sense for java)

    /**
     * Returns the first element of a sequence, or a default value if the sequence contains no elements.
     * @param src The {@link Iterable} to return the first element of.
     * @param <T> The type of the elements of source.
     * @return {@code null} if source is empty; otherwise, the first element in source.
     * @throws NullArgumentException if src is null
     */
    public static <T> T firstOrNull(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try(ClosableIterator<T> it = (ClosableIterator<T>)src.iterator()){
            return it.hasNext() ? it.next() : null;
        }
    }

    /**
     * Returns the first element of the sequence that satisfies a condition or a default value if no such element is found.
     * @param src An {@link Iterable} to return an element from.
     * @param predicate A function to test each element for a condition.
     * @param <T> The type of the elements of source.
     * @return {@code null} if source is empty or if no element passes the test specified by {@code predicate};
     *         otherwise, the first element in source that passes the test specified by {@code predicate}.
     */
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

    // ----------------------------- Single -----------------------------

    public static <T> T single(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try(ClosableIterator<T> it = (ClosableIterator<T>) src.iterator()){
            if(!it.hasNext())
                throw new InvalidOperationException("Sequence is empty");
            T e = it.next();
            if(it.hasNext())
                throw new InvalidOperationException("Sequence has multiple elements");
            return e;
        }
    }

    public static <T> T single(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        T e = null;
        boolean found = false;
        for(T i : src){
            if(predicate.test(i)){
                if(found)
                    throw new InvalidOperationException("Sequence contains multiple matching elements");
                found = true;
                e = i;
            }
        }
        if(!found)
            throw new InvalidOperationException("Sequence has no matching elemetns");
        return e;
    }

    // ----------------------------- SingleOrDefault -----------------------------
    public static <T> T singleOrNull(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try(ClosableIterator<T> it = (ClosableIterator<T>)src.iterator()){
            if(!it.hasNext())
                return null;
            T e = it.next();
            if(it.hasNext())
                throw new InvalidOperationException("Sequence contained multiple elements");
            return e;
        }
    }

    public static <T> T singleOrNull(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        T e = null;
        boolean found = false;
        for(T i : src){
            if(predicate.test(i)) {
                if (found)
                    throw new InvalidOperationException("Sequence contained multiple element");
                found = true;
                e = i;
            }
        }
        return e;
    }

    // ----------------------------- Last -----------------------------
    // Difficult. Should be O(1) but the toList call makes it O(n). Java lacks indexers so must work around.

    /**
     * Returns the last element of a sequence.
     * @param src An {@link Iterable} to return the last element of.
     * @param <T> The type of the elements of source.
     * @return The value at the last position in the source sequence.
     * @throws NullArgumentException if src is null.
     * @throws InvalidOperationException if sequence is empty.
     */
    public static <T> T last(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src");
        try{
            List<T> c = (List<T>)src;
            if(c.size() == 0)
                throw new InvalidOperationException("Sequence is empty");
            return c.get(c.size() - 1);
        } catch (ClassCastException ignored){}
        try(ClosableIterator<T> it = (ClosableIterator<T>)src.iterator()){
            if(!it.hasNext())
               throw new  InvalidOperationException("Sequence is empty");
            T last = it.next();
            while(it.hasNext())
                last = it.next();
            return last;
        }
    }

    /**
     * Returns the last element of a sequence that satisfies a specified condition.
     * @param src An {@link Iterable} to return an element from.
     * @param predicate A function to test each element for a condition.
     * @param <T> The type of the elements of source.
     * @return The last element in the sequence that passes the test in the specified predicate function.
     * @throws NullArgumentException if {@code src} or {@code predicate} is empty.
     * @throws InvalidOperationException No element satisfies the condition in {@code predicate}.
     */
    public static <T> T last(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        boolean found = false;
        T last = null;
        for(T item : src){
            if(predicate.test(item)) {
                found = true;
                last = item;
            }
        }
        if(!found)
            throw new InvalidOperationException("No items matched the predicate");
        return last;
    }

    // ----------------------------- LastOrDefault -----------------------------
    public static <T> T lastOrDefault(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("src") ;
        try{
            List<T> c = (List<T>)src;
            return c.size() == 0 ? null : c.get(c.size() - 1);
        } catch (ClassCastException ignored){}
        T last = null;
        for(T i : src)
            last = i;
        return last;
    }

    public static <T> T lastOrDefault(Iterable<T> src, Predicate<T> predicate){
        if(src == null)
            throw new NullArgumentException("src");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        T last = null;
        for(T i : src)
            if(predicate.test(i))
                last = i;
        return last;
    }

    // ----------------------------- DefaultIfEmpty -----------------------------
    // Default of Object types is always null in java.

    public static <T> Iterable<T> defaultIfEmpty(Iterable<T> src){
        return defaultIfEmpty(src, null);
    }

    public static <T> Iterable<T> defaultIfEmpty(Iterable<T> src, T defaultVal){
        if(src == null)
            throw new NullArgumentException("src");
        
        return defaultIfEmptyImp(src, defaultVal);
    }

    private static <T> Iterable<T> defaultIfEmptyImp(Iterable<T> src, T defaultVal) {
        return (Yield<T>) yield -> {
            boolean foundAny = false;
            for(T i : src){
              yield.returning(i);
              foundAny = true;
          }
          if(!foundAny)
              yield.returning(defaultVal);
        };
    }

    // ----------------------------- Aggregate (IE)-----------------------------
    public static <T> T aggregate(Iterable<T> src, BiFunction<T, T, T> function){
        if(src == null)
            throw new NullArgumentException("src");
        if(function == null)
            throw new NullArgumentException("function");

        try(ClosableIterator<T> it = (ClosableIterator<T>) src.iterator()){
            if(!it.hasNext())
                throw new InvalidOperationException("Source sequence was empty");
            T current = it.next();
            while(it.hasNext())
                current = function.apply(current, it.next());
            return current;
        }
    }

    public static <T, S> S aggregate(Iterable<T> src, S seed, BiFunction<S, T, S> function){
        return aggregate(src, seed, function, x -> x);
    }

    public static <T, S, R> R aggregate(Iterable<T> src, S seed, BiFunction<S, T, S> function,
                                        Function<S, R> resultProjector){
        if(src == null)
            throw new NullArgumentException("src");
        if(function == null)
            throw new NullArgumentException("function");
        if(resultProjector == null)
            throw new NullArgumentException("resultProjector");

        S current = seed;
        for(T item : src)
            current = function.apply(current, item);
        return resultProjector.apply(current);
    }

    // ~~~~~~~~~ SET Operators! ~~~~~~~~~
    // NOTE: Seems there are some minor draw-backs in efficiency by using Comparator rather than IEqualityComparer.
    // https://stackoverflow.com/questions/7751170/why-we-need-the-iequalitycomparer-iequalitycomparert-interface
    // TODO: Re-implement these after creating HashSet wrapper

    // ----------------------------- Distinct (DE) ----------------------------- Set-based
    // Need to create IEqualityComparer (ICompareEquality) -- Nothing like it exists in java.

    public static <T> Iterable<T> distinct(Iterable<T> src){
        return distinct(src, null);
    }

    public static <T> Iterable<T> distinct(Iterable<T> src, ICompareEquality<T> compareEquality){
        if(src == null)
            throw new NullArgumentException("src");
        return distinctImp(src, compareEquality != null ? compareEquality : new DefaultEquality<>());
    }

    private static <T> Iterable<T> distinctImp(Iterable<T> src, ICompareEquality<T> compareEquality) {
        return (Yield<T>) yield -> {
            HashSet<T> passedElements = new HashSet<>(/*compareEquality*/);
            for(T item : src)
                if(passedElements.add(item))
                    yield.returning(item);
        };
    }

    // ----------------------------- Union (DE) ----------------------------- Set-based

    public static <T> Iterable<T> union(Iterable<T> first, Iterable<T> second){
        return union(first,second, null);
    }

    public static <T> Iterable<T> union(Iterable<T> first, Iterable<T> second, ICompareEquality<T> compareEquality) {
        // Could implement using Concat & Distinct, but let's not couple the methods.
        if(first == null)
            throw new NullArgumentException("first");
        if(second == null)
            throw new NullArgumentException("second");
        return unionImp(first, second, compareEquality != null? compareEquality : new DefaultEquality<>());
    }

    private static <T> Iterable<T> unionImp(Iterable<T> first, Iterable<T> second, ICompareEquality<T> compareEquality) {
        return (Yield<T>) y -> {
            HashSet<T> passedElements = new HashSet<>(/*compareEquality*/);
            for(T item : first)
                if(passedElements.add(item))
                    y.returning(item);
            for(T item : second)
                if(passedElements.add(item))
                    y.returning(item);
        };
    }

    // ----------------------------- Intersect (DE) ----------------------------- Set-based

    public static <T> Iterable<T> intersect(Iterable<T> first, Iterable<T> second){
        return intersect(first, second, null);
    }

    private static <T> Iterable<T> intersect(Iterable<T> first, Iterable<T> second, ICompareEquality<T> compareEquality) {
        if(first == null)
            throw new NullArgumentException("first");
        if(second == null)
            throw new NullArgumentException("second");
        return intersectImp(first, second, compareEquality != null ? compareEquality : new DefaultEquality<>());
    }

    // NOTE: For use, best to Dev use the longer list as the first and shorter as second (to maximise performance).
    private static <T> Iterable<T> intersectImp(Iterable<T> first, Iterable<T> second, ICompareEquality<T> compareEquality) {
//        Collection<T> conduit = toList(second);
        return (Yield<T>) y -> {
          HashSet<T> potentialItems = new HashSet<>(/*, compareEquality*/);
          addToCollection(potentialItems, second);
          for(T item : first)
              if(potentialItems.remove(item))
                  y.returning(item);
        };
    }

    // ----------------------------- Except (DE) -----------------------------

    public static <T> Iterable<T> except(Iterable<T> first, Iterable<T> second){
        return except(first, second, null);
    }

    public static <T> Iterable<T> except(Iterable<T> first, Iterable<T> second, ICompareEquality<T> compareEquality){
        if(first == null)
            throw new NullArgumentException("first");
        if(second == null)
            throw new NullArgumentException("second");
        return exceptImp(first, second, compareEquality != null ? compareEquality : new DefaultEquality<>());
    }

    private static <T> Iterable<T> exceptImp(Iterable<T> first, Iterable<T> second, ICompareEquality<T> compareEquality) {
        return (Yield<T>) yield -> {
            HashSet<T> excludedElements = new HashSet<>(/*compareEquality*/);
            addToCollection(excludedElements, second);
            for(T item : first)
                if(excludedElements.add(item))
                    yield.returning(item);
        };
    }

    // ----------------------------- ToLookup (IE)-----------------------------
    // Think about thread safety

    public static <T, K> ILookup<K, T> toLookup(Iterable<T> source, Function<T, K> keySelector){
        return toLookup(source, keySelector, new DefaultEquality<>());
    }

    public static <T, K> ILookup<K, T> toLookup(Iterable<T> src, Function<T, K> keySelector,
                                                ICompareEquality<K> compareEquality){
        return toLookup(src, keySelector, e -> e, compareEquality);
    }

    public static <T, K, V>  ILookup<K, V> toLookup(Iterable<T> source, Function<T, K> keySelector,
                                                    Function<T, V> valueSelector){
        return toLookup(source, keySelector, valueSelector, new DefaultEquality<>());
    }

    public static <T, K, V>  ILookup<K, V> toLookup(Iterable<T> source, Function<T, K> keySelector,
                                                    Function<T, V> valueSelector, ICompareEquality<K> compareEquality){
        if(source == null)
            throw new NullArgumentException("source");
        if(keySelector == null)
            throw new NullArgumentException("keySelector");
        if(valueSelector == null)
            throw new NullArgumentException("valueSelector");

        Lookup<K, V> lookup = new Lookup<>(compareEquality != null ? compareEquality : new DefaultEquality<>());
        for(T item : source){
            K key = keySelector.apply(item);
            V value = valueSelector.apply(item);
            lookup.add(key, value);
        }
        return lookup;
    }

    private static <T, K> ILookup<K, T> noNullLookup(Iterable<T> src, Function<T, K> keySelector,
                                                ICompareEquality<K> compareEquality){
        Lookup<K, T> lookup = new Lookup<>(compareEquality != null ? compareEquality : new DefaultEquality<>());
        for(T item : src){
            K key = keySelector.apply(item);
            if(key != null)
                lookup.add(key, item);
        }

        return lookup;
    }

    // ----------------------------- Join (DE) -----------------------------
    // Ignores Null keys

    public static <TOuter, TInner, TKey, TResult> Iterable<TResult> join(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                         Function<TOuter, TKey> outerKeySelector,
                                                                         Function<TInner, TKey> innerKeySelector,
                                                                         BiFunction<TOuter, TInner, TResult> resultSelector){
        return join(outer, inner, outerKeySelector, innerKeySelector, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> Iterable<TResult> join(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                         Function<TOuter, TKey> outerKeySelector,
                                                                         Function<TInner, TKey> innerKeySelector,
                                                                         BiFunction<TOuter, TInner, TResult> resultSelector,
                                                                         ICompareEquality<TKey> compareEquality){
        if(outer == null)
            throw new NullArgumentException("outer");
        if(inner == null)
            throw new NullArgumentException("inner");
        if(outerKeySelector == null)
            throw new NullArgumentException("outer key selector");
        if(innerKeySelector == null)
            throw new NullArgumentException("inner key selector");
        if(resultSelector == null)
            throw new NullArgumentException("result selector");

        return joinImp(outer, inner, outerKeySelector, innerKeySelector, resultSelector, compareEquality);
    }

    private static <TResult, TOuter, TInner, TKey> Iterable<TResult> joinImp(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                             Function<TOuter, TKey> outerKeySelector,
                                                                             Function<TInner, TKey> innerKeySelector,
                                                                             BiFunction<TOuter, TInner, TResult> resultSelector,
                                                                             ICompareEquality<TKey> compareEquality) {
        ILookup<TKey, TInner> lookup = noNullLookup(inner, innerKeySelector, compareEquality);
        return (Yield<TResult>) yield -> {
            for(TOuter outerItem : outer){
                TKey key = outerKeySelector.apply(outerItem);
                for(TInner innerItem : lookup.getItem(key))
                    yield.returning(resultSelector.apply(outerItem, innerItem));
            }
        };
    }

    // ----------------------------- GroupBy (DE... Semi-DE) -----------------------------
    // Logically Function<K, Iterable<V>, S> == Function<IGrouping<K, V>, S>

    public static <T, K> Iterable<IGrouping<K, T>> groupBy(Iterable<T> src,
                                                           Function<T, K> keyFunction){
        return groupBy(src, keyFunction, x -> x, new DefaultEquality<>());
    }

    public static <T, K> Iterable<IGrouping<K, T>> groupBy(Iterable<T> src,
                                                           Function<T, K> keyFunction,
                                                           ICompareEquality<K> compareEquality){
        return groupBy(src, keyFunction, x -> x, compareEquality);
    }

    public static <T, K, V> Iterable<IGrouping<K, V>> groupBy(Iterable<T> src,
                                                              Function<T, K> keyFunction,
                                                              Function<T, V> elementFunction){
        return groupBy(src, keyFunction, elementFunction, new DefaultEquality<>());
    }

    public static <T, K, V> Iterable<IGrouping<K, V>> groupBy(Iterable<T> src,
                                                              Function<T, K> keyFunction,
                                                              Function<T, V> elementFunction,
                                                              ICompareEquality<K> compareEquality){
        if(src == null)
            throw new NullArgumentException("source");
        if(keyFunction == null)
            throw new NullArgumentException("keyFunction");
        if(elementFunction == null)
            throw new NullArgumentException("elementFunction");

        return groupByImp(src, keyFunction, elementFunction, compareEquality);
    }

    private static <T, K, V> Iterable<IGrouping<K, V>> groupByImp(Iterable<T> src,
                                                                  Function<T, K> keyFunction,
                                                                  Function<T, V> elementFunction,
                                                                  ICompareEquality<K> compareEquality){
        ILookup<K, V> lookup = toLookup(src, keyFunction, elementFunction, compareEquality);
        return (Yield<IGrouping<K, V>>) yield -> {
            for(IGrouping<K, V> result : lookup)
                yield.returning(result);
        };
    }

    public static <T, K, S> Iterable<S> groupBy(Iterable<T> src,
                                                Function<T, K> keyFunction,
                                                BiFunction<K, Iterable<T>, S> resultFunction){
        return groupBy(src, keyFunction, x -> x, resultFunction, new DefaultEquality<>());
    }

    public static <T, K, S> Iterable<S> groupBy(Iterable<T> src,
                                                Function<T, K> keyFunction,
                                                BiFunction<K, Iterable<T>, S> resultFunction,
                                                ICompareEquality<K> compareEquality){
        return groupBy(src, keyFunction, x -> x, resultFunction, compareEquality);
    }

    public static <T, K, V, S> Iterable<S> groupBy(Iterable<T> src,
                                                   Function<T, K> keyFunction,
                                                   Function<T, V> elementFunction,
                                                   BiFunction<K, Iterable<V>, S> resultFunction){
        return groupBy(src, keyFunction, elementFunction, resultFunction, new DefaultEquality<>());
    }

    public static <T, K, V, S> Iterable<S> groupBy(Iterable<T> src,
                                                   Function<T, K> keyFunction,
                                                   Function<T, V> elementFunction,
                                                   BiFunction<K, Iterable<V>, S> resultFunction,
                                                   ICompareEquality<K> compareEquality){
        if(resultFunction == null)
            throw new NullArgumentException("resultFunction");

        return project(
                groupBy(src, keyFunction, elementFunction, compareEquality),
                group -> resultFunction.apply(group.getKey(), group));
    }

    // ----------------------------- GroupJoin (DE) -----------------------------
    // Ignores Null Keys
    // When iterating through result, 'inner' sequence is immediately read all the way through

    public static <TOuter, TInner, TKey, TResult> Iterable<TResult> groupJoin(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                              Function<TOuter, TKey> outerKeySelector,
                                                                              Function<TInner, TKey> innerKeySelector,
                                                                              BiFunction<TOuter, Iterable<TInner>, TResult> resultSelector){
        return groupJoin(outer, inner, outerKeySelector, innerKeySelector, resultSelector, new DefaultEquality<>());
    }

    public static <TOuter, TInner, TKey, TResult> Iterable<TResult> groupJoin(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                              Function<TOuter, TKey> outerKeySelector,
                                                                              Function<TInner, TKey> innerKeySelector,
                                                                              BiFunction<TOuter, Iterable<TInner>, TResult> resultSelector,
                                                                              ICompareEquality<TKey> compareEquality){
        if(outer == null)
            throw new NullArgumentException("outer");
        if(inner == null)
            throw new NullArgumentException("inner");
        if(outerKeySelector == null)
            throw new NullArgumentException("outerKeySelector");
        if(innerKeySelector == null)
            throw new NullArgumentException("innerKeySelector");
        if(resultSelector == null)
            throw new NullArgumentException("resultSelector");

        ILookup<TKey, TInner> lookup = toLookup(inner, innerKeySelector, compareEquality);
        return (Yield<TResult>) yield -> {
            for(TOuter outerVal : outer){
                TKey key = outerKeySelector.apply(outerVal);
                yield.returning(resultSelector.apply(outerVal, lookup.getItem(key)));
            }
        };
    }

    // ----------------------------- Take (DE) -----------------------------
    // Might need CloseableIterator because Yield is a resource (due to threads)

    public static <T> Iterable<T> take(Iterable<T> src, int count){
        if (src == null)
            throw new NullArgumentException("source");
        return takeImp(src, count);
    }

    private static <T> Iterable<T> takeImp(Iterable<T> src, int count) {
        return (Yield<T>) yield -> {
            Iterator<T> it = src.iterator();
            for(int i = 0; i < count && it.hasNext(); i++)
                yield.returning(it.next());
        };
    }

    // ----------------------------- TakeWhile (DE) -----------------------------

    public static <T> Iterable<T> takeWhile(Iterable<T> src, Function<T, Boolean> predicate){
        if (src == null)
            throw new NullArgumentException("source");
        if (predicate == null)
            throw new NullArgumentException("predicate");
        return takeWhileImp(src, predicate);
    }

    private static <T> Iterable<T> takeWhileImp(Iterable<T> src, Function<T, Boolean> predicate) {
        return (Yield<T>) yield ->{
          for (T item : src){
              if(!predicate.apply(item))
                  yield.breaking();

              yield.returning(item);
          }
        };
    }

    public static <T> Iterable<T> takeWhile(Iterable<T> src, BiFunction<T, Integer, Boolean> predicate){
        if(src == null)
            throw new NullArgumentException("source");
        if (predicate == null)
            throw new NullArgumentException("predicate");
        return takeWhileImp(src, predicate);
    }

    private static <T> Iterable<T> takeWhileImp(Iterable<T> src, BiFunction<T, Integer, Boolean> predicate) {
        return (Yield<T>) yield -> {
          int i = 0;
          for(T item : src){
              if(!predicate.apply(item, i))
                  yield.breaking();

              i++;
              yield.returning(item);
          }
        };
    }

    // ----------------------------- Skip (DE) -----------------------------

    public static <T> Iterable<T> skip(Iterable<T> src, int count){
        if (src == null)
            throw new NullArgumentException("source");
        if(count <= 0)
            return src;
        return skipImp(src, count);
    }

    private static <T> Iterable<T> skipImp(Iterable<T> src, int count) {
        return (Yield<T>) yield -> {
            Iterator<T> it = src.iterator();
            for(int i = 0; i < count; i++)
                if(!it.hasNext())
                    yield.breaking();

            while(it.hasNext())
                yield.returning(it.next());
        };
    }

    // ----------------------------- SkipWhile (DE) -----------------------------

    public static <T> Iterable<T> skipWhile(Iterable<T> src, Function<T, Boolean> predicate){
        if(src == null)
            throw new NullArgumentException("source");
        if (predicate == null)
            throw new NullArgumentException("predicate");

        return skipWhileImp(src, predicate);
    }

    private static <T> Iterable<T> skipWhileImp(Iterable<T> src, Function<T, Boolean> predicate) {
        return (Yield<T>) yield -> {
            Iterator<T> it = src.iterator();
            while(it.hasNext()){
              T item = it.next();
              if(!predicate.apply(item)){
                  // Stop skipping & yield the current item
                  yield.returning(item);
                  break;
              }
            }

            while(it.hasNext())
              yield.returning(it.next());
        };
    }

    public static <T> Iterable<T> skipWhile(Iterable<T> src, BiFunction<T, Integer, Boolean> predicate){
        if(src == null)
            throw new NullArgumentException("source");
        if (predicate == null)
            throw new NullArgumentException("predicate");

        return skipWhileImp(src, predicate);
    }

    private static <T> Iterable<T> skipWhileImp(Iterable<T> src, BiFunction<T, Integer, Boolean> predicate) {
        return (Yield<T>) yield -> {
            Iterator<T> it = src.iterator();
            int i = 0;
            while(it.hasNext()){
                T item = it.next();
                if(!predicate.apply(item, i)){
                    // Stop skipping & yield current item.
                    yield.returning(item);
                    break;
                }
                i++;
            }

            while(it.hasNext())
                yield.returning(it.next());
        };
    }

    // ----------------------------- ToArray (IE) -----------------------------
    // Heavily check this for type issues.

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterable<T> src){
        if (src == null)
            throw new NullArgumentException("src");
        Collection<T> collection;
        if(src instanceof Collection){
            collection = (Collection<T>) src;
            return (T[])collection.toArray();
        }
        collection = new ArrayList<>();
        addToCollection(collection, src);
        return (T[])collection.toArray();
    }

    // ----------------------------- ToDictionary (IE) -----------------------------
    /*
    * The main difference between ToDictionary and ToLookup is that a dictionary can
    * only store a single value per key, rather than a whole sequence of values. Of course,
    * another difference is that Dictionary<TKey, TValue> (Map<K, V>) is mutable concrete class,
    * whereas ILookup<TKey, TElement> is an interface usually implemented in an
    * immutable fashion.
    *
    * This Java variant allows ONE Null Key.
    * */

    public static <T, K> Map<K, T> toMap(Iterable<T> src, Function<T, K> keySelector){
        return toMap(src, keySelector, x -> x, new DefaultEquality<>());
    }

    public static <T, K, V> Map<K, V> toMap(Iterable<T> src, Function<T, K> keySelector, Function<T, V> valueSelector){
        return toMap(src, keySelector, valueSelector, new DefaultEquality<>());
    }

    public static <T, K> Map<K, T> toMap(Iterable<T> src, Function<T, K> keySelector, ICompareEquality<K> compareEquality){
        return toMap(src, keySelector, x -> x, compareEquality);
    }

    public static <T, K, V> Map<K, V> toMap(Iterable<T> src, Function<T, K> keySelector,
                                            Function<T, V> valueSelector, ICompareEquality<K> compareEquality){
        if(src == null)
            throw new NullArgumentException("source");
        if(keySelector == null)
            throw new NullArgumentException("key selector");
        if(valueSelector == null)
            throw new NullArgumentException("value selector");
        /*compareEquality != null ? compareEquality : new DefaultEquality*/

        Map<K, V> ret = src instanceof Collection ? new HashMap<>(((Collection) src).size()) : new HashMap<>();
        for(T item : src)
            ret.put(keySelector.apply(item), valueSelector.apply(item));

        return ret;
    }

    // ----------------------------- OrderBy (DE) -----------------------------

    @SuppressWarnings("unchecked")
    public static <T, K> IOrderedIterable<T> orderBy(Iterable<T> src, Function<T, K> keySelector){
        return orderBy(src, keySelector, (Comparator<K>) Comparator.naturalOrder());
    }

    @SuppressWarnings("unchecked")
    public static <T, K> IOrderedIterable<T> orderBy(Iterable<T> src, Function<T, K> keySelector, Comparator<K> comparator){
        if (src == null)
            throw new NullArgumentException("source");
        if(keySelector == null)
            throw new NullArgumentException("key selector");

        return new OrderedIterable<>(src, keySelector, comparator != null ? comparator : (Comparator<K>) Comparator.naturalOrder());
    }

    // ----------------------------- OrderByDescending (DE) -----------------------------

    @SuppressWarnings("unchecked")
    public static <T, K> IOrderedIterable<T> orderByDescending(Iterable<T> src, Function<T, K> keySelector){
        return orderByDescending(src, keySelector, (Comparator<K>) Comparator.naturalOrder());
    }

    @SuppressWarnings("unchecked")
    public static <T, K> IOrderedIterable<T> orderByDescending(Iterable<T> src, Function<T, K> keySelector,
                                                               Comparator<K> comparator){
        if (src == null)
            throw new NullArgumentException("source");
        if(keySelector == null)
            throw new NullArgumentException("key selector");
        return new OrderedIterable<>(src, keySelector, comparator != null ? comparator.reversed()
                : (Comparator<K>) Comparator.reverseOrder());
    }

    // ----------------------------- ThenBy (DE) -----------------------------

    @SuppressWarnings("unchecked")
    public static <T, K> IOrderedIterable<T> thenBy(IOrderedIterable<T> src, Function<T, K> keySelector){
        return thenBy(src, keySelector, (Comparator<K>) Comparator.naturalOrder());
    }

    public static <T, K> IOrderedIterable<T> thenBy(IOrderedIterable<T> src, Function<T, K> keySelector,
                                                    Comparator<K> comparator){
        if (src == null)
            throw new NullArgumentException("source");
        if(keySelector == null)
            throw new NullArgumentException("key selector");
        return src.createOrderedIterable(keySelector, comparator, false);
    }

    // ----------------------------- ThenByDescending (DE) -----------------------------

    @SuppressWarnings("unchecked")
    public static <T, K> IOrderedIterable<T> thenByDescending(IOrderedIterable<T> src, Function<T, K> keySelector){
        return thenByDescending(src, keySelector, (Comparator<K>) Comparator.naturalOrder());
    }

    public static <T, K> IOrderedIterable<T> thenByDescending(IOrderedIterable<T> src, Function<T, K> keySelector,
                                                             Comparator<K> comparator){
        if (src == null)
            throw new NullArgumentException("source");
        if(keySelector == null)
            throw new NullArgumentException("key selector");
        return src.createOrderedIterable(keySelector, comparator, true);
    }

    // ----------------------------- Reverse (IE) -----------------------------

    public static <T> Iterable<T> reverse(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("source");
        return reverseImp(src);
    }

    private static <T> Iterable<T> reverseImp(Iterable<T> src) {
        Stack<T> stack = new Stack<>();
        for(T item : src)
            stack.push(item);

        return (Yield<T>) yield -> {
            for(T item : stack)
                yield.returning(item);
        };
    }

    // ----------------------------- Sum (IE) -----------------------------
    public static int sum(Iterable<Integer> src){
        return sum(src, x -> x);
    }

    // TODO: numerical type which can be nullable or non-nullable
    //public static Supplier<Integer> sum(Iterable<Supplier<Integer>> src)

    public static <T> int sum(Iterable<T> src, Function<T, Integer> selector){
        if(src == null)
            throw new NullArgumentException("source");
        if(selector == null)
            throw new NullArgumentException("selector");

        int sum = 0;
        for(T item : src){
            if(sum < Integer.MAX_VALUE)
                sum += selector.apply(item);
            else
                throw new ArithmeticException("Overflow exception.");
        }
        return sum;
    }

    public static long longSum(Iterable<Long> src){
        return longSum(src, x -> x);
    }

    public static <T> long longSum(Iterable<T> src, Function<T, Long> selector){
        if(src == null)
            throw new NullArgumentException("source");
        if(selector == null)
            throw new NullArgumentException("selector");

        long sum = 0;
        for(T item : src){
            if(sum < Long.MAX_VALUE)
                sum += selector.apply(item);
            else
                throw new ArithmeticException("Overflow exception.");
        }
        return sum;
    }

    public static double doubleSum(Iterable<Double> src){
        return doubleSum(src, x -> x);
    }

    public static <T> double doubleSum(Iterable<T> src, Function<T, Double> selector){
        if(src == null)
            throw new NullArgumentException("source");
        if(selector == null)
            throw new NullArgumentException("selector");

        double sum = 0;
        for(T item : src)
            sum += selector.apply(item);
        return sum;
    }

    // TODO: Consider Kahan Summation algorithm. Aggregate Ops nightmare
    public static float floatSum(Iterable<Float> src){
        return floatSum(src, x -> x);
    }

    public static <T> float floatSum(Iterable<T> src, Function<T, Float> selector){
        if(src == null)
            throw new NullArgumentException("source");
        if(selector == null)
            throw new NullArgumentException("selector");

        float sum = 0;
        for(T item : src)
            sum += selector.apply(item);
        return sum;
    }

    // ----------------------------- Min (IE) -----------------------------
    // Normally there are 4 primitive overloads of Max/Min, however, due to Type Erasure
    // The Generic overload erases to the same
//    public static int min(Iterable<Integer> src){
//        return min(src, x -> x);
//    }
//
//    public static <T> int min(Iterable<T> src, Function<T, Integer> selector){
//
//    }

    // Consider handling null values
    public static <T> T min(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("Source");
        return minImp(src);
    }

    public static <T, S> S min(Iterable<T> src, Function<T, S> selector){
        return min(project(src, selector));
    }

    @SuppressWarnings("unchecked")
    private static <T> T minImp(Iterable<T> src){
        Iterator<T> it = src.iterator();
        if(!it.hasNext())
            throw new InvalidOperationException("Sequence was empty.");
        T min = it.next();
        while(it.hasNext()){
            T item = it.next();
            if(((Comparator<T>)Comparator.naturalOrder()).compare(min, item) > 0)
                min = item;
        }
        return min;
    }

    // ----------------------------- MinBy (IE) MoreLINQ -----------------------------

    public static <T, K> T minBy(Iterable<T> src, Function<T, K> selector){
        return minBy(src, selector, null);
    }

    @SuppressWarnings("unchecked")
    public static <T, K> T minBy(Iterable<T> src, Function<T, K> selector, Comparator<K> comparator){
        if(src == null)
            throw new NullArgumentException("Source");
        if(selector == null)
            throw new NullArgumentException("Selector");
        comparator = comparator != null ? comparator : (Comparator<K>)Comparator.naturalOrder();

        Iterator<T> it = src.iterator();
        if(!it.hasNext())
            throw new InvalidOperationException("Sequence contains no elements!");

        T min = it.next();
        K minKey = selector.apply(min);
        while(it.hasNext()){
            T candidate = it.next();
            K candidateKey = selector.apply(candidate);
            if(comparator.compare(candidateKey, minKey) < 0){
                min = candidate;
                minKey = candidateKey;
            }
        }
        if(it instanceof ClosableIterator) ((ClosableIterator) it).close();
        return min;
    }

    // ----------------------------- Max (IE) -----------------------------

    public static <T> T max(Iterable<T> src){
        if(src == null)
            throw new NullArgumentException("Source");
        return maxImp(src);
    }

    public static <T, S> S max(Iterable<T> src, Function<T, S> selector){
        return max(project(src, selector));
    }

    @SuppressWarnings("unchecked")
    private static <T> T maxImp(Iterable<T> src){
        Iterator<T> it = src.iterator();
        if(!it.hasNext())
            throw new InvalidOperationException("Sequence was empty.");
        T max = it.next();
        while(it.hasNext()){
            T item = it.next();
            if(((Comparator<T>)Comparator.naturalOrder()).compare(max, item) < 0)
                max = item;
        }
        return max;
    }

    // ----------------------------- MaxBy (IE) MoreLINQ -----------------------------

    public static <T, K> T maxBy(Iterable<T> src, Function<T, K> selector){
        return maxBy(src, selector, null);
    }

    @SuppressWarnings("unchecked")
    public static <T, K> T maxBy(Iterable<T> src, Function<T, K> selector, Comparator<K> comparator){
        if(src == null)
            throw new NullArgumentException("Source");
        if(selector == null)
            throw new NullArgumentException("Selector");
        comparator = comparator != null ? comparator : (Comparator<K>)Comparator.naturalOrder();

        Iterator<T> it = src.iterator();
        if(!it.hasNext())
            throw new InvalidOperationException("Sequence contains no elements!");

        T max = it.next();
        K maxKey = selector.apply(max);
        while(it.hasNext()){
            T candidate = it.next();
            K candidateKey = selector.apply(candidate);
            if(comparator.compare(candidateKey, maxKey) > 0){
                max = candidate;
                maxKey = candidateKey;
            }
        }
        if(it instanceof ClosableIterator) ((ClosableIterator) it).close();
        return max;
    }

    // ----------------------------- Average (IE) -----------------------------

    public static double average(Iterable<Number> src){
        if(src == null)
            throw new NullArgumentException("Source");

        Iterator<Number> it = src.iterator();

        if(!it.hasNext())
            throw new InvalidOperationException("Sequence contains no elements!");

        Number n = it.next();
        long count = 0;
        double total = 0;
        if(n instanceof Long || n instanceof Integer){
            for(Number item : src){
                total += item.longValue();
                count++;
            }
        } else if(n instanceof Double || n instanceof Float){
            for(Number item : src){
                total += item.doubleValue();
                count++;
            }
        } else {
            for(Number item : src){
                total += item.shortValue();
                count++;
            }
        }

        return total/ count;
    }

    // TODO: Better optimisation for primitives!
    public static <T> double average(Iterable<T> src, Function<T, Number> selector){
        if(selector == null)
            throw new NullArgumentException("Selector");

        return average(project(src, selector));
    }

    // work on Float version

    // ----------------------------- ElementAt (IE) -----------------------------


    // Consider using reflection to handle Argument validation messages. C# uses nameof()

    /* --------------------Nested Classes---------------------- */

    private static class EmptyIterable<T> implements Iterable<T>{

        static final EmptyIterable<Object> EMPTY_ITERABLE = new EmptyIterable<>();

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public void forEach(Consumer<? super T> action){ Objects.requireNonNull(action); }
    }

    private static class DefaultEquality<T> implements ICompareEquality<T>{}
}
