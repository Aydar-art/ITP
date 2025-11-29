//import java.io.*;
//import java.util.*;
//
//public class Main {
//
//    static class InvalidBoardSizeException extends Exception {
//        public InvalidBoardSizeException() { super("Invalid board size"); }
//    }
//
//    static class InvalidNumberOfInsectsException extends Exception {
//        public InvalidNumberOfInsectsException() { super("Invalid number of insects"); }
//    }
//
//    static class InvalidNumberOfFoodPointsException extends Exception {
//        public InvalidNumberOfFoodPointsException() { super("Invalid number of food points"); }
//    }
//
//    static class InvalidInsectColorException extends Exception {
//        public InvalidInsectColorException() { super("Invalid insect color"); }
//    }
//
//    static class InvalidInsectTypeException extends Exception {
//        public InvalidInsectTypeException() { super("Invalid insect type"); }
//    }
//
//    static class InvalidEntityPositionException extends Exception {
//        public InvalidEntityPositionException() { super("Invalid entity position"); }
//    }
//
//    static class DuplicateInsectException extends Exception {
//        public DuplicateInsectException() { super("Duplicate insects"); }
//    }
//
//    static class TwoEntitiesOnSamePositionException extends Exception {
//        public TwoEntitiesOnSamePositionException() { super("Two entities in the same position"); }
//    }
//
//    public static void main(String[] args) {
//        try {
//            List<String> results = processGame("input.txt");
//            writeOutput("output.txt", results);
//        } catch (Exception e) {
//            try {
//                writeOutput("output.txt", Collections.singletonList(e.getMessage()));
//            } catch (IOException ioException) {
//                System.err.println("Error writing output: " + ioException.getMessage());
//            }
//        }
//    }
//
//    public static List<String> processGame(String inputFilename)
//            throws Assigment4.Main.InvalidBoardSizeException, Assigment4.Main.InvalidNumberOfInsectsException,
//            Assigment4.Main.InvalidNumberOfFoodPointsException, Assigment4.Main.InvalidInsectColorException,
//            Assigment4.Main.InvalidInsectTypeException, Assigment4.Main.InvalidEntityPositionException,
//            Assigment4.Main.DuplicateInsectException, Assigment4.Main.TwoEntitiesOnSamePositionException {
//
//        Assigment4.Board board = null;
//        List<Assigment4.Insect> insectsInOrder = new ArrayList<>();
//        Map<String, String> occupiedPositions = new HashMap<>();
//        Map<String, Integer> insectTypeCount = new HashMap<>();
//
//        try (Scanner scanner = new Scanner(new File(inputFilename))) {
//            // Read board size
//            if (!scanner.hasNextInt()) {
//                throw new Assigment4.Main.InvalidBoardSizeException();
//            }
//            int boardSize = scanner.nextInt();
//            if (boardSize < 4 || boardSize > 1000) {
//                throw new Assigment4.Main.InvalidBoardSizeException();
//            }
//            board = new Assigment4.Board(boardSize);
//
//            // Read number of insects
//            if (!scanner.hasNextInt()) {
//                throw new Assigment4.Main.InvalidNumberOfInsectsException();
//            }
//            int numInsects = scanner.nextInt();
//            if (numInsects < 1 || numInsects > 16) {
//                throw new Assigment4.Main.InvalidNumberOfInsectsException();
//            }
//
//            // Read number of food points
//            if (!scanner.hasNextInt()) {
//                throw new Assigment4.Main.InvalidNumberOfFoodPointsException();
//            }
//            int numFoodPoints = scanner.nextInt();
//            if (numFoodPoints < 1 || numFoodPoints > 200) {
//                throw new Assigment4.Main.InvalidNumberOfFoodPointsException();
//            }
//
//            // Read insects
//            for (int i = 0; i < numInsects; i++) {
//                if (!scanner.hasNext()) break;
//
//                String colorStr = scanner.next();
//                String typeStr = scanner.next();
//                int x = scanner.nextInt();
//                int y = scanner.nextInt();
//
//                // Validate color
//                Assigment4.InsectColor color;
//                try {
//                    color = Assigment4.InsectColor.toColor(colorStr);
//                } catch (IllegalArgumentException e) {
//                    throw new Assigment4.Main.InvalidInsectColorException();
//                }
//
//                // Validate type
//                if (!isValidInsectType(typeStr)) {
//                    throw new Assigment4.Main.InvalidInsectTypeException();
//                }
//
//                // Validate position (convert to 0-based for board check)
//                int boardX = x - 1;
//                int boardY = y - 1;
//                if (!board.isValidPosition(boardX, boardY)) {
//                    throw new Assigment4.Main.InvalidEntityPositionException();
//                }
//
//                // Check for duplicate position
//                String positionKey = x + "," + y;
//                if (occupiedPositions.containsKey(positionKey)) {
//                    throw new Assigment4.Main.TwoEntitiesOnSamePositionException();
//                }
//
//                // Create entity position and insect
//                Assigment4.EntityPosition position = createPosition(x, y);
//                Assigment4.Insect insect = createInsect(typeStr, position, color);
//
//                // Check for duplicate insects (same color and type)
//                String insectKey = color + "_" + typeStr.toLowerCase();
//                insectTypeCount.put(insectKey, insectTypeCount.getOrDefault(insectKey, 0) + 1);
//                if (insectTypeCount.get(insectKey) > 1) {
//                    throw new Assigment4.Main.DuplicateInsectException();
//                }
//
//                // Add to board and tracking maps
//                board.addEntity(insect);
//                occupiedPositions.put(positionKey, "insect");
//                insectsInOrder.add(insect);
//            }
//
//            // Read food points
//            for (int i = 0; i < numFoodPoints; i++) {
//                if (!scanner.hasNext()) break;
//
//                int foodValue = scanner.nextInt();
//                int x = scanner.nextInt();
//                int y = scanner.nextInt();
//
//                // Validate position (convert to 0-based for board check)
//                int boardX = x - 1;
//                int boardY = y - 1;
//                if (!board.isValidPosition(boardX, boardY)) {
//                    throw new Assigment4.Main.InvalidEntityPositionException();
//                }
//
//                // Check for duplicate position
//                String positionKey = x + "," + y;
//                if (occupiedPositions.containsKey(positionKey)) {
//                    throw new Assigment4.Main.TwoEntitiesOnSamePositionException();
//                }
//
//                // Create food point
//                Assigment4.EntityPosition position = createPosition(x, y);
//                Assigment4.FoodPoint foodPoint = new Assigment4.FoodPoint(position, foodValue);
//
//                // Add to board
//                board.addEntity(foodPoint);
//                occupiedPositions.put(positionKey, "food");
//            }
//
//        } catch (FileNotFoundException e) {
//            throw new Assigment4.Main.InvalidEntityPositionException();
//        }
//
//        // Run the game simulation using only Board methods
//        return runGameSimulation(board, insectsInOrder);
//    }
//
//    private static boolean isValidInsectType(String type) {
//        return type.equalsIgnoreCase("Ant") ||
//                type.equalsIgnoreCase("Butterfly") ||
//                type.equalsIgnoreCase("Spider") ||
//                type.equalsIgnoreCase("Grasshopper");
//    }
//
//    private static Assigment4.EntityPosition createPosition(int x, int y) {
//        Assigment4.EntityPosition position = new Assigment4.EntityPosition();
//        position.entityPosition(x - 1, y - 1);
//        return position;
//    }
//
//    private static Assigment4.Insect createInsect(String type, Assigment4.EntityPosition position, Assigment4.InsectColor color) {
//        return switch (type.toLowerCase()) {
//            case "ant" -> new Assigment4.Ant(position, color);
//            case "butterfly" -> new Assigment4.Butterfly(position, color);
//            case "spider" -> new Assigment4.Spider(position, color);
//            case "grasshopper" -> new Assigment4.GrassHopper(position, color);
//            default -> throw new IllegalArgumentException("Unknown insect type");
//        };
//    }
//
//    private static List<String> runGameSimulation(Assigment4.Board board, List<Assigment4.Insect> insectsInOrder) {
//        List<String> results = new ArrayList<>();
//
//        for (Assigment4.Insect insect : insectsInOrder) {
//            // Check if insect is still on the board using Board methods
//            Assigment4.EntityPosition pos = insect.getEntityPosition();
//            Assigment4.BoardEntity entity = board.getEntity(pos);
//            if (entity != insect) {
//                continue;
//            }
//
//            // Use only Board methods for simulation
//            Assigment4.Direction bestDirection = board.getDirection(insect);
//            int foodEaten = board.getDirectionSum(insect);
//
//            // Format the result
//            String result = formatResult(insect, bestDirection, foodEaten);
//            results.add(result);
//        }
//
//        return results;
//    }
//
//    private static String formatResult(Assigment4.Insect insect, Assigment4.Direction direction, int foodEaten) {
//        String color = capitalizeFirst(insect.getColor().toString().toLowerCase());
//        String type = insect.getClass().getSimpleName();
//        String directionName = direction.getTextRepresentation();
//
//        return color + " " + type + " " + directionName + " " + foodEaten;
//    }
//
//    private static String capitalizeFirst(String str) {
//        if (str == null || str.isEmpty()) return str;
//        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
//    }
//
//    private static void writeOutput(String filename, List<String> lines) throws IOException {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
//            for (String line : lines) {
//                writer.println(line);
//            }
//        }
//    }
//}
//
//class EntityPosition {
//    private int x;
//    private int y;
//
//    public void entityPosition(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    public int getX() {
//        return x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public boolean addX(int delta, int boardSize) {
//        int newX = x + delta;
//        if (newX >= 0 && newX < boardSize) {
//            x = newX;
//            return true;
//        }
//        return false;
//    }
//
//    public boolean addY(int delta, int boardSize) {
//        int newY = y + delta;
//        if (newY >= 0 && newY < boardSize) {
//            y = newY;
//            return true;
//        }
//        return false;
//    }
//
//    public boolean isValid(int boardSize) {
//        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        EntityPosition that = (EntityPosition) o;
//        return x == that.x && y == that.y;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(x, y);
//    }
//
//    @Override
//    public String toString() {
//        return x + "," + y;
//    }
//}
//
//enum Direction {
//    N("North"),
//    E("East"),
//    S("South"),
//    W("West"),
//    NE("North-East"),
//    SE("South-East"),
//    SW("South-West"),
//    NW("North-West");
//
//    private final String textRepresentation;
//
//    Direction(String text) {
//        this.textRepresentation = text;
//    }
//
//    public String getTextRepresentation() {
//        return textRepresentation;
//    }
//
//    @Override
//    public String toString() {
//        return textRepresentation;
//    }
//}
//
//enum InsectColor {
//    RED,
//    GREEN,
//    BLUE,
//    YELLOW
//}
//
//class FoodPoint extends BoardEntity {
//    private int value;
//
//    public FoodPoint(EntityPosition position, int value) {
//        this.entityPosition = position;
//        this.value = value;
//    }
//
//    public EntityPosition getPosition() {
//        return entityPosition;
//    }
//
//    public int getValue() {
//        return value;
//    }
//}
//
//interface DiagonalMoving {
//    int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size);
//    int travelDiagonally(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size);
//}
//
//interface OrthogonalMoving {
//    int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size);
//    int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String, BoardEntity> boardData, int size);
//}
//
//abstract class Insect extends BoardEntity {
//    protected InsectColor color;
//    protected List<Direction> directions = new ArrayList<>();
//
//    public Insect(EntityPosition entityPosition, InsectColor color) {
//        this.entityPosition = entityPosition;
//        this.color = color;
//    }
//
//    public abstract Direction getBestDirection(Map<String, BoardEntity> boardData, int size);
//    public abstract int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize);
//
//    public InsectColor getColor() {
//        return color;
//    }
//
//    public List<Direction> getDirections() {
//        return directions;
//    }
//
//    protected boolean hasHigherPriority(Direction newDir, Direction currentBestDir) {
//        if (currentBestDir == null) return true;
//        return getDirectionPriority(newDir) < getDirectionPriority(currentBestDir);
//    }
//
//    protected int getDirectionPriority(Direction dir) {
//        return switch (dir) {
//            case N -> 1;
//            case E -> 2;
//            case S -> 3;
//            case W -> 4;
//            case NE -> 5;
//            case SE -> 6;
//            case SW -> 7;
//            case NW -> 8;
//            default -> 9;
//        };
//    }
//}
//
//class Spider extends Insect implements DiagonalMoving {
//    public Spider(EntityPosition entityPosition, InsectColor color) {
//        super(entityPosition, color);
//        directions.add(Direction.NE);
//        directions.add(Direction.SE);
//        directions.add(Direction.SW);
//        directions.add(Direction.NW);
//    }
//
//    @Override
//    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodCount = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.NE) {
//                moved = currentPos.addY(1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SE) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SW) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(-1, size);
//            } else if (dir == Direction.NW) {
//                moved = currentPos.addY(1, size) && currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//            if (entity instanceof FoodPoint) {
//                foodCount += ((FoodPoint) entity).getValue();
//            }
//        }
//        return foodCount;
//    }
//
//    @Override
//    public int travelDiagonally(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodEaten = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.NE) {
//                moved = currentPos.addY(1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SE) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SW) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(-1, size);
//            } else if (dir == Direction.NW) {
//                moved = currentPos.addY(1, size) && currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//
//            if (entity instanceof Insect otherInsect) {
//                if (!otherInsect.getColor().equals(this.color)) {
//                    break;
//                }
//            }
//
//            if (entity instanceof FoodPoint) {
//                foodEaten += ((FoodPoint) entity).getValue();
//                boardData.remove(positionKey);
//            }
//        }
//        return foodEaten;
//    }
//
//    @Override
//    public Direction getBestDirection(Map<String, BoardEntity> boardData, int size) {
//        Direction bestDir = null;
//        int maxFood = -1;
//
//        for (Direction direction : directions) {
//            int food = getDiagonalDirectionVisibleValue(direction, entityPosition, boardData, size);
//            if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
//                maxFood = food;
//                bestDir = direction;
//            }
//        }
//        return bestDir;
//    }
//
//    @Override
//    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
//        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
//        boardData.remove(startPosition);
//        return travelDiagonally(dir, entityPosition, boardData, size);
//    }
//}
//
//class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {
//    public Ant(EntityPosition entityPosition, InsectColor color) {
//        super(entityPosition, color);
//        directions.add(Direction.N);
//        directions.add(Direction.E);
//        directions.add(Direction.S);
//        directions.add(Direction.W);
//        directions.add(Direction.NE);
//        directions.add(Direction.SE);
//        directions.add(Direction.SW);
//        directions.add(Direction.NW);
//    }
//
//    @Override
//    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodCount = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.N) {
//                moved = currentPos.addY(1, size);
//            } else if (dir == Direction.E) {
//                moved = currentPos.addX(1, size);
//            } else if (dir == Direction.S) {
//                moved = currentPos.addY(-1, size);
//            } else if (dir == Direction.W) {
//                moved = currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//            if (entity instanceof FoodPoint) {
//                foodCount += ((FoodPoint) entity).getValue();
//            }
//        }
//        return foodCount;
//    }
//
//    @Override
//    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodEaten = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.N) {
//                moved = currentPos.addY(1, size);
//            } else if (dir == Direction.E) {
//                moved = currentPos.addX(1, size);
//            } else if (dir == Direction.S) {
//                moved = currentPos.addY(-1, size);
//            } else if (dir == Direction.W) {
//                moved = currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//
//            if (entity instanceof Insect otherInsect) {
//                if (!otherInsect.getColor().equals(color)) {
//                    break;
//                }
//            }
//
//            if (entity instanceof FoodPoint) {
//                foodEaten += ((FoodPoint) entity).getValue();
//                boardData.remove(positionKey);
//            }
//        }
//        return foodEaten;
//    }
//
//    @Override
//    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodCount = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.NE) {
//                moved = currentPos.addY(1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SE) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SW) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(-1, size);
//            } else if (dir == Direction.NW) {
//                moved = currentPos.addY(1, size) && currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//            if (entity instanceof FoodPoint) {
//                foodCount += ((FoodPoint) entity).getValue();
//            }
//        }
//        return foodCount;
//    }
//
//    @Override
//    public int travelDiagonally(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodEaten = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.NE) {
//                moved = currentPos.addY(1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SE) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(1, size);
//            } else if (dir == Direction.SW) {
//                moved = currentPos.addY(-1, size) && currentPos.addX(-1, size);
//            } else if (dir == Direction.NW) {
//                moved = currentPos.addY(1, size) && currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//
//            if (entity instanceof Insect otherInsect) {
//                if (!otherInsect.getColor().equals(this.color)) {
//                    break;
//                }
//            }
//
//            if (entity instanceof FoodPoint) {
//                foodEaten += ((FoodPoint) entity).getValue();
//                boardData.remove(positionKey);
//            }
//        }
//        return foodEaten;
//    }
//
//    @Override
//    public Direction getBestDirection(Map<String, BoardEntity> boardData, int size) {
//        Direction bestDir = null;
//        int maxFood = -1;
//
//        // Check all directions
//        for (Direction direction : directions) {
//            int food = 0;
//            if (direction == Direction.N || direction == Direction.E ||
//                    direction == Direction.S || direction == Direction.W) {
//                food = getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, size);
//            } else {
//                food = getDiagonalDirectionVisibleValue(direction, entityPosition, boardData, size);
//            }
//
//            if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
//                maxFood = food;
//                bestDir = direction;
//            }
//        }
//        return bestDir;
//    }
//
//    @Override
//    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
//        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
//        boardData.remove(startPosition);
//
//        if (dir == Direction.N || dir == Direction.E || dir == Direction.S || dir == Direction.W) {
//            return travelOrthogonally(dir, entityPosition, color, boardData, size);
//        } else {
//            return travelDiagonally(dir, entityPosition, boardData, size);
//        }
//    }
//}
//
//class GrassHopper extends Insect implements OrthogonalMoving {
//    public GrassHopper(EntityPosition entityPosition, InsectColor color) {
//        super(entityPosition, color);
//        directions.add(Direction.N);
//        directions.add(Direction.E);
//        directions.add(Direction.S);
//        directions.add(Direction.W);
//    }
//
//    @Override
//    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodCount = 0;
//
//        while(jumpPosition(currentPos, getJumpDeltaX(dir), getJumpDeltaY(dir), size)) {
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//            if (entity instanceof FoodPoint) {
//                foodCount += ((FoodPoint) entity).getValue();
//            }
//        }
//        return foodCount;
//    }
//
//    @Override
//    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodEaten = 0;
//
//        while(true) {
//            // Check current position for food
//            String currentKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity currentEntity = boardData.get(currentKey);
//            if (currentEntity instanceof FoodPoint) {
//                foodEaten += ((FoodPoint) currentEntity).getValue();
//                boardData.remove(currentKey);
//            }
//
//            // Try to jump to next position
//            EntityPosition nextPos = new EntityPosition();
//            nextPos.entityPosition(currentPos.getX(), currentPos.getY());
//            if (!jumpPosition(nextPos, getJumpDeltaX(dir), getJumpDeltaY(dir), size)) {
//                break;
//            }
//
//            // Check if next position has enemy insect
//            String nextKey = nextPos.getX() + "," + nextPos.getY();
//            BoardEntity nextEntity = boardData.get(nextKey);
//            if (nextEntity instanceof Insect otherInsect) {
//                if (!otherInsect.getColor().equals(color)) {
//                    break;
//                }
//            }
//
//            // Move to next position
//            currentPos.entityPosition(nextPos.getX(), nextPos.getY());
//        }
//
//        return foodEaten;
//    }
//
//    private int getJumpDeltaX(Direction dir) {
//        return switch (dir) {
//            case E -> 2;
//            case W -> -2;
//            default -> 0;
//        };
//    }
//
//    private int getJumpDeltaY(Direction dir) {
//        return switch (dir) {
//            case N -> 2;
//            case S -> -2;
//            default -> 0;
//        };
//    }
//
//    private boolean jumpPosition(EntityPosition currentPos, int deltaX, int deltaY, int size) {
//        int newX = currentPos.getX() + deltaX;
//        int newY = currentPos.getY() + deltaY;
//        return newX >= 0 && newX < size && newY >= 0 && newY < size;
//    }
//
//    @Override
//    public Direction getBestDirection(Map<String, BoardEntity> boardData, int size) {
//        Direction bestDir = null;
//        int maxFood = -1;
//
//        for (Direction direction : directions) {
//            int food = getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, size);
//            if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
//                maxFood = food;
//                bestDir = direction;
//            }
//        }
//        return bestDir;
//    }
//
//    @Override
//    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
//        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
//        boardData.remove(startPosition);
//        return travelOrthogonally(dir, entityPosition, color, boardData, size);
//    }
//}
//
//class Butterfly extends Insect implements OrthogonalMoving {
//    public Butterfly(EntityPosition entityPosition, InsectColor color) {
//        super(entityPosition, color);
//        directions.add(Direction.N);
//        directions.add(Direction.E);
//        directions.add(Direction.S);
//        directions.add(Direction.W);
//    }
//
//    @Override
//    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodCount = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.N) {
//                moved = currentPos.addY(1, size);
//            } else if (dir == Direction.E) {
//                moved = currentPos.addX(1, size);
//            } else if (dir == Direction.S) {
//                moved = currentPos.addY(-1, size);
//            } else if (dir == Direction.W) {
//                moved = currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//            if (entity instanceof FoodPoint) {
//                foodCount += ((FoodPoint) entity).getValue();
//            }
//        }
//        return foodCount;
//    }
//
//    @Override
//    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Map<String, BoardEntity> boardData, int size) {
//        EntityPosition currentPos = new EntityPosition();
//        currentPos.entityPosition(entityPosition.getX(), entityPosition.getY());
//        int foodEaten = 0;
//
//        while(true) {
//            boolean moved = false;
//            if (dir == Direction.N) {
//                moved = currentPos.addY(1, size);
//            } else if (dir == Direction.E) {
//                moved = currentPos.addX(1, size);
//            } else if (dir == Direction.S) {
//                moved = currentPos.addY(-1, size);
//            } else if (dir == Direction.W) {
//                moved = currentPos.addX(-1, size);
//            }
//
//            if (!moved) break;
//
//            String positionKey = currentPos.getX() + "," + currentPos.getY();
//            BoardEntity entity = boardData.get(positionKey);
//
//            if (entity instanceof Insect otherInsect) {
//                if (!otherInsect.getColor().equals(color)) {
//                    break;
//                }
//            }
//
//            if (entity instanceof FoodPoint) {
//                foodEaten += ((FoodPoint) entity).getValue();
//                boardData.remove(positionKey);
//            }
//        }
//        return foodEaten;
//    }
//
//    @Override
//    public Direction getBestDirection(Map<String, BoardEntity> boardData, int size) {
//        Direction bestDir = null;
//        int maxFood = -1;
//
//        for (Direction direction : directions) {
//            int food = getOrthogonalDirectionVisibleValue(direction, entityPosition, boardData, size);
//            if (food > maxFood || (food == maxFood && hasHigherPriority(direction, bestDir))) {
//                maxFood = food;
//                bestDir = direction;
//            }
//        }
//        return bestDir;
//    }
//
//    @Override
//    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int size) {
//        String startPosition = entityPosition.getX() + "," + entityPosition.getY();
//        boardData.remove(startPosition);
//        return travelOrthogonally(dir, entityPosition, color, boardData, size);
//    }
//}
//
//abstract class BoardEntity {
//    protected EntityPosition entityPosition;
//
//    public EntityPosition getEntityPosition() {
//        return entityPosition;
//    }
//}
//
//class Board {
//    private Map<String, Assigment4.BoardEntity> boardData;
//    private int size;
//
//    public Board(int boardSize) {
//        this.boardData = new HashMap<>();
//        this.size = boardSize;
//    }
//
//    public void addEntity(Assigment4.BoardEntity entity) {
//        boardData.put(entity.getEntityPosition().toString(), entity);
//    }
//
//    public Assigment4.BoardEntity getEntity(Assigment4.EntityPosition position) {
//        return boardData.get(position.toString());
//    }
//
//    public Assigment4.Direction getDirection(Assigment4.Insect insect) {
//        return insect.getBestDirection(boardData, size);
//    }
//
//    public int getDirectionSum(Assigment4.Insect insect) {
//        Assigment4.Direction direction = getDirection(insect);
//        return insect.travelDirection(direction, boardData, size);
//    }
//
//    public boolean isValidPosition(int x, int y) {
//        return (0 <= x && x < size && 0 <= y && y < size);
//    }
//}
