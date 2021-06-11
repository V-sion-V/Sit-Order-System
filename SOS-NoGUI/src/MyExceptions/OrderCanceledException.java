package MyExceptions;

public class OrderCanceledException extends Exception{
    private final int oid;

    public OrderCanceledException(int oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "Order " + oid + " is canceled";
    }

    @Override
    public String getMessage() {
        return String.valueOf(oid);
    }
}
