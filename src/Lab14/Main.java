package Lab14;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> alphabet = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int randLen = random.nextInt(10) + 2;

            String temp = "[A-Za-z0-9]{" + randLen + "}";

        }
    }

    private String getRandomString() {
        Random random = new Random();
        String s = "";
        int length = random.nextInt(10);

        for (int i = 0; i < length; i++) {
            boolean isDigit = random.nextBoolean();
            boolean isUpper = random.nextBoolean();

            if (isDigit) {
                s += random.nextInt(9).
            } else {
                if (isUpper) {

                }
            }
        }

        return s;
    }
}