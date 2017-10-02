package playground;

import org.junit.jupiter.api.Test;
import playground.Collections.ClosableIterator;
import playground.FillerKeywords.Yield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGrounds {
    public static void main(String[] args) {
//        List<Integer> testList = Arrays.asList(1, 13, -4, 0, 100, 142, 4);
//
//        // Test Where
//        Iterable<Integer> filteredList = Enumerable.where(testList, i -> i < 5);
//        filteredList.forEach(System.out::println);
//
//        Iterable<Integer> mutatedList = Enumerable.project(testList, i -> i*2);
//        mutatedList.forEach(System.out::println);
//
//        // Lack of toList atm.
//        Iterable<String> comboList = Enumerable.projectMany(testList, x -> mutatedList, (n, s) -> "C: " + n.toString() + s);
//        comboList.forEach(System.out::println);
        new TestGrounds().autoclose_infinite_iterator();
        System.exit(0);
    }

    @Test
    void autoclose_infinite_iterator() {
        try (ClosableIterator<Integer> positiveIntegers = positiveIntegers().iterator()) {
//            assertEquals(Integer.valueOf(1), positiveIntegers.next());
//            assertEquals(Integer.valueOf(2), positiveIntegers.next());
//            assertEquals(Integer.valueOf(3), positiveIntegers.next());
            System.out.println(positiveIntegers.next()); // <-- Getting stuck here
            System.out.println(positiveIntegers.hasNext());
        }
    }

    public static Yield<Integer> positiveIntegers() {
        return yield -> {
            int i = 0;
            while (true) yield.returning(++i);
        };
    }
}
