package Lab13;

public class Newspaper extends Media{
    int price;

    public Newspaper(String title, String author, int price) {
        super(title, author);
        this.price = price;
    }
}
