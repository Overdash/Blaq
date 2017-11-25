package playground.util;

/**
 * Represents a collection of objects that have a common key.
 * A grouping is a sequence with an associated key.
 * @param <K> The type of key of the collection
 * @param <E> The type of values in the collection
 */
public interface IGrouping<K, E> extends Iterable<E>{
    /**
     * Gets the key of the IGrouping collection.
     * @return The key of the collection.
     */
    K getKey();
}
