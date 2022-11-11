package ToDo.controllers;


import ToDo.models.DataManager;
import ToDo.models.days.DayTaskList;
import ToDo.models.tasks.TaskTypes;
import ToDo.models.tasks.ToDoTask;
import ToDo.utils.NodeUtils;
import ToDo.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ToDoController {

    private DataManager dataManager;
    private ObservableList<ToDoTask> observableList;

    @FXML
    private VBox gridPane;

    @FXML
    private Button addListButton;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<ToDoTask> tableView;

    @FXML
    private TableColumn<?, ?> taskColumn;

    @FXML
    private TableColumn<?, ?> timeColumn;

    @FXML
    private GridPane addTaskPanel;

    @FXML
    private Button saveTaskButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<String> taskTypesList;

    @FXML
    private TitledPane dayAccordion;

    @FXML
    private ListView<DayTaskList> todayList;

    @FXML
    private ListView<?> tomorrowList;

    public void initialize() {
        dataManager = new DataManager();
        dataManager.initDayListData(todayList);

        taskTypesList.setItems(TaskTypes.getTaskTypes());
        addTaskPanel.setVisible(false);

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
        String taskType = taskTypesList.getValue();
        String taskName = nameTextField.getText();

        if (StringUtils.isInvalidText(name) || StringUtils.isInvalidText(taskType)) {
            alert.setContentText("Task name or type can not be empty.");
            alert.showAndWait();

        } else if (dataManager.isTaskInTheList(taskName)) {
            alert.setContentText("Task name is already is already taken. Please select another name.");
            alert.showAndWait();

        } else {
            ToDoTask newTask = new ToDoTask(taskName, taskType);

            dataManager.saveTask(newTask);
            observableList.add(newTask);
            tableView.setItems(observableList);
            addTaskPanel.setVisible(false);
        }
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

    @FXML
    void cancelNewTask(ActionEvent event) {
        addTaskPanel.setVisible(false);
    }

    @FXML
    void clickTask(MouseEvent event) {
        // get task name
        Node selectedList = event.getPickResult().getIntersectedNode();
        String listName = NodeUtils.getNodeText(selectedList);

        // select list based on name
        ObservableList<ToDoTask> listTasks = dataManager.getListTasks(listName);
        tableView.setItems(listTasks);
    }
}
