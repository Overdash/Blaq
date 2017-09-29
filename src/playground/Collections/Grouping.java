package playground.Collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Grouping<K, E> implements IGrouping<K, E> {
    private Iterable<E> elements;
    private K key;

    public Grouping(K key, Iterable<E> elements){
        this.elements = elements;
        this.key = key;
    }

    @Override
    public K getKey() {
        return key;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }
}
