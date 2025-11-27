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

    public void addEntity(String title, BoardEntity entity) {
        boardData.put(title, entity);
    }

    public BoardEntity getEntity(EntityPosition position) {

    }


}
