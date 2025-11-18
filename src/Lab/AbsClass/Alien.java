package Lab.AbsClass;

public class Alien extends Animal {

    public Alien(String name) {
        super(name);
    }

    public void bark() {
        System.out.println("Auf!!!");
    }

    @Override
    public void bear() {
        System.out.println("The " + this.getClass().getSimpleName() + name + " alien was born");
        this.isAlive = true;
    }

    @Override
    public void die() {
        System.out.println("The " + this.getClass().getSimpleName() + name + " alien has died");
        this.isAlive = false;
    }
}
