import MyExceptions.*;
/**
 * <p>本类为管理员类
 * <p>对学生、教室、座位进行增删改查
 */
public class Admin {
    private String adminPwd;
    private boolean isLogin;
    private Admin() {
        isLogin = true;
        adminPwd = "admin";
    }

    private static Admin admin = null;

    public static Admin getInstance(){
        if(admin ==null) {
            admin = new Admin();
        }
        return admin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getAdminPwd() {
        return adminPwd;
    }

    public void login(String pwd)
            throws PasswordNotMatchException {
        if(isLogin||pwd.equals(this.adminPwd)) {
            isLogin = true;
        } else {
            throw new PasswordNotMatchException();
        }
    }

    public void logout() throws DoesNotLoginException {
        if(isLogin){
            isLogin = false;
        } else {
            throw new DoesNotLoginException("logout");
        }
    }
/**
 * <p>更改管理员密码。此密码会在系统重启后重置，为了保证安全性，请在系统重启后重设。
 * 新密码需要包含字母和数字，并且长度至少为8。原密码错误、新密码不符合标准都会输出对应异常信息。
 * @param oldPwd
 * @param newPwd
 * @throws PasswordNotMatchException
 * @throws PasswordFormatException
 * @throws DoesNotLoginException
 */
    //oldPwd to verify, change to newPwd
    public void changeAdminPwd(String oldPwd, String newPwd)
            throws PasswordNotMatchException, PasswordFormatException,
            DoesNotLoginException {
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
        } else {
            throw new DoesNotLoginException("change password");
        }
    }
/**
 * 通过name或者id查询教室
 * @param op
 * @param key
 * @return
 * @throws ClassroomNotExistException
 * @throws NumberFormatException
 * @throws OperationNotExistException
 */
    //use "-id" to search for classroom by id, "-name" by name
    public Classroom getClassroom(String op,String key)
            throws ClassroomNotExistException,
            NumberFormatException, OperationNotExistException {
        if (op.equals("-id")) {
            return ClassroomList.getInstance().getClassroom(Integer.parseInt(key));
        } else if (op.equals("-name")) {
            return ClassroomList.getInstance().getClassroom(key);
        } else {
            throw new OperationNotExistException(op);
        }
    }
/**
 * <p>新建一间教室。定义了编号、名称、行数、列数。
 * 其中编号和名称不能与存在的教室编号相同，
 * 如果编号或名称已存在、行数列数不是小于100的正整数，则会输出异常信息。
 * @param id
 * @param name
 * @param rowCount
 * @param colCount
 * @throws NumberFormatException
 * @throws ClassroomExistException
 * @throws NonPositiveNumberException
 * @throws DoesNotLoginException
 * @throws NameFormatException
 * @throws ClassroomSizeException
 */
    //Check existence
    public void addClassroom(String id,String name,String rowCount,String colCount)
            throws NumberFormatException, ClassroomExistException,
            NonPositiveNumberException, DoesNotLoginException,
            NameFormatException, ClassroomSizeException {
        if(isLogin) {
            if(name.matches("[a-zA-Z0-9]{1,10}")) {
                int intRow = Integer.parseInt(rowCount);
                int intCol = Integer.parseInt(colCount);
                if(intRow<100&&intCol<100) {
                    ClassroomList.getInstance().addClassroom(
                            Integer.parseInt(id),
                            name, intRow, intCol
                    );
                } else {
                    throw new ClassroomSizeException(intRow, intCol);
                }
            } else {
                throw new NameFormatException(name);
            }
        } else {
            throw new DoesNotLoginException("add classroom");
        }
    }
/**
 * <p>删除一间教室。选项有“-id”和“-name”，对应的将关键字与编号或名称进行比对，来删除指定的教室。
 * 后文的f、g、h条目中的选项和关键字功能与此条目相同。
 * 删除一间教室将会删除其中的所有座位，并删除强制取消座位上的所有订单。
 * 如果教室的编号或是名称不存在，则会输出对应的异常信息。
 * @param op
 * @param key
 * @throws ClassroomNotExistException
 * @throws NumberFormatException
 * @throws DoesNotLoginException
 * @throws OperationNotExistException
 */
    public void deleteClassroom(String op,String key)
            throws ClassroomNotExistException, NumberFormatException,
            DoesNotLoginException, OperationNotExistException {
        if(isLogin) {
            if (op.equals("-id")) {
                ClassroomList.getInstance().deleteClassroom(Integer.parseInt(key));
            } else if (op.equals("-name")) {
                ClassroomList.getInstance().deleteClassroom(key);
            } else {
                throw new OperationNotExistException(op);
            }
        } else {
            throw new DoesNotLoginException("delete classroom");
        }
    }
/**
 * <p>通过id或name锁定教室，启用教室
 * @param op
 * @param key
 * @throws OperationNotExistException
 * @throws ClassroomNotExistException
 * @throws DoesNotLoginException
 * @throws NumberFormatException
 */
    //use "-id" to search for and delete classroom by id, "-name" by name
    public void enableClassroom(String op,String key)
            throws OperationNotExistException, ClassroomNotExistException,
            DoesNotLoginException, NumberFormatException{
        if(isLogin) {
            if (op.equals("-id")) {
                ClassroomList.getInstance().getClassroom(Integer.parseInt(key)).enable();
            } else if (op.equals("-name")) {
                ClassroomList.getInstance().getClassroom(key).enable();
            } else {
                throw new OperationNotExistException(op);
            }
        } else {
            throw new DoesNotLoginException("enable classroom");
        }
    }
/**
 *  <p>禁用一间教室将禁用其中的所有座位，这些座位上的所有订单将被强制取消，并给学生补偿10点信用值。
 * @param op
 * @param key
 * @throws OperationNotExistException
 * @throws ClassroomNotExistException
 * @throws DoesNotLoginException
 * @throws NumberFormatException
 */
    //use "-id" to search for and delete classroom by id, "-name" by name
    public void disableClassroom(String op,String key)
            throws OperationNotExistException, ClassroomNotExistException,
            DoesNotLoginException, NumberFormatException{
        if(isLogin) {
            if (op.equals("-id")) {
                ClassroomList.getInstance().getClassroom(Integer.parseInt(key)).disable();
            } else if (op.equals("-name")) {
                ClassroomList.getInstance().getClassroom(key).disable();
            } else {
                throw new OperationNotExistException(op);
            }
        } else {
            throw new DoesNotLoginException("disable classroom");
        }
    }
/**
 * <P>通过教室的行列确定座位
 * @param classroom
 * @param row
 * @param col
 * @return
 * @throws OutOfBoundException
 */
    public Sit getSit(Classroom classroom, String row, String col)
            throws OutOfBoundException {
        int intRow = Integer.parseInt(row)-1;
        int intCol = Integer.parseInt(col)-1;
        return classroom.getSit(intRow,intCol);
    }
/**
 * <p>将对应的座位启用。这将开放学生对他的预定权限。
 * 如果对应的行或列不存在，将输出行、列范围错误的异常信息；如果输入了除数字以外的其他字符，将会输出数字格式错误的异常信息。
 * 无特殊说明，下文中对于行和列的异常信息与此条目相同。
 * @param classroom
 * @param row
 * @param col
 * @throws OutOfBoundException
 * @throws DoesNotLoginException
 * @throws NumberFormatException
 */
    public void enableSit(Classroom classroom, String row, String col)
            throws OutOfBoundException,
            DoesNotLoginException,NumberFormatException{
        if(isLogin) {
            int intRow = Integer.parseInt(row)-1;
            int intCol = Integer.parseInt(col)-1;
            Sit sit = classroom.getSit(intRow,intCol);
            sit.enable();
        } else {
            throw new DoesNotLoginException("enable sit");
        }
    }
/**
 * <p>将对应的座位禁用。这将取消座位上的所有订单，并补偿相应学生10点信用，并关闭学生对该座位的预定权限。
 * @param classroom
 * @param row
 * @param col
 * @throws OutOfBoundException
 * @throws DoesNotLoginException
 * @throws NumberFormatException
 */
    public void disableSit(Classroom classroom, String row, String col)
            throws OutOfBoundException,
            DoesNotLoginException,NumberFormatException{
        if(isLogin) {
            int intRow = Integer.parseInt(row)-1;
            int intCol = Integer.parseInt(col)-1;
            Sit sit = classroom.getSit(intRow,intCol);
            sit.disable();
        } else {
            throw new DoesNotLoginException("disable sit");
        }
    }

