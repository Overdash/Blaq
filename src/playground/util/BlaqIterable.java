package playground.util;

import playground.Enumerable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface used for simulating the same syntactical sugar of Extension Methods in C#, exclusively for the BLAQ Library.
 * <p>Programmers can use Data Structures implementing this interface in the following way:</p>
 * <p>{@code list.where(x -> x < 4).project(x -> x*x)}</p> Where {@code list} is a {@code BlaqList} of numbers.
 * @param <T>
 */
public interface BlaqIterable<T> extends Iterable<T> {

    default BlaqIterable<T> where(Predicate<T> predicate){
        // make it return a BlaqIterable so you have continuous calls, e.g. list.where(...).select(...);
        // investigate how LINQ/C# use collections.
        return new BlaqList<>(Enumerable.where(this, predicate));
    }

    default <R> BlaqIterable<R> project(Function<T, R> projector){
        return new BlaqList<>(Enumerable.project(this, projector));
    }
}
