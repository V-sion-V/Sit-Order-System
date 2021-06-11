package src.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.MainUI;
import src.Model.*;
import src.MyExceptions.HoldingOrderException;
import src.MyExceptions.OutOfBoundException;
import src.MyExceptions.TimeIllegalException;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;

/**
 * 座位列表界面控制类
 */
public class SitsController {
    /**
     * 预订座位选择的时间
     */
    private GregorianCalendar starttime, endtime;

    public void setStarttime(GregorianCalendar starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(GregorianCalendar endtime) {
        this.endtime = endtime;
    }

    /**
     * 座位所在教室
     */
    private Classroom classroom;

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    /**
     * 当前界面主窗体
     */
    private BorderPane MainLayout;

    public void setMainLayout(BorderPane mainLayout) {
        MainLayout = mainLayout;
    }

    /**
     * 上级教室界面
     */
    private AnchorPane RoomsView;

    public void setRoomsView(AnchorPane roomsView) {
        RoomsView = roomsView;
    }

    /**
     * 预订的学生
     */
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    @FXML
    private Label top;

    /**
     * 列表显示窗格
     */
    @FXML
    private TableView<ObservableList<Sit>> sitTable;

    /**
     * 显示窗格的内容列表
     */
    private final ObservableList<ObservableList<Sit>> sitlist = FXCollections.observableArrayList();

    @FXML
    private TextField input_row;

    @FXML
    private TextField input_col;

    @FXML
    private JFXButton currentOrder;

    @FXML
    void seeCurrentOrder(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        alert.setTitle("提示");
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        Sit sit;
        try {
            if (input_row.getText() == null || input_row.getText().length() == 0) {
                alert.setHeaderText("查看失败");
                alert.setContentText("请输入行号");
                alert.showAndWait();
            } else if (input_col.getText() == null || input_col.getText().length() == 0) {
                alert.setHeaderText("查看失败");
                alert.setContentText("请输入列号");
                alert.showAndWait();
            } else {
                sit = Admin.getInstance().getSit(this.classroom, input_row.getText(), input_col.getText());
                showCurrentOrder(sit, MainLayout);
            }
        } catch (OutOfBoundException e) {
            alert.setHeaderText("查看失败");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private JFXButton historyOrder;

    @FXML
    void seeHistoryOrder(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        alert.setTitle("提示");
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        Sit sit;
        try {
            if (input_row.getText() == null || input_row.getText().length() == 0) {
                alert.setHeaderText("查看失败");
                alert.setContentText("请输入行号");
                alert.showAndWait();
            } else if (input_col.getText() == null || input_col.getText().length() == 0) {
                alert.setHeaderText("查看失败");
                alert.setContentText("请输入列号");
                alert.showAndWait();
            } else {
                sit = Admin.getInstance().getSit(this.classroom, input_row.getText(), input_col.getText());
                showHistoryOrder(sit, MainLayout);
            }
        } catch (OutOfBoundException e) {
            alert.setHeaderText("查看失败");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    @FXML
    private JFXButton back;

    @FXML
    void retRoom(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText("是否返回教室列表?");
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            MainLayout.setCenter(RoomsView);
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }
    }

    public void init() {
        setupSitTable();

        TableColumn<ObservableList<Sit>, String> index = new TableColumn<>();
        index.setPrefWidth(80);
        index.setCellFactory((col) -> new TableCell<ObservableList<Sit>, String>() {
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
        sitTable.getColumns().add(index);
        //sitTable表格生成列
        for (int i = 0; i < classroom.getColCount(); i++) {
            TableColumn<ObservableList<Sit>, Boolean> column = new TableColumn<>(String.valueOf(i + 1));
            column.setPrefWidth(80);
            int finalI = i;
            column.setCellFactory((col) -> new TableCell<ObservableList<Sit>, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        boolean ischecked = this.getTableView().getItems().get(this.getIndex()).get(finalI).isAvl();
                        JFXCheckBox checkBox = new JFXCheckBox();
                        Sit sit = this.getTableView().getItems().get(this.getIndex()).get(finalI);
                        this.setGraphic(checkBox);
                        this.setAlignment(Pos.CENTER);
                        checkBox.setSelected(ischecked);
                        checkBox.setText((sit.getOnSit() == null) ? "无人" : "有人");
                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {

                            if (newVal) {
                                sit.enable();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("提示");
                                alert.setHeaderText(null);
                                alert.setContentText("是否确定禁用此座位?");
                                Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                                dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK) {
                                    // ... user chose OK
                                    sit.disable();
                                    checkBox.setText((sit.getOnSit() == null) ? "无人" : "有人");
                                } else {
                                    // ... user chose CANCEL or closed the dialog
                                    alert.close();
                                    checkBox.setSelected(true);
                                }
                            }
                        });
                    }
                }

            });
            sitTable.getColumns().add(column);
        }
    }

    public void init_reserve() {
        setupSitTable();

        TableColumn<ObservableList<Sit>, String> index = new TableColumn<>();
        index.setPrefWidth(80);
        index.setCellFactory((col) -> new TableCell<ObservableList<Sit>, String>() {
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
        sitTable.getColumns().add(index);
        //sitTable表格生成列
        for (int i = 0; i < classroom.getColCount(); i++) {
            TableColumn<ObservableList<Sit>, Boolean> column = new TableColumn<>(String.valueOf(i + 1));
            column.setPrefWidth(80);
            int finalI = i;
            column.setCellFactory((col) -> new TableCell<ObservableList<Sit>, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        Sit sit = this.getTableView().getItems().get(this.getIndex()).get(finalI);
                        JFXCheckBox checkBox = new JFXCheckBox();
                        this.setGraphic(checkBox);
                        this.setAlignment(Pos.CENTER);
                        if (sit.isAvl() && sit.checkSitInTimeRange(starttime,endtime)) {
                            checkBox.setSelected(false);
                        } else {
                            checkBox.setSelected(true);
                            checkBox.setDisable(true);
                        }

                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                            if (newVal) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("提示");
                                alert.setHeaderText(null);
                                alert.setContentText("是否预约此座位?");
                                Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                                dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK) {
                                    // ... user chose OK
                                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                    alert2.setTitle("提示");
                                    Stage dialog2 = ((Stage) alert2.getDialogPane().getScene().getWindow());
                                    dialog2.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
                                    try {
                                        student.generateOrder(sit, starttime, endtime);
                                        MainLayout.setCenter(RoomsView);
                                        alert2.setHeaderText(null);
                                        alert2.setContentText("预约成功");
                                        alert2.showAndWait();
                                    } catch (TimeIllegalException | HoldingOrderException e) {
                                        alert2.setHeaderText("预约失败");
                                        alert2.setContentText(e.toString());
                                        alert2.showAndWait();
                                        checkBox.setSelected(false);
                                    }
                                } else {
                                    // ... user chose CANCEL or closed the dialog
                                    alert.close();
                                    checkBox.setSelected(false);
                                }
                            }
                        });
                    }
                }

            });
            sitTable.getColumns().add(column);
        }
    }

    public void showCurrentOrder(Sit sit, BorderPane MainLayout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/sit_currentorder.fxml"));
            AnchorPane Listview = loader.load(), Oldview = (AnchorPane) MainLayout.getCenter();

            OrderListController controller = loader.getController();
            controller.setMainLayout(MainLayout);
            controller.setSit(sit);
            controller.setBackView(Oldview);
            controller.init_sit_current();

            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSitTable() {
        top.setText("教室：" + classroom.getName());
        //获取sit列表按行加入到sitlist中，
        for (int i = 0; i < classroom.getRowCount(); i++) {
            ObservableList<Sit> row = FXCollections.observableArrayList();
            for (int j = 0; j < classroom.getColCount(); j++) {
                try {
                    row.add(classroom.getSit(i, j));
                } catch (OutOfBoundException ignored) {
                }
            }
            sitlist.add(row);
        }
        sitTable.setItems(sitlist);
    }

    public void showHistoryOrder(Sit sit, BorderPane MainLayout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/sit_historyorder.fxml"));
            AnchorPane Listview = loader.load(), Oldview = (AnchorPane) MainLayout.getCenter();

            OrderListController controller = loader.getController();
            controller.setMainLayout(MainLayout);
            controller.setSit(sit);
            controller.setBackView(Oldview);
            controller.init_sit_history();

            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}