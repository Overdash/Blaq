package playground.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import playground.ArgumentOutOfRangeException;
import playground.Enumerable;

import java.util.Arrays;
import java.util.Collections;

class RangeTest {

    @Test
    void negativeCount(){
        Assertions.assertThrows(ArgumentOutOfRangeException.class, () -> Enumerable.range(10, -1));
    }

    @Test
    void countTooLarge(){
        Assertions.assertThrows(ArgumentOutOfRangeException.class, () -> Enumerable.range(Integer.MAX_VALUE, 2));
        Assertions.assertThrows(ArgumentOutOfRangeException.class, () -> Enumerable.range(2, Integer.MAX_VALUE));
        Assertions.assertThrows(ArgumentOutOfRangeException.class, () -> Enumerable.range(Integer.MAX_VALUE/2, (Integer.MAX_VALUE/2) + 3));
    }

    @Test
    void largeBoundariesCount(){
        Enumerable.range(Integer.MAX_VALUE, 1);
        Enumerable.range(1, Integer.MAX_VALUE);
        Enumerable.range(Integer.MAX_VALUE / 2, (Integer.MAX_VALUE / 2) + 2);
    }

    @Test
    void validRange(){
        Assertions.assertEquals(Enumerable.toList(Enumerable.range(5, 3)), Arrays.asList(5, 6, 7));
    }

    @Test
    void negativeStart(){
        Assertions.assertEquals(Enumerable.toList(Enumerable.range(-2, 5)), Arrays.asList(-2, -1, 0, 1, 2));
    }

    @Test
    void emptyRange(){
        Assertions.assertEquals(Enumerable.toList(Enumerable.range(100, 0)), Collections.emptyList());
    }

    @Test
    void singleValueOfMaxInt(){
        Assertions.assertEquals(Enumerable.toList(Enumerable.range(Integer.MAX_VALUE, 1)), Collections.singletonList(Integer.MAX_VALUE));
    }

    @Test
    void emptyRangeStartingAtMinInt(){
        Assertions.assertEquals(Enumerable.toList(Enumerable.range(Integer.MIN_VALUE, 0)), Collections.emptyList());
    }
}
