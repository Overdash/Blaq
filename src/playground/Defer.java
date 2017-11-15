package playground;

@FunctionalInterface
public interface Defer<T> {
    T defer(T val);
}
