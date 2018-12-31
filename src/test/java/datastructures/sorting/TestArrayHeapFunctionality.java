package datastructures.sorting;

import java.util.Random;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import misc.exceptions.EmptyContainerException;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testNullHeapThrowsExceptions() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException()");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException()");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testNullAddTrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException()");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testSmallTree() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        for (int i = 2; i < 6; i++) {
            heap.insert(i);
            assertFalse(((Integer) i).compareTo(heap.peekMin()) < 0);
        }
        for (int i = 1; i < 6; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinPercolate() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i < 20; i++) {
            heap.insert(i);
        }
        for (int i = 1; i < 20; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testEqualValueTree() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100; i++) {
            heap.insert(1);
        }
        assertEquals(100, heap.size());

        int count = 0;
        while (!heap.isEmpty()) {
            heap.removeMin();
            count++;
        }
        assertEquals(100, count);
    }
    
    @Test(timeout=SECOND)
    public void testNegativeValues() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -100; i < 0; i++) {
            heap.insert(i);
        }
        for (int i = -100; i < 0; i++) {
            heap.removeMin();
        }
    }
    
    @Test(timeout=SECOND)
    public void testUnorderedEntry() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            heap.insert(r.nextInt());
        }
        while (heap.size() > 1) {
            assertTrue(heap.removeMin().compareTo(heap.peekMin()) < 0);
        }
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testStringEntry() {
        IPriorityQueue<String> heap = this.makeInstance();
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            String s = "";
            for (int j = 0; j < 10; j++) {
                s += (char) r.nextInt(26);
              }
            heap.insert(s);
        }
        while (heap.size() > 1) {
            assertTrue(heap.removeMin().compareTo(heap.peekMin()) < 0);
        }
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRepeatInsertRemoveMinOnFullHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Random r = new Random();
        for (int i = 0; i < 25; i++) {
            heap.insert(r.nextInt());
        }
        for (int i = 0; i < 100; i++) {
            heap.removeMin();
            heap.insert(r.nextInt());
        }
        while (heap.size() > 1) {
            assertTrue(heap.removeMin().compareTo(heap.peekMin()) < 0);
        }
        assertEquals(1, heap.size());
    }
}
