package playground;

import blaq.core.Enumerable;
import org.junit.Test;
import blaq.tools.Yield;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
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

    @Test
    public void shouldHaveExpectedValues() {
        ArrayList<String> results = new ArrayList<>();

        for (String result : fooBar(ignoreSideEffects)) {
            results.add(result);
        }

        assertEquals(Arrays.asList("foo","bar"), results);
    }


    @Test public void shouldPerformSideEffectsInExpectedOrder() {
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

    @Test public void shouldBreakOut() {
        ArrayList<Integer> results = new ArrayList<>();
        for (Integer number : oneToFive(ignoreSideEffects)) {
            results.add(number);
        }
        assertEquals(Arrays.asList(1,2,3,4,5), results);
    }


    @Test public void shouldPerformSideEffectsInExpectedOrderWithLoop() {
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

    @Test public void iteratorShouldNotRequireCallToHasNext() {
        Iterable<String> strings = fooBar(ignoreSideEffects);
        Iterator<String> iterator = strings.iterator();
        assertEquals("foo", iterator.next());
        assertEquals("bar", iterator.next());
    }

    @Test public void iterableShouldNotBeStateful() {
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

    @Test public void blaqList_test(){
        BlaqIterable<Integer> source = new BlaqList<>((Iterable<Integer>) Arrays.asList(1,3,7,9,10));
        BlaqIterable<Integer> res = source.where(x -> x < 8).project(x -> x*x);
        assertEquals(res.toList(), Arrays.asList(1, 9, 7*7));
    }

}
