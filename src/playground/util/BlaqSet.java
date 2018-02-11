package playground.util;

import playground.NullArgumentException;

import java.util.Collection;
import java.util.HashSet;

public class BlaqSet<T> extends HashSet<T> implements BlaqIterable<T> {

    private ICompareEquality<T> compareEquality = null;

    public BlaqSet(){super();}

    public BlaqSet(int cap){super(cap);}

    public BlaqSet(int cap, int loadFactor){super(cap, loadFactor);}

    public BlaqSet(Iterable<T> src){
        super();
        if(src == null)
            throw new NullArgumentException("source iterable null");
        for(T item: src)
            add(item);
    }

    public BlaqSet(Collection<? extends T> c){super(c);}

    public BlaqSet(ICompareEquality<T> ce){
        super();
        compareEquality = ce;
    }

    public BlaqSet(Iterable<T> src, ICompareEquality<T> ce){
        super();
        if(src == null)
            throw new NullArgumentException("source iterable null");
    }
}
