package Lab13;

import java.util.List;
import java.util.ArrayList;

public class Library<T extends Media> {
    List<T> shelf = new ArrayList<>();

    public void addMedia(T el) {
        shelf.add(el);
    }

    public boolean removeMedia(T el) {
        if (shelf.contains(el)) {
            shelf.remove(el);
            return true;
        }
        return false;
    }

    public void showShelf() {
        int i = 1;
        for (Media media : shelf) {
            System.out.println(i + ". " + media.getClass().getSimpleName() + " " +media.getTitle() + " was written by:" + media.getAuthor());
            i++;
        }
    }

}
