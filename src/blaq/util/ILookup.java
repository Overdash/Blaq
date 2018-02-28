package blaq.util;

import blaq.annotations.Readonly;

@Readonly
public interface ILookup<K, E> extends BlaqIterable<IGrouping<K, E>> {

    boolean containsKey(K key);

    int size();

    Iterable<E> getItem(K key);
}
