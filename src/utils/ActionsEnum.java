package utils;

/**
 * Created by Danil on 12.03.2017.
 */
public enum ActionsEnum {
    INTERSEPT("Перехват мяча"),
    PASS("Пас"),
    DRIBBLING("Ведение мяча"),
    MARK_OPPONENT("Опека оппонента");

    private String description;

    ActionsEnum(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
