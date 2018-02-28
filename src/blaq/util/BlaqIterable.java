package blaq.util;

import org.jetbrains.annotations.NotNull;
import blaq.core.Enumerable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface used for simulating the same syntactical sugar of Extension Methods in C#, exclusively for the BLAQ Library.
 * <p>Programmers can use Data Structures implementing this interface in the following way:</p>
 * <p>{@code list.where(x -> x < 4).project(x -> x*x)}</p> Where {@code list} is a {@code BlaqList} of numbers.
 * @param <T>
 */
public interface BlaqIterable<T> extends Iterable<T> {

    /* Enumerable methods */
    default BlaqIterable<T> where(Predicate<T> p){
        // make it return a BlaqIterable so you have continuous calls, e.g. list.where(...).select(...);
        // investigate how LINQ/C# use collections.
        return new Blaqen<>(Enumerable.where(this, p));
    }

    default BlaqIterable<T> where(BiPredicate<T, Integer> p){
        return new Blaqen<>(Enumerable.where(this, p));
    }

    default <R> BlaqIterable<R> project(Function<T, R> projector){
        return new Blaqen<>(Enumerable.project(this, projector));
    }

    default <R> BlaqIterable<R> project(BiFunction<T, Integer, R> projector){
        return new Blaqen<>(Enumerable.project(this, projector));
    }

    // range only in Enumerable

    // empty only in Enumerable

    // repeat only in Enumerable

    default int count(){
        return Enumerable.count(this);
    }

    default int count(Predicate<T> p){
        return Enumerable.count(this, p);
    }

    default long longCount(){
        return Enumerable.longCount(this);
    }

    default long longCount(Predicate<T> p){
        return Enumerable.longCount(this, p);
    }

    default BlaqIterable<T> concat(Iterable<T> second){
        return new Blaqen<>(Enumerable.concat(this, second));
    }

    default List<T> toList(){
        return Enumerable.toList(this);
    }

    // addToCollection x2 only in Enumerable

    default <S, R> BlaqIterable<R> projectMany(Function<T, Iterable<S>> colProjector,
                                           BiFunction<T, S, R> resultProjector){
        return new Blaqen<>(Enumerable.projectMany(this, colProjector, resultProjector));
    }

    default <S, R> BlaqIterable<R> projectMany(BiFunction<T, Integer, Iterable<S>> colProjector,
                                           BiFunction<T, S, R> resultProject){
        return new Blaqen<>(Enumerable.projectMany(this, colProjector, resultProject));
    }

    default <R> BlaqIterable<R> projectMany(Function<T, Iterable<R>> projector){
        return new Blaqen<>(Enumerable.projectMany(this, projector));
    }

    default <R> BlaqIterable<R> projectMany(BiFunction<T, Integer, Iterable<R>> projector){
        return new Blaqen<>(Enumerable.projectMany(this, projector));
    }

    default boolean any(){
        return Enumerable.any(this);
    }

    default boolean any(Predicate<T> p){
        return Enumerable.any(this, p);
    }

    default boolean all(Predicate<T> p){
        return Enumerable.all(this, p);
    }

    default T first(){
        return Enumerable.first(this);
    }

    default T first(Predicate<T> p){
        return Enumerable.first(this, p);
    }

    default T firstOrNull(){
        return Enumerable.firstOrNull(this);
    }

    default T firstOrNull(Predicate<T> p){
        return Enumerable.firstOrNull(this, p);
    }

    default T single(){
        return Enumerable.single(this);
    }

    default T single(Predicate<T> p){
        return Enumerable.single(this, p);
    }

    default T singleOrNull(){
        return Enumerable.singleOrNull(this);
    }

    default T singleOrNull(Predicate<T> p){
        return Enumerable.singleOrNull(this, p);
    }

    default T last(){
        return Enumerable.last(this);
    }

    default T last(Predicate<T> p){
        return Enumerable.last(this, p);
    }

    default T lastOrDefault(){
        return Enumerable.lastOrDefault(this);
    }

    default T lastOrDefault(Predicate<T> p){
        return Enumerable.lastOrDefault(this, p);
    }

    default BlaqIterable<T> defaultIfEmpty(){
        return new Blaqen<>(Enumerable.defaultIfEmpty(this));
    }

    default BlaqIterable<T> defaultIfEmpty(T v){
        return new Blaqen<>(Enumerable.defaultIfEmpty(this, v));
    }

    default T aggregate(BiFunction<T, T, T> f){
        return Enumerable.aggregate(this, f);
    }

    default <S> S aggregate(S seed, BiFunction<S, T, S> f){
        return Enumerable.aggregate(this, seed, f);
    }

    default <S, R> R aggregate(S seed, BiFunction<S, T, S> biF, Function<S, R> f){
        return Enumerable.aggregate(this, seed, biF, f);
    }

    default BlaqIterable<T> distinct(){
        return new Blaqen<>(Enumerable.distinct(this));
    }

    default BlaqIterable<T> distinct(ICompareEquality<T> c){
        return new Blaqen<>(Enumerable.distinct(this, c));
    }

