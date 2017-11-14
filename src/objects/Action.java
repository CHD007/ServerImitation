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

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + (actionType == null ? 0 : actionType.hashCode());
        result = result * 31 + new Double(power).intValue();
        result = result * 31 + new Double(moment).intValue();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Action)) {
            return false;
        }

        Action otherAction = (Action) obj;

        return (actionType == null ? otherAction.getActionType() == null : actionType.equals(otherAction.getActionType()))
                && (power == otherAction.power)
                && (moment == otherAction.moment);
    }
}
