package ToDo.controllers;


import ToDo.data.DataManager;
import ToDo.models.days.TaskList;
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

public class ToDoController {

    private DataManager dataManager;
    private ObservableList<ToDoTask> tableObservableList;

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
    private TextField taskNameTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<String> taskTypesList;

    @FXML
    private TitledPane dayAccordion;

    @FXML
    private ListView<TaskList> todaySidePanelList;

    @FXML
    private ListView<?> tomorrowList;

    public void initialize() {
        dataManager = new DataManager();
        dataManager.loadDummyData(todaySidePanelList);

        taskTypesList.setItems(TaskTypes.getTaskTypes());
        addTaskPanel.setVisible(false);

        ArrayList<ToDoTask> taskArrayList = new ArrayList<>();
        tableObservableList = FXCollections.observableArrayList();

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("taskTime"));

        dataManager.loadData(taskArrayList);
        tableObservableList.setAll(taskArrayList);
        tableView.setItems(tableObservableList);
    }

    @FXML
    void addList(ActionEvent event) {
        TextInputDialog newListDialog = new TextInputDialog();
        newListDialog.setHeaderText("Enter list name");
        newListDialog.showAndWait();
        String taskListName = newListDialog.getEditor().getText();
        ObservableList<TaskList> newList = dataManager.addTaskList(taskListName);
        todaySidePanelList.setItems(newList);
    }

    /**
     * Opens the  dialog to enter the information for a new task
     */
    @FXML
    void addTask(ActionEvent event) {
        addTaskPanel.setVisible(true);
    }

    /**
     * Saves the new task. This button is from the new task dialog.
     */
    @FXML
    void saveTask(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        String taskType = taskTypesList.getValue();
        String taskName = taskNameTextField.getText();
        String selectedList = todaySidePanelList.getSelectionModel().getSelectedItem().getName();

        if (StringUtils.isInvalidText(taskName) || StringUtils.isInvalidText(taskType)) {
            alert.setContentText("Task name or type can not be empty.");
            alert.showAndWait();

        } else if (dataManager.isTaskInTheList(selectedList, taskName)) {
            alert.setContentText("Task name is already is already taken. Please select another name.");
            alert.showAndWait();

        } else {
            ToDoTask newTask = new ToDoTask(taskName, taskType);
            ObservableList<ToDoTask> observableTempList = dataManager.updateTaskList(selectedList, newTask);
            tableView.setItems(observableTempList);
            addTaskPanel.setVisible(false);
            taskNameTextField.clear();
        }
    }

    @FXML
    void removeTask(ActionEvent event) {
        String selectTaskName = null;
        String selectedList = null;

        try {
            selectTaskName = tableView.getSelectionModel().getSelectedItem().getName();
            selectedList = todaySidePanelList.getSelectionModel().getSelectedItem().getName();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a list and a task to delete");
            alert.showAndWait();
            return;
        }


        //-1 means there is no items in tableView
        if (!StringUtils.isInvalidText(selectTaskName) && !StringUtils.isInvalidText(selectedList)) {
            ObservableList<ToDoTask> observableListTemp = dataManager.removeTaskFromList(selectedList, selectTaskName);
            tableView.setItems(observableListTemp);
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

        if (StringUtils.isInvalidText(listName)) {
            return;
        }

        // select list based on name
        ObservableList<ToDoTask> listTasks = dataManager.getObservableList(listName);
        tableView.setItems(listTasks);
    }
}
