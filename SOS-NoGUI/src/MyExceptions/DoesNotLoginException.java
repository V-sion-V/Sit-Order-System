package MyExceptions;

public class DoesNotLoginException extends Exception{
    private final String todo;

    public DoesNotLoginException(String todo){
        this.todo = todo;
    }

    @Override
    public String toString() {
        return "Pleas login to " + todo;
    }

    @Override
    public String getMessage() {
        return todo;
    }
}
