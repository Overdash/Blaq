package blaq.util;

import java.util.Iterator;

// Investigate more on Iterators and if they're collected if not exhausted.
public interface CloseableIterator<E> extends Iterator<E>, AutoCloseable {
    void close(); // provide a default imp for this.
}
