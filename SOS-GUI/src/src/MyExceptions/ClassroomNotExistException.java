package src.MyExceptions;

public class ClassroomNotExistException extends Exception{
    private int cid = -1;
    private String name = null;

    public ClassroomNotExistException(int cid) {
        this.cid = cid;
    }

    public ClassroomNotExistException(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if(name == null) {
            return "Classroom id:" + cid
                    + " dose not exists";
        } else {
            return "Classroom name:" + name
                    + " dose not exists";
        }
    }

    @Override
    public String getMessage() {
        if(name == null) {
            return String.valueOf(cid);
        } else {
            return name;
        }
    }
}
