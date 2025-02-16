package blaq.util;

import org.jetbrains.annotations.NotNull;
import blaq.core.Enumerable;
import blaq.annotations.Readonly;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Lightweight wrapper around an ArrayList.
 * Groupings are immutable.
 * @param <K>
 * @param <E>
 */
public final class Grouping<K, E> extends ArrayList<E> implements IGrouping<K, E> {
    @Readonly
    private ArrayList<E> elements;
    @Readonly
    private K key;

    public Grouping(K key, Iterable<E> elements){
        this.elements = new ArrayList<>();
        Enumerable.addToCollection(this.elements, elements);
        this.key = key;
    }

    public Grouping(K key){
        this.elements = new ArrayList<>();
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

    @Override
    public boolean add(E item){
        return elements.add(item);
    }

    @Override
    public int size(){
        return elements.size();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(key.toString());
        sb.append(elements.toString());
//        sb.append("]");
        return sb.toString();
    }
}
