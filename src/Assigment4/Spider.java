package Assigment4;

import java.util.Map;

public class Spider extends Insect implements DiagonalMoving {

    public Spider(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
        directions.add(Direction.NE);
        directions.add(Direction.SE);
        directions.add(Direction.SW);
        directions.add(Direction.NW);
    }

    @Override
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        int foodCount = 0;

        while(true) {
            boolean moved = false;
            if (dir == Direction.SE) {
                moved = currentPos.addY(1, size) && currentPos.addX(1, size);
            } else if (dir == Direction.SW) {
                moved = currentPos.addY(-1, size) && currentPos.addX(1, size);
            } else if (dir == Direction.NW) {
                moved = currentPos.addY(-1, size) && currentPos.addX(-1, size);
            } else if (dir == Direction.NE) {
                moved = currentPos.addY(1, size) && currentPos.addX(-1, size);
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
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while(true) {
            boolean moved = false;
            if (dir == Direction.SE) {
                moved = currentPos.addY(1, size) && currentPos.addX(1, size);
            } else if (dir == Direction.SW) {
                moved = currentPos.addY(-1, size) && currentPos.addX(1, size);
            } else if (dir == Direction.NW) {
                moved = currentPos.addY(-1, size) && currentPos.addX(-1, size);
            } else if (dir == Direction.NE) {
                moved = currentPos.addY(1, size) && currentPos.addX(-1, size);
            }

            if (!moved) break;

            String positionKey = currentPos.getX() + "," + currentPos.getY();
            BoardEntity entity = boardData.get(positionKey);

            if (entity instanceof Insect otherInsect) {
                if (!otherInsect.getColor().equals(this.color)) {
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
            int food = getDiagonalDirectionVisibleValue(direction, entityPosition, boardData, size);
            if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
                maxFood = food;
                bestDir = direction;
            }
        }

        System.out.println("Spider" + bestDir);

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        return travelDiagonally(dir, entityPosition, boardData, size);
    }
}