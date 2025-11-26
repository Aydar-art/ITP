package Lab13;

public class Video extends Media{
    int duration;

    public Video(String title, String author, int duration) {
        super(title, author);
        this.duration = duration;
    }
}
