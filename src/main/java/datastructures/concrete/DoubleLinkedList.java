package datastructures.concrete;

import datastructures.interfaces.IList;


import java.util.Iterator;
import java.util.NoSuchElementException;
import misc.exceptions.EmptyContainerException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    // Adds given item to the end of the list
    @Override
    public void add(T item) {
        back = new Node<T>(back, item, null);
        if (size == 0) {
            front = back;
        } else {
            back.prev.next = back;
        }
        size++;
    }

    // Removes and returns the item from the end of the list
    // Throws EmptyContainerException if the list is empty
    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        Node<T> temp = back;
        if (back == front) {
            front = null;
            back = null;
        } else {
            back = temp.prev;
            back.next = null;
        }
        size--;
        return temp.data;
    }

    // Returns value at given index
    // Throws IndexOutOfBoundsException if index < 0 or index >= size
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return findNode(index).data;
    }
    
    // Changes value at given index to item
    // Throws IndexOutOfBoundsException if index < 0 or index >= size
    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size() == 1) {
            front = new Node<T>(item);
            back = front;
        } else if (index == size() - 1) {
            remove();
            add(item);
        } else {
            if (index == 0) {
                front = new Node<T>(null, item, front.next);
                front.next.prev = front;
            } else {
                Node<T> curr = findNode(index);
                connectNodes(curr.prev, new Node<T>(curr.prev, item, curr.next), curr.next);
            }
        }
    }

    // Adds item at given index moving all elements >= index up one index
    // Throws IndexOutOfBoundsException if index < 0 or index > size
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) {
            add(item);
        } else {
            if (index == 0) {
                front = new Node<T>(null, item, front);
                front.next.prev = front;
                
            } else {
                Node<T> curr = findNode(index);
                connectNodes(curr.prev, new Node<T>(curr.prev, item, curr), curr);
            }   
            size++;
        }
    }

    // Removes and returns the value at the given index shifting all following values down one
    // Throws IndexOutOfBoundsException if index < 0 or index >= size
    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size - 1) {
            return remove();
        } else {
            Node<T> temp;
            if (index == 0) {
                temp = front;
                front = front.next;
                front.prev = null;
            } else {
                temp = findNode(index);
                connectNodes(temp.prev, null, temp.next);
            }
            size--;
            return temp.data;
        }
    }

    // Returns index of the first occurrence of the item, returns -1 if not found
    @Override
    public int indexOf(T item) {
        int index = 0;
        if (item == null) {
            for (T other : this) {
                if (other == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (T other : this) {
                if (item.equals(other)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    // Returns the size of the list
    @Override
    public int size() {
        return size;
    }

    // Returns true if other is in the list, returns false otherwise
    @Override
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }

    // Returns an iterator of the list
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }
    
    // Returns the Node at the given index
    private Node<T> findNode(int index) {
        Node<T> temp;
        if (index > (size - 1) / 2) {
            temp = back;
            while (index < size - 1) {
                temp = temp.prev;
                index++;
            }
        } else {
            temp = front;
            while (index > 0) {
                temp = temp.next;
                index--;
            }
        }
        return temp;
    }
    
    // Connects prev and next items to curr item.  If curr item is null, connects prev and next
    private void connectNodes(Node<T> prev, Node<T> curr, Node<T> next) {
        if (curr == null) {
            prev.next = next;
            next.prev = prev;
        } else {
            prev.next = curr;
            next.prev = curr;
        }
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return (current != null);
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T temp = current.data;
            current = current.next;
            return temp;
        }
    }
}
