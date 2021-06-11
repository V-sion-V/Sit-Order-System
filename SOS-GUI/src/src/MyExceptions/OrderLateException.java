package src.MyExceptions;

public class OrderLateException extends Exception{
    private final int oid;

    public OrderLateException(int oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "未按时到达";
    }

    @Override
    public String getMessage() {
        return String.valueOf(oid);
    }
}
