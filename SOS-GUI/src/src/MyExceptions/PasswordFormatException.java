package src.MyExceptions;

public class PasswordFormatException extends Exception{
    private final String pwd;
    private final String type;

    public PasswordFormatException(String pwd){
        this.pwd = pwd;
        if(pwd.length()<8) type = "密码过短!";
        else if(pwd.matches("[0-9]*")) type = "密码需要包含字母!";
        else type = "密码需要包含数字!";
    }

    @Override
    public String toString() {
        return "新密码不合法: " + pwd
                + " ("+ type + ")";
    }

    @Override
    public String getMessage() {
        return pwd;
    }
}
