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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.MainUI;
import src.Model.Classroom;
import src.Model.ClassroomList;
import src.Model.Student;
import src.MyExceptions.ClassroomExistException;
import src.MyExceptions.TimeIllegalException;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;

/**
 * 教室列表界面控制器
 */
public class RoomsController {

    private BorderPane MainLayout;

    public void setMainLayout(BorderPane mainLayout) {
        MainLayout = mainLayout;
    }

    /**
     * 预订座位的学生
     */
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * 列表显示窗格
     */
    @FXML
    private TableView<Classroom> roomtable;

    /**
     * 显示窗格的内容列表
     */
    private final ObservableList<Classroom> classrooms = FXCollections.observableArrayList();

    //显示窗格的字段
    @FXML
    private TableColumn<Classroom, Integer> cid;

    @FXML
    private TableColumn<Classroom, String> name;

    @FXML
    private TableColumn<Classroom, Integer> row;

    @FXML
    private TableColumn<Classroom, Integer> col;

    @FXML
    private TableColumn<Classroom, Boolean> avl;

    @FXML
    private TableColumn<Classroom, String> getsit;

    @FXML
    private TableColumn<Classroom, String> del;

    //管理员添加教室输入信息控件
    @FXML
    private TextField input_id;

    @FXML
    private TextField input_name;

    @FXML
    private TextField input_row;

    @FXML
    private TextField input_col;

    @FXML
    private JFXButton addRoom;

