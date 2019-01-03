package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int capacity;
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        capacity = 100;
        this.pairs = makeArrayOfPairs(capacity);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }
    
    // Returns the value associated with the given key
    // if Key is not in the dictionary, throws NoSuchKeyException
    @Override
    public V get(K key) {
        int index = findIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        return pairs[index].value;
    }

    // Adds Key Value pair to the dictionary, overwriting the existing entry if the key is
    //      already in the dictionary.
    @Override
    public void put(K key, V value) {
        int index = findIndex(key);
        if (index == -1) {
            if (size == capacity) {
                pairs = increaseCapacity();
            }
            pairs[size] = new Pair<K, V>(key, value);
            size++;
        } else {
            pairs[index].value = value;
        }
    }

    // Removes the Key - Value pair associated with the given key from the dictionary
    // Throws NoSuchKeyException if key is not found in the dictionary
    @Override
    public V remove(K key) {
        int index = findIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        Pair<K, V> temp = pairs[index];
        if (index != size - 1) {
            pairs[index] = pairs[size - 1];
        }
        size--;
        return temp.value;
    }

    // Returns true if key is found in the dictionary, returns false otherwise
    @Override
    public boolean containsKey(K key) {
        return findIndex(key) != -1;
    }

    // Returns the number of pairs in the dictionary
    @Override
    public int size() {
        return size;
    }
    
    // Returns the location of the entered key
    private int findIndex(K key) {
        if (key == null) {
            for (int i = 0; i < size; i++) {
                if (!pairs[i].hasKey()) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (key.equals(pairs[i].key)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private Pair<K, V>[] increaseCapacity() {
        capacity *= 2;
        Pair<K, V>[] temp = makeArrayOfPairs(capacity);
        for (int i = 0; i < size; i++) {
            temp[i] = pairs[i];
        }
        return temp;
    }    
    
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(this.pairs, this.size);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean hasKey() {
            return this.key != null;
        }
        
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] p2;
        private int size;
        private int index;
        
        public ArrayDictionaryIterator(Pair<K, V>[] p1, int size) {
            this.index = 0;
            this.p2 = p1;
            this.size = size;
        }
        
        public boolean hasNext() {
            return (index < size);
        }
        
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.index++;
            return new KVPair<K, V>(p2[this.index - 1].key, p2[this.index - 1].value);
        }
    }
}
