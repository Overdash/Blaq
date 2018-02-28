package blaq.util;

import org.jetbrains.annotations.NotNull;

// TODO
public class Tuple2<T1, T2> implements Comparable, Tuple {

    private T1 item1;
    private T2 item2;

    public Tuple2(T1 item1, T2 item2){
        this.item1 = item1;
        this.item2 = item2;
    }

    public T1 getItem1() {
        return item1;
    }

    public T2 getItem2() {
        return item2;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public int hashCode(ICompareEquality compareEquality) {
        return 0;
    }

    @Override
    public String toString(StringBuilder sb) {
        return null;
    }
}
