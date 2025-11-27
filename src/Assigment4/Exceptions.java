package Assigment4;

public class Exceptions {
    String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }
}

class InvalidBoardSizeException extends Exceptions{
    public void InvalidBoardSizeException() {
        errorMessage = "Invalid board size";
    }
}

class InvalidNumberOfInsectsException extends Exceptions{
    public void InvalidNumberOfInsectsException() {
        errorMessage = "Invalid number of insects";
    }
}

class InvalidNumberOfFoodPointsException extends Exceptions{
    public void InvalidNumberOfFoodPointsException() {
        errorMessage = "Invalid number of food points";
    }
}

class InvalidInsectColorException extends Exceptions{
    public void InvalidInsectColorException() {
        errorMessage = "Invalid insect color";
    }
}

class InvalidInsectTypeException extends Exceptions{
    public void InvalidInsectTypeException() {
        errorMessage = "Invalid insect type";
    }
}

class InvalidEntityPositionException extends Exceptions{
    public void InvalidEntityPositionException() {
        errorMessage = "Invalid entity position";
    }
}

class DuplicateInsectException extends Exceptions{
    public void DuplicateInsectException() {
        errorMessage = "Duplicate insects";
    }
}

class TwoEntitiesOnSamePositionException extends Exceptions{
    public void TwoEntitiesOnSamePositionException() {
        errorMessage = "Two entities in the same position";
    }
}
