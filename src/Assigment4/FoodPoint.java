package Assigment4;

public class FoodPoint extends BoardEntity{ // Позиция точки
    private int value;               // Значение точки

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