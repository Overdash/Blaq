package playground;

import blaq.core.Enumerable;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;
import blaq.util.Tuple;
import blaq.util.Tuple2;

import java.util.*;

public class MemorySuite {

    private static void memoryTest(){
        Iterable<Integer> src = Arrays.asList(1, 3, 4, 2, 8, 1);
        Iterable<Integer> second = Arrays.asList(14, 19, 78, 8, 6, 4, 31);
        Iterable<String> firstStr = Arrays.asList("a", "b");
        Iterable<String> secondStr = Arrays.asList("c", "d");
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

//        Enumerable.aggregate(src, (acc, val) -> acc * val);
//        Enumerable.all(src, x -> x >= 1);
//        Enumerable.any(src, x -> x >= 1);
//        Enumerable.asBlaqIterable(src);
//        Enumerable.average(src);
//        Enumerable.first(Enumerable.concat(firstStr, secondStr));
//        Enumerable.contains(firstStr, "b");
//        Enumerable.defaultIfEmpty(src);
//        Enumerable.first(Enumerable.distinct(src));
//        Enumerable.elementAt(src, 3);
//        Enumerable.elementAtOrNull(src, 3);
//        Enumerable.empty();
//        Enumerable.first(Enumerable.except(src, second));
//        Enumerable.first(src);
//        Enumerable.firstOrNull(src);
//        Enumerable.first(Enumerable.groupBy(src, x -> x));
//        Enumerable.first(Enumerable.groupJoin(src, second, x -> x * 2, y -> y - 4, (outer, inner) -> outer + " -> " + inner));
//        Enumerable.first(Enumerable.intersect(src, second));
//        Enumerable.first(Enumerable.join(src, second, x -> x * 2, y -> y - 4, (outer, inner) -> outer + " -> " + inner));
//        Enumerable.last(src);
//        Enumerable.lastOrDefault(src);
//        Enumerable.longCount(src, x -> x % 2 == 0);
//        Enumerable.max(src);
//        Enumerable.min(src);
//        Enumerable.first(Enumerable.orderBy(second, x -> x));
//        Enumerable.first(Enumerable.orderByDescending(second, x -> x));
//        Enumerable.first(Enumerable.project(src, x -> x * x));
//        Enumerable.first(Enumerable.projectMany(src, x -> Arrays.asList(x*x, x)));
//        Enumerable.first(Enumerable.range(0, 15));
//        Enumerable.first(Enumerable.repeat(3, 4));
//        Enumerable.first(Enumerable.reverse(src));
//        Enumerable.sequenceEqual(src, second);
//        Enumerable.single(Arrays.asList(1));
//        Enumerable.singleOrNull(src);
//        Enumerable.first(Enumerable.skip(src, 6));
//        Enumerable.sum(src);
//        Enumerable.first(Enumerable.take(src, 3));
//        thenBy
//        thenByDesc
//        Enumerable.toArray(src);
//        Enumerable.toList(src);
//        Enumerable.toLookup(src, x -> x*3);
//        Enumerable.toMap(src, x -> String.valueOf(x*4));
//        Enumerable.first(Enumerable.union(src, second));
//        Enumerable.first(Enumerable.where(src, x -> x>3));
        Enumerable.first(Enumerable.zip(src, second, (first, sec) -> first + " : " + sec*first));

        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long actualMemUsed = afterUsedMem - beforeUsedMem;
        System.out.println(((double)actualMemUsed/1048576) + " mb");
    }

    public static void main(String[] args) {
        /*Iterable<Integer> thing = new Stack<>();
        BlaqIterable<Integer> bl = Enumerable.asBlaqIterable(thing);
        System.out.println(bl.count());*/
        /*List<Integer> t = Arrays.asList(14, 2, 3, 42, 5, 9, 2, 94, 2, 31, 3);
        BlaqIterable<Integer> b1 = Enumerable.asBlaqIterable(t);
        System.out.println(b1.orderBy(x -> x).toList());*/
        memoryTest();
        deferredExecTest();
    }

    private static void deferredExecTest(){
        List<String> l = new ArrayList<>();

        l.add("Joy");
        l.add("ho");
        l.add("meh");
        Iterable<String> q = Enumerable.where(l, str -> str.length() >= 3);
//        BlaqList<String> bl = new BlaqList<>(l);
        l.add("heal");
        l.add("el");
        l.add("test1");
        l.add("test2");

        for(String s : q)
            System.out.println(s); // should be {Joy, meh, heal, test1, test2}
    }
}
