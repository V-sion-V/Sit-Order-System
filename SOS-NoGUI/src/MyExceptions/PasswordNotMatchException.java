package MyExceptions;

public class PasswordNotMatchException extends Exception{
    public PasswordNotMatchException() {}

    @Override
    public String toString() {
        return "Password dose not match, please retry!";
    }

    @Override
    public String getMessage() {
        return "Wrong password!";
    }
}
