package src.MyExceptions;

public class ClassroomSizeException extends Exception{
    private final int row, col;

    public ClassroomSizeException(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "教室: (" +(row)
                +", " +(col)
                +") 容量太大";
    }

    @Override
    public String getMessage() {
        return "("+row+", "+col+")";
    }
}
