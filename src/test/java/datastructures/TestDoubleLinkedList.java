package datastructures;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class TestDoubleLinkedList extends BaseTest {
    /**
     * This method creates a simple list containing three elements to help minimize
     * redundancy later in our tests.
     *
     * Please do not modify this method: our private tests will also use this method.
     */
    protected IList<String> makeBasicList() {
        IList<String> list = new DoubleLinkedList<>();

        list.add("a");
        list.add("b");
        list.add("c");

        return list;
    }

    /**
     * This test will check if a list contains exactly the same elements as
     * the "expected" array. See the tests you were provided for example
     * usage.
     *
     * Please do not modify this method: our private tests rely on this.
     */
    protected <T> void assertListMatches(T[] expected, IList<T> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());

        for (int i = 0; i < expected.length; i++) {
            try {
                assertEquals("Item at index " + i + " does not match", expected[i], actual.get(i));
            } catch (Exception ex) {
                String errorMessage = String.format(
                        "Got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        expected[i]);
                throw new AssertionError(errorMessage, ex);
            }
        }
    }

    /**
     * Note: We use 1 second as the default timeout for many of our tests.
     *
     * One second is typically extremely generous: most of your tests should
     * finish in milliseconds. If one of your tests is timing out, you're almost
     * certainly doing something wrong.
     */

    @Test(timeout=SECOND)
    public void testAddAndGetBasic() {
        IList<String> list = makeBasicList();
        this.assertListMatches(new String[] {"a", "b", "c"}, list);
    }

    @Test(timeout=2 * SECOND)
    public void testAddAndGetWorksForManyNumbers() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
        for (int i = 0; i < cap; i++) {
            int value = list.get(i);
            assertEquals(i* 2, value);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testAddIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=SECOND)
    public void testAddAndRemoveMultiple() {
        IList<String> list = this.makeBasicList();
        assertEquals("c", list.remove());
        this.assertListMatches(new String[] {"a", "b"}, list);

        assertEquals("b", list.remove());
        this.assertListMatches(new String[] {"a"}, list);

        assertEquals("a", list.remove());
        this.assertListMatches(new String[] {}, list);
    }

    @Test(timeout=SECOND)
    public void testAddAndRemoveFromEnd() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;

        for (int i = 0; i < cap; i++) {
            list.add(i);
        }

        assertEquals(cap, list.size());

        for (int i = cap - 1; i >= 0; i--) {
            int value = list.remove();
            assertEquals(i, value);
        }

        assertEquals(0, list.size());
    }

    @Test(timeout=SECOND)
    public void testAlternatingAddAndRemove() {
        int iterators = 1000;

        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < iterators; i++) {
            String entry = "" + i;
            list.add(entry);
            assertEquals(1, list.size());

            String out = list.remove();
            assertEquals(entry, out);
            assertEquals(0, list.size());
        }
    }

    @Test(timeout=5 * SECOND)
    public void testAddAndRemoveFromEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        for (int i = 0; i < 10000; i++) {
            list.add(-1);
            list.remove();
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveOnEmptyListThrowsException() {
        IList<String> list = this.makeBasicList();
        list.remove();
        list.remove();
        list.remove();
        try {
            list.remove();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testGetOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();
        try {
            list.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        // This should be ok
        list.get(2);

        try {
            // Now we're out of bounds
            list.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.get(1000);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testSetElements() {
        IList<String> list = this.makeBasicList();

        list.set(0, "AAA");
        assertListMatches(new String[] {"AAA", "b", "c"}, list);

        list.set(1, "BBB");
        assertListMatches(new String[] {"AAA", "BBB", "c"}, list);

        list.set(2, "CCC");
        assertListMatches(new String[] {"AAA", "BBB", "CCC"}, list);
    }

    @Test(timeout=SECOND)
    public void testSetWithOneElement() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");

        list.set(0, "bar");
        assertListMatches(new String[] {"bar"}, list);

        list.set(0, "baz");
        assertListMatches(new String[] {"baz"}, list);
    }

    @Test(timeout=SECOND)
    public void testSetOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        try {
            list.set(-1, "AAA");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }

        try {
            list.set(3, "AAA");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }
    }

    @Test(timeout=5 * SECOND)
    public void testSetManyItems() {
        IList<String> list = new DoubleLinkedList<>();
        int cap = 10000;

        for (int i = 0; i < cap; i++) {
            list.add("foo" + i);
        }

        for (int i = 0; i < cap; i++) {
            list.set(i, "bar" + i);
        }

        for (int i = 0; i < cap; i++) {
            assertEquals("bar" + i, list.get(i));
        }

        for (int i = cap - 1; i >= 0; i--) {
            list.set(i, "qux" + i);
        }

        for (int i = cap - 1; i >= 0; i--) {
            assertEquals("qux" + i, list.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testInsertBasic() {
        IList<String> list = this.makeBasicList();
        list.insert(0, "x");
        this.assertListMatches(new String[] {"x", "a", "b", "c"}, list);

        list.insert(2, "y");
        this.assertListMatches(new String[] {"x", "a", "y", "b", "c"}, list);

        list.insert(5, "z");
        this.assertListMatches(new String[] {"x", "a", "y", "b", "c", "z"}, list);
    }

    @Test(timeout=SECOND)
    public void testInsertEmptyAndSingleElement() {
        // Lists 1 and 2: insert into empty
        IList<String> list1 = new DoubleLinkedList<>();
        IList<String> list2 = new DoubleLinkedList<>();
        list1.insert(0, "a");
        list2.insert(0, "a");

        // No point in checking both lists
        this.assertListMatches(new String[] {"a"}, list1);

        // List 1: insert at front
        list1.insert(0, "b");
        this.assertListMatches(new String[] {"b", "a"}, list1);

        // List 2: insert at end
        list2.insert(1, "b");
        this.assertListMatches(new String[] {"a", "b"}, list2);
    }

    @Test(timeout=SECOND)
    public void testInsertOutOfBounds() {
        IList<String> list = this.makeBasicList();

        try {
            list.insert(-1, "a");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.insert(4, "a");
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=15 * SECOND)
    public void testInsertAtEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.insert(list.size(), i * 2);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testInsertNearEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(-1);
        list.add(-2);

        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.insert(list.size() - 2, i * 2);
        }
        assertEquals(cap + 2, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testInsertAtFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.insert(0, i * 2);
        }
        assertEquals(cap, list.size());
    }

    @Test(timeout=SECOND)
    public void testIndexOfAndContainsBasic() {
        IList<String> list = new DoubleLinkedList<>();

        list.add("a");
        list.add("b");
        list.add("c");
        list.add("q");
        list.add("a");

        // Test index of
        assertEquals(0, list.indexOf("a"));
        assertEquals(2, list.indexOf("c"));
        assertEquals(-1, list.indexOf("z"));

        // Test equivalent logic using list.contains(...)
        assertTrue(list.contains("a"));
        assertTrue(list.contains("c"));
        assertFalse(list.contains("z"));
    }

    @Test(timeout=SECOND)
    public void testIndexOfAndContainsCorrectlyComparesItems() {
        // Two different String objects, but with equal values
        String item1 = "abcdefghijklmnopqrstuvwxyz";
        String item2 = item1 + "";

        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");
        list.add(item1);

        assertEquals(1, list.indexOf(item2));
        assertTrue(list.contains(item2));
    }

    @Test(timeout=5 * SECOND)
    public void testIndexOfAndContainsMany() {
        int cap = 1000;
        int stringLength = 100;
        String validChars = "abcdefghijklmnopqrstuvwxyz0123456789";

        // By setting the seed to some arbitrary but constant number, we guarantee
        // this random number generator will produce the exact same sequence of numbers
        // every time we run this test. This helps us keep our tests deterministic, which
        // can help with debugging.
        Random rand = new Random();
        rand.setSeed(12345);

        IList<String> list = new DoubleLinkedList<>();
        IList<String> refList = new DoubleLinkedList<>();

        for (int i = 0; i < cap; i++) {
            String entry = "";
            for (int j = 0; j < stringLength; j++) {
                int charIndex = rand.nextInt(validChars.length());
                entry += validChars.charAt(charIndex);
            }

            list.add(entry);
            if (i % 100 == 0) {
                refList.add(entry);
            }
        }

        for (int i = 0; i < refList.size(); i++) {
            String entry = refList.get(i);
            assertEquals(i * 100, list.indexOf(entry));
            assertTrue(list.contains(entry));
        }
    }

    @Test(timeout=SECOND)
    public void testNullEntry() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        assertEquals(-1, list.indexOf(null));
        assertFalse(list.contains(null));

        list.insert(2, null);
        assertListMatches(new Integer[]{1, 2, null, 3, 4}, list);

        assertEquals(2, list.indexOf(null));
        assertTrue(list.contains(null));
    }

    @Test(timeout=SECOND)
    public void testIteratorBasic() {
        IList<String> list = this.makeBasicList();
        Iterator<String> iter = list.iterator();

        // Get first element
        for (int i = 0; i < 5; i++) {
            assertTrue(iter.hasNext());
        }
        assertEquals("a", iter.next());

        // Get second
        for (int i = 0; i < 5; i++) {
            assertTrue(iter.hasNext());
        }
        assertEquals("b", iter.next());

        // Get third
        for (int i = 0; i < 5; i++) {
            assertTrue(iter.hasNext());
        }
        assertEquals("c", iter.next());

        for (int i = 0; i < 5; i++) {
            assertFalse(iter.hasNext());
        }

        try {
            iter.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // This is ok: do nothing
        }

        // Check that the list is unchanged
        this.assertListMatches(new String[]{"a", "b", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testIteratorOnEmptyList() {
        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < 5; j++) {
                assertFalse(iter.hasNext());
            }
            try {
                iter.next();
                fail("Expected NoSuchElementException");
            } catch (NoSuchElementException ex) {
                // This is ok: do nothing
            }
        }

        assertListMatches(new String[] {}, list);
    }

    @Test(timeout=SECOND)
    public void testIteratorOnSingleElementList() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < 5; j++) {
                assertTrue(iter.hasNext());
            }
            assertEquals("foo", iter.next());
            for (int j = 0; j < 5; j++) {
                assertFalse(iter.hasNext());
            }
            try {
                iter.next();
                fail("Expected NoSuchElementException");
            } catch (NoSuchElementException ex) {
                // This is ok: do nothing
            }
        }

        assertListMatches(new String[] {"foo"}, list);
    }

    @Test(timeout=SECOND)
    public void testIteratorOnLargerList() {
        IList<String> list = this.makeBasicList();
        String[] expected = {"a", "b", "c"};

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < expected.length; j++) {
                for (int k = 0; k < 5; k++) {
                    assertTrue(iter.hasNext());
                }
                assertEquals(expected[j], iter.next());
            }

            for (int j = 0; j < 5; j++) {
                assertFalse(iter.hasNext());
            }
        }

        assertListMatches(expected, list);
        list.insert(2, "z");
        assertListMatches(new String[] {"a", "b", "z", "c"}, list);
    }

    @Test(timeout=15 * SECOND)
    public void testAddAndIteratorIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());
        int count = 0;
        for (int num : list) {
            assertEquals(count, num);
            count += 2;
        }
    }
    
    @Test(timeout=SECOND)
    public void testManyDeletes() {
        int cap = 1000;
        int stringLength = 100;
        String validChars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        rand.setSeed(12345);

        IList<String> list = new DoubleLinkedList<>();
        IList<String> refList = new DoubleLinkedList<>();

        for (int i = 0; i < cap; i++) {
            String entry = "";
            for (int j = 0; j < stringLength; j++) {
                int charIndex = rand.nextInt(validChars.length());
                entry += validChars.charAt(charIndex);
            }

            list.add(entry);
            if (i % 100 == 0) {
                refList.add(entry);
            }
        }
        for (int i = 0; i < refList.size(); i++) {

                String entry = refList.delete(i);
                assertEquals(2 * i * 100, list.indexOf(entry));
                assertFalse(refList.contains(entry));
        }
    }         
    
    @Test(timeout=SECOND)
    public void testNullDelete() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.set(2, null);
        list.delete(2);
        
        assertListMatches(new Integer[]{1, 2, 4}, list);
    }
    
    @Test(timeout=2*SECOND)
    public void testIteratorAndDelete() {
        int cap = 1000;
        int stringLength = 100;        
        String validChars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        rand.setSeed(12345);
        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            for (int l = 0; l < cap; l++) {
                String entry = "";
                for (int m = 0; m < stringLength; m++) {
                    int charIndex = rand.nextInt(validChars.length());
                    entry += validChars.charAt(charIndex);
                }
                list.add(entry);
            }
            Iterator<String> iter = list.iterator();
            for (int j = 1; j < list.size(); j++) {
                list.delete(j);
                for (int k = 0; k < 5; k++) {
                    assertTrue(iter.hasNext());
                }
                assertEquals(list.get(j - 1), iter.next());
            }
        }
    }
    
    @Test(timeout=SECOND)
    public void deleteFromEnd() {
        IList<String> list = makeBasicList();
        int init = list.size();
        assertEquals(list.get(list.size()-1), list.delete(list.size() - 1));
        assertEquals(list.size(), init - 1);
    }
    
    @Test(timeout=SECOND)
    public void deleteFromFront() {
        IList<String> list = makeBasicList();
        String init = list.get(1);
        assertEquals(list.get(0), list.delete(0));
        assertEquals(init, list.get(0));       
    }
    
    @Test(timeout=SECOND)
    public void deleteOutsideListThrowsException() {
        IList<String> list = makeBasicList();
        try {
            list.delete(list.size());
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
        list = makeBasicList();
        try {
            list.delete(-1);
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void deleteAllFromFront() {
        IList<String> list = makeBasicList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.delete(0);
        }
        assertEquals(0, list.size());
    }
    
    @Test(timeout=SECOND)
    public void deleteAllFromBack() {
        IList<String> list = makeBasicList();
        int size = list.size();
        for (int i = size - 1; i >= 0; i--) {
            list.delete(i);
        }
        assertEquals(0, list.size());
    }
    
    @Test(timeout=SECOND)
    public void deleteAlternating() {
        IList<String> list = makeBasicList();
        String end = list.get(list.size() - 2);
        int init = (list.size() - 1) / 2;
        for (int i = 0; i < list.size(); i++) {
            list.delete(i);
        }
        assertEquals(init, list.size());
        assertEquals(list.remove(), end);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteMiddle() {
        IList<String> list = makeBasicList();
        assertEquals("b", list.delete(1));
        assertListMatches(new String[] {"a", "c"}, list);
        assertEquals(2, list.size()); 
    }
    
    @Test(timeout=SECOND)
    public void testDeleteFront() {
        IList<String> list = makeBasicList();
        assertEquals("a", list.delete(0));
        assertListMatches(new String[] {"b", "c"}, list);
        assertEquals(2, list.size()); 
    }
    
    @Test(timeout=SECOND)
    public void testDeleteBack() {
        IList<String> list = makeBasicList();
        assertEquals("c", list.delete(2));
        assertListMatches(new String[] {"a", "b"}, list);
        assertEquals(2, list.size()); 
    }
    
    @Test(timeout=SECOND)
    public void testDeleteOneItemInList() {
        IList<String> list = new DoubleLinkedList<String>();
        list.add("a");
        assertEquals("a", list.delete(0));
        assertListMatches(new String[] {}, list);
        assertEquals(0, list.size()); 
    }
    
    @Test(timeout=SECOND)
    public void testDeleteFromEmptyThrowsIndexOutOfBoundsException() {
        IList<String> list = new DoubleLinkedList<String>();
        try {
            list.delete(0);
            fail("Expected an IndexOutOfBounds exception");
        } catch (IndexOutOfBoundsException ex) {
            assertListMatches(new String[] {}, list);
            assertEquals(0, list.size());
        }
        assertEquals(0, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testNegativeIndexThrowsIndexOutOfBoundsException() {
        IList<String> list = makeBasicList();
        try {
            list.delete(-1);
            fail("Expected an IndexOutOfBounds exception");
        } catch (IndexOutOfBoundsException ex) {
            assertListMatches(new String[] {"a", "b", "c"}, list);
            assertEquals(3, list.size());
        }
        assertEquals(3, list.size()); 
    }
    
    @Test(timeout=SECOND)
    public void testIndexGreaterThanSizeThrowsIndexOutOfBoundsException() {
        IList<String> list = makeBasicList();
        try {
            list.delete(5);
            assertTrue(false);
        } catch (IndexOutOfBoundsException ex) {
            assertListMatches(new String[] {"a", "b", "c"}, list);
            assertEquals(3, list.size());
        }
        assertEquals(3, list.size()); 
    }
    
    @Test(timeout=SECOND)
    public void testDeleteMultipleFromBack() {
        IList<String> list = makeBasicList();
        list.delete(2);
        assertListMatches(new String[] {"a", "b"}, list);
        list.delete(1);
        assertListMatches(new String[] {"a"}, list);
        list.delete(0);
        assertListMatches(new String[] {}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteMultipleFromFront() {
        IList<String> list = makeBasicList();
        list.delete(0);
        assertListMatches(new String[] {"b", "c"}, list);
        list.delete(0);
        assertListMatches(new String[] {"c"}, list);
        list.delete(0);
        assertListMatches(new String[] {}, list);
    }
    
    @Test(timeout=SECOND)
    public void testAlternatingAddAndDeleteOnEmptyList() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;
        for (int i = 0; i < cap; i++) {
            list.add(i);
            int value = list.delete(0);
            assertEquals(i, value);
        }
        assertListMatches(new Integer[] {}, list);
        assertEquals(0, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testAlternatingInsertAndDeleteOnEmptyList() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.insert(0, i);
            int value = list.delete(0);
            assertEquals(i, value);
        }
        assertListMatches(new Integer[] {}, list);
        assertEquals(0, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testAlternatingAddAndDelete() {
        IList<String> list = makeBasicList();
        int cap = 1000;
        for (int i = 0; i < cap; i++) {
            list.add("" + i);
            String value = list.delete(3);
            assertEquals("" + i, value);
        }
        assertListMatches(new String[] {"a", "b", "c"}, list);
        assertEquals(3, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testAlternatingInsertAndDelete() {
        IList<String> list = makeBasicList();
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.insert(1, "" + i);
            String value = list.delete(1);
            assertEquals("" + i, value);
        }
        assertListMatches(new String[] {"a", "b", "c"}, list);
        assertEquals(3, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testAlternatingSetAndDelete() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.add(1);
        }
        for (int i = 0; i < cap; i++) {
            list.set(list.size() - 1, 0);
            int value = list.delete(list.size() - 1);
            assertEquals(0, value);
        }
        assertListMatches(new Integer[] {}, list);
        assertEquals(0, list.size());
    }
   
    @Test(timeout=SECOND)
    public void testAddAndAlternatingRemoveAndDelete() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.add(1);
        }
        for (int i = 0; i < cap / 2; i++) {
            list.remove();
            int value = list.delete(list.size() - 1);
            assertEquals(1, value);
        }
        assertListMatches(new Integer[] {}, list);
        assertEquals(0, list.size());
    }
    
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }
    
    @Test(timeout=2 * SECOND)
    public void testAddAndDeleteWorksForManyNumbers() {
         IList<Integer> list = new DoubleLinkedList<>();
         int cap = 1000;
         for (int i = 0; i < cap; i++) {
             list.add(i);
         }
         assertEquals(cap, list.size());
         for (int i = 0; i < cap; i++) {
             int value = list.delete(0);
         }
    }
    
    @Test(timeout=15 * SECOND)
    public void testAddAndDeleteNearEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.add(-1);
        }
        for (int i = 0; i < cap - 2; i++) {
            list.delete(list.size() - 2);
        }
    }
    
    @Test(timeout=15 * SECOND)
    public void testAddAndDeleteFromEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i);
        }
        for (int i = 0; i < cap; i++) {
            list.add(-1);
            list.delete(list.size() - 1);
        }
    }
    
    @Test(timeout=15 * SECOND)
    public void testAddAndDeleteAtFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 10000;
        for (int i = 0; i < cap; i++) {
            list.add(-1);
        }
        for (int i = 0; i < cap; i++) {
            list.delete(0);
        }
    }
    
}
