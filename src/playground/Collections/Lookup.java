package playground.Collections;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Collection of keys mapped to one or more values.
 * @param <K>
 * @param <E>
 */
public class Lookup<K, E> implements ILookup<K, E> {

    private Map<K, List<E>> multimap = new HashMap<>();

    public boolean add(K k, E e){
        List<E> elements = new ArrayList<>();
        if(!multimap.containsKey(k)){
            multimap.put(k, elements);
            elements.add(e);
            return true;
        } else {
            elements.add(e);
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
    public Iterable<E> getItem(K key) {
        return multimap.get(key);
    }

    @NotNull
    @Override
    public Iterator<IGrouping<K, E>> iterator() {
        Collection<IGrouping<K, E>> result = new ArrayList<>();
        for(Map.Entry<K, List<E>> pair : multimap.entrySet())
            result.add(new Grouping<>(pair.getKey(), pair.getValue()));
        return result.iterator();
    }
}
