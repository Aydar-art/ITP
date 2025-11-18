package Lab.AbsClass;

public class Human extends Animal {

    public Human(String name) {
        super(name);
    }

    @Override
    public void bear() {
        System.out.println("The " + this.getClass().getSimpleName() + name + " human was born");
        this.isAlive = true;
    }

    @Override
    public void die() {
        System.out.println("The " + this.getClass().getSimpleName() + name + " human has died");
        this.isAlive = false;
    }
}

