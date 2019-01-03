package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int heapSize;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heap = makeArrayOfT(1);
        this.heapSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new EmptyContainerException();
        }
        T out = heap[0];
        heapSize--;
        heap[0] = heap[heapSize];
        heap[heapSize] = null;
        percolateDown(heap[0], 0);
        return out;        
    }
    
    private void percolateDown(T item, int index) {
        int child = index * NUM_CHILDREN;
        int min = index;
        for (int i = child + 1; i <= child + NUM_CHILDREN; i++) {
            if (i < heapSize && heap[min].compareTo(heap[i]) > 0) {
                min = i;
            }
        }
        if (min != index) {
            heap[index] = heap[min];
            heap[min] = item;
            percolateDown(item, min);
        }
}

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        heapSize++;
        if (heapSize > 1) {
            if (heapSize > heap.length) {
                resize();
            }
            heap[heapSize - 1] = item;
            percolateUp(item, heapSize - 1);
        } else {
            heap[0] = item;
        }
    }
    
    private void percolateUp(T item, int index) {
        int parent = (index - 1) / NUM_CHILDREN;
        if (item.compareTo(heap[parent]) < 0) {
            T temp = heap[parent];
            heap[parent] = item;
            heap[index] = temp;
            percolateUp(item, parent);
        }
    }
    
    private void resize() {
        T[] temp = heap;
        heap = makeArrayOfT(heapSize * NUM_CHILDREN + 1);
        for (int i = 0; i < temp.length; i++) {
            heap[i] = temp[i];
        }
    }

    @Override
    public int size() {
        return heapSize;
    }
}
