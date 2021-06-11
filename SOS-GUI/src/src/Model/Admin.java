package src.Model;

import src.MyExceptions.*;

public class Admin {
    private String adminPwd;
    private boolean isLogin;
    private Admin() {
        isLogin = false;
        adminPwd = "admin";
    }

    private static Admin admin = null;

    public static Admin getInstance(){
        if(admin ==null) {
            admin = new Admin();
        }
        return admin;
    }

    public String getAdminPwd() {
        return adminPwd;
    }

    public void login(String pwd)
            throws PasswordNotMatchException {
        if(!isLogin && pwd.equals(this.adminPwd)) {
            isLogin = true;
        } else {
            throw new PasswordNotMatchException();
        }
    }

    public void logout() {
            isLogin = false;
    }

    //oldPwd to verify, change to newPwd
    public void changeAdminPwd(String oldPwd, String newPwd)
            throws PasswordNotMatchException, PasswordFormatException {
        if(isLogin) {
            if (oldPwd.equals(adminPwd)) {
                if (newPwd.matches("[0-9a-zA-Z]{8,}") &&
                        !newPwd.matches("[0-9]*") &&
                        !newPwd.matches("[a-zA-Z]*")) {
                    adminPwd = newPwd;
                } else {
                    throw new PasswordFormatException(newPwd);
                }
            } else {
                throw new PasswordNotMatchException();
            }
        }
    }

    public Sit getSit(Classroom classroom, String row, String col)
            throws OutOfBoundException {
        int intRow = Integer.parseInt(row)-1;
        int intCol = Integer.parseInt(col)-1;
        return classroom.getSit(intRow,intCol);
    }

    public void renameClassroom(Classroom classroom, String newName)
            throws ClassroomExistException {
        classroom.changeName(newName);
    }

    public void addStudent(String id,String pwd,String name)
            throws StudentIdExistException, PasswordFormatException
             {
        if(isLogin) {
            int iid = Integer.parseInt(id);
            if(pwd.matches("[a-zA-Z0-9]{8,}")){
                StudentList.getInstance().addStudent(iid,pwd,name);
            } else {
                throw new PasswordFormatException(pwd);
            }
        }
    }

}