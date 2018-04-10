package playground;

import blaq.core.Enumerable;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Main {


    public static void main(String[] args) {
        /*Iterable<Integer> thing = new Stack<>();
        BlaqIterable<Integer> bl = Enumerable.asBlaqIterable(thing);
        System.out.println(bl.count());*/
        List<Integer> t = Arrays.asList(14, 2, 3, 42, 5, 9, 2, 94, 2, 31, 3);
        BlaqIterable<Integer> b1 = Enumerable.asBlaqIterable(t);
        System.out.println(b1.orderBy(x -> x).toList());
//        memoryTest();

    }

    private static void memoryTest(){
        Iterable<Integer> src = Arrays.asList(1,3,4,2,8,1);
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        Iterable<Integer> filteredList = Enumerable.where(src, x -> x < 4);
        Enumerable.count(filteredList);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long actualMemUsed = afterUsedMem - beforeUsedMem;
        System.out.println(((double)actualMemUsed/1048576) + " mb");
    }

    private static void defferedExecTest(){
        List<String> l = new ArrayList<>();
        Iterable<String> q = Enumerable.where(l, str -> str.length() >= 3);

        l.add("Joy");
        l.add("ho");
        l.add("meh");
//        BlaqList<String> bl = new BlaqList<>(l);
        l.add("heal");
        l.add("el");
        l.add("test1");
        l.add("test2");

        for(String s : q)
            System.out.println(s); // should be {Joy, meh, heal, test1, test2}
    }
}
