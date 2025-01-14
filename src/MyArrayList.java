import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class MyArrayList<T> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int size;

    public MyArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY]; //инициализация массива элекментов
        this.size = 0; //установка начального значения счетчика
    }

    public void add (T element) {
        ensureCapacity(size + 1);
        elements[size++] = element; //добавление в конец списка
    }
    public void addWithIndex(int i, T element) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        System.arraycopy(elements, i, elements, i + 1, size - 1); //сдвиг элементов после добавления
        elements[i] = element; //добавление элемента на позицию i после сдвига элементов
        size++;
    }

    public void ensureCapacity(int minCapacity) {
        if (minCapacity - elements.length > 0) {   //проверка необходимости увеличения, minCapacity - минимальная необходимая емкость
            int newCapacity = elements.length * 2;
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements; //обновление ссылки на массив (с увеличенной емкостью)
        }
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection == null || collection.isEmpty()) {
            return;
        }
        int numNewElements = collection.size(); //количество новых элементов
        ensureCapacity(size + numNewElements); //достаточно ли места

        //через System.arraycopy с преобразованием коллекции в массив, эффективнее чем в цикле
        Object[] newElements = collection.toArray();
        System.arraycopy(newElements, 0, elements, size, numNewElements);
        size += numNewElements;

        //добавление элементов через цикл
        /*
        for (T element : collection) {
            elements[size++] = element; //добавление элементов и одновременно увеличение массива
        }
        */
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    } //очищение списка, размер в 0, удаляются ссылки

    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
        }
        return (T) elements[i];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T remove(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
        }
        T removedElement = (T) elements[i]; //сохраняем удаляемый элемент, чтобы вернуть позже
        int numMoved = size - i - 1; //сколько элементов нужно переместить - колво справа от индекса i
        if (numMoved > 0) {
            System.arraycopy(elements, i + 1, elements, i, numMoved); // сдвиг элементов с i+1 (следующий индекс после удаляемого) на одну позицию влево начиная с i (заполнить позицию)
        }
        elements[--size] = null; //уменьшение списка на один и установление ссылки на последний элеменет в null (удаление ссылки)
        return removedElement;
    }

    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    public void sort(Comparator<? super T> comparator) {
        quickSort(0, size -1, comparator);
    }

    private void quickSort(int low, int high, Comparator<? super T> comparator) {
        //low - начальный индекс, high - конечный
        //рекурсия для сортировки подмассивов
        if (low < high) { //проверка на пустой массив или один элемент
            int pivotIndex = partition(low, high, comparator); //вызов partition для разделения массива на основе pivot индекса
            quickSort(low, pivotIndex - 1, comparator); //рекурсивный вызов для сортировки слева от опорного
            quickSort(pivotIndex + 1, high, comparator); //справа
        }
    }

    private int partition(int low, int high, Comparator<? super T> comparator) {
        //разделение массива на две части на основе pivot (опорного элемента)
        T pivot = (T) elements[high]; //опорный элемент - high последний
        int i = (low - 1); //индекс для меньших элементов (на данный момент нет) - влево
        for (int j = low; j < high; j++) {
            if (comparator.compare((T) elements[j], pivot) <= 0); //сравнение с опорным элементом черз компаратор
            i++; //увеличение индекса для меньших элементов для перемены местами
            swap(i, j);
        }
        swap(i + 1, high); //перемещение pivot на его окончательную позицию
        return i + 1;
    }

    private void swap(int i, int j) {
        Object temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    public int size() {
        return size;
    }

}
