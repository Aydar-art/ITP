package Lab13;

public class main {
    public static void main(String[] args) {
        var lib = new Library<>();

        Book book1 = new Book("Heaven and hell", "Godness", 999999);
        Book book2 = new Book("People and life", "Hastle", 0);
        Book book3 = new Book("Without dreams", "Unknown", 100);
        lib.addMedia(book1);
        lib.addMedia(book2);
        lib.addMedia(book3);
        lib.showShelf();
        lib.removeMedia(book2);

        lib.showShelf();
    }
}
