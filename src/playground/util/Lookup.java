package playground.util;

import playground.Enumerable;
import playground.annotations.Readonly;

import java.util.*;

/**
 * Collection of keys mapped to one or more values.
 * A Lookup is a map where each key is associated with
 * a sequence of values instead of single one.
 *
 * As well as looking up a single sequence of values
 * associated with a key, you can also iterate over
 * the whole lookup in terms of groupings.
 *
 * Lookups are Mutable internally - immutable in public API. -- Cannot add or remove items from public API (change modifiers)
 * Have to make sure we never mutate it after it becomes visible to a caller.
 * @param <K> Keys.
 * @param <V> Sequence of values.
 */
public final class Lookup<K, V> implements ILookup<K, V> {

    @Readonly
    private Map<K, Grouping<K, V>> multimap;

    @Readonly
    private List<K> keys;

    public Lookup(ICompareEquality<K> compareEquality){
        multimap = new HashMap<>(/*compareEquality*/);
        keys = new ArrayList<>();
    }

    public void add(K k, V v){
        Grouping<K, V> elements = new Grouping<>(k);
        if(!multimap.containsKey(k)){
            multimap.put(k, elements);
            keys.add(k);
        } else
            elements = multimap.get(k);
        elements.add(v);
    }

    @Override
    public boolean contains(K key) {
        return multimap.containsKey(key);
    }

    @Override
    public int count() {
        return multimap.size();
    }

    @Override
    public Iterable<V> getItem(K key) {
        if(!multimap.containsKey(key))
            return Collections.emptyList();
        return Enumerable.project(multimap.get(key), x->x);
    }

    @Override
    public Iterator<IGrouping<K, V>> iterator() {
        return Enumerable.project(keys, k -> (IGrouping<K,V>)multimap.get(k)).iterator();
    }
}