    public void renameClassroom(Classroom classroom, String newName)
            throws NameFormatException, ClassroomExistException, DoesNotLoginException {
        if(isLogin) {
            if (newName.matches("[a-zA-Z0-9]{1,10}")) {
                classroom.changeName(newName);
            } else {
                throw new NameFormatException(newName);
            }
        } else {
            throw new DoesNotLoginException("rename classroom");
        }
    }

    public void addStudent(String id,String pwd,String name)
            throws StudentIdExistException, PasswordFormatException,
            NameFormatException, DoesNotLoginException {
        if(isLogin) {
            int iid = Integer.parseInt(id);
            if(name.matches("[a-zA-Z0-9]{1,10}")) {
                if(pwd.matches("[a-zA-Z0-9]{8,}")){
                    StudentList.getInstance().addStudent(iid,pwd,name);
                } else {
                    throw new PasswordFormatException(pwd);
                }
            } else {
                throw new NameFormatException(name);
            }
        } else {
            throw new DoesNotLoginException("add student");
        }
    }

    public Student getStudent(String id)
            throws StudentIdDoesNotExistException,NumberFormatException {
        return StudentList.getInstance().getStudent(Integer.parseInt(id));
    }

    public void deleteStudent(String id)
            throws StudentIdDoesNotExistException,
            NumberFormatException, DoesNotLoginException {
        if(isLogin) {
            StudentList.getInstance().deleteStudent(Integer.parseInt(id));
        } else {
            throw new DoesNotLoginException("delete student");
        }
    }
}