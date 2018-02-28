package blaq.util;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * An immutable convenience class that represents a Key Value Pair
 * @param <K> Key
 * @param <V> Value
 */
public class Pair<K, V> implements Serializable {

    private K key;

    private V value;

    /**
     * Get the key for this {@code Pair}
     * @return Key of this pair
     */
    public K getKey() {
        return key;
    }

    /**
     * Get the value of this {@code Pair}
     * @return value of this pair
     */
    public V getValue() {
        return value;
    }

    public Pair(final @NotNull K key, final V value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    // Override .hashcode and .equals

    @Override
    public int hashCode(){
        // The key's hashCode is multiplied by an arbitrary prime number (263)
        // in order to make sure there is a difference in the hashCode() between
        // these two parameters:
        //  name: a  value: aa
        //  name: aa value: a
        return key.hashCode() * 263 + (value == null ? 0 : value.hashCode());
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if(other instanceof Pair){
            Pair pair = (Pair) other;
            if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
            if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
            return true;
        }
        return false;
    }
}
