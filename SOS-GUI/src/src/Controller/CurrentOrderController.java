package src.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import src.MainUI;
import src.Model.Student;
import src.MyExceptions.CanNotCancelOrderException;
import src.MyExceptions.HoldingOrderException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * 学生当前预订显示窗口控制类
 */
public class CurrentOrderController {
    /**
     * 当前窗口
     */
    private Stage infostage;

    public void setFormstage(Stage stage) {
        this.infostage = stage;
    }

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    @FXML
    private Label cid;

    @FXML
    private Label sid;

    @FXML
    private Label start;

    @FXML
    private Label end;

    @FXML
    private JFXButton conf;

    @FXML
    void conf(ActionEvent event) {
        infostage.close();
    }

    @FXML
    private JFXButton canc;

    @FXML
    void canc(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        try {
            student.cancelOrder();
            alert.setHeaderText(null);
            alert.setContentText("取消成功");
            alert.showAndWait();
            init();
        } catch (CanNotCancelOrderException | HoldingOrderException e) {
            alert.setHeaderText("取消失败");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    @FXML
    void conf_enter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            infostage.close();
        }
    }

    /**
     * 表单初始化
     */
    public void init() {
        if (student.getServingOrder() != null) {
            cid.setText(String.valueOf(student.getServingOrder().getTarget().getClassroom().getCid()));
            sid.setText(String.valueOf(student.getServingOrder().getTarget().getSid()));
            GregorianCalendar starttime = student.getServingOrder().getStartTime(), endtime = student.getServingOrder().getEndTime();
            start.setText((starttime.get(Calendar.MONTH) + 1)
                    + "/" + starttime.get(Calendar.DAY_OF_MONTH)
                    + "/" + starttime.get(Calendar.HOUR_OF_DAY)
                    + ":00");
            end.setText(endtime.get(Calendar.MONTH) + 1
                    + "/" + endtime.get(Calendar.DAY_OF_MONTH)
                    + "/" + endtime.get(Calendar.HOUR_OF_DAY)
                    + ":00");
        } else {
            cid.setText(null);
            sid.setText(null);
            start.setText(null);
            end.setText(null);
        }
    }
}