    @FXML
    void addClassroom(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        alert.setTitle("提示");
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
        int id, row, col;
        String name = input_name.getText();

        try {
            try {
                id = Integer.parseInt(input_id.getText());
                try {
                    if (name == null || name.length() == 0) {
                        alert.setHeaderText("添加失败");
                        alert.setContentText("请输入教室名");
                        alert.showAndWait();
                    } else {
                        row = Integer.parseInt(input_row.getText());
                        try {
                            col = Integer.parseInt(input_col.getText());
                            ClassroomList.getInstance().addClassroom(id, name, row, col);
                            roomtable.getItems().clear();
                            roomtable.getItems().setAll(ClassroomList.getInstance().getClassrooms());
                            alert.setHeaderText(null);
                            alert.setContentText("添加成功");
                            alert.showAndWait();
                        } catch (NumberFormatException e) {
                            if (input_col.getText() == null || input_col.getText().length() == 0) {
                                alert.setHeaderText("添加失败");
                                alert.setContentText("请输入列号");
                                alert.showAndWait();
                            } else {
                                alert.setHeaderText("添加失败");
                                alert.setContentText("列号不全为数字");
                                alert.showAndWait();
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    if (input_row.getText() == null || input_row.getText().length() == 0) {
                        alert.setHeaderText("添加失败");
                        alert.setContentText("请输入行号");
                        alert.showAndWait();
                    } else {
                        alert.setHeaderText("添加失败");
                        alert.setContentText("行号不全为数字");
                        alert.showAndWait();
                    }
                }
            } catch (NumberFormatException e) {
                if (input_id.getText() == null || input_id.getText().length() == 0) {
                    alert.setHeaderText("添加失败");
                    alert.setContentText("请输入教室号");
                    alert.showAndWait();
                } else {
                    alert.setHeaderText("添加失败");
                    alert.setContentText("教室号不全为数字");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            alert.setHeaderText("添加失败");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }

    }


    /**
     * 管理员管理教室界面
     */
    public void init() {
        cid.setCellFactory((col) -> new TableCell<Classroom, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int cid1 = this.getTableView().getItems().get(this.getIndex()).getCid();
                    this.setText(String.valueOf(cid1));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        name.setCellFactory(col -> {
            TableCell<Classroom, String> cell = TextFieldTableCell.<Classroom>forTableColumn().call(col);
            cell.setAlignment(Pos.CENTER);
            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow<Classroom> row = cell.getTableRow();
                if (row == null) {
                    cell.setEditable(false);
                } else {
                    Classroom item = (Classroom) cell.getTableRow().getItem();
                    cell.setEditable(item != null);
                }
            });
            return cell;
        });
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setOnEditCommit((col) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
            dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
            Classroom classroom = col.getTableView().getItems().get(col.getTablePosition().getRow());
            try {
                classroom.changeName(col.getNewValue());
                alert.setHeaderText(null);
                alert.setContentText("修改成功");
                alert.showAndWait();
            } catch (ClassroomExistException e) {
                alert.setHeaderText("修改失败");
                alert.setContentText(e.toString());
                alert.showAndWait();
            }
        });


        row.setCellFactory((col) -> new TableCell<Classroom, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int rowCount = this.getTableView().getItems().get(this.getIndex()).getRowCount();
                    this.setText(String.valueOf(rowCount));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        col.setCellFactory((col) -> new TableCell<Classroom, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int colCount = this.getTableView().getItems().get(this.getIndex()).getColCount();
                    this.setText(String.valueOf(colCount));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        avl.setCellFactory((col) -> new TableCell<Classroom, Boolean>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    boolean ischecked = this.getTableView().getItems().get(this.getIndex()).isAvl();
                    JFXCheckBox checkBox = new JFXCheckBox();
                    this.setGraphic(checkBox);
                    this.setAlignment(Pos.CENTER);
                    checkBox.setSelected(ischecked);
                    checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                        if (newVal) {
                            this.getTableView().getItems().get(this.getIndex()).enable();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("提示");
                            alert.setHeaderText(null);
                            alert.setContentText("是否确定禁用此教室?");
                            Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                            dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                // ... user chose OK
                                this.getTableView().getItems().get(this.getIndex()).disable();
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

        getsit.setCellFactory((col) -> new TableCell<Classroom, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    Button delBtn = new Button("查看座位");
                    this.setGraphic(delBtn);
                    this.setAlignment(Pos.CENTER);
                    delBtn.setOnMouseClicked((me) -> showSitlistOverview(this.getTableView().getItems().get(this.getIndex())));
                }
            }

        });

        del.setCellFactory((col) -> new TableCell<Classroom, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    Button delBtn = new Button("删除");
                    this.setGraphic(delBtn);
                    this.setAlignment(Pos.CENTER);
                    delBtn.setOnMouseClicked((me) -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText(null);
                        alert.setContentText("是否确定删除此教室?");
                        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            // ... user chose OK
                            ClassroomList.getInstance().getClassrooms().remove(this.getTableView().getItems().get(this.getIndex()));
                            this.getTableView().getItems().remove(this.getIndex());
                        } else {
                            // ... user chose CANCEL or closed the dialog
                            alert.close();
                        }
                    });
                }
            }

        });

