package Assigment4;

public class EntityPosition {
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