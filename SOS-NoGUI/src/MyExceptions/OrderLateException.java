package MyExceptions;

public class OrderLateException extends Exception{
    private final int oid;

    public OrderLateException(int oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "Order " + oid + " is late";
    }

    @Override
    public String getMessage() {
        return String.valueOf(oid);
    }
}
