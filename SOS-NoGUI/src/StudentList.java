import MyExceptions.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
/**
 * 学生列表类，用于对学生类进行操作
 */
public class StudentList {
    private static StudentList studentList = null;
    private StudentList() {
        students = new ArrayList<>();
    }

    public static StudentList getInstance() {
        if(studentList == null){
            studentList = new StudentList();
        }
        return studentList;
    }

    private final ArrayList<Student> students;

    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * 添加学生
     * @param id 学生id
     * @param pwd 密码
     * @param name 姓名
     * @throws StudentIdExistException 学生id不能重复
     */
    public void addStudent(int id, String pwd,String name)
            throws  StudentIdExistException {
        for(Student i : students) {
            if(i.getId()==id) {
                throw new StudentIdExistException(id);
            }
        }
        students.add(new Student(id, pwd, name));
    }

    /**
     * 通过id获取学生
     * @param id 学生id
     * @return 返回学生对象
     * @throws StudentIdDoesNotExistException
     */
    public Student getStudent(int id)
            throws StudentIdDoesNotExistException {
        for(Student i: students) {
            if(i.getId()==id) {
                return i;
            }
        }
        throw new StudentIdDoesNotExistException(id);
    }

    /**
     * 删除学生
     * @param id 学生id
     * @throws StudentIdDoesNotExistException
     */
    public void deleteStudent(int id)
            throws StudentIdDoesNotExistException {
        Student student = getStudent(id);
        students.remove(student);
        throw new StudentIdDoesNotExistException(id);
    }

    /**
     * 程序退出时存至文件
     * @param file 文件名
     */
    public void saveToFile(File file) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file,false), StandardCharsets.UTF_8));
        for(Student i : students) {
            try {
                if(i.getServingOrder()!=null)i.getServingOrder().cancel(true);
                i.checkOrder();
            } catch (Exception ignored) {}
            bufferedWriter.write(i.getId() + "\\%\\" +
                    i.getPwd() + "\\%\\" +
                    i.getName() + "\\%\\" +
                    i.getCredit() + "\\%\\" +
                    i.getHistoryOrders().size());
            bufferedWriter.newLine();
            for(Order j : i.getHistoryOrders()) {
                bufferedWriter.write(j.toFileString());
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
    }

    /**
     * 程序启动时从文件读取数据
     * @param file 文件名
     */
    public void readFromFile(File file) throws IOException {
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8));
        students.clear();
        String in;
        while((in=bufferedReader.readLine())!=null) {
            String[] inArgs = in.split("\\\\%\\\\");
            Student student = new Student(Integer.parseInt(inArgs[0]),inArgs[1],inArgs[2]);
            student.setCredit(Integer.parseInt(inArgs[3]));
            int his = Integer.parseInt(inArgs[4]);
            for(int i = 0; i < his; i++) {
                in = bufferedReader.readLine();
                student.getHistoryOrders().push(new Order(in,student));
            }
            students.add(student);
        }
        bufferedReader.close();
    }
}
