package server;

import objects.Velocity;

/**
 * Created by Danil on 27.02.2016.
 */
public class SenseMessage {
    private double stamina;
    private double effort;
    private Velocity velocity;

    public SenseMessage() {
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public double getEffort() {
        return effort;
    }

    public void setEffort(double effort) {
        this.effort = effort;
    }
}
