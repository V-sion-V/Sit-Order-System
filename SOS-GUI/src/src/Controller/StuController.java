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
import src.Model.Student;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class StuController {
    private MainUI mainui;

    public void setMainui(MainUI mainui) {
        this.mainui = mainui;
    }

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    private BorderPane MainLayout;

    public void setMainLayout(BorderPane mainLayout) {
        MainLayout = mainLayout;
    }

    @FXML
    private JFXButton info;

    @FXML
    void handle_info(ActionEvent event) {
        Stage s = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/stu_info.fxml"));

            AnchorPane MainLayout = loader.load();
            InfoController controller = loader.getController();
            controller.setFormstage(s);
            controller.setStudent(student);
            controller.s_init();

            Scene scene = new Scene(MainLayout);
            s.setScene(scene);
            s.setTitle("学生信息");
            mainui.SetupStage(s);
            s.showAndWait();
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
            loader.setLocation(MainUI.class.getResource("View/stu_chpwd.fxml"));
            AnchorPane MainLayout = loader.load();

            FormController controller = loader.getController();
            controller.setFormstage(s);
            controller.setMainui(this.mainui);
            controller.setStudent(student);

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
            // ... user chose OK
            mainui.getPrimaryStage().close();
            mainui.Login();
            student.logout();
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }
    }

    @FXML
    private JFXButton signin;

    @FXML
    void handle_signin(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        try {
            student.arrive();
        } catch (Exception e) {
            alert.setHeaderText(null);
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private JFXButton signout;

    @FXML
    void handle_signout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        try {
            student.leave();
        } catch (Exception e) {
            alert.setHeaderText(null);
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }


    @FXML
    private JFXButton reserve;

    @FXML
    void handle_reserve(ActionEvent event) {
        Reserve(this.MainLayout);
    }

    @FXML
    private JFXButton currentOrder;

    @FXML
    void handle_currentOrder(ActionEvent event) {
        Stage s = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/CurrentOrder.fxml"));

            AnchorPane MainLayout = loader.load();
            CurrentOrderController controller = loader.getController();
            controller.setFormstage(s);
            controller.setStudent(student);
            controller.init();

            Scene scene = new Scene(MainLayout);
            s.setScene(scene);
            s.setTitle("当前预订信息");
            mainui.SetupStage(s);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private JFXButton historyOrder;

    @FXML
    void handle_historyOrder(ActionEvent event) {
        showHistoryOrder(student, MainLayout);
    }

    public void Reserve(BorderPane MainLayout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/Room_stu.fxml"));
            AnchorPane Listview = loader.load();

            RoomsController controller = loader.getController();
            controller.setMainLayout(MainLayout);
            controller.setStudent(student);
            controller.init_stu();

            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHistoryOrder(Student student, BorderPane MainLayout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/stu_historyOrder.fxml"));
            AnchorPane Listview = loader.load();

            OrderListController controller = loader.getController();
            controller.setMainLayout(MainLayout);
            controller.setStudent(student);
            controller.init_stu();

            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
