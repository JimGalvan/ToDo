package ToDo.controllers;


import ToDo.data.DataManager;
import ToDo.models.tasklist.TaskList;
import ToDo.models.tasklist.TaskListType;
import ToDo.models.tasks.ToDoTask;
import ToDo.utils.NodeUtils;
import ToDo.utils.StringUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class ToDoController {

    private DataManager dataManager;

    @FXML
    private VBox gridPane;

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
    private ListView<TaskList> todaySideList;

    @FXML
    private ListView<?> tomorrowList;

    @FXML
    private ImageView deleteListButton;

    @FXML
    private ImageView addListButton;

    @FXML
    private Button markImportantButton;

    public void initialize() {
        dataManager = new DataManager();
        dataManager.loadJsonData(todaySideList);
        addTaskPanel.setVisible(false);

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        dataManager.saveUpdates();

        // temp solution to hide important list
        hideImportantList(todaySideList);
    }

    @FXML
    void showImportantList(MouseEvent event) {
        ObservableList<ToDoTask> importantList = dataManager.getObservableList(TaskListType.IMPORTANT.name());
        tableView.setItems(importantList);
    }

    @FXML
    void markImportant(ActionEvent event) {

        String selectedTaskName;
        String selectedList = String.valueOf(TaskListType.IMPORTANT);

        try {
            selectedTaskName = tableView.getSelectionModel().getSelectedItem().getName();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a list and a task to delete");
            alert.showAndWait();
            return;
        }

        if (StringUtils.isInvalidText(selectedList)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "List name can't be empty");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            return;
        }

        dataManager.updateTaskList(selectedList, new ToDoTask(selectedTaskName, null));
    }

    @FXML
    void deleteList(MouseEvent event) {
        TaskList selectedList = todaySideList.getSelectionModel().getSelectedItem();
        if (selectedList != null && dataManager.isTaskListPresent(selectedList)) {
            ObservableList<TaskList> tempList = dataManager.removeTaskList(selectedList.getName());
            todaySideList.setItems(tempList);
            hideImportantList(todaySideList);
        }

        if (todaySideList.getItems().size() == 0) {
            tableView.getItems().clear();
        }
    }

    @FXML
    void addList(MouseEvent event) {
        TextInputDialog newListDialog = new TextInputDialog();
        newListDialog.setHeaderText("Enter list name");
        Optional<String> userSelection = newListDialog.showAndWait();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (userSelection.equals(Optional.empty())) {
            return;
        }

        String taskListName = newListDialog.getEditor().getText();

        if (taskListName.toLowerCase().equals(TaskListType.IMPORTANT.name().toLowerCase())) {
            alert.setContentText("Can't use the given name");
            return;
        }

        if (StringUtils.isInvalidText(taskListName)) {
            alert.setContentText("List name can't be empty");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            return;
        }

        ObservableList<TaskList> newList = dataManager.addTaskList(taskListName);
        todaySideList.setItems(newList);
        hideImportantList(todaySideList);
    }

    /**
     * Opens the  dialog to enter the information for a new task
     */
    @FXML
    void addTask(ActionEvent event) {
        int selectedIndex = todaySideList.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a list to add a task.");
            alert.showAndWait();
            return;
        }
        addTaskPanel.setVisible(true);
    }

    /**
     * Saves the new task. This button is from the new task dialog.
     */
    @FXML
    void saveTask(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        String taskName = taskNameTextField.getText();
        String selectedList = todaySideList.getSelectionModel().getSelectedItem().getName();

        if (StringUtils.isInvalidText(taskName)) {
            alert.setContentText("Task name or type can not be empty.");
            alert.showAndWait();

        } else if (dataManager.isTaskInTheList(selectedList, taskName)) {
            alert.setContentText("Task name is already is already taken. Please select another name.");
            alert.showAndWait();

        } else {
            ToDoTask newTask = new ToDoTask(taskName, null);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a list and a task to delete");

        if (isImportantListInUse()) {
            selectedList = TaskListType.IMPORTANT.name();
        } else {
            try {
                selectedList = todaySideList.getSelectionModel().getSelectedItem().getName();
            } catch (Exception e) {
                alert.showAndWait();
                return;
            }
        }

        try {
            selectTaskName = tableView.getSelectionModel().getSelectedItem().getName();
        } catch (Exception e) {
            alert.showAndWait();
            return;
        }

        //-1 means there is no items in tableView
        if (!StringUtils.isInvalidText(selectTaskName) && !StringUtils.isInvalidText(selectedList)) {
            ObservableList<ToDoTask> observableListTemp = dataManager.removeTaskFromList(selectedList, selectTaskName);
            tableView.setItems(observableListTemp);
        } else {
            alert.showAndWait();
        }
    }

    @FXML
    void cancelNewTask(ActionEvent event) {
        addTaskPanel.setVisible(false);
    }

    @FXML
    void clickTask(MouseEvent event) {
        Node selectedList = event.getPickResult().getIntersectedNode();
        if (selectedList == null) return;
        String listName;

        try {
            listName = NodeUtils.getNodeText(selectedList);
        } catch (ClassCastException e) {
            return;
        }

        if (StringUtils.isInvalidText(listName)) return;

        // select list based on name
        ObservableList<ToDoTask> listTasks = dataManager.getObservableList(listName);
        tableView.setItems(listTasks);
    }

    private boolean isImportantListInUse() {
        ObservableList<ToDoTask> importantList = dataManager.getObservableList(TaskListType.IMPORTANT.name());
        return (importantList.size() > 0);
    }

    private void hideImportantList(ListView<TaskList> todaySideList) {
        todaySideList.getItems().removeIf(taskList -> taskList.getName().equalsIgnoreCase("important"));
    }
}
