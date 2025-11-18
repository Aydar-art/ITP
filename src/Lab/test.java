package Lab;

import java.util.Scanner;
import Lab.Lecture.*;

enum Menu {
    Nothing("Nan", 0),
    Water("Water", 3),
    Tatar_tea("Tatar tea", 5),
    Juice("Juice", 10);

    private final String name;
    private final int price;

    Menu(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void printInfo() {
        System.out.println(name + " " + price + "$");
    }
}

public class test {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("----PRICE LIST----\n1. Water - 3$\n2. Tatar Tea - 5$\n3. Juice - 10$\nInsert money");

        int money = getValidNumber(input);

        System.out.println("Choose position of drink");
        int drinkChoice = getValidNumber(input);

        Menu selectedDrink = getDrinkByChoice(drinkChoice);

        if (selectedDrink != null) {
            if (money >= selectedDrink.getPrice()) {
                int change = money - selectedDrink.getPrice();
                System.out.println("You bought: ");
                selectedDrink.printInfo();
                System.out.println("Your change: " + (change - change % 5) + "$");
            } else {
                System.out.println("Not enough money! You need " +
                        (selectedDrink.getPrice() - money) + "$ more");
            }
        } else {
            System.out.println("Invalid drink selection!");
        }

        input.close();
    }

    private static int getValidNumber(Scanner input) {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("It is not correct input. Please enter a number:");
            }
        }
    }

    private static Menu getDrinkByChoice(int choice) {
        switch (choice) {
            case 1: return Menu.Water;
            case 2: return Menu.Tatar_tea;
            case 3: return Menu.Juice;
            default: return null;
        }
    }
}
