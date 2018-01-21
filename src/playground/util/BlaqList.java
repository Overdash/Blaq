package playground.util;

import playground.Enumerable;

import java.util.ArrayList;

public class BlaqList<T> extends ArrayList<T> implements BlaqIterable<T> {

    public BlaqList(){super();}

    public BlaqList(int cap){super(cap);}

    public BlaqList(Iterable<T> src){
        super();
        for(T item : src)
            this.add(item);
    }
}
