package objects;

import server.MyMath;

/**
 * Created by Danil on 27.02.2016.
 */
public class MobileObject extends FieldObject {
    protected Velocity globalVelocity;
    protected double directionChange;
    protected double distanceChange;

    public MobileObject() {
        globalVelocity = new Velocity();
    }

    public Velocity getGlobalVelocity() {
        return globalVelocity;
    }

    public void setGlobalVelocity(Velocity globalVelocity) {
        this.globalVelocity = globalVelocity;
    }

    public boolean isZeroVelocity() {
        return !(MyMath.velocityModule(globalVelocity) > 0);
    }
}
