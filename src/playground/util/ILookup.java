package playground.util;

public interface ILookup<K, E> extends Iterable<IGrouping<K, E>> {

    boolean contains(K key);

    int count();

    Iterable<E> getItem(K key);
}
