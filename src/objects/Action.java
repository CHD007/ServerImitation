package objects;

/**
 * Класс для описания действия с параметрами, которое хочет выполнить игрок
 */
public class Action {
    private String actionType;
    private double power;
    private double moment;

    public Action() {
        actionType = null;
        power = 0;
        moment = 0;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getMoment() {
        return moment;
    }

    public void setMoment(double moment) {
        this.moment = moment;
    }
}
