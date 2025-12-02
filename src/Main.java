// ai.gaifullin@innopolis.university

import java.io.FileWriter;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Collections;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Main class for the Insect Board Game simulation program.
 *
 * This program simulates game where different types of insects move on board
 * to collect food points. Each insect type has unique movement patterns and behaviors.
 *
 * INPUT FORMAT (input.txt):
 * 1. Board size (N)
 * 2. Number of insects
 * 3. Number of food points
 * 4. Insect data (color, type, x, y for each insect)
 * 5. Food point data (value, x, y for each food point)
 *
 * OUTPUT FORMAT (output.txt):
 * For each insect in input order:
 *   Color Type Direction FoodEaten
 *
 *  EXCEPTIONS AND LIMITS:
 *  - Limit board size x (4 <= x <= 1000)
 *  - Limit number of insects x (1 <= x <= 16)
 *  - Limit number of food points x (1 <= x <= 200)
 *  - Valid insect color (BLUE, RED, YELLOW, GREEN)
 *  - Valid insect type (Ant, Butterfly, Grasshopper, Spider)
 *  - Valid entity position (within size of the board)
 *  - Unable to duplicate insects
 *  - Unable two entities on same position
 *
 * INSECT TYPES AND MOVEMENT PATTERNS:
 * - Ant: Moves orthogonally (N, E, S, W) and diagonally (NE, SE, SW, NW)
 * - Spider: Moves only diagonally (NE, SE, SW, NW)
 * - Grasshopper: Jumps 2 squares orthogonally (N, E, S, W)
 * - Butterfly: Moves orthogonally (N, E, S, W) one square at time
 *
 * MOVEMENT RULES:
 * - Insects stop moving when encountering board boundaries
 * - Insects stop when encountering another insect of different color
 * - Insects can pass through insects of the same color
 * - Collected food points are removed from the board
 *
 * DIRECTION PRIORITY (for tie-breaking):
 * 1. North (N)
 * 2. East (E)
 * 3. South (S)
 * 4. West (W)
 * 5. North-East (NE)
 * 6. South-East (SE)
 * 7. South-West (SW)
 * 8. North-West (NW)
 *
 * PRIMARY METHODS:
 * - main(): calls method startGo
 * - startGo(String inputFileName):
 *      Read data from "input.txt"
 *      Check if input is valid
 *      Write to Board and List
 *
 * PRIMARY OBJECTS:
 * - Direction <enum>
 * - EntityPosition --> Insect | FoodPoint
 * - Insect <abstract> --> Ant | Butterfly | Grasshopper | Spider
 * - DiagonalMoving <interface> --> Ant | Spider
 * - OrthogonalMoving <interface> --> Ant | Butterfly
 * - InsectColor <enum> --> Insect
 *
 * INSECT METHODS:
 * - getBestDirection(boardData, boardSize):
 *      Finds best possible direction with the biggest count of food
 *      return direction <Direction>
 * - travelDirection(direction, boardData, boardSize):
 *      Goes within given direction while left board or die
 *      return countOfEatenFood <int>
 *
 *  DIOGONALMOVING METHODS:
 *  - getDiagonalDirectionVisibleValue(direction, entityPosition, boardData, boardSize):
 *      Checks count of food on current direction
 *      return countOfFood <int>
 *  - travelDiagonally(direction, entityPosition, boardData, boardSize)
 *      Travel within current direction while left board or die
 *      return countOfFood <int>
 *
 *  ORTHOGONALMOVING METHODS:
 *  *  - getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, boardSize):
 *  *      Checks count of food on current direction
 *  *      return countOfFood <int>
 *  *  - travelOrthogonally(direction, entityPosition, boardData, boardSize)
 *  *      Travel within current direction while left board or die
 *  *      return countOfFood <int>
 */

public class Main {
    static final int MIN_BOARD_SIZE = 4;
    static final int MAX_BOARD_SIZE = 1000;
    static final int MAX_INSECTS = 16;
    static final int MAX_FOOD_POINTS = 200;

    static class InvalidBoardSizeException extends Exception {
        public InvalidBoardSizeException() {
            super("Invalid board size");
        }
    }

    static class InvalidNumberOfInsectsException extends Exception {
        public InvalidNumberOfInsectsException() {
            super("Invalid number of insects");
        }
    }

    static class InvalidNumberOfFoodPointsException extends Exception {
        public InvalidNumberOfFoodPointsException() {
            super("Invalid number of food points");
        }
    }

