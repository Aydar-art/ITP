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

        if (dir == Direction.N) {
            while(currentPos.addY(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.E) {
            while(currentPos.addX(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.S) {
            while(currentPos.addY(-1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.W) {
            while(currentPos.addX(-1, size)) {
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

            if (entity instanceof Insect) {
                Insect otherInsect = (Insect) entity;
                if (!otherInsect.getColor().equals(color)) {
                    break;
                }
            }

            if (entity instanceof FoodPoint) {
                foodEaten += ((FoodPoint) entity).getValue();
                boardData.remove(positionKey);
            }

            if (dir == Direction.N) {
                if (!currentPos.addY(1, size)) break;
            } else if (dir == Direction.E) {
                if (!currentPos.addX(1, size)) break;
            } else if (dir == Direction.S) {
                if (!currentPos.addY(-1, size)) break;
            } else if (dir == Direction.W) {
                if (!currentPos.addX(-1, size)) break;
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

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        return travelOrthogonally(dir, entityPosition, color, boardData, size);
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
            case N -> 1;
            case E -> 2;
            case S -> 3;
            case W -> 4;
            default -> 5;
        };
    }
}