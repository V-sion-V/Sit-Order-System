package src.MyExceptions;

public class StudentIdDoesNotExistException extends Exception{
    private final int id;

    public StudentIdDoesNotExistException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "此学号不存在:" + id;
    }

    public String getMessage() {
        return String.valueOf(id);
    }
}
