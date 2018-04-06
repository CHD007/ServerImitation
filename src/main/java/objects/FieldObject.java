package objects;

/**
 * Created by Danil on 04.03.2016.
 */
public class FieldObject {
    protected double posX;
    protected double posY;

    public FieldObject() {
        posX = 0;
        posY = 0;
    }

    public FieldObject(FieldObject fieldObject) {
        posX = fieldObject.getPosX();
        posY = fieldObject.getPosY();
    }

    public FieldObject(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Double.hashCode(posX);
        result = 31 * result + Double.hashCode(posY);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FieldObject)) {
            return false;
        }
        FieldObject otherPoint = (FieldObject) obj;

        return Double.compare(posX, otherPoint.posX) == 0 && Double.compare(posY, otherPoint.posY) == 0;
    }
}
