package Assigment4;

public enum InsectColor {
    RED,
    GREEN,
    BLUE,
    YELLOW;

    public static InsectColor toColor(String s) {
        return InsectColor.valueOf(s.toUpperCase());
    }
}