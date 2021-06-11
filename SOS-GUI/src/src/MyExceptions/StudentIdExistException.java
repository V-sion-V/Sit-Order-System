package src.MyExceptions;

public class StudentIdExistException extends Exception{
    private final int id;

    public StudentIdExistException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "学号已存在: " + id;
    }

    public String getMessage() {
        return String.valueOf(id);
    }
}
