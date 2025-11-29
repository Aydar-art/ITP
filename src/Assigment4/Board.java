package Assigment4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<String, BoardEntity> boardData = new HashMap<>();
    private int size;

    public void Board(int size) {
        this.boardData = new HashMap<>();
        this.size = size;
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
        return insect.travelDirection(insect.getBestDirection(boardData, size), boardData, size);
    }

}
