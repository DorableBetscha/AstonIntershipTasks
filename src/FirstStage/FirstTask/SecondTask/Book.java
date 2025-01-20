package FirstStage.FirstTask.SecondTask;

public final class Book {
    private final String title;
    private final int yearOfPublication;

    public Book(String title, int yearOfPublication) {
        this.title = title;
        this.yearOfPublication = yearOfPublication;
    }

    public String getTitle(){
        return title;
    }

    public int getYearOfPublication(){
        return yearOfPublication;
    }
}
