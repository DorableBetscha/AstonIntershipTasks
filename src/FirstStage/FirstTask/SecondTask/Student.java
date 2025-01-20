package FirstStage.FirstTask.SecondTask;

import java.util.Collections;
import java.util.List;

public final class Student {
    private final String name;
    private final List<Book> books;

    public Student(String name, List<Book> books) {
        this.name = name;
        this.books = Collections.unmodifiableList(books);
    }
    public String getName() {
        return name;
    }
    public List<Book> getBooks() {
        return books;
    }
}
