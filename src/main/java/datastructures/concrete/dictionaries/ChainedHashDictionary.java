package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;     // ChainedHashDictionary
    private int capacity;                   // ChainedHashDictionary capacity
    private int dictionarySize;             // number of Key-Value pairs in the ChainedHashDictionary

    // You're encouraged to add extra fields (and helper methods) though!

    // constructs a  new ChainedHashDictionary
    public ChainedHashDictionary() {
        this.dictionarySize = 0;
        this.capacity = 97;
        this.chains = makeArrayOfChains(capacity);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    // returns the Value associated with the entered key
    // throws NoSuchKeyException if key is not in the ChainedHashDictionary
    @Override
    public V get(K key) {
        if (chains[getHash(key)] == null) {
            throw new NoSuchKeyException();
        }
        return chains[getHash(key)].get(key);
    }

    // adds the key-value pair to the ChainedHashDictionary, replaces the pair if the entered key
    //      already exists in the ChainedHashDictionary
    @Override
    public void put(K key, V value) {
        if ((double) dictionarySize / capacity > 1.5) {
            capacity *= 2;
            chains = increaseCapacity();
        }
        int hash = getHash(key);
        if (chains[hash] == null) {
            chains[hash] = new ArrayDictionary<K, V>();
        }
        int size = chains[hash].size();
        chains[hash].put(key, value);
        this.dictionarySize += (chains[hash].size() - size);
    }

    // removes the key-value pair associated with the entered key
    // throws NoSuchKeyException if key is not in the ChainedHashDictionary
    @Override
    public V remove(K key) {
        int hash = getHash(key);
        if (chains[hash] == null || !chains[hash].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        this.dictionarySize--;
        return chains[hash].remove(key);
    }

    // returns true if ChainedHashDictionary contains the key, returns false otherwise
    @Override
    public boolean containsKey(K key) {
        return (chains[getHash(key)] != null && chains[getHash(key)].containsKey(key));
    }

    // returns the number of elements in the ChainedHashDictionary
    @Override
    public int size() {
        return dictionarySize;
    }

    //  returns hash code for entered key
    private int getHash(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % capacity;
        }
    }
    
    // increases capacity of ChainedHashArray
    private IDictionary<K, V>[] increaseCapacity() {
        IDictionary<K, V>[] temp = makeArrayOfChains(capacity);
        for (IDictionary<K, V> dictionary : chains) {
            if (dictionary != null) {
                for (KVPair<K, V> pair : dictionary) {
                    int hash = getHash(pair.getKey());
                    if (temp[hash] == null) {
                        temp[hash] = new ArrayDictionary<K, V>();
                    }
                    temp[hash].put(pair.getKey(), pair.getValue());
                }
            }   
        }
        return temp;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;             // ChainedHashDictionary to be iterated over
        private Iterator<KVPair<K, V>> iter;        // iterates over ChainedHashDictionary index
        private int chainIndex;                     // ChainedHashDictionary index

        // Constructs an iterator
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chainIndex = 0;
            this.chains = chains;
            if (this.chains[chainIndex] != null) {
                this.iter = chains[chainIndex].iterator();
            }
        }
            
        @Override
        public boolean hasNext() {
            if (iter == null || !iter.hasNext()) {
                if (chainIndex == chains.length - 1) {
                    return false;
                }
                chainIndex++;
                if (chains[chainIndex] != null) {
                    this.iter = chains[chainIndex].iterator();
                } else {
                    iter = null;
                }
                return this.hasNext();
            }
            return true;
        }
    
        // returns next element in ChainedHashDictionary
        // throws NoSuchElementException if there are no elements left in the iterator
        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iter.next();
        }
    }
}
