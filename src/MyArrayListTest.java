import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyArrayListTest {
    MyArrayList<Integer> list = new MyArrayList<>();

    @Before
    public void setUp(){
        list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
    }

    @Test
    public void testAdd() {
        list.add(4);
        list.add(5);
        assertEquals(5, list.size());
        list.print();
        assertEquals(Integer.valueOf(4), list.get(3));
        assertEquals(Integer.valueOf(5), list.get(4));
    }

    @Test
    public void testAddWithIndex() {
        list.add(2, 4);
        list.add(0, 5);
        assertEquals(5, list.size());
        assertEquals(Integer.valueOf(4), list.get(3));
        assertEquals(Integer.valueOf(5), list.get(0));
        assertEquals(Integer.valueOf(1), list.get(1));
    }

    @Test
    public void testAddAll() {
        List<Integer> testList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            testList.add(i);
        }
        list.addAll(testList);
        assertEquals(Integer.valueOf(13), list.size());
    }

    @Test
    public void testClear() {
        list.clear();
        assertEquals(list.isEmpty(), true);
    }

    @Test
    public void testRemoveIndex() {
        list.remove(1);
        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(3), list.get(1));
        assertEquals(Integer.valueOf(2), list.size());
    }

    @Test
    public void testRemoveObject() {
        list.removeObj(3);
        assertEquals(Integer.valueOf(2), list.size());
    }

    @Test
    public void testSort() {
        list.add(5);
        list.add(4);
        list.add(1);
        list.print();

        list.sort(Comparator.naturalOrder());
        list.print();

    }
}
