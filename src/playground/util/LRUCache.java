package playground.util;

import java.util.HashMap;

@Deprecated
public class LRUCache<K, V> {

    private int capacity;
    private HashMap<K, Node> cache;
    private Node head;
    private Node end;

    public LRUCache(int cap){
        capacity = cap;
        cache = new HashMap<>();
        head = end = null;
    }

    public V get(K key) {
        if (cache.containsKey(key)) {
            Node n = cache.get(key);
            remove(n);
            setHead(n);
            return n.value;
        }

        return null;
    }

    private void remove(Node n) {
        if(n.prev != null)
            n.prev.next = n.next;
        else
            head = n.next;

        if(n.next != null)
            n.next.prev = n.prev;
        else
            end = n.prev;
    }

    private void setHead(Node n) {
        n.next = head;
        n.prev = null;

        if(head != null)
            head.prev = n;

        head = n;

        if(end == null)
            end = head;
    }

    public void set(K key, V value){
        if(cache.containsKey(key)){
            Node old = cache.get(key);
            old.value = value;
            remove(old);
            setHead(old);
        } else {
            Node created = new Node(key, value);
            if(cache.size() >= capacity){
                cache.remove(end.key);
                remove(end);
                setHead(created);
            } else
                setHead(created);

            cache.put(key, created);
        }
    }

    class Node{
        K key;
        V value;
        Node next;
        Node prev;

        Node (K key, V value){
            this.key = key;
            this.value = value;
            next = prev = null;
        }
    }
}
