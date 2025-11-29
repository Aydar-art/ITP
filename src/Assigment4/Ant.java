package Assigment4;

import java.util.Map;

public class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {

    public Ant(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
        directions.add(Direction.N);
        directions.add(Direction.E);
        directions.add(Direction.S);
        directions.add(Direction.W);
        directions.add(Direction.NE);
        directions.add(Direction.SE);
        directions.add(Direction.SW);
        directions.add(Direction.NW);
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

            if (entity instanceof Insect otherInsect) {
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
            if (direction == Direction.N || direction == Direction.E ||
                    direction == Direction.S || direction == Direction.W) {
                int food = getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, size);
                if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
                    maxFood = food;
                    bestDir = direction;
                }
            }
        }

        for (Direction direction : directions) {
            if (direction == Direction.NE || direction == Direction.SE ||
                    direction == Direction.SW || direction == Direction.NW) {
                int food = getDiagonalDirectionVisibleValue(direction, entityPosition, boardData, size);
                if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
                    maxFood = food;
                    bestDir = direction;
                }
            }
        }

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        if (dir == Direction.N || dir == Direction.E || dir == Direction.S || dir == Direction.W) {
            return travelOrthogonally(dir, entityPosition, color, boardData, size);
        } else {
            return travelDiagonally(dir, entityPosition, boardData, size);
        }
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
            case NE -> 5;
            case SE -> 6;
            case SW -> 7;
            case NW -> 8;
            default -> 9;
        };
    }
}