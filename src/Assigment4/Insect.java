package Assigment4;

abstract class Insect {
    private InsectColor color;

    // Конструктор для инициализации цвета
    public Insect(EntityPosition position, InsectColor color) {
        this.color = color;
    }

    // Метод для получения лучшего направления движения
    public abstract Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize);

    // Метод для перемещения в указанном направлении
    public abstract int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize);

    // Метод для получения цвета насекомого
    public InsectColor getColor() {
        return color;
    }
}