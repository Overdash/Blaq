package blaq.util;

import java.util.Collection;
import java.util.LinkedList;

public class BlaqLinkedList<T> extends LinkedList<T> implements BlaqIterable<T>{

    public BlaqLinkedList(){super();}

    public BlaqLinkedList(Iterable<T> src){
        super();
        for(T item : src)
            addLast(item);
    }

    public BlaqLinkedList(Collection<? extends T> c){
        super(c);
    }
}
