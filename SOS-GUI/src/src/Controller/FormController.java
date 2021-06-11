package src.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import src.MainUI;
import src.Model.Admin;
import src.Model.Student;
import src.Model.StudentList;
import src.MyExceptions.PasswordFormatException;
import src.MyExceptions.PasswordNotMatchException;
import src.MyExceptions.StudentIdDoesNotExistException;

import java.util.Objects;

/**
 * 表单填写控制类
 */
public class FormController {
    /**
     * 当前窗口
     */
    private Stage formstage;
    private MainUI mainui;
    private Student student;

    /**
     * 登录控制器
     */
    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setMainui(MainUI mainui) {
        this.mainui = mainui;
    }

    @FXML
    private TextField s_l_id;

    @FXML
    private TextField s_l_pwd;

    @FXML
    private JFXButton s_l_conf;

    @FXML
    void handle_s_l_conf(ActionEvent event) {
        exe_s_l_conf();
    }

    @FXML
    void stu_l_enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            exe_s_l_conf();
        }
    }

    private void exe_s_l_conf() {
        Student student;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        alert.setTitle("提示");
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        try {
            student = StudentList.getInstance().getStudent(Integer.parseInt(s_l_id.getText()));
            student.login(s_l_pwd.getText());
            alert.setHeaderText(null);
            alert.setContentText("登录成功");
            alert.showAndWait();
            formstage.close();
            mainui.getPrimaryStage().close();
            mainui.StuMain(student);
        } catch (NumberFormatException e) {
            if (s_l_id.getText().length() == 0) {
                alert.setHeaderText("登录失败");
                alert.setContentText("请输入学号");
                alert.showAndWait();
            } else {
                alert.setHeaderText("登录失败");
                alert.setContentText("学号只含有数字");
                alert.showAndWait();
            }
        } catch (StudentIdDoesNotExistException e) {
            alert.setHeaderText("登录失败");
            alert.setContentText(e.toString());
            alert.showAndWait();
        } catch (PasswordNotMatchException e) {
            if (s_l_pwd.getText().length() == 0) {
                alert.setHeaderText("登录失败");
                alert.setContentText("请输入密码");
                alert.showAndWait();
            } else {
                alert.setHeaderText("登录失败");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private JFXButton s_l_canc;

    @FXML
    private TextField ad_l_pwd;

    @FXML
    private JFXButton ad_l_conf;

    @FXML
    void handle_ad_l_conf(ActionEvent event) {
        exe_ad_l_conf();
    }

    @FXML
    void ad_l_enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            exe_ad_l_conf();
        }
    }

    private void exe_ad_l_conf() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        alert.setTitle("提示");
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        try {
            Admin.getInstance().login(ad_l_pwd.getText());
            alert.setHeaderText(null);
            alert.setContentText("登录成功");
            alert.showAndWait();
            formstage.close();
            mainui.getPrimaryStage().close();
            mainui.AdminMain();
        } catch (PasswordNotMatchException e) {
            alert.setHeaderText("登录失败");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private JFXButton ad_l_canc;

    @FXML
    private TextField ad_ch_opwd;

    @FXML
    private TextField ad_ch_npwd;

    @FXML
    private JFXButton ad_ch_conf;

    @FXML
    void handle_ad_ch_conf(ActionEvent event) {
        exe_ad_ch_conf();
    }

    @FXML
    void ad_ch_enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            exe_ad_ch_conf();
        }
    }

    private void exe_ad_ch_conf() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

        try {
            Admin.getInstance().changeAdminPwd(ad_ch_opwd.getText(), ad_ch_npwd.getText());

            alert.setHeaderText(null);
            alert.setContentText("修改成功");
            alert.showAndWait();
            formstage.close();
            mainui.getPrimaryStage().close();
            mainui.AdminMain();
        }  catch (PasswordNotMatchException | PasswordFormatException e) {
            if (ad_ch_opwd.getText().length() == 0) {
                alert.setHeaderText("修改失败");
                if (e instanceof PasswordFormatException) alert.setContentText("请输入原密码");
                else alert.setContentText("请输入新密码");
                alert.showAndWait();
            } else {
                alert.setHeaderText("修改失败");
                if (e instanceof PasswordFormatException) alert.setContentText("原" + e);
                else alert.setContentText(e.toString());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private JFXButton ad_ch_canc;

    @FXML
    private TextField stu_ch_opwd;

    @FXML
    private TextField stu_ch_npwd;

    @FXML
    private JFXButton stu_ch_conf;

    @FXML
    void handle_stu_ch_conf(ActionEvent event) {
        exe_stu_ch_conf();
    }

    @FXML
    void stu_ch_enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            exe_stu_ch_conf();
        }
    }

    private void exe_stu_ch_conf() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

        try {
            student.changePwd(stu_ch_opwd.getText(), stu_ch_npwd.getText());

            alert.setHeaderText(null);
            alert.setContentText("修改成功");
            alert.showAndWait();
            formstage.close();
            mainui.getPrimaryStage().close();
            mainui.StuMain(student);
        }  catch (PasswordNotMatchException | PasswordFormatException e) {
            if (stu_ch_opwd.getText().length() == 0) {
                alert.setHeaderText("修改失败");
                if (e instanceof PasswordFormatException) alert.setContentText("请输入原密码");
                else alert.setContentText("请输入新密码");
                alert.showAndWait();
            } else {
                alert.setHeaderText("修改失败");
                if (e instanceof PasswordFormatException) alert.setContentText("原" + e);
                else alert.setContentText(e.toString());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private JFXButton stu_ch_canc;

    @FXML
    void canc(ActionEvent event) {
        formstage.close();
        loginController.setEnable();
    }

    @FXML
    void canc_ch(ActionEvent event) {
        formstage.close();
    }

    public void setFormstage(Stage stage) {
        this.formstage = stage;
    }
}