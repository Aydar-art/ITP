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

//    public Direction getDirection(Insect insect) {
//
//    }
//
//    public int travelDirection(Insect insect, Direction direction) {
//        int foodEaten = 0;
//        EntityPosition currentPosition = insect.getEntityPosition();
//        while (currentPosition.isValid(size)) {
//            BoardEntity entity = getEntity(currentPosition);
//
//            if (entity instanceof FoodPoint) {
//                foodEaten += ((FoodPoint) entity).getValue();
//                boardData.remove(currentPosition.toString());
//            } else if (entity instanceof Insect && !((Insect) entity).getColor().equals(insect.getColor())) {
//                boardData.remove(insect.getEntityPosition().toString());
//                return foodEaten;
//            }
//            currentPosition = currentPosition.move(direction);
//        }
//
//        boardData.remove(insect.getEntityPosition().toString());
//        return foodEaten;
//    }

}