    default BlaqIterable<T> union(Iterable<T> other){
        return new Blaqen<>(Enumerable.union(this, other));
    }

    default BlaqIterable<T> union(Iterable<T> other, ICompareEquality<T> c){
        return new Blaqen<>(Enumerable.union(this, other, c));
    }

    default BlaqIterable<T> intersect(Iterable<T> other){
        return new Blaqen<>(Enumerable.intersect(this, other));
    }

    default BlaqIterable<T> intersect(Iterable<T> other, ICompareEquality<T> c){
        return new Blaqen<>(Enumerable.intersect(this, other, c));
    }

    default BlaqIterable<T> except(Iterable<T> other){
        return new Blaqen<>(Enumerable.except(this, other));
    }

    default BlaqIterable<T> except(Iterable<T> other, ICompareEquality<T> c){
        return new Blaqen<>(Enumerable.except(this, other, c));
    }

    default <K> ILookup<K, T> toLookup(Function<T, K> keySelector){
        return Enumerable.toLookup(this, keySelector);
    }

    default <K> ILookup<K, T> toLookup(Function<T, K> keySelector, ICompareEquality<K> c){
        return Enumerable.toLookup(this, keySelector, c);
    }

    default <K, V> ILookup<K, V> toLookup(Function<T, K> keySelector, Function<T, V> valueSelector){
        return Enumerable.toLookup(this, keySelector, valueSelector);
    }

    default <K, V> ILookup<K, V> toLookup(Function<T, K> keyF, Function<T, V> valueF, ICompareEquality<K> c){
        return Enumerable.toLookup(this, keyF, valueF, c);
    }

    default <TInner, TKey, TResult> BlaqIterable<TResult> join(Iterable<TInner> inner,
                                                               Function<T, TKey> oks, Function<TInner, TKey> iks,
                                                               BiFunction<T, TInner, TResult> resultSelector){
        return new Blaqen<>(Enumerable.join(this, inner, oks, iks, resultSelector));
    }

    default <TInner, TKey, TResult> BlaqIterable<TResult> join(Iterable<TInner> inner,
                                                               Function<T, TKey> oks, Function<TInner, TKey> iks,
                                                               BiFunction<T, TInner, TResult> resultSelector,
                                                               ICompareEquality<TKey> c){
        return new Blaqen<>(Enumerable.join(this, inner, oks, iks, resultSelector, c));
    }

    default <K> BlaqIterable<IGrouping<K, T>> groupBy(Function<T, K> keyF){
        return new Blaqen<>(Enumerable.groupBy(this, keyF));
    }

    default <K> BlaqIterable<IGrouping<K, T>> groupBy(Function<T, K> keyF, ICompareEquality<K> c){
        return new Blaqen<>(Enumerable.groupBy(this, keyF, c));
    }

    default <K, V> BlaqIterable<IGrouping<K, V>> groupBy(Function<T, K> keyF, Function<T, V> elementF){
        return new Blaqen<>(Enumerable.groupBy(this, keyF, elementF));
    }

    default <K, V> BlaqIterable<IGrouping<K, V>> groupBy(Function<T, K> keyF, Function<T, V> elementF,
                                                         ICompareEquality<K> c){
        return new Blaqen<>(Enumerable.groupBy(this, keyF, elementF, c));
    }

    default <K, S> BlaqIterable<S> groupBy(Function<T, K> keyF, BiFunction<K, Iterable<T>, S> resultF){
        return new Blaqen<>(Enumerable.groupBy(this, keyF, resultF));
    }

    default <K, S> BlaqIterable<S> groupBy(Function<T, K> keyF, BiFunction<K, Iterable<T>, S> resultF,
                                           ICompareEquality<K> c){
        return new Blaqen<>(Enumerable.groupBy(this, keyF, resultF, c));
    }

    default <K, V, S> BlaqIterable<S> groupBy(Function<T, K> keyF, Function<T, V> elementF,
                                              BiFunction<K, Iterable<V>, S> resF){
        return new Blaqen<>(Enumerable.groupBy(this, keyF, elementF, resF));
    }

    default <K, V, S> BlaqIterable<S> groupBy(Function<T, K> keyF, Function<T, V> elementF,
                                              BiFunction<K, Iterable<V>, S> resF, ICompareEquality<K> c){
        return new Blaqen<>(Enumerable.groupBy(this, keyF, elementF, resF, c));
    }

    default <TInner, TKey, TResult> BlaqIterable<TResult> groupJoin(Iterable<TInner> inner,
                                                               Function<T, TKey> oks, Function<TInner, TKey> iks,
                                                               BiFunction<T, Iterable<TInner>, TResult> resultSelector){
        return new Blaqen<>(Enumerable.groupJoin(this, inner, oks, iks, resultSelector));
    }

    default <TInner, TKey, TResult> BlaqIterable<TResult> groupJoin(Iterable<TInner> inner,
                                                               Function<T, TKey> oks, Function<TInner, TKey> iks,
                                                               BiFunction<T, Iterable<TInner>, TResult> resultSelector,
                                                               ICompareEquality<TKey> c){
        return new Blaqen<>(Enumerable.groupJoin(this, inner, oks, iks, resultSelector, c));
    }

