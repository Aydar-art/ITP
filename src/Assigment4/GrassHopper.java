package Assigment4;

import java.util.Map;

public class GrassHopper extends Insect{
    public GrassHopper(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int size) {
        return null;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        return 0;
    }
}
