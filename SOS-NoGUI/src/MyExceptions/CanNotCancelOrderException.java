package MyExceptions;

public class CanNotCancelOrderException extends Exception{
    private final int stuid;
    private final int oid;

    public CanNotCancelOrderException(int stu,int oid) {
        this.stuid = stu;
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "This order: " + stuid + " : " + oid
                + " could not be canceled";
    }

    public String getMessage() {
        return stuid + ": " + oid;
    }
}
