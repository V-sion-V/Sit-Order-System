import MyExceptions.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
/**
 * 对classroom对象进行增删改查等操作
 */
public class ClassroomList {
    private static ClassroomList classroomList = null;
    private final ArrayList<Classroom> classrooms;
    private ClassroomList(){
        classrooms = new ArrayList<>();
    }

    public static ClassroomList getInstance() {
        if(classroomList == null) {
            classroomList = new ClassroomList();
        }
        return classroomList;
    }

    public ArrayList<Classroom> getClassrooms() {
        return classrooms;
    }
    /**
     * 通过id号查找教室
     * @param id
     * @return
     * @throws ClassroomNotExistException
     */
    public Classroom getClassroom(int id)
            throws ClassroomNotExistException {
        for(Classroom i : classrooms) {
            if(i.getCid()==id) {
                return i;
            }
        }
        throw new ClassroomNotExistException(id);
    }
    /**
     * 通过name查找教室
     * @param name
     * @return
     * @throws ClassroomNotExistException
     */
    public Classroom getClassroom(String name)
            throws ClassroomNotExistException {
        for(Classroom i : classrooms) {
            if(i.getName().equals(name)) {
                return i;
            }
        }
        throw new ClassroomNotExistException(name);
    }

    /**
     * 新建一间教室。定义了编号、名称、行数、列数。
     * 其中编号和名称不能与存在的教室编号相同，如果编号或名称已存在、行数列数不是小于100的正整数，则会输出异常信息。
     * @param id 编号
     * @param name 名称
     * @param rowCount 行
     * @param colCount 列
     * @throws ClassroomExistException
     * @throws NonPositiveNumberException
     */
    public void addClassroom(int id, String name, int rowCount, int colCount)
            throws ClassroomExistException, NonPositiveNumberException {
        try {
            getClassroom(id);
            throw new ClassroomExistException(id);
        } catch (ClassroomNotExistException e1) {
            try {
                getClassroom(name);
                throw new ClassroomExistException(name);
            } catch (ClassroomNotExistException e2) {
                if(rowCount>0&&colCount>0) {
                    classrooms.add(new Classroom(id,name,rowCount,colCount));
                } else {
                    throw new NonPositiveNumberException((rowCount>0)?colCount:rowCount);
                }
            }
        }
    }

    /**
     * 通过编号删除一间教室。
     * @param id
     * @throws ClassroomNotExistException 如果教室的编号或是名称不存在，则会输出对应的异常信息。
     */
    public void deleteClassroom(int id)
            throws ClassroomNotExistException {
        Classroom temp = getClassroom(id);
        classrooms.remove(temp);
    }

    /**
     * 通过名字删除一间教室。
     * @param name
     * @throws ClassroomNotExistException 如果教室的编号或是名称不存在，则会输出对应的异常信息。
     */
    public void deleteClassroom(String name)
            throws ClassroomNotExistException {
        Classroom temp = getClassroom(name);
        classrooms.remove(temp);
    }

    /**
     * 程序退出时存至文件
     * @param file
     */
    public void saveToFile(File file) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file,false), StandardCharsets.UTF_8));
        for(Classroom i : classrooms) {
            bufferedWriter.write(i.getCid()+"\\%\\"+
                    i.getName()+"\\%\\"+
                    i.getRowCount()+"\\%\\"+
                    i.getColCount());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    /**
     * 程序启动时从文件读取数据
     * @param file
     */
    public void readFromFile(File file) throws IOException {
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8));
        classrooms.clear();
        String in;
        while((in=bufferedReader.readLine())!=null) {
            String[] inArgs = in.split("\\\\%\\\\");
            classrooms.add(new Classroom(
                    Integer.parseInt(inArgs[0]),
                    inArgs[1],
                    Integer.parseInt(inArgs[2]),
                    Integer.parseInt(inArgs[3])));
        }
        bufferedReader.close();
    }
}