package src.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.MainUI;
import src.Model.Admin;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


/**
 * 管理员界面控制器类.
 */
public class AdminController {

    private MainUI mainui;  public void setMainui(MainUI mainui) {
        this.mainui = mainui;
    }

    /**
     * 当前界面主窗体
     */
    private BorderPane MainLayout;  public void setMainLayout(BorderPane mainLayout) {
        MainLayout = mainLayout;
    }

    // 顶部按钮
    @FXML
    private JFXButton info;

    @FXML
    void handle_info(ActionEvent event) {
        Stage s = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/admin_info.fxml"));
            AnchorPane MainLayout = loader.load();

            InfoController controller = loader.getController();
            controller.setFormstage(s);
            controller.ad_init();

            Scene scene = new Scene(MainLayout);
            s.setScene(scene);
            s.setTitle("管理员信息");
            mainui.SetupStage(s);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private JFXButton chpwd;

    @FXML
    void handle_chpwd(ActionEvent event) {
        Stage s = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/admin_chpwd.fxml"));
            AnchorPane MainLayout = loader.load();

            FormController controller = loader.getController();
            controller.setFormstage(s);
            controller.setMainui(this.mainui);

            Scene scene = new Scene(MainLayout);
            s.setScene(scene);
            s.setTitle("修改密码");
            mainui.SetupStage(s);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private JFXButton logout;

    @FXML
    void handle_logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("是否退出登录?");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //  OK
            mainui.getPrimaryStage().close();
            mainui.Login();
            Admin.getInstance().logout();
        } else {
            //  CANCEL or closed the dialog
            alert.close();
        }
    }

    //侧边按钮
    @FXML
    private JFXButton manage_room;

    @FXML
    void handle_manage_room(ActionEvent event) {
        showRoomsOverview(this.MainLayout);
    }

    @FXML
    private JFXButton manage_stu;

    @FXML
    void handle_manage_stu(ActionEvent event) {
        showStulistOverview(this.MainLayout);
    }

    /**
     * 显示管理教室界面
     * @param MainLayout 当前界面主窗体
     */
    public void showRoomsOverview(BorderPane MainLayout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/Room.fxml"));
            AnchorPane Listview = loader.load();

            RoomsController controller = loader.getController();
            controller.setMainLayout(MainLayout);
            controller.init();

            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示管理学生界面
     * @param MainLayout 当前界面主窗体
     */
    public void showStulistOverview(BorderPane MainLayout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/Stulist.fxml"));
            AnchorPane Listview = loader.load();

            StulistController controller = loader.getController();
            controller.setMainLayout(MainLayout);
            controller.setMainui(mainui);
            controller.init();

            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}