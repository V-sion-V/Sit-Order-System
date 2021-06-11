package MyExceptions;

public class ArgumentFormatException extends Exception{
    String arg;
    public ArgumentFormatException(String arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "Argument format wrong:" + arg;
    }

    @Override
    public String getMessage() {
        return arg;
    }
}
