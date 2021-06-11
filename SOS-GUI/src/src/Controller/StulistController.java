package src.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.MainUI;
import src.Model.Admin;
import src.Model.Student;
import src.Model.StudentList;
import src.MyExceptions.PasswordFormatException;
import src.MyExceptions.StudentIdExistException;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class StulistController {
    private MainUI mainui;

    public void setMainui(MainUI mainui) {
        this.mainui = mainui;
    }

    private BorderPane MainLayout;

    public void setMainLayout(BorderPane mainLayout) {
        MainLayout = mainLayout;
    }

    @FXML
    private TableView<Student> studentTableView;
    private final ObservableList<Student> studentObservableList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Student, String> index;

    @FXML
    private TableColumn<Student, Integer> id;

    @FXML
    private TableColumn<Student, String> name;

    @FXML
    private TableColumn<Student, String> pwd;

    @FXML
    private TableColumn<Student, Integer> credit;

    @FXML
    private TableColumn<Student, String> getHistoryOrder;

    @FXML
    private TableColumn<Student, String> getCurrentOrder;

    @FXML
    private TableColumn<Student, String> del;

    @FXML
    private TextField input_id;

    @FXML
    private TextField input_name;

    @FXML
    private JFXButton addStudent;

    @FXML
    void addStudent(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
        alert.setTitle("提示");
        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

        try {
            if (input_id.getText() == null || input_id.getText().length() == 0) {
                alert.setHeaderText("查看失败");
                alert.setContentText("请输入学号");
                alert.showAndWait();
            } else if (input_name.getText() == null || input_name.getText().length() == 0) {
                alert.setHeaderText("查看失败");
                alert.setContentText("请输入学生姓名");
                alert.showAndWait();
            } else {
                Admin.getInstance().addStudent(input_id.getText(), "00000000", input_name.getText());
                studentTableView.getItems().clear();
                studentTableView.getItems().setAll(StudentList.getInstance().getStudents());
                input_id.clear();
                input_name.clear();
            }
        } catch (StudentIdExistException | PasswordFormatException e) {
            alert.setHeaderText("添加失败");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }

    }

    public void init() {
        index.setCellFactory((col) -> new TableCell<Student, String>() {
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
        id.setCellFactory((col) -> new TableCell<Student, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int id1 = this.getTableView().getItems().get(this.getIndex()).getId();
                    this.setText(String.valueOf(id1));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        name.setCellFactory((col) -> new TableCell<Student, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    String name1 = this.getTableView().getItems().get(this.getIndex()).getName();
                    this.setText(name1);
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        pwd.setCellFactory((col) -> new TableCell<Student, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    String pwd1 = this.getTableView().getItems().get(this.getIndex()).getPwd();
                    this.setText(pwd1);
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        credit.setCellFactory((col) -> new TableCell<Student, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    int credit1 = this.getTableView().getItems().get(this.getIndex()).getCredit();
                    this.setText(String.valueOf(credit1));
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        getHistoryOrder.setCellFactory((col) -> new TableCell<Student, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    Button delBtn = new Button("查看历史预订");
                    this.setGraphic(delBtn);
                    this.setAlignment(Pos.CENTER);
                    delBtn.setOnMouseClicked((me) -> {
                        if (!this.getTableView().getItems().get(this.getIndex()).getHistoryOrders().isEmpty())
                            showHistoryOrder(this.getTableView().getItems().get(this.getIndex()), MainLayout);
                        else {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                            alert.setTitle("提示");
                            dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
                            alert.setHeaderText(null);
                            alert.setContentText("此学生不存在历史预订信息");
                            alert.showAndWait();
                        }
                    });
                }
            }

        });

        getCurrentOrder.setCellFactory((col) -> new TableCell<Student, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    Button delBtn = new Button("查看当前预订");
                    this.setGraphic(delBtn);
                    this.setAlignment(Pos.CENTER);
                    delBtn.setOnMouseClicked((me) -> {
                        if (this.getTableView().getItems().get(this.getIndex()).getServingOrder() != null) {
                            Stage s = new Stage();
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(MainUI.class.getResource("View/CurrentOrder.fxml"));

                                AnchorPane MainLayout = loader.load();
                                CurrentOrderController controller = loader.getController();
                                controller.setFormstage(s);
                                controller.setStudent(this.getTableView().getItems().get(this.getIndex()));
                                controller.init();

                                Scene scene = new Scene(MainLayout);
                                s.setScene(scene);
                                s.setTitle("当前预订信息");
                                mainui.SetupStage(s);
                                s.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                            alert.setTitle("提示");
                            dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));
                            alert.setHeaderText(null);
                            alert.setContentText("此学生当前不存在预订");
                            alert.showAndWait();
                        }
                    });
                }
            }

        });

        del.setCellFactory((col) -> new TableCell<Student, String>() {
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
                        alert.setContentText("是否确定删除此学生?");
                        Stage dialog = ((Stage) alert.getDialogPane().getScene().getWindow());
                        dialog.getIcons().add(new Image(Objects.requireNonNull(MainUI.class.getResourceAsStream("View/img/bh.jpg"))));

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            // ... user chose OK
                            StudentList.getInstance().getStudents().remove(this.getTableView().getItems().get(this.getIndex()));
                            this.getTableView().getItems().remove(this.getIndex());
                        } else {
                            // ... user chose CANCEL or closed the dialog
                            alert.close();
                        }
                    });
                }
            }

        });

        studentTableView.setEditable(true);
        studentTableView.setItems(studentObservableList);
        studentTableView.getItems().setAll(StudentList.getInstance().getStudents());
    }

    public void showHistoryOrder(Student student, BorderPane MainLayout) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainUI.class.getResource("View/stu_historyOrder.fxml"));
            AnchorPane Listview = loader.load(), Oldview = (AnchorPane) MainLayout.getCenter();

            OrderListController controller = loader.getController();
            controller.setMainLayout(MainLayout);
            controller.setStudent(student);
            controller.setBackView(Oldview);
            controller.init_ad_stulist();

            MainLayout.setCenter(Listview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}