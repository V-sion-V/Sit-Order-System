package MyExceptions;

public class ClassroomSizeException extends Exception{
    private final int row, col;

    public ClassroomSizeException(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Classroom: (" +(row)
                +", " +(col)
                +") is out of bound";
    }

    @Override
    public String getMessage() {
        return "("+row+", "+col+")";
    }
}
