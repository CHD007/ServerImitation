package utils;

/**
 * Действия, доступные игроку
 * @author dchernyshov
 */
public enum ActionsEnum {
    INTERSEPT("Перехват мяча"),
    PASS("Пас"),
    DRIBBLING("Ведение мяча"),
    MARK_OPPONENT("Опека оппонента"),
    KEEP_TO_OFFSIDE("Держаться на уровне посл. защитника");

    private String description;

    ActionsEnum(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
