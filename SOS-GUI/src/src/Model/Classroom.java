package src.Model;

import src.MyExceptions.ClassroomExistException;
import src.MyExceptions.ClassroomNotExistException;
import src.MyExceptions.OutOfBoundException;

import java.util.ArrayList;


public class Classroom {
    private final int cid;
    private String name;
    private boolean avl = true;
    private final ArrayList<Sit> sits;
    private final int rowCount;
    private final int colCount;

    //constructor
    public Classroom(int cid, String name, int rowCount, int colCount) {
        this.cid = cid;
        this.name = name;
        this.sits = new ArrayList<>();
        this.rowCount = rowCount;
        this.colCount = colCount;
        initialSits();
    }

    //getter and setter
    public int getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

    public int getColCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public ArrayList<Sit> getSits() {
        return sits;
    }

    public boolean isAvl() {
        return avl;
    }

    public void enable() {
        this.avl = true;
        for(Sit i : sits) {
            i.enable();
        }
    }

    public void disable() {
        this.avl = false;
        for(Sit i : sits) {
            i.disable();
        }
    }

    public void changeName(String newName)
            throws ClassroomExistException {
        if(!newName.equals(name)) {
            try{
                ClassroomList.getInstance().getClassroom(newName);
                throw new ClassroomExistException(newName);
            }catch(ClassroomNotExistException e) {
                this.name = newName;
            }
        }
    }

    public void initialSits() {
        sits.clear();
        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < colCount; j++) {
                sits.add(new Sit(i*colCount+j,this));
            }
        }
    }

    public Sit getSit(int row,int col)
            throws OutOfBoundException {
        if(row<rowCount&&col<colCount&&row>=0&&col>=0) {
            return sits.get(row*colCount+col);
        } else {
            throw new OutOfBoundException(row,col);
        }
    }



}
