package blaq.util;

import blaq.core.NullArgumentException;

import java.util.Collection;
import java.util.HashSet;

public class BlaqSet<T> extends HashSet<T> implements BlaqIterable<T> {

    private ICompareEquality<T> compareEquality;

    public BlaqSet(){super();}

    public BlaqSet(int cap){super(cap);}

    public BlaqSet(int cap, int loadFactor){super(cap, loadFactor);}

    public BlaqSet(Iterable<T> src){
        super();
        if(src == null)
            throw new NullArgumentException("source iterable null");
        fromIterable(src);
    }

    public BlaqSet(Collection<? extends T> c){super(c);}

    public BlaqSet(ICompareEquality<T> ce){
        super();
        compareEquality = ce == null ? new ICompareEquality<T>() {} : ce;
    }

    public BlaqSet(Iterable<T> src, ICompareEquality<T> ce){
        super();
        if(src == null)
            throw new NullArgumentException("source iterable null");
        compareEquality = ce == null ? new ICompareEquality<T>() {} : ce;
        fromIterable(src);
    }

    public void fromIterable(Iterable<T> src){
        for(T item: src)
            add(item);
    }
}
