package MyExceptions;

public class TimeIllegalException extends Exception{

    public TimeIllegalException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return "Time illegal: " + getMessage();
    }
}
