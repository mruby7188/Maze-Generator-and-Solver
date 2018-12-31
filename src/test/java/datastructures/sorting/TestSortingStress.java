package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Random;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {

    @Test(timeout=10*SECOND)
    public void testRemoveMinStress() {
        IPriorityQueue<Integer> testHeap = new ArrayHeap<Integer>();
        for (int i = 0; i < 500000; i++) {
            testHeap.insert(i);
        }
        int counter = 0;
        while (!testHeap.isEmpty()) {
            int min = testHeap.removeMin();
            assertTrue(counter == min);
            counter++;
        }
    }

    @Test(timeout=1*SECOND)
    public void testPeekMinStress() {
        IPriorityQueue<Integer> testHeap = new ArrayHeap<Integer>();
        for (int i = 0; i < 500000; i++) {
            testHeap.insert(i);
        }
        assertEquals(500000, testHeap.size());
        for (int i = 0; i < 500000; i++) {
            int min = testHeap.peekMin();
            assertTrue(min == 0);
        }
    }

    @Test(timeout=10*SECOND)
    public void testInsertStress() {
        Random r = new Random();
        SortedSet<Integer> comp = new TreeSet<Integer>();
        IPriorityQueue<Integer> testHeap = new ArrayHeap<Integer>();
        for (int i = 0; i > 500000; i++) {
            int n = r.nextInt();
            testHeap.insert(n);
            comp.add(n);
        }
        assertEquals(comp.size(), testHeap.size());
        try {
            for (Integer t : comp) {
                assertEquals(t, testHeap.removeMin());
            }
        } catch (EmptyContainerException ex) {
            System.out.println("Not all elements added to Heap");
        }
    }

    @Test(timeout=1*SECOND)
    public void testSizeStress() {
        IPriorityQueue<Integer> testHeap = new ArrayHeap<Integer>();
        for (int i = 0; i < 500000; i++) {
            testHeap.insert(i);
        }
        for (int i = 0; i < 500000; i++) {
            assertTrue(testHeap.size() == 500000);
        }
    }

    @Test(timeout=10*SECOND)
    public void testTopKSortStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 500000; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(500, list);
        assertEquals(500, top.size());
        for (int i = 499500; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testInverseTopKSortStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 499999; i >=0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(500, list);
        assertEquals(500, top.size());
        for (int i = 499500; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
}
