package objects;

import java.util.Optional;

/**
 * Описывает уравнение прямой по двум точкам вида: x(y2 - y1) + y(x1 - x2) - x1y2 + y1x2 = 0
 */
public class Line {
    private double argumentNearX;   // y2 - y1
    private double argumentNearY;   // x1 - x2
    private double freeArgument;    // y1x2 - x1y2

    private Line() {
    }

    private Line(FieldObject firstPoint, FieldObject secondPoint) {
        argumentNearX = secondPoint.getPosY() - firstPoint.getPosY();
        argumentNearY = firstPoint.getPosX() - secondPoint.getPosX();
        freeArgument = firstPoint.getPosY() * secondPoint.getPosX() - firstPoint.getPosX() * secondPoint.getPosY();
    }

    /**
     * Находит точку пересечения этой прямой с заданной прямой,
     * путем решения системы уравнений:
     *  a1x + b1y + c1 = 0;
     *  a2x + b2y + c2 = 0;
     *
     * @param line  прямая, точку пересечения с которой нужно найти
     * @return      точка пересечения
     */
    public Optional<FieldObject> getIntersectionPointWithLine(Line line) {
        if ((this.equals(line)) || (this.argumentNearX == 0.0 && line.argumentNearX == 0.0)) {
            return Optional.empty();
        }

        if (this.argumentNearX == 0.0) {
            double posY = -this.freeArgument / this.argumentNearY;
            double posX = (-line.getFreeArgument() - line.argumentNearY * posY) / line.argumentNearX;
            return Optional.of(new FieldObject(posX, posY));
        }

        double posY = (this.freeArgument * line.argumentNearX - line.freeArgument * this.argumentNearX)
                / (line.argumentNearY * this.argumentNearX - this.argumentNearY * line.argumentNearX);
        double posX = (-(this.argumentNearY * posY) - this.freeArgument) / this.argumentNearX;
        return Optional.of(new FieldObject(posX, posY));
    }

    /**
     * Фабричный метод для получения экземпляра линии по двум точкам.
     *
     * @param firstPoint    первая точка
     * @param secondPoint   вторая точка
     * @return линия по двум заданным точкам
     */
    public static Line getLineByTwoPoints(FieldObject firstPoint, FieldObject secondPoint) {
        return new Line(firstPoint, secondPoint);
    }

    /**
     * Фарбичный метод для получения экземпляра линии по абсциссе,
     * т.е. в формате x = <tt>abscissa</tt>.
     *
     * @param abscissa координата x
     * @return линия по абсциссе
     */
    public static Line getLineByAbscissa(double abscissa) {
        Line line = new Line();
        line.argumentNearX = 1.0;
        line.argumentNearY = 0.0;
        line.freeArgument = -abscissa;
        return line;
    }

    /**
     * Фабричный метод для получения экземпляра линии по ординате,
     * т.е. в формате y = <tt>ordinate</tt>
     *
     * @param ordinate координата y
     * @return линия по ординате
     */
    public static Line getLineByOrdinate(double ordinate) {
        Line line = new Line();
        line.argumentNearX = 0.0;
        line.argumentNearY = 1.0;
        line.freeArgument = -ordinate;
        return line;
    }

    public double getArgumentNearX() {
        return argumentNearX;
    }

    public double getArgumentNearY() {
        return argumentNearY;
    }

    public double getFreeArgument() {
        return freeArgument;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Double.hashCode(argumentNearX);
        result = 31 * result + Double.hashCode(argumentNearY);
        result = 31 * result + Double.hashCode(freeArgument);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Line)) {
            return false;
        }
        Line otherLine = (Line) obj;

        return Double.compare(this.argumentNearX, otherLine.argumentNearX) == 0
                && Double.compare(this.argumentNearY, otherLine.argumentNearY) == 0
                && Double.compare(this.freeArgument, otherLine.freeArgument) == 0;
    }
}
