package Assigment4;

public interface DiagonalMoving {
    public int getDiaginalDirection(Direction dir, EntityPosition entityPosition, Board board);
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, Board board);
}