    static class InvalidInsectColorException extends Exception {
        public InvalidInsectColorException() {
            super("Invalid insect color");
        }
    }

    static class InvalidInsectTypeException extends Exception {
        public InvalidInsectTypeException() {
            super("Invalid insect type");
        }
    }

    static class InvalidEntityPositionException extends Exception {
        public InvalidEntityPositionException() {
            super("Invalid entity position");
        }
    }

    static class DuplicateInsectException extends Exception {
        public DuplicateInsectException() {
            super("Duplicate insects");
        }
    }

    static class TwoEntitiesOnSamePositionException extends Exception {
        public TwoEntitiesOnSamePositionException() {
            super("Two entities in the same position");
        }
    }

    public static void main(String[] args) {
        try {
            List<String> results = startGo("input.txt");
            writeOutput(results);
        } catch (Exception e) {
            try {
                writeOutput(Collections.singletonList(e.getMessage()));
            } catch (IOException ioException) {
                System.err.println("Error writing output: " + ioException.getMessage());
            }
        }
    }

    public static List<String> startGo(String inputFilename)
            throws InvalidBoardSizeException, InvalidNumberOfInsectsException,
            InvalidNumberOfFoodPointsException, InvalidInsectColorException,
            InvalidInsectTypeException, InvalidEntityPositionException,
            DuplicateInsectException, TwoEntitiesOnSamePositionException {

        Board board = null;
        List<Insect> insectsInOrder = new ArrayList<>();
        Map<String, String> occupiedPositions = new HashMap<>();
        Map<String, Integer> insectTypeCount = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(inputFilename))) {
            if (!scanner.hasNextInt()) {
                throw new InvalidBoardSizeException();
            }
            int boardSize = scanner.nextInt();



            if (boardSize < MIN_BOARD_SIZE || boardSize > MAX_BOARD_SIZE) {
                throw new InvalidBoardSizeException();
            }
            board = new Board(boardSize);

            if (!scanner.hasNextInt()) {
                throw new InvalidNumberOfInsectsException();
            }
            int numInsects = scanner.nextInt();
            if (numInsects < 1 || numInsects > MAX_INSECTS) {
                throw new InvalidNumberOfInsectsException();
            }

            if (!scanner.hasNextInt()) {
                throw new InvalidNumberOfFoodPointsException();
            }
            int numFoodPoints = scanner.nextInt();
            if (numFoodPoints < 1 || numFoodPoints > MAX_FOOD_POINTS) {
                throw new InvalidNumberOfFoodPointsException();
            }

            int countOfIncests = 0;
            for (int i = 0; i < numInsects; i++) {
                if (!scanner.hasNext()) {
                    break;
                }

                String colorStr = scanner.next();
                String typeStr = scanner.next();
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                InsectColor color;
                try {
                    color = InsectColor.toColor(colorStr);
                } catch (IllegalArgumentException e) {
                    throw new InvalidInsectColorException();
                }

                if (!isValidInsectType(typeStr)) {
                    throw new InvalidInsectTypeException();
                }

                int boardX = x - 1;
                int boardY = y - 1;
                if (board.isValidPosition(boardX, boardY)) {
                    throw new InvalidEntityPositionException();
                }

                String positionKey = x + "," + y;
                if (occupiedPositions.containsKey(positionKey)) {
                    throw new TwoEntitiesOnSamePositionException();
                }

                EntityPosition position = createPosition(x, y);
                Insect insect = createInsect(typeStr, position, color);

                String insectKey = color + "_" + typeStr.toLowerCase();
                insectTypeCount.put(insectKey, insectTypeCount.getOrDefault(insectKey, 0) + 1);
                if (insectTypeCount.get(insectKey) > 1) {
                    throw new DuplicateInsectException();
                }

                board.addEntity(insect);
                occupiedPositions.put(positionKey, "insect");
                insectsInOrder.add(insect);

                countOfIncests++;
            }

            if (countOfIncests != numInsects) {
                throw new InvalidNumberOfInsectsException();
            }

            int countOfFood = 0;
            for (int i = 0; i < numFoodPoints; i++) {
                if (!scanner.hasNext()) {
                    break;
                }

                int foodValue = scanner.nextInt();
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                int boardX = x - 1;
                int boardY = y - 1;
                if (board.isValidPosition(boardX, boardY)) {
                    throw new InvalidEntityPositionException();
                }

                String positionKey = x + "," + y;
                if (occupiedPositions.containsKey(positionKey)) {
                    throw new TwoEntitiesOnSamePositionException();
                }

                EntityPosition position = createPosition(x, y);
                FoodPoint foodPoint = new FoodPoint(position, foodValue);

                board.addEntity(foodPoint);
                occupiedPositions.put(positionKey, "food");
                countOfFood++;
            }

            if (countOfFood != numFoodPoints) {
                throw new InvalidNumberOfFoodPointsException();
            }

        } catch (FileNotFoundException e) {
            throw new InvalidEntityPositionException();
        }


        List<String> results = new ArrayList<>();

        for (Insect insect : insectsInOrder) {
            EntityPosition pos = insect.getEntityPosition();
            BoardEntity entity = board.getEntity(pos);
            if (entity != insect) {
                continue;
            }

            Direction bestDirection = board.getDirection(insect);
            int foodEaten = board.getDirectionSum(insect);

            String result = formatResult(insect, bestDirection, foodEaten);
            results.add(result);
        }

        return results;
    }

    private static boolean isValidInsectType(String type) {
        return type.equalsIgnoreCase("Ant")
                || type.equalsIgnoreCase("Butterfly")
                || type.equalsIgnoreCase("Spider")
                || type.equalsIgnoreCase("Grasshopper");
    }

    private static EntityPosition createPosition(int x, int y) {
        EntityPosition position = new EntityPosition();
        position.entityPosition(x - 1, y - 1);
        return position;
    }

    private static Insect createInsect(String type, EntityPosition position, InsectColor color) {
        return switch (type.toLowerCase()) {
            case "ant" -> new Ant(position, color);
            case "butterfly" -> new Butterfly(position, color);
            case "spider" -> new Spider(position, color);
            case "grasshopper" -> new Grasshopper(position, color);
            default -> throw new IllegalArgumentException("Unknown insect type");
        };
    }

    private static String formatResult(Insect insect, Direction direction, int foodEaten) {
        String colorName = insect.getColor().toString().toLowerCase();

        if (colorName.isEmpty()) {
            return null;
        }
        String color = colorName.substring(0, 1).toUpperCase() + colorName.substring(1).toLowerCase();
        String type = insect.getClass().getSimpleName();
        String directionName = direction.getTextRepresentation();

        return color + " " + type + " " + directionName + " " + foodEaten;
    }

    private static void writeOutput(List<String> lines) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))) {
            for (String line : lines) {
                writer.println(line);
            }
        }
    }
}

