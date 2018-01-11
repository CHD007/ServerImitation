package objects;

/**
 * Created by Danil on 27.02.2016.
 */
public class Ball extends MobileObject {
    private double angle;

    public Ball() {
        angle = 0;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