    default BlaqIterable<T> take(int n){
        return new Blaqen<>(Enumerable.take(this, n));
    }

    default BlaqIterable<T> takeWhile(Function<T, Boolean> p){
        return new Blaqen<>(Enumerable.takeWhile(this, p));
    }

    default BlaqIterable<T> takeWhile(BiFunction<T, Integer, Boolean> p){
        return new Blaqen<>(Enumerable.takeWhile(this, p));
    }

    default BlaqIterable<T> skip(int n){
        return new Blaqen<>(Enumerable.skip(this, n));
    }

    default BlaqIterable<T> skipWhile(Function<T, Boolean> p){
        return new Blaqen<>(Enumerable.skipWhile(this, p));
    }

    default BlaqIterable<T> skipWhile(BiFunction<T, Integer, Boolean> p){
        return new Blaqen<>(Enumerable.skipWhile(this, p));
    }

//    default T[] toArray(){
//        return Enumerable.toArray(this);
//    }

    default <K> Map<K, T> toMap(Function<T, K> keyS){
        return Enumerable.toMap(this, keyS);
    }

    default <K, V> Map<K, V> toMap(Function<T, K> keyS, Function<T, V> valueS){
        return Enumerable.toMap(this, keyS, valueS);
    }

    default <K> Map<K, T> toMap(Function<T, K> keyS, ICompareEquality<K> c){
        return Enumerable.toMap(this, keyS, c);
    }

    default <K, V> Map<K, V> toMap(Function<T, K> keyS, Function<T, V> valueS, ICompareEquality<K> c){
        return Enumerable.toMap(this, keyS, valueS, c);
    }

    /* Ordering Operators */

    default <K> IOrderedIterable<T> orderBy(Function<T, K> keyS){
        return Enumerable.orderBy(this, keyS);
    }

    default <K> IOrderedIterable<T> orderBy(Function<T, K> keyS, Comparator<K> c){
        return Enumerable.orderBy(this, keyS, c);
    }

    default <K> IOrderedIterable<T> orderByDescending(Function<T, K> keyS){
        return Enumerable.orderByDescending(this, keyS);
    }

    default <K> IOrderedIterable<T> orderByDescending(Function<T, K> keyS, Comparator<K> c){
        return Enumerable.orderByDescending(this, keyS, c);
    }

    /* End Ordering Operators */

    default BlaqIterable<T> reverse(){
        return new Blaqen<>(Enumerable.reverse(this));
    }

    // TODO no arg sums

    default int sum(Function<T, Integer> f){
        return Enumerable.sum(this, f);
    }

    default long longSum(Function<T, Long> f){
        return Enumerable.longSum(this, f);
    }

    default double doubleSum(Function<T, Double> f){
        return Enumerable.doubleSum(this, f);
    }

    default float floatSum(Function<T, Float> f){
        return Enumerable.floatSum(this, f);
    }

    // TODO check min and max for Comparator overloads

    default T min(){
        return Enumerable.min(this);
    }

    default <S> S min(Function<T, S> f){
        return Enumerable.min(this, f);
    }

    default <K> T minBy(Function<T, K> f){
        return Enumerable.minBy(this, f);
    }

    default <K> T minBy(Function<T, K> f, Comparator<K> c){
        return Enumerable.minBy(this, f, c);
    }

    default T max(){
        return Enumerable.max(this);
    }

    default <S> S max(Function<T, S> f){
        return Enumerable.max(this, f);
    }

    default <K> T maxBy(Function<T, K> f){
        return Enumerable.maxBy(this, f);
    }

    default double average(Function<T, Number> f){
        return Enumerable.average(this, f);
    }

    default T elementAt(int n){
        return Enumerable.elementAt(this, n);
    }

    default T elementAtOrNull(int n){
        return Enumerable.elementAtOrNull(this, n);
    }

    default boolean contains(T v){
        return Enumerable.contains(this, v);
    }

    default boolean contains(T v, ICompareEquality<T> c){
        return Enumerable.contains(this, v, c);
    }

    default boolean sequenceEqual(Iterable<T> other){
        return Enumerable.sequenceEqual(this, other);
    }

    default boolean sequenceEqual(Iterable<T> other, ICompareEquality<T> c){
        return Enumerable.sequenceEqual(this, other, c);
    }

    default <S, R> BlaqIterable<R> zip(Iterable<S> other, BiFunction<T, S, R> f){
        return new Blaqen<>(Enumerable.zip(this, other, f));
    }

    // Shouldn't need asBlaqIterable here since objects of this type will already be BlaqIterables.

    /**
     * Nested class. Helps in simulating C# style syntactical sugar of Extension Methods.
     * @param <T>
     */
    class Blaqen<T> implements BlaqIterable<T> {

        Iterable<T> iterable;

        Blaqen(Iterable<T> it){
            iterable = it;
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return iterable.iterator();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
