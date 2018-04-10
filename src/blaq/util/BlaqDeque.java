package blaq.util;

import java.util.ArrayDeque;
import java.util.Collection;

public class BlaqDeque<T> extends ArrayDeque<T> implements BlaqIterable<T> {

    // Represents a Stack and/or Queue.

    public BlaqDeque(){super();}

    public BlaqDeque(Collection<? extends T> c){super(c);}

    public BlaqDeque(Iterable<T> src){  // Stack Constructor
        //TODO Will the stack be reversed?
        super();
        for(T i : src)
            push(i);
    }

    public BlaqDeque(Iterable<T> src, boolean dummy){ // Queue Constructor
        super();
        for(T i : src)
            offer(i);
    }
}
