package src.MyExceptions;
public class OutOfBoundException extends Exception {
    private final int row, col;

    public OutOfBoundException(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "座位: (" +(row+1)
                +", " +(col+1)
                +") 不存在";
    }

    @Override
    public String getMessage() {
        return "("+row+", "+col+")";
    }
}

