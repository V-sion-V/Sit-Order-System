package src.Model;

import src.MyExceptions.ClassroomExistException;
import src.MyExceptions.ClassroomNotExistException;
import src.MyExceptions.ClassroomSizeException;
import src.MyExceptions.NonPositiveNumberException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

    public Classroom getClassroom(int id)
            throws ClassroomNotExistException {
        for(Classroom i : classrooms) {
            if(i.getCid()==id) {
                return i;
            }
        }
        throw new ClassroomNotExistException(id);
    }

    public Classroom getClassroom(String name)
            throws ClassroomNotExistException {
        for(Classroom i : classrooms) {
            if(i.getName().equals(name)) {
                return i;
            }
        }
        throw new ClassroomNotExistException(name);
    }

    public void addClassroom(int id, String name, int rowCount, int colCount)
            throws ClassroomExistException, NonPositiveNumberException , ClassroomSizeException {
        try {
            getClassroom(id);
            throw new ClassroomExistException(id);
        } catch (ClassroomNotExistException e1) {
            try {
                getClassroom(name);
                throw new ClassroomExistException(name);
            } catch (ClassroomNotExistException e2) {
                if(rowCount>0&&colCount>0) {
                    if(rowCount<100&&colCount<100){
                        classrooms.add(new Classroom(id,name,rowCount,colCount));
                    }else{
                        throw new ClassroomSizeException(rowCount, colCount);
                    }
                } else {
                    throw new NonPositiveNumberException((rowCount>0)?colCount:rowCount);
                }
            }
        }
    }

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