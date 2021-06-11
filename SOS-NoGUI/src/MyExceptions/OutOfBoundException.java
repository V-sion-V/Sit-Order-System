package MyExceptions;

public class OutOfBoundException extends Exception {
    private final int row, col;

    public OutOfBoundException(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Sit: (" +(row+1)
                +", " +(col+1)
                +") is out of bound";
    }

    @Override
    public String getMessage() {
        return "("+row+", "+col+")";
    }
}

