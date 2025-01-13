package SecondTask;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String args[]) {
        Book book1 = new Book("Война и мир", 1867);
        Book book2 = new Book("1984", 1949);
        Book book3 = new Book("Мастер и Маргарита", 1967);

        Student student1 = new Student("Anton", Arrays.asList(book1, book2));
        Student student2 = new Student("Maxim", Arrays.asList(book3));

        List<Student> students = Arrays.asList(student1, student2);

        Optional<Integer> yearBook = students.stream()
                .flatMap(student -> student.getBooks().stream())
                .filter(book -> book.getTitle().equals("Мастер и Маргарита"))
                .map(Book::getYearOfPublication)
                .findFirst();

        yearBook.ifPresentOrElse(
                year -> System.out.println(year),
                () -> System.out.println("Книга 'Мастер и Маргарита' не найдена"));
    }
}