class EntityPosition {
    private int x;
    private int y;

    public void entityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean addX(int delta, int boardSize) {
        x += delta;
        return isValid(boardSize);
    }

    public boolean addY(int delta, int boardSize) {
        y += delta;
        return isValid(boardSize);
    }

    public boolean isValid(int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}

enum Direction {
    N("North"),
    E("East"),
    S("South"),
    W("West"),
    NE("North-East"),
    SE("South-East"),
    SW("South-West"),
    NW("North-West");

    private final String textRepresentation;

    Direction(String text) {
        this.textRepresentation = text;
    }

    public String getTextRepresentation() {
        return textRepresentation;
    }

    public static Direction fromText(String text) {
        for (Direction direction : Direction.values()) {
            if (direction.textRepresentation.equals(text)) {
                return direction;
            }
        }
        return null;
    }
}

enum InsectColor {
    RED,
    GREEN,
    BLUE,
    YELLOW;

    public static InsectColor toColor(String s) {
        return InsectColor.valueOf(s.toUpperCase());
    }
}

class FoodPoint extends BoardEntity {
    private int value;

    public FoodPoint(EntityPosition position, int value) {
        this.entityPosition = position;
        this.value = value;
    }

    public EntityPosition getPosition() {
        return entityPosition;
    }

    public int getValue() {
        return value;
    }
}

@SuppressWarnings("checkstyle:LineLength")
interface DiagonalMoving {
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                Map<String, BoardEntity> boardData, int size);

    public int travelDiagonally(Direction dir, EntityPosition entityPosition,
                                Map<String, BoardEntity> boardData, int size);
}

interface OrthogonalMoving {
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Map<String, BoardEntity> boardData, int size);
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                  Map<String, BoardEntity> boardData, int size);
}

