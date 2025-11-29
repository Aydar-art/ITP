package Assigment4;

import java.io.*;
import java.util.*;

public class Main {

    static class InvalidBoardSizeException extends Exception {
        public InvalidBoardSizeException() { super("Invalid board size"); }
    }

    static class InvalidNumberOfInsectsException extends Exception {
        public InvalidNumberOfInsectsException() { super("Invalid number of insects"); }
    }

    static class InvalidNumberOfFoodPointsException extends Exception {
        public InvalidNumberOfFoodPointsException() { super("Invalid number of food points"); }
    }

    static class InvalidInsectColorException extends Exception {
        public InvalidInsectColorException() { super("Invalid insect color"); }
    }

    static class InvalidInsectTypeException extends Exception {
        public InvalidInsectTypeException() { super("Invalid insect type"); }
    }

    static class InvalidEntityPositionException extends Exception {
        public InvalidEntityPositionException() { super("Invalid entity position"); }
    }

    static class DuplicateInsectException extends Exception {
        public DuplicateInsectException() { super("Duplicate insects"); }
    }

    static class TwoEntitiesOnSamePositionException extends Exception {
        public TwoEntitiesOnSamePositionException() { super("Two entities in the same position"); }
    }

    public static void main(String[] args) {
        try {
            List<String> results = processGame("input.txt");
            writeOutput("output.txt", results);
        } catch (Exception e) {
            try {
                writeOutput("output.txt", Collections.singletonList(e.getMessage()));
            } catch (IOException ioException) {
                System.err.println("Error writing output: " + ioException.getMessage());
            }
        }
    }

    public static List<String> processGame(String inputFilename)
            throws InvalidBoardSizeException, InvalidNumberOfInsectsException,
            InvalidNumberOfFoodPointsException, InvalidInsectColorException,
            InvalidInsectTypeException, InvalidEntityPositionException,
            DuplicateInsectException, TwoEntitiesOnSamePositionException {

        Board board = null;
        List<Insect> insectsInOrder = new ArrayList<>();
        Map<String, String> occupiedPositions = new HashMap<>();
        Map<String, Integer> insectTypeCount = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(inputFilename))) {
            // Read board size
            if (!scanner.hasNextInt()) {
                throw new InvalidBoardSizeException();
            }
            int boardSize = scanner.nextInt();
            if (boardSize < 4 || boardSize > 1000) {
                throw new InvalidBoardSizeException();
            }
            board = new Board(boardSize);

            // Read number of insects
            if (!scanner.hasNextInt()) {
                throw new InvalidNumberOfInsectsException();
            }
            int numInsects = scanner.nextInt();
            if (numInsects < 1 || numInsects > 16) {
                throw new InvalidNumberOfInsectsException();
            }

            // Read number of food points
            if (!scanner.hasNextInt()) {
                throw new InvalidNumberOfFoodPointsException();
            }
            int numFoodPoints = scanner.nextInt();
            if (numFoodPoints < 1 || numFoodPoints > 200) {
                throw new InvalidNumberOfFoodPointsException();
            }

            // Read insects
            for (int i = 0; i < numInsects; i++) {
                if (!scanner.hasNext()) break;

                String colorStr = scanner.next();
                String typeStr = scanner.next();
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                // Validate color
                InsectColor color;
                try {
                    color = InsectColor.toColor(colorStr);
                } catch (IllegalArgumentException e) {
                    throw new InvalidInsectColorException();
                }

                // Validate type
                if (!isValidInsectType(typeStr)) {
                    throw new InvalidInsectTypeException();
                }

                // Validate position (convert to 0-based for board check)
                int boardX = x - 1;
                int boardY = y - 1;
                if (!board.isValidPosition(boardX, boardY)) {
                    throw new InvalidEntityPositionException();
                }

                // Check for duplicate position
                String positionKey = x + "," + y;
                if (occupiedPositions.containsKey(positionKey)) {
                    throw new TwoEntitiesOnSamePositionException();
                }

                // Create entity position and insect
                EntityPosition position = createPosition(x, y);
                Insect insect = createInsect(typeStr, position, color);

                // Check for duplicate insects (same color and type)
                String insectKey = color + "_" + typeStr.toLowerCase();
                insectTypeCount.put(insectKey, insectTypeCount.getOrDefault(insectKey, 0) + 1);
                if (insectTypeCount.get(insectKey) > 1) {
                    throw new DuplicateInsectException();
                }

                // Add to board and tracking maps
                board.addEntity(insect);
                occupiedPositions.put(positionKey, "insect");
                insectsInOrder.add(insect);
            }

            // Read food points
            for (int i = 0; i < numFoodPoints; i++) {
                if (!scanner.hasNext()) break;

                int foodValue = scanner.nextInt();
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                // Validate position (convert to 0-based for board check)
                int boardX = x - 1;
                int boardY = y - 1;
                if (!board.isValidPosition(boardX, boardY)) {
                    throw new InvalidEntityPositionException();
                }

                // Check for duplicate position
                String positionKey = x + "," + y;
                if (occupiedPositions.containsKey(positionKey)) {
                    throw new TwoEntitiesOnSamePositionException();
                }

                // Create food point
                EntityPosition position = createPosition(x, y);
                FoodPoint foodPoint = new FoodPoint(position, foodValue);

                // Add to board
                board.addEntity(foodPoint);
                occupiedPositions.put(positionKey, "food");
            }

        } catch (FileNotFoundException e) {
            throw new InvalidEntityPositionException();
        }

        // Run the game simulation using only Board methods

        List<String> results = new ArrayList<>();

        for (Insect insect : insectsInOrder) {
            // Check if insect is still on the board using Board methods
            EntityPosition pos = insect.getEntityPosition();
            BoardEntity entity = board.getEntity(pos);
            if (entity != insect) {
                continue;
            }

            // Use only Board methods for simulation
            Direction bestDirection = board.getDirection(insect);
            int foodEaten = board.getDirectionSum(insect);

            // Format the result
            String result = formatResult(insect, bestDirection, foodEaten);
            results.add(result);
        }

        return results;
    }

    private static boolean isValidInsectType(String type) {
        return type.equalsIgnoreCase("Ant") ||
                type.equalsIgnoreCase("Butterfly") ||
                type.equalsIgnoreCase("Spider") ||
                type.equalsIgnoreCase("Grasshopper");
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
            case "grasshopper" -> new GrassHopper(position, color);
            default -> throw new IllegalArgumentException("Unknown insect type");
        };
    }

//    private static List<String> runGameSimulation(Board board, List<Insect> insectsInOrder) {
//        List<String> results = new ArrayList<>();
//
//        for (Insect insect : insectsInOrder) {
//            // Check if insect is still on the board using Board methods
//            EntityPosition pos = insect.getEntityPosition();
//            BoardEntity entity = board.getEntity(pos);
//            if (entity != insect) {
//                continue;
//            }
//
//            // Use only Board methods for simulation
//            Direction bestDirection = board.getDirection(insect);
//            int foodEaten = board.getDirectionSum(insect);
//
//            // Format the result
//            String result = formatResult(insect, bestDirection, foodEaten);
//            results.add(result);
//        }
//
//        return results;
//    }

    private static String formatResult(Insect insect, Direction direction, int foodEaten) {
        String color = capitalizeFirst(insect.getColor().toString().toLowerCase());
        String type = insect.getClass().getSimpleName();
        String directionName = direction.getTextRepresentation();

        return color + " " + type + " " + directionName + " " + foodEaten;
    }

    private static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private static void writeOutput(String filename, List<String> lines) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.println(line);
            }
        }
    }
}