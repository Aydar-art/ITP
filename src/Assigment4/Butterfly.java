package Assigment4;

import java.util.Map;

public class Butterfly extends Insect implements OrthogonalMoving {

    public Butterfly(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
        directions.add(Direction.N);
        directions.add(Direction.E);
        directions.add(Direction.S);
        directions.add(Direction.W);
    }

    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        int foodCount = 0;

        while(true) {
            boolean moved = false;
            if (dir == Direction.E) {
                moved = currentPos.addY(1, size);
            } else if (dir == Direction.S) {
                moved = currentPos.addX(1, size);
            } else if (dir == Direction.W) {
                moved = currentPos.addY(-1, size);
            } else if (dir == Direction.N) {
                moved = currentPos.addX(-1, size);
            }

            if (!moved) break;

            String positionKey = currentPos.getX() + "," + currentPos.getY();
            BoardEntity entity = boardData.get(positionKey);

            if (entity instanceof FoodPoint) {
                foodCount += ((FoodPoint) entity).getValue();
            }
        }

        return foodCount;
    }

    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while(true) {
            boolean moved = false;
            if (dir == Direction.E) {
                moved = currentPos.addY(1, size);
            } else if (dir == Direction.S) {
                moved = currentPos.addX(1, size);
            } else if (dir == Direction.W) {
                moved = currentPos.addY(-1, size);
            } else if (dir == Direction.N) {
                moved = currentPos.addX(-1, size);
            }

            if (!moved) break;

            String positionKey = currentPos.getX() + "," + currentPos.getY();
            BoardEntity entity = boardData.get(positionKey);

            if (entity instanceof Insect otherInsect) {
                if (!otherInsect.getColor().equals(color)) {
                    break;
                }
            }

            if (entity instanceof FoodPoint) {
                foodEaten += ((FoodPoint) entity).getValue();
                boardData.remove(positionKey);
            }
        }

        return foodEaten;
    }

    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int size) {
        Direction bestDir = null;
        int maxFood = -1;

        for (Direction direction : directions) {
            int food = getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, size);
            if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
                maxFood = food;
                bestDir = direction;
            }
        }

        System.out.println("Butterfly" + bestDir);

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        return travelOrthogonally(dir, entityPosition, color, boardData, size);
    }

}