abstract class Insect extends BoardEntity {
    static final int PRIORITY_S = 3;
    static final int PRIORITY_W = 4;
    static final int PRIORITY_NE = 5;
    static final int PRIORITY_SE = 6;
    static final int PRIORITY_SW = 7;
    static final int PRIORITY_NW = 8;
    protected InsectColor color;
    protected List<Direction> directions = new ArrayList<>();

    public Insect(EntityPosition entityPosition, InsectColor color) {
        this.entityPosition = entityPosition;
        this.color = color;
    }

    public abstract Direction getBestDirection(Map<String, BoardEntity> boardData, int size);

    public abstract int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize);

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public InsectColor getColor() {
        return color;
    }

    public int getDirectionPriority(Direction dir) {
        return switch (dir) {
            case N -> 1;
            case E -> 2;
            case S -> PRIORITY_S;
            case W -> PRIORITY_W;
            case NE -> PRIORITY_NE;
            case SE -> PRIORITY_SE;
            case SW -> PRIORITY_SW;
            case NW -> PRIORITY_NW;
        };
    }

    protected boolean hasHigherPriority(Direction newDir, Direction currentBestDir) {
        if (currentBestDir == null) {
            return true;
        }

        int newPriority = getDirectionPriority(newDir);
        int currentPriority = getDirectionPriority(currentBestDir);

        return newPriority < currentPriority;
    }
}

class Spider extends Insect implements DiagonalMoving {

    public Spider(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
        directions.add(Direction.NE);
        directions.add(Direction.SE);
        directions.add(Direction.SW);
        directions.add(Direction.NW);
    }

    @Override
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                Map<String, BoardEntity> boardData, int size) {
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        int foodCount = 0;

        while (true) {
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

            if (!moved) {
                break;
            }

            String positionKey = currentPos.getX() + "," + currentPos.getY();
            BoardEntity entity = boardData.get(positionKey);

            if (entity instanceof FoodPoint) {
                foodCount += ((FoodPoint) entity).getValue();
            }
        }

        return foodCount;
    }

    @Override
    public int travelDiagonally(Direction dir, EntityPosition entityPosition,
                                Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while (true) {
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

            if (!moved) {
                break;
            }

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

//        System.out.println("Spider" + bestDir);

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        return travelDiagonally(dir, entityPosition, boardData, size);
    }
}

