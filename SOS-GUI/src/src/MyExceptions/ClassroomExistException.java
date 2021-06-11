package src.MyExceptions;

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
            return "教室号:" + cid
                    + " 已存在";
        } else {
            return "教室名:" + name
                    + " 已存在";
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
