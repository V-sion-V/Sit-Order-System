import MyExceptions.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class NoGUI {
    public static Admin admin = Admin.getInstance();
    public static Scanner sc = new Scanner(System.in);

    public static String[] getLine() {
        String str = sc.nextLine();
        return str.trim().split("[ \\t]+");
    }

    //Interface
    public static void main(String[] args) {
        try{
            ClassroomList.getInstance().readFromFile(new File("savedClassrooms.txt"));
            StudentList.getInstance().readFromFile(new File("savedStudents.txt"));
        } catch (NumberFormatException | IOException e) {
            System.out.println("Input file error");
        }
        while(true) {
            String[] inArgs = getLine();
            try {
                if(inArgs.length == 1 && inArgs[0].equals("quit")) {
                    ClassroomList.getInstance().saveToFile(new File("savedClassrooms.txt"));
                    StudentList.getInstance().saveToFile(new File("savedStudents.txt"));
                    System.exit(0);
                }
                else if(inArgs.length == 2 && inArgs[0].equals("su")) adminLogin(inArgs[1]);
                else if(inArgs.length == 3 && inArgs[0].equals("login")) studentLogin(inArgs[1], inArgs[2]);
                else System.out.println("Input illegal");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    //Interface for administrator
    public static void adminLogin(String pwd) {
        try {
            admin.login(pwd);
            System.out.println("Entered super user mode");
            while(true) {
                String[] inArgs = getLine();
                if(inArgs.length == 1 && inArgs[0].equals("logout")) {
                    if (adminLogout()) return;
                } else if(inArgs.length == 3 && inArgs[0].equals("changepwd")) {
                    changeAdminPassword(inArgs[1], inArgs[2]);
                } else if(inArgs.length == 1 && inArgs[0].equals("info")) {
                    showAdminInfo();
                } else if(inArgs.length == 1 && inArgs[0].equals("rooms")) {
                    showClassrooms();
                } else if(inArgs.length == 5 && inArgs[0].equals("addroom")) {
                    addClassroom(inArgs[1],inArgs[2],inArgs[3],inArgs[4]);
                } else if(inArgs.length == 3 && inArgs[0].equals("delroom")) {
                    deleteClassroom(inArgs[1],inArgs[2]);
                } else if(inArgs.length == 3 && inArgs[0].equals("enabler")) {
                    enableClassroom(inArgs[1],inArgs[2]);
                } else if(inArgs.length == 3 && inArgs[0].equals("disabler")) {
                    disableClassroom(inArgs[1],inArgs[2]);
                } else if(inArgs.length == 3 && inArgs[0].equals("showr")) {
                    showClassroom(inArgs[1],inArgs[2]);
                } else if(inArgs.length == 3 && inArgs[0].equals("enter")) {
                    adminEnterClassroom(inArgs[1],inArgs[2]);
                } else if(inArgs.length == 3 && inArgs[0].equals("addstu")) {
                    addStudent(inArgs[1],"00000000",inArgs[2]);
                } else if(inArgs.length == 2 && inArgs[0].equals("delstu")) {
                    deleteStudent(inArgs[1]);
                } else if(inArgs.length == 1 && inArgs[0].equals("stus")) {
                    showStudents();
                } else if(inArgs.length == 2 && inArgs[0].equals("stuso")) {
                    showStudentOrder(inArgs[1]);
                } else if(inArgs.length == 2 && inArgs[0].equals("stuho")) {
                    showStudentHistory(inArgs[1]);
                }else System.out.println("Input illegal");
            }
        } catch (Exception e) {
            if(e.getClass().equals(PasswordNotMatchException.class)) {
                System.out.println(e.toString());
            } else {
                System.out.println("Unknown exception");
            }
        }
    }

    public static void showAdminInfo() {
        System.out.println("Admin:\n|Pwd:"+admin.getAdminPwd()+"\n|Login:"+((admin.isLogin())?"yes":"no"));
    }

    public static boolean adminLogout() {
        try {
            admin.logout();
            System.out.println("Logout success");
            return true;
        } catch (Exception e) {
            if(e.getClass().equals(DoesNotLoginException.class)) {
                System.out.println(e.toString());
            } else {
                System.out.println("Unknown exception");
            }
            return false;
        }
    }

    public static void changeAdminPassword(String oldPwd, String newPwd) {
        try {
            admin.changeAdminPwd(oldPwd, newPwd);
            System.out.println("Password was changed to: " + admin.getAdminPwd());
        } catch (Exception e) {
            if(e.getClass().equals(DoesNotLoginException.class)||
                    e.getClass().equals(PasswordNotMatchException.class)||
                    e.getClass().equals(PasswordFormatException.class)) {
                System.out.println(e.toString());
            } else {
                System.out.println("Unknown exception");
            }
        }
    }

    public static void addClassroom(String id, String name, String rowCount, String colCount) {
        try {
            admin.addClassroom(id,name,rowCount,colCount);
            System.out.println("Classroom " + id
                    + ":[" + name
                    + "] (" + rowCount
                    + ", " + colCount
                    + ")");
        } catch (NonPositiveNumberException | ClassroomExistException |
                DoesNotLoginException | NameFormatException |
                ClassroomSizeException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void deleteClassroom(String op,String key) {
        try {
            System.out.println("(Y/N)?");
            String[] inArgs = getLine();
            if(inArgs.length==1&&inArgs[0].equals("Y")) {
                admin.deleteClassroom(op, key);
                System.out.println("Classroom removed");
            } else if(inArgs.length==1&&inArgs[0].equals("N")) {
                System.out.println("Operation canceled");
            } else throw new OperationNotExistException(inArgs[0]);
        } catch (OperationNotExistException |
                ClassroomNotExistException |
                DoesNotLoginException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void enableClassroom(String op,String key) {
        try {
            admin.enableClassroom(op,key);
            System.out.println("Classroom enabled");
        } catch (OperationNotExistException |
                ClassroomNotExistException |
                DoesNotLoginException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void disableClassroom(String op,String key) {
        try {
            System.out.println("(Y/N)?");
            String[] inArgs = getLine();
            if(inArgs.length==1&&inArgs[0].equals("Y")) {
                admin.disableClassroom(op, key);
                System.out.println("Classroom disabled");
            } else if(inArgs.length==1&&inArgs[0].equals("N")) {
                System.out.println("Operation canceled");
            } else throw new OperationNotExistException(inArgs[0]);
        } catch (OperationNotExistException |
                ClassroomNotExistException |
                DoesNotLoginException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void showClassrooms() {
        ArrayList<Classroom> classrooms = ClassroomList.getInstance().getClassrooms();
        if(classrooms.size()==0) {
            System.out.println("No classroom");
        } else {
            System.out.println("<-----------All Classrooms----------->");
            for(Classroom i : classrooms) {
                System.out.println( (i.isAvl()?"AVL":"DIS")
                        + " ,Cid: " + i.getCid()
                        + " ,Name: " + i.getName()
                        + " ,Rows: " + i.getRowCount()
                        + " ,Cols: " + i.getColCount());
            }
            System.out.println("<-----------All Classrooms----------->");
        }
    }

    public static void showClassroom(String op, String key) {
        try{
            Classroom classroom = admin.getClassroom(op, key);
            showClassroom(classroom);
        } catch (OperationNotExistException |
                ClassroomNotExistException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void showClassroom(Classroom classroom) {
        try {
            System.out.print("   ");
            for (int i = 0; i < classroom.getColCount(); i++) {
                System.out.printf("%02d.", i + 1);
            }
            System.out.println();
            for (int i = 0; i < classroom.getRowCount(); i++) {
                StringBuilder row = new StringBuilder(String.format("%02d.", i + 1));
                for (int j = 0; j < classroom.getColCount(); j++) {
                    row.append("[");
                    Sit temp = classroom.getSits().get(i * classroom.getColCount() + j);
                    if (temp.isAvl() && temp.getOnSit() != null) {
                        row.append("U");
                    } else if (temp.isAvl() && temp.getOnSit() == null) {
                        row.append(" ");
                    } else if (!temp.isAvl() && temp.getOnSit() != null) {
                        row.append("E");
                    } else {
                        row.append("X");
                    }
                    row.append("]");
                }
                System.out.println(row);
            }
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void addStudent(String id,String pwd, String name) {
        try {
            admin.addStudent(id, pwd, name);
            System.out.println("Student " + id
                    + ":[" + name
                    + "] (" + pwd
                    + ")");
        } catch ( DoesNotLoginException |
                NameFormatException |
                StudentIdExistException |
                PasswordFormatException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void deleteStudent(String id) {
        try {
            admin.deleteStudent(id);
        }catch (StudentIdDoesNotExistException | DoesNotLoginException e) {
            System.out.println(e.toString());
        }
    }

    public static void showStudents() {
        if(StudentList.getInstance().getStudents().isEmpty()) {
            System.out.println("No student");
        } else {
            System.out.println("<---------------All Students--------------->");
            for(Student i : StudentList.getInstance().getStudents()) {
                System.out.println(" Sid: " + i.getId()
                        + " ,Name: " + i.getName()
                        + " ,Pwd: " + i.getPwd()
                        + " ,Credit: " + i.getCredit());
            }
            System.out.println("<---------------All Students--------------->");
        }
    }

    public static void showStudentOrder(String id){
        try {
            Student student = StudentList.getInstance().getStudent(Integer.parseInt(id));
            showStudentOrder(student);
        } catch (StudentIdDoesNotExistException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void showStudentOrder(Student student) {
        try {
            Order order = student.getServingOrder();
            if(order != null) {
                System.out.println("Classroom " + order.getTarget().getClassroom().getName()
                        + " Sit(" + (order.getTarget().getSid()/order.getTarget().getClassroom().getColCount()+1)
                        + " ," + (order.getTarget().getSid()%order.getTarget().getClassroom().getColCount()+1)
                        + ") Time: " + (order.getStartTime().get(Calendar.MONTH) + 1)
                        + "/" + order.getStartTime().get(Calendar.DAY_OF_MONTH)
                        + "/" + order.getStartTime().get(Calendar.HOUR_OF_DAY)
                        + ":00-" + order.getEndTime().get(Calendar.HOUR_OF_DAY)
                        + ":00");
            } else {
                System.out.println("No order");
            }
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void showStudentHistory(String id){
        try {
            Student student = StudentList.getInstance().getStudent(Integer.parseInt(id));
            showStudentHistory(student);
        } catch (StudentIdDoesNotExistException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void showStudentHistory(Student student){
        try {
            LinkedList<Order> orders = student.getHistoryOrders();
            if(!orders.isEmpty()) {
                student.sortHistory();
                System.out.println("<-----------------------History Orders----------------------->");
                for(Order i : orders)
                    System.out.println("Classroom " + i.getTarget().getClassroom().getName()
                            + " Sit(" + (i.getTarget().getSid()/i.getTarget().getClassroom().getColCount()+1)
                            + " ," + (i.getTarget().getSid()%i.getTarget().getClassroom().getColCount()+1)
                            + ") Time: " + (i.getStartTime().get(Calendar.MONTH) + 1)
                            + "/" + i.getStartTime().get(Calendar.DAY_OF_MONTH)
                            + "/" + i.getStartTime().get(Calendar.HOUR_OF_DAY)
                            + ":00-" + i.getEndTime().get(Calendar.HOUR_OF_DAY)
                            + ":00 State:" + (i.isArrived() ? "A" : "_")
                            + (i.isCanceled() ? "C" : "_") + (i.isEnded() ? "E" : "_"));
                System.out.println("<-----------------------History Orders----------------------->");
            } else {
                System.out.println("No history");
            }
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    //Interface for administrator after entered a classroom
    public static void adminEnterClassroom(String op,String key) {
        try{
            Classroom classroom = admin.getClassroom(op, key);
            System.out.println("Entered classroom id: " + classroom.getCid());
            showClassroom(classroom);
            while(true) {
                String[] inArgs = getLine();
                if(inArgs.length == 1 && inArgs[0].equals("back")) {
                    System.out.println("Exited classroom");
                    return;
                } else if(inArgs.length == 3 && inArgs[0].equals("enables")) {
                    enableSit(classroom,inArgs[1], inArgs[2]);
                } else if(inArgs.length == 3 && inArgs[0].equals("disables")) {
                    disableSit(classroom,inArgs[1],inArgs[2]);
                } else if(inArgs.length == 1 && inArgs[0].equals("show")) {
                    showClassroom(classroom);
                } else if(inArgs.length == 2 && inArgs[0].equals("rename")) {
                    renameClassroom(classroom,inArgs[1]);
                } else if(inArgs.length == 3 && inArgs[0].equals("sitso")) {
                    getSitOrders(classroom,inArgs[1],inArgs[2]);
                } else if(inArgs.length == 3 && inArgs[0].equals("sitho")) {
                    getSitHistory(classroom,inArgs[1],inArgs[2]);
                } else System.out.println("Input illegal");
            }
        } catch (OperationNotExistException |
                ClassroomNotExistException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void enableSit(Classroom classroom, String row, String col) {
        try {
            admin.enableSit(classroom,row,col);
            System.out.println("Sit enabled");
        } catch (OutOfBoundException | DoesNotLoginException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void disableSit(Classroom classroom, String row, String col) {
        try {
            System.out.println("(Y/N)?");
            String[] inArgs = getLine();
            if(inArgs.length==1&&inArgs[0].equals("Y")) {
                admin.disableSit(classroom,row,col);
                System.out.println("Sit disabled");
            } else if(inArgs.length==1&&inArgs[0].equals("N")) {
                System.out.println("Operation canceled");
            } else throw new OperationNotExistException(inArgs[0]);
        } catch (OutOfBoundException |
                DoesNotLoginException |
                OperationNotExistException e) {
            System.out.println(e.toString());
        } catch (NumberFormatException e) {
            System.out.println("Id should be a number");
        }
    }

    public static void renameClassroom(Classroom classroom, String newName) {
        try {
            admin.renameClassroom(classroom,newName);
            System.out.println("Renamed");
        } catch (ClassroomExistException |
                NameFormatException |
                DoesNotLoginException e) {
            System.out.println(e.toString());
        }
    }

    public static void getSitOrders(Classroom classroom, String row, String col) {
        try {
            Sit sit = admin.getSit(classroom, row, col);
            if (sit.getServingOrders().isEmpty()) {
                System.out.println("No serving order");
            } else {
                sit.sortServing();
                System.out.println("<-----------------Serving Orders----------------->");
                for (Order i : sit.getServingOrders()) {
                    System.out.println(" StuId: " + i.getOwner().getId()
                            + " StuName: " + i.getOwner().getName()
                            + " Time: " + (i.getStartTime().get(Calendar.MONTH) + 1)
                            + "/" + i.getStartTime().get(Calendar.DAY_OF_MONTH)
                            + "/" + i.getStartTime().get(Calendar.HOUR_OF_DAY)
                            + ":00-" + i.getEndTime().get(Calendar.HOUR_OF_DAY)
                            + ":00");
                }
                System.out.println("<-----------------Serving Orders----------------->");
            }
        } catch (OutOfBoundException e) {
            System.out.println(e.toString());
        }
    }

    public static void getSitHistory(Classroom classroom, String row, String col) {
        try {
            Sit sit = admin.getSit(classroom, row, col);
            if (sit.getHistoryOrders().isEmpty()) {
                System.out.println("No history order");
            } else {
                sit.sortHistory();
                System.out.println("<-----------------History Orders----------------->");
                for (Order i : sit.getHistoryOrders()) {
                    System.out.println(" StuId: " + i.getOwner().getPwd()
                            + " StuName: " + i.getOwner().getName()
                            + " Time: " + (i.getStartTime().get(Calendar.MONTH) + 1)
                            + "/" + i.getStartTime().get(Calendar.DAY_OF_MONTH)
                            + "/" + i.getStartTime().get(Calendar.HOUR_OF_DAY)
                            + ":00-" + i.getEndTime().get(Calendar.HOUR_OF_DAY)
                            + ":00 State:" + (i.isArrived() ? "A" : "_")
                            + (i.isCanceled() ? "C" : "_") + (i.isEnded() ? "E" : "_"));
                }
                System.out.println("<-----------------History Orders----------------->");
            }
        } catch (OutOfBoundException e) {
            System.out.println(e.toString());
        }
    }

    public static void studentLogin(String id,String pwd) {
        Student student;
        try {
            student = StudentList.getInstance().getStudent(Integer.parseInt(id));
            student.login(pwd);
            System.out.println("Student: " + id + " login successful");
            if(student.getServingOrder()!=null)checkOrder(student);
            while(true) {
                String[] inArgs = getLine();
                if(inArgs.length == 1 && inArgs[0].equals("logout")) {
                    if (studentLogout(student)) return;
                } else if(inArgs.length == 3 && inArgs[0].equals("changepwd")) {
                    changeStudentPassword(student,inArgs[1], inArgs[2]);
                } else if(inArgs.length == 1 && inArgs[0].equals("info")) {
                    if(student.getServingOrder()!=null)checkOrder(student);
                    showStudentInfo(student);
                } else if(inArgs.length == 1 && inArgs[0].equals("so")) {
                    if(student.getServingOrder()!=null)checkOrder(student);
                    showStudentOrder(student);
                } else if(inArgs.length == 1 && inArgs[0].equals("ho")) {
                    if(student.getServingOrder()!=null)checkOrder(student);
                    showStudentHistory(student);
                } else if(inArgs.length == 1 && inArgs[0].equals("check")) {
                    if(student.getServingOrder()!=null)checkOrder(student);
                    else System.out.println("No order");
                } else if(inArgs.length == 1 && inArgs[0].equals("arrive")) {
                    arrive(student);
                } else if(inArgs.length == 1 && inArgs[0].equals("leave")) {
                    leave(student);
                } else if(inArgs.length == 1 && inArgs[0].equals("rooms")) {
                    showClassrooms();
                } else if(inArgs.length == 3 && inArgs[0].equals("showr")) {
                    showClassroom(inArgs[1],inArgs[2]);
                } else if(inArgs.length == 1 && inArgs[0].equals("order")) {
                    createAnOrder(student);
                } else if(inArgs.length == 1 && inArgs[0].equals("cancel")) {
                    studentCancel(student);
                } else {
                    System.out.println("Input illegal");
                }
            }
        } catch (StudentIdDoesNotExistException |
                PasswordNotMatchException e) {
            System.out.println(e.toString());
        }
    }

    private static void studentCancel(Student student) {
        try {
            System.out.println("(Y/N)?");
            String[] inArgs = getLine();
            if(inArgs.length==1&&inArgs[0].equals("Y")) {
                student.cancelOrder();
                System.out.println("Order canceled");
            } else if(inArgs.length==1&&inArgs[0].equals("N")) {
                System.out.println("Operation canceled");
            } else throw new OperationNotExistException(inArgs[0]);
        } catch (OperationNotExistException |
                HoldingOrderException |
                CanNotCancelOrderException e) {
            System.out.println(e.toString());
        }
    }

    private static void createAnOrder(Student student) {
        if(student.getServingOrder()==null) {
            while(true) {
                System.out.println("Please enter your order's time:");
                System.out.println("[Date]: 0 for today, 1 for tomorrow");
                System.out.println("[Start|End]: 8 for 8:00, 18 for 18:00, etc.");
                System.out.println("Input [Date] [Start] [End] to select range");
                System.out.println("Input \"-q\" to quit");
                String[] inArgs = getLine();
                if(inArgs.length==1&&inArgs[0].equals("-q")){
                    System.out.println("Exit order mode");
                    return;
                } else if(inArgs.length == 3) {
                    try{
                        GregorianCalendar start = student.inputTime(inArgs[0], inArgs[1]);
                        GregorianCalendar end = student.inputTime(inArgs[0], inArgs[2]);
                        if(start.compareTo(end)>0) throw new TimeIllegalException("end<=start");
                        while(true) {
                            System.out.println("<-----------All Classrooms----------->");
                            for (Classroom i : ClassroomList.getInstance().getClassrooms()) {
                                if (i.isAvl()) {
                                    System.out.println("Cid: " + i.getCid()
                                            + " ,Name: " + i.getName()
                                            + " ,Rows: " + i.getRowCount()
                                            + " ,Cols: " + i.getColCount());
                                }
                            }
                            System.out.println("<-----------All Classrooms----------->");
                            System.out.println("Please choose a classroom: [id]");
                            inArgs = getLine();
                            if(inArgs.length==1&&inArgs[0].equals("-q")){
                                break;
                            } else if (inArgs.length==1) {
                                try {
                                    int id = Integer.parseInt(inArgs[0]);
                                    Classroom classroom = ClassroomList.getInstance().getClassroom(id);
                                    if(classroom.isAvl()){
                                        Sit sit = selectSit(classroom,start,end);
                                        if(sit == null) continue;
                                        try {
                                            student.generateOrder(sit,start,end);
                                            System.out.println("Order created");
                                            showStudentOrder(student);
                                            return;
                                        } catch(Exception e) {
                                            System.out.println(e.toString());
                                        }
                                    } else {
                                        throw new ClassroomNotExistException(id);
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Id should be a number");
                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }
                            } else {
                                System.out.println("Input illegal");
                            }
                        }
                    } catch (TimeIllegalException e) {
                        System.out.println(e.toString());
                    } catch (NumberFormatException e) {
                        System.out.println("Please input numbers");
                    }
                } else {
                    System.out.println("Input illegal");
                }
            }
        } else {
            System.out.println("You have an order, please cancel or check first");
        }
    }

    private static void showSitInRange(Classroom classroom, GregorianCalendar start, GregorianCalendar end) {
        System.out.print("   ");
        for (int i = 0; i < classroom.getColCount(); i++) {
            System.out.printf("%02d.", i + 1);
        }
        System.out.println();
        for (int i = 0; i < classroom.getRowCount(); i++) {
            StringBuilder row = new StringBuilder(String.format("%02d.", i + 1));
            for (int j = 0; j < classroom.getColCount(); j++) {
                row.append("[");
                Sit temp = classroom.getSits().get(i * classroom.getColCount() + j);
                if (temp.isAvl() && temp.checkSitInTimeRange(start,end)) {
                    row.append(" ");
                } else {
                    row.append("X");
                }
                row.append("]");
            }
            System.out.println(row);
        }
    }

    private static Sit selectSit(Classroom classroom, GregorianCalendar start, GregorianCalendar end) {
        while(true) {
            showSitInRange(classroom, start, end);
            System.out.println("Please input row and col:");
            String[] inArgs = getLine();
            if(inArgs.length==1&&inArgs[0].equals("-q")){
                return null;
            } else if (inArgs.length==2) {
                try {
                    Sit sit = admin.getSit(classroom, inArgs[0], inArgs[1]);
                    if(sit.isAvl()&&sit.checkSitInTimeRange(start,end)) {
                        return sit;
                    } else {
                        System.out.println("This sit is disabled");
                    }
                } catch (OutOfBoundException e) {
                    System.out.println(e.toString());
                } catch (NumberFormatException e) {
                    System.out.println("Please input numbers");
                }
            } else {
                System.out.println("Input illegal");
            }
        }
    }

    private static void leave(Student student) {
        try{
            student.leave();
            System.out.println("Left");
        } catch (TimeIllegalException |
                HoldingOrderException e) {
            System.out.println(e.toString());
        } catch (DoesNotLeaveException e) {
            System.out.println("-------------Warning-------------");
            System.out.println("You did not leave on time");
            System.out.println("Please leave your seat");
            System.out.println("Your order has been canceled");
            System.out.println("Credit decreased to: " + student.getCredit());
            System.out.println("-------------Warning-------------");
        } catch (OrderLateException e) {
            System.out.println("-------------Warning-------------");
            System.out.println("You did not arrive on time");
            System.out.println("Your order has been canceled");
            System.out.println("Credit decreased to: " + student.getCredit());
            System.out.println("-------------Warning-------------");
        } catch (OrderCanceledException e) {
            System.out.println("-------------Notice-------------");
            System.out.println("Something unpredictable happened");
            System.out.println("Your order has been canceled");
            System.out.println("Credit increased to: " + student.getCredit());
            System.out.println("Please leave your seat");
            System.out.println("-------------Notice-------------");
        }
    }

    private static void arrive(Student student) {
        try {
            student.arrive();
            System.out.println("Arrived");
        } catch (HoldingOrderException |
                TimeIllegalException e) {
            System.out.println(e.toString());
        } catch (DoesNotLeaveException e) {
            System.out.println("-------------Warning-------------");
            System.out.println("You have already arrived");
            System.out.println("But you did not leave on time");
            System.out.println("Please leave your seat");
            System.out.println("Your order has been canceled");
            System.out.println("Credit decreased to: " + student.getCredit());
            System.out.println("-------------Warning-------------");
        } catch (OrderLateException e) {
            System.out.println("-------------Warning-------------");
            System.out.println("You did not arrive on time");
            System.out.println("Your order has been canceled");
            System.out.println("Credit decreased to: " + student.getCredit());
            System.out.println("-------------Warning-------------");
        } catch (OrderCanceledException e) {
            System.out.println("-------------Notice-------------");
            System.out.println("Something unpredictable happened");
            System.out.println("Your order has been canceled");
            System.out.println("Credit increased to: " + student.getCredit());
            System.out.println("Please leave your seat");
            System.out.println("-------------Notice-------------");
        }
    }

    private static void showStudentInfo(Student student) {
        System.out.println("info:" +
                "\n|Name: " + student.getName() +
                "\n|Id: " + student.getId() +
                "\n|Pwd: " + student.getPwd() +
                "\n|Credit: " + student.getCredit());
    }

    public static void checkOrder(Student student) {
        try{
            student.checkOrder();
        } catch (DoesNotLeaveException e) {
            System.out.println("-------------Warning-------------");
            System.out.println("You did not leave on time");
            System.out.println("Your order has been canceled");
            System.out.println("Credit decreased to: " + student.getCredit());
            System.out.println("-------------Warning-------------");
        } catch (OrderLateException e) {
            System.out.println("-------------Warning-------------");
            System.out.println("You did not arrive on time");
            System.out.println("Your order has been canceled");
            System.out.println("Credit decreased to: " + student.getCredit());
            System.out.println("-------------Warning-------------");
        } catch (OrderCanceledException e) {
            System.out.println("-------------Notice-------------");
            System.out.println("Something unpredictable happened");
            System.out.println("Your order has been canceled");
            System.out.println("Credit increased to: " + student.getCredit());
            System.out.println("-------------Notice-------------");
        } catch(HoldingOrderException ignored){}
    }

    public static boolean studentLogout(Student student){
        try {
            System.out.println("Student logout success");
            student.logout();
            return true;
        } catch (DoesNotLoginException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static void changeStudentPassword(Student student, String oldPwd, String newPwd){
        try {
            student.changePwd(oldPwd, newPwd);
            System.out.println("Password changed");
        } catch (PasswordNotMatchException |
                PasswordFormatException |
                DoesNotLoginException e) {
            System.out.println(e.toString());
        }
    }
}