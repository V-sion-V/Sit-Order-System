package MyExceptions;

public class ClassroomExistException extends Exception{
    private int cid = -1;
    private String name = null;

    public ClassroomExistException(int cid) {
        this.cid = cid;
    }

    public ClassroomExistException(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if(name == null) {
            return "Classroom id:" + cid
                    + " exists";
        } else {
            return "Classroom name:" + name
                    + " exists";
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
