package src.Model;

import src.MyExceptions.CanNotCancelOrderException;
import src.MyExceptions.StudentIdDoesNotExistException;
import src.MyExceptions.StudentIdExistException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

    public void addStudent(int id, String pwd,String name)
            throws  StudentIdExistException {
        for(Student i : students) {
            if(i.getId()==id) {
                throw new StudentIdExistException(id);
            }
        }
        students.add(new Student(id, pwd, name));
    }

    public Student getStudent(int id)
            throws StudentIdDoesNotExistException {
        for(Student i: students) {
            if(i.getId()==id) {
                return i;
            }
        }
        throw new StudentIdDoesNotExistException(id);
    }

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
