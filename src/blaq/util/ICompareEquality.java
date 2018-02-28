package blaq.util;

import blaq.core.NullArgumentException;

/**
 * <p>Distinct from Comparator & Comparable interfaces -- Attempts to deal with Universal Equality
 * Users can define bespoke .equals and .hashCode for elements in collections.
 * Useful in HashMap for example, since .hashCode is used to retrieve Value's bucket numbers.
 * Able to:
 * Determine the hash code for a single item of type T
 * Compare any two items of type T for equality.
 *
 * Comparator (from JavaDoc) - comparison function, which imposes a total ordering on some collection of objects.
 * Should provide some basic ICompareEquality concrete classes (like comparing String cases).</p>
 *
 * <p>This interface allows the implementation of customized equality comparison for BLAQ collections.
 * That is, you can create your own definition of equality for type T, and specify that this definition
 * be used with a collection type that accepts the {@code ICompareEquality<T>} interface. Constructors of
 * {@code BlaqMap} accept {@code ICompareEquality}</p>
 * @param <T>
 */
public interface ICompareEquality<T> {

    default boolean equals(T obj1, T obj2){
        return obj1.equals(obj2);
    }

    default int hashCode(T obj){
        if(obj == null)
            throw new NullArgumentException("obj");
        return obj.hashCode();
    }

    // TODO https://referencesource.microsoft.com/#mscorlib/system/collections/generic/equalitycomparer.cs,941f562252ae00b9
}
