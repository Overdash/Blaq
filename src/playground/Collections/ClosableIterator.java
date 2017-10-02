package playground.Collections;

import java.util.Iterator;

public interface ClosableIterator<E> extends Iterator<E>, AutoCloseable {
    void close();
}
