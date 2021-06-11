package MyExceptions;

public class OperationNotExistException extends Exception{
    private final String op;

    public OperationNotExistException(String op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return "Operation flag \"" + op + "\" dose not exist";
    }

    @Override
    public String getMessage() {
        return op;
    }
}
