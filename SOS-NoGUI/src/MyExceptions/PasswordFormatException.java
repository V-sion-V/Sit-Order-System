package MyExceptions;

public class PasswordFormatException extends Exception{
    private final String pwd;
    private final String type;

    public PasswordFormatException(String pwd){
        this.pwd = pwd;
        if(pwd.length()<8) type = "Password is too short!";
        else if(pwd.matches("[0-9]*")) type = "Password needs alpha!";
        else type = "Password needs number!";
    }

    @Override
    public String toString() {
        return "You can't use this password: " + pwd
                + " ("+ type + ")";
    }

    @Override
    public String getMessage() {
        return pwd;
    }
}