class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {

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
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Map<String, BoardEntity> boardData, int size) {
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        int foodCount = 0;

        if (dir == Direction.N) {
            while (currentPos.addX(-1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.E) {
            while (currentPos.addY(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.S) {
            while (currentPos.addX(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.W) {
            while (currentPos.addY(-1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        }

//        System.out.println(foodCount + " " + dir);

        return foodCount;
    }

    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                  Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while (currentPos.isValid(size)) {
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
                if (!currentPos.addX(-1, size)) {
                    break;
                }
            } else if (dir == Direction.E) {
                if (!currentPos.addY(1, size)) {
                    break;
                }
            } else if (dir == Direction.S) {
                if (!currentPos.addX(1, size)) {
                    break;
                }
            } else if (dir == Direction.W) {
                if (!currentPos.addY(-1, size)) {
                    break;
                }
            }
        }

        return foodEaten;
    }

    @Override
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                Map<String, BoardEntity> boardData, int size) {
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        int foodCount = 0;

        if (dir == Direction.NE) {
            while (currentPos.addX(-1, size) && currentPos.addY(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.SE) {
            while (currentPos.addX(1, size) && currentPos.addY(1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.SW) {
            while (currentPos.addX(1, size) && currentPos.addY(-1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.NW) {
            while (currentPos.addX(-1, size) && currentPos.addY(-1, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        }

//        System.out.println(foodCount + " " + dir);

        return foodCount;
    }

    @Override
    public int travelDiagonally(Direction dir, EntityPosition entityPosition,
                                Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while (currentPos.isValid(size)) {
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

            if (dir == Direction.SW) {
                if (!(currentPos.addY(-1, size) && currentPos.addX(1, size))) {
                    break;
                }
            } else if (dir == Direction.NW) {
                if (!(currentPos.addY(-1, size) && currentPos.addX(-1, size))) {
                    break;
                }
            } else if (dir == Direction.NE) {
                if (!(currentPos.addX(-1, size) && currentPos.addY(1, size))) {
                    break;
                }
            } else if (dir == Direction.SE) {
                if (!(currentPos.addY(1, size) && currentPos.addX(1, size))) {
                    break;
                }
            }
        }

        return foodEaten;
    }

    @Override
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int size) {
        Direction bestDir = null;
        int maxFood = -1;

        for (Direction direction : directions) {
            if (direction == Direction.N || direction == Direction.E
                    || direction == Direction.S || direction == Direction.W) {
                int food = getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, size);
                if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
                    maxFood = food;
                    bestDir = direction;
                }
            }
        }

//        System.out.println("Ant" + bestDir);

        for (Direction direction : directions) {
            if (direction == Direction.NE || direction == Direction.SE
                    || direction == Direction.SW || direction == Direction.NW) {
                int food = getDiagonalDirectionVisibleValue(direction, entityPosition, boardData, size);
                if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
                    maxFood = food;
                    bestDir = direction;
                }
            }
        }

//        System.out.println("Ant" + bestDir);

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
}

class Grasshopper extends Insect implements OrthogonalMoving {
    static final int GRASSHOPPER_JUMP_DISTANCE = 2;

    public Grasshopper(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
        directions.add(Direction.N);
        directions.add(Direction.E);
        directions.add(Direction.S);
        directions.add(Direction.W);
    }

    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Map<String, BoardEntity> boardData, int size) {
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        int foodCount = 0;

        if (dir == Direction.E) {
            while (jumpPosition(currentPos, 0, GRASSHOPPER_JUMP_DISTANCE, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.S) {
            while (jumpPosition(currentPos, GRASSHOPPER_JUMP_DISTANCE, 0, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.W) {
            while (jumpPosition(currentPos, 0, -GRASSHOPPER_JUMP_DISTANCE, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        } else if (dir == Direction.N) {
            while (jumpPosition(currentPos, -GRASSHOPPER_JUMP_DISTANCE, 0, size)) {
                BoardEntity entity = boardData.get(currentPos.getX() + "," + currentPos.getY());
                if (entity instanceof FoodPoint) {
                    foodCount += ((FoodPoint) entity).getValue();
                }
            }
        }

        return foodCount;
    }

    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition,
                                  InsectColor color, Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while (currentPos.isValid(size)) {
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
                canContinue = jumpPosition(nextPos, 0, GRASSHOPPER_JUMP_DISTANCE, size);
            } else if (dir == Direction.S) {
                canContinue = jumpPosition(nextPos, GRASSHOPPER_JUMP_DISTANCE, 0, size);
            } else if (dir == Direction.W) {
                canContinue = jumpPosition(nextPos, 0, -GRASSHOPPER_JUMP_DISTANCE, size);
            } else if (dir == Direction.N) {
                canContinue = jumpPosition(nextPos, -GRASSHOPPER_JUMP_DISTANCE, 0, size);
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

class Butterfly extends Insect implements OrthogonalMoving {

    public Butterfly(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
        directions.add(Direction.N);
        directions.add(Direction.E);
        directions.add(Direction.S);
        directions.add(Direction.W);
    }

    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Map<String, BoardEntity> boardData, int size) {
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        int foodCount = 0;

        while (true) {
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

            if (!moved) {
                break;
            }

            String positionKey = currentPos.getX() + "," + currentPos.getY();
            BoardEntity entity = boardData.get(positionKey);

            if (entity instanceof FoodPoint) {
                foodCount += ((FoodPoint) entity).getValue();
            }
        }

        return foodCount;
    }

    @Override
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                  Map<String, BoardEntity> boardData, int size) {
        int foodEaten = 0;
        EntityPosition currentPos = new EntityPosition();
        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());

        while (true) {
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

            if (!moved) {
                break;
            }

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

//        System.out.println("Butterfly" + bestDir);

        return bestDir;
    }

    @Override
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
        boardData.remove(startPosition);

        return travelOrthogonally(dir, entityPosition, color, boardData, size);
    }
}

abstract class BoardEntity {
    protected EntityPosition entityPosition;

    public EntityPosition getEntityPosition() {
        return entityPosition;
    }
}

class Board {
    private Map<String, BoardEntity> boardData;
    private int size;

    public Board(int boardSize) {
        this.boardData = new HashMap<>();
        this.size = boardSize;
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
        Direction direction = getDirection(insect);
        return insect.travelDirection(direction, boardData, size);
    }

    public boolean isValidPosition(int x, int y) {
        return (0 > x || x >= size || 0 > y || y >= size);
    }
}
