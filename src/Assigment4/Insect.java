package Assigment4;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public abstract class Insect extends BoardEntity{
    protected InsectColor color;
    protected List<Direction> directions = new ArrayList<>();

    public Insect(EntityPosition entityPosition, InsectColor color) {
        this.entityPosition = entityPosition;
        this.color = color;
    }

    public abstract Direction getBestDirection(Map<String, BoardEntity> boardData, int size);

    public abstract int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize);

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public InsectColor getColor() {
        return color;
    }

    public int getDirectionPriority(Direction dir) {
        return switch (dir) {
            case N -> 1;
            case E -> 2;
            case S -> 3;
            case W -> 4;
            case NE -> 5;
            case SE -> 6;
            case SW -> 7;
            case NW -> 8;
            default -> 9;
        };
    }

    protected boolean hasHigherPriority(Direction newDir, Direction currentBestDir) {
        if (currentBestDir == null) return true;

        int newPriority = getDirectionPriority(newDir);
        int currentPriority = getDirectionPriority(currentBestDir);

        return newPriority < currentPriority;
    }
}