        roomtable.setEditable(true);
        roomtable.setItems(classrooms);
        roomtable.getItems().setAll(ClassroomList.getInstance().getClassrooms());
    }


    //学生预订座位输入信息控件
    @FXML
    private TextField start;

    @FXML
    private TextField end;

    @FXML
    private JFXCheckBox today;

    @FXML
    private JFXCheckBox tomorrow;

    /**
     * 学生预订选择教室界面
     */
    public void init_stu() {
        today.setText(null);
        today.setSelected(Boolean.TRUE);
        today.selectedProperty().addListener((obVal, oldVal, newVal) -> {
            if (newVal) {
                tomorrow.setSelected(Boolean.FALSE);
            }
        });
        tomorrow.setText(null);
        tomorrow.setSelected(Boolean.FALSE);
        tomorrow.selectedProperty().addListener((obVal, oldVal, newVal) -> {
            if (newVal) {
                today.setSelected(Boolean.FALSE);
            }
        });
        cid.setCellFactory((col) -> new TableCell<Classroom, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int cid1 = this.getTableView().getItems().get(this.getIndex()).getCid();
                    this.setText(String.valueOf(cid1));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        name.setCellFactory((col) -> new TableCell<Classroom, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    this.setText(this.getTableView().getItems().get(this.getIndex()).getName());
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        row.setCellFactory((col) -> new TableCell<Classroom, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int rowCount = this.getTableView().getItems().get(this.getIndex()).getRowCount();
                    this.setText(String.valueOf(rowCount));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        col.setCellFactory((col) -> new TableCell<Classroom, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int colCount = this.getTableView().getItems().get(this.getIndex()).getColCount();
                    this.setText(String.valueOf(colCount));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        avl.setCellFactory((col) -> new TableCell<Classroom, Boolean>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    boolean ischecked = this.getTableView().getItems().get(this.getIndex()).isAvl();
                    JFXCheckBox checkBox = new JFXCheckBox();
                    this.setGraphic(checkBox);
                    this.setAlignment(Pos.CENTER);
                    checkBox.setSelected(ischecked);
                    checkBox.setDisable(true);
                }
            }

        });

        getsit.setCellFactory((col) -> new TableCell<Classroom, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    Button delBtn = new Button("查看座位");
                    this.setGraphic(delBtn);
                    this.setAlignment(Pos.CENTER);
                    delBtn.setOnMouseClicked((me) -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("提示");
                        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
                        if (this.getTableView().getItems().get(this.getIndex()).isAvl()) {
                            if (start.getText() == null || start.getText().length() == 0) {
                                alert.setHeaderText("预约失败");
                                alert.setContentText("请输入开始时间");
                                alert.showAndWait();
                            } else if (end.getText() == null || end.getText().length() == 0) {
                                alert.setHeaderText("预约失败");
                                alert.setContentText("请输入结束时间");
                                alert.showAndWait();
                            } else {
                                int date = (today.selectedProperty().get()) ? 0 : 1;
                                try {
                                    GregorianCalendar starttime = student.inputTime(String.valueOf(date), start.getText());
                                    GregorianCalendar endtime = student.inputTime(String.valueOf(date), end.getText());
                                    if (starttime.compareTo(endtime) > 0)
                                        throw new TimeIllegalException("结束时间不能早于开始时间");
                                    reserveSitlist(this.getTableView().getItems().get(this.getIndex()), starttime, endtime);
                                } catch (NumberFormatException e) {
                                    alert.setHeaderText("预约失败");
                                    alert.setContentText("预约时间请输入数字");
                                    alert.showAndWait();
                                } catch (TimeIllegalException e) {
                                    alert.setHeaderText("预约失败");
                                    alert.setContentText(e.toString());
                                    alert.showAndWait();
                                }
                            }
                        } else {
                            alert.setHeaderText("预约失败");
                            alert.setContentText("此教室无法预约");
                            alert.showAndWait();
                        }
                    });
                }
            }

        });

        roomtable.setEditable(true);
        roomtable.setItems(classrooms);
        roomtable.getItems().setAll(ClassroomList.getInstance().getClassrooms());
    }

    /**
     * 管理员查看教室
     * @param classroom 所查看的教室
     */
    public void showSitlistOverview(Classroom classroom) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/Sits.fxml"));

            AnchorPane Listview = loader.load(), Oldview = (AnchorPane) MainLayout.getCenter();
            SitsController controller = loader.getController();
            controller.setClassroom(classroom);
            controller.setMainLayout(MainLayout);
            controller.setRoomsView(Oldview);
            controller.init();
            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 学生预订所在教室座位
     * @param classroom 所预订座位所在教室
     * @param starttime 预订开始时间
     * @param endtime   预订结束时间
     */
    public void reserveSitlist(Classroom classroom, GregorianCalendar starttime, GregorianCalendar endtime) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/Sits_reserve.fxml"));

            AnchorPane Listview = loader.load(), Oldview = (AnchorPane) MainLayout.getCenter();
            SitsController controller = loader.getController();
            controller.setClassroom(classroom);
            controller.setMainLayout(MainLayout);
            controller.setRoomsView(Oldview);
            controller.setStarttime(starttime);
            controller.setEndtime(endtime);
            controller.setStudent(student);
            controller.init_reserve();
            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}