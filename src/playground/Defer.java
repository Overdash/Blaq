package playground;

@FunctionalInterface
@Deprecated
public interface Defer<T> {
    T defer(T val);
}
