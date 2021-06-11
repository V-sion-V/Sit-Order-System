package src.MyExceptions;

public class OrderCanceledException extends Exception{
    private final int oid;

    public OrderCanceledException(int oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "预订已被取消";
    }

    @Override
    public String getMessage() {
        return String.valueOf(oid);
    }
}
