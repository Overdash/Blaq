package playground;

import playground.Collections.ILookup;
import playground.Collections.Lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.*;

/**
 * Class filled with helper methods. Equivalent of IEnumerable in Java is Iterable... however, for convenience
 * Iterable and Collection interfaces will be used where sensible.
 */
public class Enumerable {

    // Refactor Collection to Iterable. -- Make a helper method to check arguments

    // Where - O(n)
    public static <T> Iterable<T> where(Iterable<T> source, Predicate<T> predicate){
        if(source == null)
            throw new NullArgumentException("source");
        if(predicate == null)
            throw new NullArgumentException("predicate");
        return whereImp(source, predicate);
    }

    private static <T> Iterable<T> whereImp(Iterable<T> source, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<>();
        for(T item: source)
            if(predicate.test(item))
                result.add(item);
        return result;
    }


    // Select - O(n)
    public static <TSource, TResult> Iterable<TResult> project(Iterable<TSource> source,
                                                               Function<TSource, TResult> projector){
        if(source == null)
            throw new NullArgumentException("source");
        if(projector == null)
            throw new NullArgumentException("projector");
        return projectionImp(source, projector);
    }

    private static <TSource, TResult> Iterable<TResult> projectionImp(Iterable<TSource> source,
                                                                      Function<TSource, TResult> projector){
        Collection<TResult> result = new ArrayList<>();
        for(TSource item : source)
            result.add(projector.apply(item));
        return result;
    }

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

    // ToLookup
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
}
