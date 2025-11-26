package Lab12;
import java.io.*;

public class Divide {
    public static void main(String[] args) {
        try (FileInputStream in = new FileInputStream("/Users/aydar/IdeaProjects/Intro/src/Lab12/input.txt")) {
            StringBuilder content = new StringBuilder();
            int byteData;

            while ((byteData = in.read()) != -1) {
                content.append((char) byteData);
            }

            String[] numberStrings = content.toString().trim().split("\\s+");

            if (numberStrings.length < 2) {
                System.out.println("Not enough numbers for operation");
                return;
            }

            int num1 = Integer.parseInt(numberStrings[0]);
            int num2 = Integer.parseInt(numberStrings[1]);

            if (num2 == 0) {
                throw new ArithmeticException("Divide by 0");
            }

            int result = num1 / num2;
            System.out.println(num1 + " / " + num2 + " = " + result);

        } catch (IOException | NumberFormatException | ArithmeticException e) {
            System.out.println(e.getMessage());
        }
    }
}