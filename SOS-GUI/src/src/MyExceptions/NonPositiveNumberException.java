package src.MyExceptions;
public class NonPositiveNumberException extends Exception{
    private final int number;

    public NonPositiveNumberException(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "此数字应为正数 (" + number + ")";
    }

    @Override
    public String getMessage() {
        return String.valueOf(number);
    }
}
