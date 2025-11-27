package Assigment4;

public interface OrthogonalMoving {
    public int getOrthogonalDirection(Direction dir, EntityPosition entityPosition, Board board);
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color, Board board);
}
