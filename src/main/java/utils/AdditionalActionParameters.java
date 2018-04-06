package utils;

/**
 * Дополнительные параметры для действия
 *
 * @author dchernyshov
 */
public enum AdditionalActionParameters {
    LEFT("Слева"),
    RIGHT("Справа"),
    BACK("Сзади"),
    IN_DEPTH_ON_THE_RIGHT("Забежать за спину справа"),
    IN_DEPTH_ON_THE_LEFT("Забежать за спину слева"),
    ;
    
    private String description;
    
    AdditionalActionParameters(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
