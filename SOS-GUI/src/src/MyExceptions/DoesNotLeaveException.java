package src.MyExceptions;

public class DoesNotLeaveException extends Exception{
    private final int oid;

    public DoesNotLeaveException(int oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "未按时离开" ;
    }

    @Override
    public String getMessage() {
        return String.valueOf(oid);
    }
}
