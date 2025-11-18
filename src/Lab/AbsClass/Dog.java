package Lab.AbsClass;

public class Dog extends Animal {

    public Dog(String name) {
        super(name);
    }

    @Override
    public void bear() {
        System.out.println("The " + this.getClass().getSimpleName() + " " + name + " dog was born");
        this.isAlive = true;
    }

    @Override
    public void die() {
        System.out.println("The " + this.getClass().getSimpleName() + " " + name + " dog has died");
        this.isAlive = false;
    }

    public void bark() {
        if (isAlive == true) {
            System.out.println(name + " barks: Woof! Woof!");
        } else {
            System.out.println("Dog can not to bark, becouse it is not alive");
        }

    }
}