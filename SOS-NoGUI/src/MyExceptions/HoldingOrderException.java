package MyExceptions;

public class HoldingOrderException extends Exception{
    public HoldingOrderException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
