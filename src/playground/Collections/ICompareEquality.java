package playground.Collections;

/**
 * Distinct from Comparator & Comparable interfaces -- Attempts to deal with Universal Equality
 * Able to:
 * Determine the hash code for a single item of type T
 * Compare any two items of type T for equality.
 * THINK OF EXPANDING COMPARATOR/ COMPARABLE TO ALLOW IT TO WORK WITH EXISTING COLLECTION FRAMEWORK
 *
 * Should provide some basic ICompareEquality concrete classes (like comparing String cases)
 * @param <T>
 */
public interface ICompareEquality<T> {
    // Consider making them default: Cater for default equality for a type.
    boolean equals(T obj1, T obj2);

    int hashCode(T obj);
}
