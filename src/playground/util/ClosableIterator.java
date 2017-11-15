package playground.util;

import java.util.Iterator;

public interface ClosableIterator<E> extends Iterator<E>, AutoCloseable {
    void close();
}
