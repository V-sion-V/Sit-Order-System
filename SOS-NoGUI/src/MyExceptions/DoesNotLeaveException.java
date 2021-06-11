package MyExceptions;

public class DoesNotLeaveException extends Exception{
    private final int oid;

    public DoesNotLeaveException(int oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "Please leave earlier: " + oid;
    }

    @Override
    public String getMessage() {
        return String.valueOf(oid);
    }
}
