package MyExceptions;

public class NonPositiveNumberException extends Exception{
    private final int number;

    public NonPositiveNumberException(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "This number should be positive (" + number + ")";
    }

    @Override
    public String getMessage() {
        return String.valueOf(number);
    }
}
