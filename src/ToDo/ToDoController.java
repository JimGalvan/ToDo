package ToDo;

import com.jfoenix.controls.JFXTimePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class ToDoController {

    DataManager dataManager;

    ObservableList<ToDoTask> observableList;

    ArrayList<ToDoTask> taskArrayList;

    @FXML
    private JFXTimePicker timer;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField taskBox;

    @FXML
    private TableView<ToDoTask> tableView;

    @FXML
    private TableColumn<ToDoTask, String> taskColumn;

    @FXML
    private TableColumn<ToDoTask, String> timeColumn;

    public void initialize() {

        dataManager = new DataManager();
        taskArrayList = new ArrayList<>();

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<ToDoTask, String>("taskName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<ToDoTask, String>("taskTime"));

        observableList = FXCollections.observableArrayList();

        dataManager.loadData(taskArrayList);
        observableList.setAll(taskArrayList);
        tableView.setItems(observableList);
    }

    @FXML
    void addTask(ActionEvent event) {
        String timeValue = (timer.getValue() != null ? timer.getValue().toString() : "");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        if (isTaskInTheList(taskBox.getText())) {
            alert.setContentText("Task is already in list. Please enter a different name.");
            alert.showAndWait();
        } else if (timeValue.equals("")) {
            alert.setContentText("Please select a time for the task before adding it.");
            alert.showAndWait();
        } else {
            String userInput = taskBox.getText();
            String time = timer.getValue().toString();
            addTaskToTable(new ToDoTask(userInput, time));
        }
    }

    private void addTaskToTable(ToDoTask task) {
        dataManager.saveTask(task);
        observableList.add(task);
        tableView.setItems(observableList);
    }

    @FXML
    void removeTask(ActionEvent event) {
        int selectedTaskIndex = tableView.getSelectionModel().getSelectedIndex();

        //-1 means there is no items in tableView
        if (selectedTaskIndex != -1) {
            dataManager.removeTask(selectedTaskIndex);
            observableList.remove(selectedTaskIndex);
        } else System.out.println("Select a task");
    }

    private boolean isTaskInTheList(String newTaskName) {
        for (ToDoTask value : dataManager.getList())
            if (value.toString().equals(newTaskName)) return true;
        return false;
    }
}
