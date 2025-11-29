package Assigment4;

import java.util.Map;

public class GrassHopper extends Insect implements OrthogonalMoving {

    public GrassHopper(EntityPosition entityPosition, InsectColor color) {
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

        if (dir == Direction.E) {
            while(jumpPosition(currentPos, 0, 2, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.S) {
            while(jumpPosition(currentPos, 2, 0, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.W) {
            while(jumpPosition(currentPos, 0, -2, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.N) {
            while(jumpPosition(currentPos, -2, 0, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        }

        return foodCount;
    }

    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while(currentPos.isValid(size)) {
            String positionKey = currentPos.getX() + "," + currentPos.getY();
            BoardEntity entity = boardData.get(positionKey);

            if (entity instanceof FoodPoint) {
                foodEaten += ((FoodPoint) entity).getValue();
                boardData.remove(positionKey);
            }

            EntityPosition nextPos = new EntityPosition();
            nextPos.entityPosition(currentPos.getX(), currentPos.getY());

            boolean canContinue = false;
            if (dir == Direction.E) {
                canContinue = jumpPosition(nextPos, 0, 2, size);
            } else if (dir == Direction.S) {
                canContinue = jumpPosition(nextPos, 2, 0, size);
            } else if (dir == Direction.W) {
                canContinue = jumpPosition(nextPos, 0, -2, size);
            } else if (dir == Direction.N) {
                canContinue = jumpPosition(nextPos, -2, 0, size);
            }

            if (!canContinue) {
                break;
            }

            String nextPositionKey = nextPos.getX() + "," + nextPos.getY();
            BoardEntity nextEntity = boardData.get(nextPositionKey);
            if (nextEntity instanceof Insect) {
                Insect otherInsect = (Insect) nextEntity;
                if (!otherInsect.getColor().equals(color)) {
                    break;
                }
            }

            currentPos.entityPosition(nextPos.getX(), nextPos.getY());
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

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        return travelOrthogonally(dir, entityPosition, color, boardData, size);
    }

    private boolean jumpPosition(EntityPosition currentPos, int deltaX, int deltaY, int size) {
        int newX = currentPos.getX() + deltaX;
        int newY = currentPos.getY() + deltaY;

        currentPos.entityPosition(newX, newY);
        return currentPos.isValid(size);
    }

}