package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i + 15, top.get(i));
        }
    }
    
    public void testSimpleUsageInverse() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 19; i >= 0; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 15; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testLargeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(30, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void test0K() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(0, list);
        assertTrue(top.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void negativeKException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try {
            IList<Integer> top = Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException()");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
}
