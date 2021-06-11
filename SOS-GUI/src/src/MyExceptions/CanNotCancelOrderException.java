package src.MyExceptions;

public class CanNotCancelOrderException extends Exception{
    private final int stuid;
    private final int oid;
    private final int type;

    public CanNotCancelOrderException(int stu,int oid,int type) {
        this.stuid = stu;
        this.oid = oid;
        this.type= type;
    }

    @Override
    public String toString() {
        if(type==1){
            return "已过可取消时间";
        }else{
            return "订单无法取消";
        }

    }

    public String getMessage() {
        return stuid + ": " + oid;
    }
}
