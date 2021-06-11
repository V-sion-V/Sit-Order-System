package MyExceptions;

public class StudentIdExistException extends Exception{
    private final int id;

    public StudentIdExistException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student id exist: " + id;
    }

    public String getMessage() {
        return String.valueOf(id);
    }
}
