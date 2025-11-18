package Lab.Lecture;
import Lab.Lecture.fly;

public abstract class Airbus {
    abstract void start();

    final void stop() {
        System.out.println("Set off");
    }
}
