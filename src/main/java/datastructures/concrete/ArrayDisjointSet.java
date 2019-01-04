package datastructures.concrete;

import datastructures.interfaces.IDisjointSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private IDictionary<T, Integer> sets;
    private int size;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        this.sets = new ChainedHashDictionary<T, Integer>();
        this.pointers = new int[97];
        this.size = 0;
    }

    @Override
    public void makeSet(T item) {
        if (sets.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        
        // if pointers is full increase array size
        if (size == pointers.length) {
            this.pointers = increaseSize();
        }
        // initiate item as root node
        pointers[size] = -1;
        sets.put(item, size);
        size++;
    }
    
    // copy array into array of twice the size
    private int[] increaseSize() {
        int[] out = new int[size * 2];
        for (int i = 0; i < size; i++) {
            out[i] = pointers[i];
        }
        return out;
    }

    // find root node of set
    @Override
    public int findSet(T item) {
        if (!sets.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int rootIndex = sets.get(item);
        if (pointers[rootIndex] < 0) {
            return rootIndex;
        } else {
            return getRoot(rootIndex, -1);
        }
    }
    
    private int getRoot(int index, int rank) {
        
        // if not the root node find root node
        // and assign each node passed value of root node
        
        if (pointers[index] >= 0) {
            int parentIndex = getRoot(pointers[index], rank--);
            this.pointers[index] = parentIndex;
            return parentIndex;
        } else if (rank < pointers[index]) {
            pointers[index] = rank;
        }
        return index;
    }

    @Override
    public void union(T item1, T item2) {
        if (!sets.containsKey(item1) || !sets.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int root1 = findSet(item1);  // index of root of item 1
        int root2 = findSet(item2);  // index of root of item 2
        if (root1 == root2) {
            throw new IllegalArgumentException();
        }
        
        // assign new rank if tree being added is bigger or of equal size
       
        if (pointers[root1] < pointers[root2] || (pointers[root1] == pointers[root2] && System.currentTimeMillis() % 2 == 0)) {
            pointers[root2] = root1;
            pointers[root1]--;
        } else {
        	pointers[root1] = root2;
            pointers[root2]--;
        }
    }
}
