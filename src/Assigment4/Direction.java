package Assigment4;


public enum Direction {
    N("North"),
    E("East"),
    S("South"),
    W("West"),
    NE("North-East"),
    SE("South-East"),
    SW("South-West"),
    NW("North-West");

    private final String textRepresentation;

    Direction(String text) {
        this.textRepresentation = text;
    }

    public String getTextRepresentation() {
        return textRepresentation;
    }

    public static Direction fromText(String text) {
        for (Direction direction : Direction.values()) {
            if (direction.textRepresentation.equals(text)) {
                return direction;
            }
        }
        return null;
    }
}