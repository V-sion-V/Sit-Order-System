package src.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.MainUI;

import java.io.IOException;

/**
 * 登录界面控制类
 */
public class LoginController {

    private MainUI mainui;

    @FXML
    private JFXButton stul;

    @FXML
    void UserLogin(ActionEvent event) {
        setDisable();
        Stage s = new Stage();
        s.setOnCloseRequest(event1 -> setEnable());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/stu_login.fxml"));
            AnchorPane MainLayout = loader.load();

            FormController formController = loader.getController();
            formController.setMainui(this.mainui);
            formController.setFormstage(s);
            formController.setLoginController(this);

            Scene scene = new Scene(MainLayout);
            s.setScene(scene);
            s.setTitle("学生登录");
            mainui.SetupStage(s);
            s.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private JFXButton admin;

    @FXML
    void AdminLogin(ActionEvent event) {
        setDisable();
        Stage s = new Stage();
        s.setOnCloseRequest(event1 -> setEnable());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/admin_login.fxml"));
            AnchorPane MainLayout = loader.load();

            FormController formController = loader.getController();
            formController.setMainui(this.mainui);
            formController.setFormstage(s);
            formController.setLoginController(this);


            Scene scene = new Scene(MainLayout);
            s.setScene(scene);
            s.setTitle("管理员登录");
            mainui.SetupStage(s);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainui(MainUI mainui) {
        this.mainui = mainui;
    }

    /**
     * 设置按钮不可用
     */
    public void setDisable() {
        stul.setDisable(true);
        admin.setDisable(true);
    }

    /**
     * 设置按钮可用
     */
    public void setEnable() {
        stul.setDisable(false);
        admin.setDisable(false);
    }
}
