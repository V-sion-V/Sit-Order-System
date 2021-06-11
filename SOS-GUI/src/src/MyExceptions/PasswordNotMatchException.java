package src.MyExceptions;

public class PasswordNotMatchException extends Exception{
    public PasswordNotMatchException() {}

    @Override
    public String toString() {
        return "密码错误！";
    }

    @Override
    public String getMessage() {
        return "Wrong password!";
    }
}
