package playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestGrounds {
    public static void main(String[] args) {
        List<Integer> testList = Arrays.asList(1, 13, -4, 0, 100, 142, 4);

        // Test Where
        Iterable<Integer> filteredList = Enumerable.where(testList, i -> i < 5);
        filteredList.forEach(System.out::println);

        Iterable<Integer> mutatedList = Enumerable.project(testList, i -> i*2);
        mutatedList.forEach(System.out::println);

        // Lack of toList atm.
        Iterable<String> comboList = Enumerable.projectMany(testList, x -> mutatedList, (n, s) -> "C: " + n.toString() + s);
        comboList.forEach(System.out::println);
    }
}
