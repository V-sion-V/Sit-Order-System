package MyExceptions;

public class StudentIdDoesNotExistException extends Exception{
    private final int id;

    public StudentIdDoesNotExistException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student id does not exist: " + id;
    }

    public String getMessage() {
        return String.valueOf(id);
    }
}
