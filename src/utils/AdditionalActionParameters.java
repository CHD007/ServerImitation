package utils;

/**
 * Дополнительные параметры для действия
 *
 * @author dchernyshov
 */
public enum AdditionalActionParameters {
    LEFT("Слева"),
    RIGHT("Справа"),
    BACK("Сзади");
    
    private String description;
    
    AdditionalActionParameters(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
