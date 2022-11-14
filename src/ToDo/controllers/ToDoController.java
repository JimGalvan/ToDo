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
    private ImageView deleteListButton;

    @FXML
    private ImageView addListButton;

    @FXML
    private Button markImportantButton;

    @FXML
    private ListView<TaskList> listViewPanel;

    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private final Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);

    public void initialize() {
        dataManager = new DataManager();
        dataManager.loadJsonData(listViewPanel);
        addTaskPanel.setVisible(false);

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        dataManager.saveUpdates();

        // temp solution to hide important list
        hideImportantList(listViewPanel);
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
            informationAlert.setContentText("Select a list and a task to delete");
            informationAlert.showAndWait();
            return;
        }

        if (StringUtils.isInvalidText(selectedList)) {
            informationAlert.setContentText("List name can't be empty");
            informationAlert.showAndWait();
            return;
        }

        dataManager.updateTaskList(selectedList, new ToDoTask(selectedTaskName, null));
    }

    @FXML
    void deleteList(MouseEvent event) {
        TaskList selectedList = listViewPanel.getSelectionModel().getSelectedItem();
        if (selectedList != null && dataManager.isTaskListPresent(selectedList)) {
            ObservableList<TaskList> tempList = dataManager.removeTaskList(selectedList.getName());
            listViewPanel.setItems(tempList);
            hideImportantList(listViewPanel);
        }

        if (listViewPanel.getItems().size() == 0) {
            tableView.getItems().clear();
        }
    }

    @FXML
    void addList(MouseEvent event) {
        TextInputDialog newListDialog = new TextInputDialog();
        newListDialog.setHeaderText("Enter list name");
        Optional<String> userSelection = newListDialog.showAndWait();

        if (userSelection.equals(Optional.empty())) {
            return;
        }

        String taskListName = newListDialog.getEditor().getText();

        if (taskListName.toLowerCase().equals(TaskListType.IMPORTANT.name().toLowerCase())) {
            informationAlert.setContentText("Can't use the given name");
            informationAlert.showAndWait();
            return;
        }

        if (StringUtils.isInvalidText(taskListName)) {
            informationAlert.setContentText("List name can't be empty");
            informationAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            informationAlert.showAndWait();
            return;
        }

        ObservableList<TaskList> newList = dataManager.addTaskList(taskListName);
        listViewPanel.setItems(newList);
        hideImportantList(listViewPanel);
    }

    /**
     * Opens the  dialog to enter the information for a new task
     */
    @FXML
    void addTask(ActionEvent event) {
        int selectedIndex = listViewPanel.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            informationAlert.setContentText("Select a list to add a task.");
            informationAlert.showAndWait();
            return;
        }
        addTaskPanel.setVisible(true);
    }

    /**
     * Saves the new task. This button is from the new task dialog.
     */
    @FXML
    void saveTask(ActionEvent event) {

        informationAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        String taskName = taskNameTextField.getText();
        String selectedList = listViewPanel.getSelectionModel().getSelectedItem().getName();

        if (StringUtils.isInvalidText(taskName)) {
            informationAlert.setContentText("Task name or type can not be empty.");
            informationAlert.showAndWait();

        } else if (dataManager.isTaskInTheList(selectedList, taskName)) {
            informationAlert.setContentText("Task name is already is already taken. Please select another name.");
            informationAlert.showAndWait();

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

        if (isImportantListInUse()) {
            selectedList = TaskListType.IMPORTANT.name();
        } else {
            try {
                selectedList = listViewPanel.getSelectionModel().getSelectedItem().getName();
            } catch (Exception e) {
                informationAlert.showAndWait();
                return;
            }
        }

        try {
            selectTaskName = tableView.getSelectionModel().getSelectedItem().getName();
        } catch (Exception e) {
            informationAlert.showAndWait();
            return;
        }

        //-1 means there is no items in tableView
        if (!StringUtils.isInvalidText(selectTaskName) && !StringUtils.isInvalidText(selectedList)) {
            ObservableList<ToDoTask> observableListTemp = dataManager.removeTaskFromList(selectedList, selectTaskName);
            tableView.setItems(observableListTemp);
        } else {
            informationAlert.showAndWait();
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

    private void hideImportantList(ListView<TaskList> listViewPanel) {
        listViewPanel.getItems().removeIf(taskList -> taskList.getName().equalsIgnoreCase("important"));
    }
}
