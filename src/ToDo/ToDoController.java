package ToDo;

import com.jfoenix.controls.JFXTimePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class ToDoController {

    DataManager dataManager;
    ObservableList<ToDoTask> observableList;
    ArrayList<ToDoTask> taskArrayList;

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
    private Spinner<Integer> hourSelector;
    @FXML
    private Spinner<Integer> minSelector;

    public void initialize() {
        addTaskPanel.setVisible(false);
        dataManager = new DataManager();
        taskArrayList = new ArrayList<>();
        observableList = FXCollections.observableArrayList();

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("taskTime"));

        dataManager.loadData(taskArrayList);
        observableList.setAll(taskArrayList);
        tableView.setItems(observableList);

        setUpTimer();
    }

    private void setUpTimer() {
        hourSelector.setPromptText("00");
        minSelector.setPromptText("00");
        final int initialValue = 3;

        hourSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 0));
        minSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0));
    }

    @FXML
    void addTask(ActionEvent event) throws IOException {

        addTaskPanel.setVisible(true);

//		String timeValue = (timer.getValue() != null ? timer.getValue().toString() : "");
//
//		if (isTaskInTheList(taskBox.getText())){
//			System.out.println("Task is in the list");
//		}
//		else if (timeValue.equals("")) System.out.println("Select a time");
//		else {
//			String userInput = taskBox.getText() ;
//			String time = timer.getValue().toString();
//			addTaskToTable(new ToDoTask(userInput, time));
//		}
    }

    @FXML
    void saveTask(ActionEvent event) {
        addTaskPanel.setVisible(false);
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
        for (ToDoTask value : dataManager.getTasks())
            if (value.toString().equals(newTaskName)) return true;
        return false;
    }

    @FXML
    void cancelNewTask(ActionEvent event) {
        addTaskPanel.setVisible(false);
        minSelector.getEditor().clear();
        hourSelector.getEditor().clear();
    }
}
