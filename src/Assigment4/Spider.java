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

        if (dir == Direction.NE) {
            while(currentPos.addY(1, size) && currentPos.addX(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.SE) {
            while(currentPos.addY(-1, size) && currentPos.addX(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.SW) {
            while(currentPos.addY(-1, size) && currentPos.addX(-1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.NW) {
            while(currentPos.addY(1, size) && currentPos.addX(-1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        }

        return foodCount;
    }

    @Override
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while(currentPos.isValid(size)) {
            String positionKey = currentPos.getX() + "," + currentPos.getY();
            BoardEntity entity = boardData.get(positionKey);

            if (entity instanceof Insect) {
                Insect otherInsect = (Insect) entity;
                if (!otherInsect.getColor().equals(this.color)) {
                    break;
                }
            }

            if (entity instanceof FoodPoint) {
                foodEaten += ((FoodPoint) entity).getValue();
                boardData.remove(positionKey);
            }

            if (dir == Direction.NE) {
                if (!(currentPos.addY(1, size) && currentPos.addX(1, size))) break;
            } else if (dir == Direction.SE) {
                if (!(currentPos.addY(-1, size) && currentPos.addX(1, size))) break;
            } else if (dir == Direction.SW) {
                if (!(currentPos.addY(-1, size) && currentPos.addX(-1, size))) break;
            } else if (dir == Direction.NW) {
                if (!(currentPos.addY(1, size) && currentPos.addX(-1, size))) break;
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

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        return travelDiagonally(dir, entityPosition, boardData, size);
    }

    /**
     * Helper method to check direction priority according to the rules
     */
    private boolean hasHigherPriority(Direction newDir, Direction currentBestDir) {
        if (currentBestDir == null) return true;

        int newPriority = getDirectionPriority(newDir);
        int currentPriority = getDirectionPriority(currentBestDir);

        return newPriority < currentPriority;
    }

    private int getDirectionPriority(Direction dir) {
        return switch (dir) {
            case NE -> 1;
            case SE -> 2;
            case SW -> 3;
            case NW -> 4;
            default -> 5;
        };
    }
}