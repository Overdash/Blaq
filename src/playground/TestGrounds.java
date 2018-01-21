package playground;

import org.junit.jupiter.api.Test;
import playground.tools.Yield;
import playground.util.BlaqIterable;
import playground.util.BlaqList;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestGrounds {

    /*-**************************** Yield Tests!*******************************-*/
    private SideEffects ignoreSideEffects = sideEffect -> {};

    public static Yield<String> fooBar(SideEffects sideEffects) {
        return yield -> {
            sideEffects.sideEffect(1);
            yield.returning("foo");
            sideEffects.sideEffect(2);
            yield.returning("bar");
            sideEffects.sideEffect(3);
        };
    }

    public static Yield<Integer> oneToFive(SideEffects sideEffects) {
        return yield -> {
            for (int i = 1; i < 10; i++) {
                sideEffects.sideEffect(i);
                if (i == 6) yield.breaking();
                yield.returning(i);
            }
        };
    }

    @Test public void should_have_expected_values() {
        ArrayList<String> results = new ArrayList<>();

        for (String result : fooBar(ignoreSideEffects)) {
            results.add(result);
        }

        assertEquals(Arrays.asList("foo","bar"), results);
    }


    @Test public void should_perform_side_effects_in_expected_order() {
        SideEffects sideEffects = mock(SideEffects.class);

        Iterable<String> foos = fooBar(sideEffects);
        verifyZeroInteractions(sideEffects);

        int sideEffectNumber = 1;
        for  (String foo : foos) {
            verify(sideEffects).sideEffect(sideEffectNumber++);
            verifyNoMoreInteractions(sideEffects);
        }
        verify(sideEffects).sideEffect(sideEffectNumber++);
        verifyNoMoreInteractions(sideEffects);

    }

    @Test public void should_break_out() {
        ArrayList<Integer> results = new ArrayList<>();
        for (Integer number : oneToFive(ignoreSideEffects)) {
            results.add(number);
        }
        assertEquals(Arrays.asList(1,2,3,4,5), results);
    }


    @Test public void should_perform_side_effects_in_expected_order_with_loop() {
        SideEffects sideEffects = mock(SideEffects.class);

        Iterable<Integer> numbers = oneToFive(sideEffects);
        verifyZeroInteractions(sideEffects);

        int sideEffectNumber = 1;
        for  (Integer ignored : numbers) {
            verify(sideEffects).sideEffect(sideEffectNumber++);
            verifyNoMoreInteractions(sideEffects);
        }
        verify(sideEffects).sideEffect(sideEffectNumber++);
        verifyNoMoreInteractions(sideEffects);
    }

    @Test public void iterator_should_not_require_call_to_hasNext() {
        Iterable<String> strings = fooBar(ignoreSideEffects);
        Iterator<String> iterator = strings.iterator();
        assertEquals("foo", iterator.next());
        assertEquals("bar", iterator.next());
    }

    @Test public void iterable_should_not_be_stateful() {
        Iterable<String> strings = fooBar(ignoreSideEffects);
        assertEquals("foo", strings.iterator().next());
        assertEquals("foo", strings.iterator().next());
    }

    interface SideEffects {
        void sideEffect(int sequence);
    }
    /*-**************************** End of Yield Tests!*******************************-*/

//    @Test void testing_this(){
//        Iterable<String> s = new ArrayList<>();
//    }

    @Test void blaqList_test(){
        BlaqIterable<Integer> source = new BlaqList<>(Arrays.asList(1,3,7,9,10));
        BlaqIterable<Integer> res = source.where(x -> x < 8).project(x -> x*x);
        assertEquals(res, Arrays.asList(1, 9, 7*7));
    }

}
