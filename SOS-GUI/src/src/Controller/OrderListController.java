package src.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.MainUI;
import src.Model.Order;
import src.Model.Sit;
import src.Model.Student;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;

/**
 * 预订列表界面控制类
 */
public class OrderListController {
    /**
     * 窗口主窗体
     */
    private BorderPane MainLayout;

    public void setMainLayout(BorderPane mainLayout) {
        MainLayout = mainLayout;
    }

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * 上一场景
     */
    private AnchorPane backView;

    public void setBackView(AnchorPane backView) {
        this.backView = backView;
    }

    private Sit sit;

    public void setSit(Sit sit) {
        this.sit = sit;
    }

    /**
     * 列表显示窗格
     */
    @FXML
    private TableView<Order> historyorder;

    /**
     * 显示窗格的内容列表
     */
    private final ObservableList<Order> orderObservableList = FXCollections.observableArrayList();

    //显示窗格的字段
    @FXML
    private TableColumn<Order, String> index;

    @FXML
    private TableColumn<Order, Integer> cid;

    @FXML
    private TableColumn<Order, Integer> sid;

    @FXML
    private TableColumn<Order, GregorianCalendar> start;

    @FXML
    private TableColumn<Order, GregorianCalendar> end;

    @FXML
    private TableColumn<Order, String> status;

    @FXML
    private TableColumn<Order, Integer> id;

    @FXML
    private TableColumn<Order, String> name;

    @FXML
    private JFXButton back;

    @FXML
    private Label top;

    @FXML
    void retBackview(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("是否" + back.getText() + "?");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            MainLayout.setCenter(backView);
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }
    }

    /**
     * 初始化 管理员查看学生历史预订列表界面
     */
    public void init_ad_stulist() {
        initindex();
        initcid();
        initsid();
        initstart();
        initend();
        initstatus();

        historyorder.setEditable(true);
        historyorder.setItems(orderObservableList);
        historyorder.getItems().addAll(student.getHistoryOrders());
    }

    /**
     * 初始化 管理员查看座位当前预订列表界面
     */
    public void init_sit_current() {
        initsitorderlist();

        historyorder.setEditable(true);
        historyorder.setItems(orderObservableList);
        historyorder.getItems().addAll(sit.getServingOrders());
    }

    /**
     * 初始化 管理员查看座位历史预订列表界面
     */
    public void init_sit_history() {
        initsitorderlist();
        initstatus();

        historyorder.setEditable(true);
        historyorder.setItems(orderObservableList);
        historyorder.getItems().addAll(sit.getHistoryOrders());
    }

    /**
     * 初始化 学生查看学生历史预订列表界面
     */
    public void init_stu() {
        init_ad_stulist();
        back.setDisable(true);
        back.setVisible(false);
    }

    //初始化字段
    private void initsid() {
        sid.setCellFactory((col) -> new TableCell<Order, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    this.setText(String.valueOf(this.getTableView().getItems().get(this.getIndex()).getTarget().getSid()));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private void initcid() {
        cid.setCellFactory((col) -> new TableCell<Order, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    this.setText(String.valueOf(this.getTableView().getItems().get(this.getIndex()).getTarget().getClassroom().getCid()));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private void initstatus() {
        status.setCellFactory((col) -> new TableCell<Order, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    this.setText(this.getTableView().getItems().get(this.getIndex()).getStatus());
                    this.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private void initend() {
        end.setCellFactory((col) -> new TableCell<Order, GregorianCalendar>() {
            @Override
            public void updateItem(GregorianCalendar item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    GregorianCalendar time = this.getTableView().getItems().get(this.getIndex()).getEndTime();
                    this.setText(time.get(Calendar.MONTH) + 1
                            + "/" + time.get(Calendar.DAY_OF_MONTH)
                            + "/" + time.get(Calendar.HOUR_OF_DAY)
                            + ":00");
                    this.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private void initstart() {
        start.setCellFactory((col) -> new TableCell<Order, GregorianCalendar>() {
            @Override
            public void updateItem(GregorianCalendar item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    GregorianCalendar time = this.getTableView().getItems().get(this.getIndex()).getStartTime();
                    this.setText(time.get(Calendar.MONTH) + 1
                            + "/" + time.get(Calendar.DAY_OF_MONTH)
                            + "/" + time.get(Calendar.HOUR_OF_DAY)
                            + ":00");
                    this.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private void initindex() {
        index.setCellFactory((col) -> new TableCell<Order, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int rowIndex = this.getIndex() + 1;
                    this.setText(String.valueOf(rowIndex));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });
    }

    //初始化座位预订列表部分字段
    private void initsitorderlist() {
        top.setText("座位号：" + (sit.getSid() + 1));
        initindex();

        id.setCellFactory((col) -> new TableCell<Order, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    this.setText(String.valueOf(this.getTableView().getItems().get(this.getIndex()).getOwner().getId()));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        name.setCellFactory((col) -> new TableCell<Order, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    this.setText(String.valueOf(this.getTableView().getItems().get(this.getIndex()).getOwner().getName()));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        initstart();

        initend();
    }
}