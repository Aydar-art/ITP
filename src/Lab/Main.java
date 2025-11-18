package Lab;
import java.util.Scanner;
import Lab.AbsClass.*;

interface Executable {
    void execute();
}

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Write name:");
        String name = input.nextLine();

        while (name == "" || name == null) {
            System.out.println("Try one more time");
            name = input.nextLine();
        }

        Dog haski = new Dog(name);

        Executable[] methodsDog = {
                () -> haski.bear(),
                () -> haski.bark(),
                () -> haski.shoutName(),
                () -> haski.die()
        };

        Human person = new Human(name);

        Executable[] methodsHuman = {
                () -> person.bear(),
                () -> person.shoutName(),
                () -> person.die()
        };

        Alien not_human = new Alien(name);

        Executable[] methodsAlien = {
                () -> person.bear(),
                () -> person.shoutName(),
                () -> person.die()
        };

        System.out.println("Choose Dog/Human/Alien");
        String choose = input.nextLine();
        Executable[] methods = {};


        if (choose.equals("Dog")) {
            methods = methodsDog;
            System.out.println("\n--- Choose method ---");
            System.out.println("0 - bear, 1 - bark, 2 - shoutName, 3 - die");
        } else if (choose.equals("Human")) {
            methods = methodsHuman;
            System.out.println("\n--- Choose method ---");
            System.out.println("0 - bear, 1 - shoutName, 2 - die");
        } else if (choose.equals("Alien")) {
            methods = methodsAlien;
            System.out.println("\n--- Choose method ---");
            System.out.println("0 - bear, 1 - shoutName, 2 - die");
        }

        for (Executable method : methods) {
            method.execute();
        }

        int choice = input.nextInt();

        while (choice != -1) {
            if (choice >= 0 && choice < methods.length) {
                methods[choice].execute();
            }
            choice = input.nextInt();
        }


    }
}
