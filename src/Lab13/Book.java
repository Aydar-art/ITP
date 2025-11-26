package Lab13;
import Lab13.Media.*;

public class Book extends Media{
    int countOfPages;

    public Book(String title, String author, int countOfPages) {
        super(title, author);
        this.countOfPages = countOfPages;
    }
}
