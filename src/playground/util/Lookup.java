package playground.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Collection of keys mapped to one or more values.
 * @param <K>
 * @param <V>
 */
public class Lookup<K, V> implements ILookup<K, V> {

    private Map<K, List<V>> multimap = new HashMap<>();

    public boolean add(K k, V v){
        List<V> elements = new ArrayList<>();
        if(!multimap.containsKey(k)){
            multimap.put(k, elements);
            elements.add(v);
            return true;
        } else {
            elements.add(v);
            return false;
        }
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
        return multimap.get(key);
    }

    @NotNull
    @Override
    public Iterator<IGrouping<K, V>> iterator() {
        Collection<IGrouping<K, V>> result = new ArrayList<>();
        for(Map.Entry<K, List<V>> pair : multimap.entrySet())
            result.add(new Grouping<>(pair.getKey(), pair.getValue()));
        return result.iterator();
    }
}
