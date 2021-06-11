package src;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.Model.ClassroomList;
import src.Model.Student;
import src.Model.StudentList;
import src.MyExceptions.HoldingOrderException;
import src.Controller.AdminController;
import src.Controller.LoginController;
import src.Controller.StuController;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


/**
 * Javafx程序运行的主类.
 */
public class MainUI extends Application {
    /**
     * 程序主窗口.
     */
    private Stage primaryStage;

    /**
     * @inheritDoc
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        //设置窗口监听，退出时保存信息.
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                ClassroomList.getInstance().saveToFile(new File("savedClassrooms.txt"));
                StudentList.getInstance().saveToFile(new File("savedStudents.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
        });
        Login();
    }

    public static void main(String[] args) {
        //从文件读取信息.
        try {
            ClassroomList.getInstance().readFromFile(new File("savedClassrooms.txt"));
            StudentList.getInstance().readFromFile(new File("savedStudents.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    /**
     * 进入登录界面.
     */
    public void Login() {
        try {
            // 加载 fxml文件.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/login.fxml"));
            BorderPane mainLayout = loader.load();

            // 设置对应控制器.
            LoginController controller = loader.getController();
            controller.setMainui(this);


            // 展示窗口.
            Scene scene = new Scene(mainLayout);
            primaryStage.setTitle("学生自习管理系统");
            primaryStage.setScene(scene);
            SetupStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入管理员界面
     */
    public void AdminMain() {
        try {
            // 加载 fxml文件.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/admin.fxml"));
            BorderPane MainLayout = loader.load();

            // 设置对应控制器.
            AdminController controller = loader.getController();
            controller.setMainui(this);
            controller.setMainLayout(MainLayout);

            // 展示窗口.
            Scene scene = new Scene(MainLayout);
            primaryStage.setTitle("管理界面");
            primaryStage.setScene(scene);
            SetupStage(primaryStage);
            primaryStage.show();

            //默认显示管理教室界面.
            controller.showRoomsOverview(MainLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入学生界面
     * @param student 登录的学生
     */
    public void StuMain(Student student) {
        try {
            // 加载 fxml文件.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/stu.fxml"));
            BorderPane MainLayout = loader.load();

            // 设置对应控制器.
            StuController controller =loader.getController();
            controller.setMainui(this);
            controller.setStudent(student);
            controller.setMainLayout(MainLayout);

            // 展示窗口.
            Scene scene = new Scene(MainLayout);
            primaryStage.setTitle("个人界面");
            primaryStage.setScene(scene);
            SetupStage(primaryStage);
            primaryStage.show();

            //默认显示预约自习界面.
            controller.Reserve(MainLayout);

            //设置定时器，每分钟更新学生当前预订状态
            Timeline checktimer = new Timeline(
                    new KeyFrame(Duration.seconds(60),
                            event -> check(student)));
            checktimer.setCycleCount(Timeline.INDEFINITE);
            checktimer.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    /**
     * 设置窗体图标以及不可放大
     * @param stage 设置的窗体
     */
    public void SetupStage(Stage stage) {
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
    }

    /**
     * 更新学生当前预订状态
     * @param student
     */
    public void check(Student student){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Stage dialog=((Stage) alert.getDialogPane().getScene().getWindow());
        alert.setTitle("提示");
        alert.setHeaderText(null);
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        try {
            student.checkOrder();
        }catch (HoldingOrderException ignored) { }
         catch (Exception e){
            alert.setContentText(e.toString());
            alert.show();
        }
    }
}
