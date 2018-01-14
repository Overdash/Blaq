package playground.util;

import playground.annotations.Readonly;

import java.util.Comparator;

public class ReverseComparator<T> implements Comparator<T> {

    @Readonly
    private Comparator<T> forwardComparator;

    public ReverseComparator(final Comparator<T> forwardComparator){
        this.forwardComparator = forwardComparator;
    }

    @Override
    public int compare(T o1, T o2) {
        return forwardComparator.compare(o1, o2);
    }
}
