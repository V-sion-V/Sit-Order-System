package src.Controller;


import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import src.Model.Admin;
import src.Model.ClassroomList;
import src.Model.Student;
import src.Model.StudentList;

/**
 * 个人信息窗口控制类
 */
public class InfoController {
    /**
     * 当前窗口
     */
    private Stage infostage;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    @FXML
    private Label ad_pwd;

    @FXML
    private Label ad_stus;

    @FXML
    private Label ad_classrooms;

    @FXML
    private JFXButton conf;

    @FXML
    private Label s_name;

    @FXML
    private Label s_id;

    @FXML
    private Label s_pwd;

    @FXML
    private Label s_credit;

    @FXML
    void conf(ActionEvent event) {
        infostage.close();
    }

    @FXML
    void conf_enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            infostage.close();
        }
    }

    /**
     * 管理员个人信息初始化
     */
    public void ad_init() {
        ad_pwd.setText(Admin.getInstance().getAdminPwd());
        ad_stus.setText(String.valueOf(StudentList.getInstance().getStudents().size()));
        ad_classrooms.setText(String.valueOf(ClassroomList.getInstance().getClassrooms().size()));
    }

    /**
     * 学生个人信息初始化
     */
    public void s_init() {
        s_name.setText(student.getName());
        s_id.setText(String.valueOf(student.getId()));
        s_pwd.setText(student.getPwd());
        s_credit.setText(String.valueOf(student.getCredit()));
    }

    public void setFormstage(Stage stage) {
        this.infostage = stage;
    }
}
