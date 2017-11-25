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
    private Enumerable(){} //prevent creating instances of this class

    //TODO Might create a one-argument constructor that takes an iterable and wraps it with Enumerable methods
    // Refactor Collection to Iterable. -- Check deferred execution
    // Think of adding where List.
    // T - Source/ Primary Param. T - Secondary Param. R - Result/ Tertiary Param.

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
    @SuppressWarnings("unchecked")
    public static <TResult> Iterable<TResult> empty(){
        return (Iterable<TResult>) EmptyIterable.EMPTY_ITERABLE;
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

    // ----------------------------- toMap - O(n) -----------------------------
    // Can make it so it can take an Iterable of Values and a List/ Iterable for the Indexes
    // Exhausts iterators

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


    // ----------------------------- GroupJoin -----------------------------
    // Ignores Null Keys

    public static <TOuter, TInner, TKey, TResult> Iterable<TResult> GroupJoin(Iterable<TOuter> outer, Iterable<TInner> inner,
                                                                              Function<TOuter, TKey> outerKeySelector,
                                                                              Function<TInner, TKey> innerKeySelector,
                                                                              BiFunction<TOuter, Iterable<TInner>, TResult> resultSelector){
        throw new InvalidOperationException("!Implementation");
    }

    /* --------------------Nested Classes---------------------- */

    private static class EmptyIterable<T> implements Iterable<T>, Iterator<T>{

        static final EmptyIterable<Object> EMPTY_ITERABLE = new EmptyIterable<>();

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            throw new NoSuchElementException();
        }

    }

    private static class DefaultEquality<T> implements ICompareEquality<T>{}
}
