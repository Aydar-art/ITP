package Assigment4;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<String, BoardEntity> boardData;
    private int size;

    public Board(int boardSize) {
        this.boardData = new HashMap<>();
        this.size = boardSize;
    }

    public void addEntity(BoardEntity entity) {
        boardData.put(entity.getEntityPosition().toString(), entity);
    }

    public BoardEntity getEntity(EntityPosition position) {
        return boardData.get(position.toString());
    }

    public Direction getDirection(Insect insect) {
        return insect.getBestDirection(boardData, size);
    }

    public int getDirectionSum(Insect insect) {
        Direction direction = getDirection(insect);
        return insect.travelDirection(direction, boardData, size);
    }

    public boolean isValidPosition(int x, int y) {
        return (0 <= x && x < size && 0 <= y && y < size);
    }
}
