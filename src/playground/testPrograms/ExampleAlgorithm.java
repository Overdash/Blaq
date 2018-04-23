package playground.testPrograms;

import blaq.util.BlaqIterable;
import blaq.util.BlaqList;
import blaq.util.Tuple2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Find the maximum interval from a list of intervals.
 */
public class ExampleAlgorithm {

    public static void main(String[] args) {
        rangeFinder(Arrays.asList(new Tuple2<>(1, 6), new Tuple2<>(3, 8), new Tuple2<>(12, 15)));
    }

    private static void rangeFinder(List<Tuple2<Integer, Integer>> list){
        BlaqList<Tuple2<Integer, Integer>> filtered = new BlaqList<>();
        list.forEach(t -> {
            filtered.add(new Tuple2<>(t.getItem1(), 1));
            filtered.add(new Tuple2<>(t.getItem2(), -1));
        });
        BlaqIterable<Tuple2<Integer, Integer>> sorted = filtered.orderBy(x -> x, new TupleComp());

        System.out.println(sorted.toList());

        int max = sorted.max(t -> t.getItem2() == -1 ? t.getItem1() : 0);

        System.out.println("[" + sorted.first().getItem1() + " , " + max + "]");
    }

    private static class TupleComp implements Comparator<Tuple2<Integer, Integer>> {

        @Override
        public int compare(Tuple2<Integer, Integer> o1, Tuple2<Integer, Integer> o2) {
            int a = o1.getItem1();
            int b = o1.getItem2();
            int c = o2.getItem1();
            int d = o2.getItem2();
            if(a < c || (a == c && b < d))
                return -1;
            else if((a == c && b == d) || o1.equals(o2))
                return 0;
            else return 1;
        }
    }
}
