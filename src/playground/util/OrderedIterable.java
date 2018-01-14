package playground.util;

import org.jetbrains.annotations.NotNull;
import playground.FillerKeywords.Yield;
import playground.NullArgumentException;
import playground.annotations.Readonly;

import java.util.*;
import java.util.function.Function;
// TODO
public class OrderedIterable<V, TCompositeKey> implements IOrderedIterable<V> {

    @Readonly
    private Iterable<V> source;
    @Readonly
    private Function<V, TCompositeKey> compositeSelector;
    @Readonly
    private Comparator<TCompositeKey> compositeComparator;

    public OrderedIterable(final Iterable<V> src,
                    Function<V, TCompositeKey> compositeSelector,
                    final Comparator<TCompositeKey> comparator){
        source = src;
        this.compositeSelector = compositeSelector;
        compositeComparator = comparator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K> IOrderedIterable<V> createOrderedIterable(Function<V, K> keySelector,
                                                         Comparator<K> comparator,
                                                         boolean descending) {
        if(keySelector == null)
            throw new NullArgumentException("key selector");

        comparator = comparator != null ? comparator : (Comparator<K>)Comparator.naturalOrder();

        if(descending)
            comparator = (Comparator<K>)Comparator.reverseOrder(); // either this or new ReverseComparator<>(comparator);

        // Copy to a local variable so there's no need to capture "this"
        // It's likely the intermediate OrderedIterable will be eligible for GC collection immediately
        // in a typical OrderBy().ThenBy() call
        Function<V, TCompositeKey> primarySelector = compositeSelector;

        Function<V, CompositeKey<TCompositeKey, K>> newKeySelector =
                x -> new CompositeKey<>(primarySelector.apply(x), keySelector.apply(x));


        // Scared of this:
        Comparator<CompositeKey<TCompositeKey, K>> newKeyComparator =
                new CompositeKey<TCompositeKey, K>().new CompComparator(compositeComparator, comparator);

        return new OrderedIterable<>(source, newKeySelector, newKeyComparator); // <V, CompositeKey<TCompositeKey, K>>
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<V> iterator() {
        // Sorting will occur here
        /*
        * The sorting algorithm needs to:
        *  - Work on arbitrary pair-based comparisons
        *  - Be stable (so no QuickSort or Heap Sort)
        *  - (Ideally) allow the first results to be yielded without performing all the sorting work, and without affecting
        *    the performance in cases where all the results are needed.
        * Despite instability, I'll be going for QuickSort. Later will enhance it to improve worst case performance.
        * */

        // First copy elements into an array
        Tuple2<V[], Integer> buffer = toBuffer(source);
        V[] data = buffer.getItem1();
        int count = buffer.getItem2();
        int[] indexes = new int[count];

        for(int i = 0; i < indexes.length ; i++)
            indexes[i] = i;

        TCompositeKey[] keys = (TCompositeKey[]) new Object[count]; // Consider HashMap for this and indexes
        for (int i = 0; i<keys.length; i++)
            keys[i] = compositeSelector.apply(data[i]);

        // We need to "fake" recursion to our sort so elements can be yielded when necessary
        // without the whole Iterable/OrderIterable being sorted yet.
        // So a Stack of "calls" to our sort needs to be kept.
        final int[] nextYield = {0}; // Use single element array due to how values in lambdas must be final.
        Stack<SortCache> stack = new Stack<>();
        stack.push(new SortCache(0, count -1)); // Simulates the call to sort(0, count-1)
        while(stack.size() > 0){
            SortCache previousCall = stack.pop();
            int start = previousCall.start;
            int end = previousCall.end;
            if(end > start){
                int pivot = start + (end - start)/2; // Using mid-point as pivot
                int pivotPos = partition(indexes, keys, start, end, pivot);
                // calls to sort() are replaced with stack.push()
                // Push the right sublist first, so that we *pop* the
                // left sublist first
                stack.push(new SortCache(pivotPos + 1, end));
                stack.push(new SortCache(start, pivotPos - 1));
            } else { // Yield when no work needs to be done.
                return ((Yield<V>) yield ->{
                    while(nextYield[0] <= end) {
                        yield.returning(data[indexes[nextYield[0]]]);
                        nextYield[0]++;
                    }
                }).iterator(); // IDK If this works -> if not I gotta scrap this yielding requirement (since it's not mandatory)
            }
        }
        return Collections.emptyIterator(); // May cause issues
        // Pg 156
    }

    private int partition(int[] indexes, TCompositeKey[] keys, int start, int end, int pivot) {
        int pIndex = indexes[pivot];
        TCompositeKey pKey = keys[pIndex];

        // Swap pivot value to the end
        indexes[pivot] = indexes[end];
        indexes[end] = pIndex;

        int storeIndex = start;
        for(int i = start; i < end; i++){
            int candidateIndex = indexes[i];
            TCompositeKey candidateKey = keys[candidateIndex];
            int comparison = compositeComparator.compare(candidateKey, pKey);
            if(comparison < 0 || (comparison == 0 && candidateIndex < pIndex)){
                // Swap storeIndex with the current location
                indexes[i] = indexes[storeIndex];
                indexes[storeIndex] = candidateIndex;
                storeIndex++;
            }
        }

        // Move the pivot to its final place
        int temp = indexes[storeIndex];
        indexes[storeIndex] = indexes[end];
        indexes[end] = temp;

        return storeIndex;
    }

    //private int partition

    @SuppressWarnings("unchecked")
    private Tuple2<V[], Integer> toBuffer(Iterable<V> src){
        // Returns the array and the actual number of elements (not indexes)
        // Change from Pair to Tuple
        int count;
        if(src instanceof Collection){
            count = ((Collection) src).size();
            return new Tuple2<>((V[])((Collection) src).toArray(), count);
        }

        V[] arr = (V[])new Object[16];
        count = 0;
        for(V item : src){
            if(count == arr.length)
                arr = Arrays.copyOf(arr, arr.length * 2);

            arr[count++] = item;
        }

        return new Tuple2<>(arr, count);
    }

    private static class SortCache{
        int start, end;
        SortCache(int start, int end){
            this.start = start;
            this.end = end;
        }
    }
}
