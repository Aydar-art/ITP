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
}