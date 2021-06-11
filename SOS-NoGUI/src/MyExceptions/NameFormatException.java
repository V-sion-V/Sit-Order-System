package MyExceptions;

public class NameFormatException extends Exception {
    private final String name;

    public NameFormatException(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + name + " illegal";
    }
}
