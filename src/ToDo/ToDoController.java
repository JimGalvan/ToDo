package ToDo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ToDoController {

    private DataManager dataManager;
    private ObservableList<ToDoTask> observableList;

    @FXML
    private Button cancelButton;
    @FXML
    private Button saveTaskButton;
    @FXML
    private Button addButton;
    @FXML
    private GridPane addTaskPanel;
    @FXML
    private VBox gridPane;
    @FXML
    private TableView<ToDoTask> tableView;
    @FXML
    private TableColumn<ToDoTask, String> taskColumn;
    @FXML
    private TableColumn<ToDoTask, String> timeColumn;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button deleteButton;


    public void initialize() {
        addTaskPanel.setVisible(false);
        dataManager = new DataManager();
        ArrayList<ToDoTask> taskArrayList = new ArrayList<>();
        observableList = FXCollections.observableArrayList();

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("taskTime"));

        dataManager.loadData(taskArrayList);
        observableList.setAll(taskArrayList);
        tableView.setItems(observableList);
    }


    @FXML
    void addTask(ActionEvent event) {
        addTaskPanel.setVisible(true);
    }

    @FXML
    void saveTask(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        String name = nameTextField.getText();

        if (isInvalidText(name)) {
            alert.setContentText("Task name can not be empty.");
            alert.showAndWait();

        } else if (isTaskInTheList(nameTextField.getText())) {
            alert.setContentText("Task name is already is already taken. Please select another name.");
            alert.showAndWait();

        } else {
            String userInput = nameTextField.getText();
            addTaskToTable(new ToDoTask(userInput, null));
            addTaskPanel.setVisible(false);
        }
    }

    private boolean isInvalidText(String name) {
        Pattern isEmptyText = Pattern.compile("\\s+");
        return (name.isEmpty()) || (isEmptyText.matcher(name).matches());
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
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a task to delete");
            alert.showAndWait();
        }
    }

    private boolean isTaskInTheList(String newTaskName) {
        for (ToDoTask value : dataManager.getTasks()) {
            if (value.toString().equals(newTaskName)) return true;
        }
        return false;
    }

    @FXML
    void cancelNewTask(ActionEvent event) {
        addTaskPanel.setVisible(false);
    }
}
