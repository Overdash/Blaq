package playground.util;

import playground.annotations.Readonly;

@Readonly
public interface ILookup<K, E> extends Iterable<IGrouping<K, E>> {

    boolean contains(K key);

    int count();

    Iterable<E> getItem(K key);
}
