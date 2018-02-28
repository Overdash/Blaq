package blaq.util;

public interface Tuple {

    int size();
    int hashCode(ICompareEquality compareEquality);
    String toString(StringBuilder sb);
}
