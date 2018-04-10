package blaq.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlaqList<E> extends ArrayList<E> implements BlaqIterable<E> {

    public BlaqList(){super();}

    public BlaqList(int cap){super(cap);}

    public BlaqList(Iterable<E> src){
        super();
        if(src != null)
            for(E item : src)
                this.add(item);
    }

//    public BlaqList(Collection<? extends E> c){super(c);}
}
