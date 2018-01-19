package playground.util;

import java.util.Iterator;

public interface CloseableIterator<E> extends Iterator<E>, AutoCloseable {
    void close(); // provide a default imp for this.
}
