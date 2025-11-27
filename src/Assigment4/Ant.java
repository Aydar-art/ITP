package Assigment4;

import java.util.Map;

public class Ant extends Insect implements OrthogonalMoving, DiagonalMoving{

    public Ant(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
        return 0;
    }

    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String, BoardEntity> boardData, int size) {
        return 0;
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
        Direction bestDir = directions.getFirst();
        int maxFood = -1;
        for (Direction direction : directions) {
            int food = getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, size);
            if (food > maxFood) {
                maxFood = food;
                bestDir = direction;
            }


        }

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
//        int foodEaten = 0;
//        EntityPosition currentPosition = getEntityPosition();
//        while (currentPosition.isValid(size)) {
//            BoardEntity entity = getEntity(currentPosition);
//
//            if (entity instanceof FoodPoint) {
//                foodEaten += ((FoodPoint) entity).getValue();
//                boardData.remove(currentPosition.toString());
//            } else if (entity instanceof Insect && !((Insect) entity).getColor().equals(getColor())) {
//                boardData.remove(getEntityPosition().toString());
//                return foodEaten;
//            }
////            currentPosition = currentPosition.move(direction);
//        }
//
//        boardData.remove(getEntityPosition().toString());
//        return foodEaten;
    }
}
