import MyExceptions.ClassroomExistException;
import MyExceptions.ClassroomNotExistException;
import MyExceptions.OutOfBoundException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
/**
 * <p>本类为教室类
 *
 */
public class Classroom {
    private final int cid;
    private String name;
    private boolean avl = true;//是否可用
    private final ArrayList<Sit> sits;//座位
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
    /**
     * 启用一间教室
     */
    public void enable() {
        this.avl = true;
        for(Sit i : sits) {
            i.enable();
        }
    }
    /**
     * 禁用一间教室，将禁用其中的所有座位，这些座位上的所有订单将被强制取消，并给学生补偿10点信用值。
     */
    public void disable() {
        this.avl = false;
        for(Sit i : sits) {
            i.disable();
        }
    }

    /**
     * 用新的名字替换老名字
     * @param newName 新名字
     * @throws ClassroomExistException
     */
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

    /**
     * 初始化椅子
     */
    public void initialSits() {
        sits.clear();
        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < colCount; j++) {
                sits.add(new Sit(i*colCount+j,this));
            }
        }
    }

    /**
     * 通过行和列获得某个椅子
     * @param row 行
     * @param col 列
     * @return 椅子对象
     * @throws OutOfBoundException 行和列不在教室范围内
     */
    public Sit getSit(int row,int col)
            throws OutOfBoundException {
        if(row<rowCount&&col<colCount&&row>=0&&col>=0) {
            return sits.get(row*colCount+col);
        } else {
            throw new OutOfBoundException(row,col);
        }
    }


}
