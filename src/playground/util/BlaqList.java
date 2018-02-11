package playground.util;

import playground.Enumerable;

import java.util.ArrayList;
import java.util.Collection;

public class BlaqList<T> extends ArrayList<T> implements BlaqIterable<T> {

    public BlaqList(){super();}

    public BlaqList(int cap){super(cap);}

    public BlaqList(Iterable<T> src){
        super();
        for(T item : src)
            this.add(item);
    }

    public BlaqList(Collection<? extends T> c){super(c);}
}
