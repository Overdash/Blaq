package playground.util;

import playground.NullArgumentException;

/**
 * Distinct from Comparator & Comparable interfaces -- Attempts to deal with Universal Equality
 * Users can define bespoke .equals and .hashCode for elements in collections.
 * Useful in HashMap for example, since .hashCode is used to retrieve Value's bucket numbers.
 * Able to:
 * Determine the hash code for a single item of type T
 * Compare any two items of type T for equality.
 * THINK OF EXPANDING COMPARATOR/ COMPARABLE TO ALLOW IT TO WORK WITH EXISTING COLLECTION FRAMEWORK
 *
 * Comparator (f/ JavaDoc) - comparison function, which imposes a total ordering on some collection of objects.
 * Should provide some basic ICompareEquality concrete classes (like comparing String cases)
 * @param <T>
 */
public interface ICompareEquality<T> {
    // Consider making them default: Cater for default equality for a type.
    default boolean equals(T obj1, T obj2){
        return obj1.equals(obj2);
    }

    // Have to implement this when time comes
    default int hashCode(T obj){
        if(obj == null)
            throw new NullArgumentException("obj");
        return obj.hashCode();
    }
}
