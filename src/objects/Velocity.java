package objects;

/**
 * Created by Danil on 27.02.2016.
 */
public class Velocity {
    private double x;
    private double y;

    public Velocity() {
        x = 0;
        y = 0;
    }

    public Velocity(Velocity velocity) {
        x = velocity.getX();
        y = velocity.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
