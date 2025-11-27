package Assigment4;

import java.util.Map;

public class Spider extends Insect implements DiagonalMoving {

    public Spider(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
        return 0;
    }

    @Override
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
        return 0;
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
