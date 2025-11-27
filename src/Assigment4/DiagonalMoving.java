package Assigment4;

import java.util.Map;

public interface DiagonalMoving {
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,Map<String, BoardEntity> boardData, int size);

    public int travelDiagonally(Direction dir, EntityPosition entityPosition, Map<String, BoardEntity> boardData, int size);
}
