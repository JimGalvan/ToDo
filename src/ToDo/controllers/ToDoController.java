package ToDo.controllers;


import ToDo.components.SnackBar;
import ToDo.components.SnackBarExecutor;
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
    @FXML
    private GridPane snackBar;

    private DataManager dataManager = new DataManager();
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private final Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    private final ButtonType buttonYes = new ButtonType("Yes");
    private final ButtonType buttonNo = new ButtonType("No");
    private final static String IMPORTANT_LIST_NAME = TaskListType.IMPORTANT.name();
    SnackBarExecutor snackBarExecutor = new SnackBarExecutor();

    public void initialize() {
        dataManager.loadJsonData(listViewPanel);

        // setup UI
        addTaskPanel.setVisible(false);
        snackBar.setVisible(false);
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        dataManager.saveUpdates();
    }

    @FXML
    void showImportantList(MouseEvent event) {
        ObservableList<ToDoTask> importantList = dataManager.getObservableList(IMPORTANT_LIST_NAME);
        tableView.setItems(importantList);
    }

    @FXML
    void markImportant(ActionEvent event) {
        if (isItemSelected(tableView)) {
            String selectedTaskName = tableView.getSelectionModel().getSelectedItem().getName();

            if (!dataManager.isTaskInTheList(IMPORTANT_LIST_NAME, selectedTaskName)) {
                dataManager.updateTaskList(IMPORTANT_LIST_NAME, new ToDoTask(selectedTaskName, null));

                SnackBar tempSnackBar = new SnackBar(snackBar);
                snackBarExecutor.start(tempSnackBar);
            } else {
                informationAlert.setContentText("Task is on important list already");
                informationAlert.showAndWait();
            }
        } else {
            informationAlert.setContentText("Select a task first");
            informationAlert.showAndWait();
        }
    }

    @FXML
    void deleteList(MouseEvent event) {
        if (isItemSelected(listViewPanel)) {
            TaskList selectedList = listViewPanel.getSelectionModel().getSelectedItem();
            confirmationAlert.setContentText("Are you sure you want to delete this list?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get().equals(buttonYes)) {

                if (!selectedList.getName().equals(IMPORTANT_LIST_NAME)) {
                    if (dataManager.isTaskListPresent(selectedList)) {
                        ObservableList<TaskList> tempList = dataManager.removeTaskList(selectedList.getName());
                        listViewPanel.setItems(tempList);
                    }

                    if (listViewPanel.getItems().size() == 0) {
                        tableView.getItems().clear();
                    }
                } else {
                    informationAlert.setContentText("You can't delete Important list");
                    informationAlert.showAndWait();
                }
            }
        } else {
            informationAlert.setContentText("Select a list first");
            informationAlert.showAndWait();
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

        if (taskListName.equalsIgnoreCase(IMPORTANT_LIST_NAME)) {
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
        if (isItemSelected(tableView)) {
            confirmationAlert.setContentText("Are you sure you want to delete this task?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get().equals(buttonYes)) {
                String selectTaskName = tableView.getSelectionModel().getSelectedItem().getName();
                String selectedList = listViewPanel.getSelectionModel().getSelectedItem().getName();

                ObservableList<ToDoTask> observableListTemp = dataManager.removeTaskFromList(selectedList, selectTaskName);
                tableView.setItems(observableListTemp);
            }
        } else {
            informationAlert.setContentText("Select a task first");
            informationAlert.showAndWait();
        }
    }

    @FXML
    void cancelNewTask(ActionEvent event) {
        addTaskPanel.setVisible(false);
    }

    @FXML
    void clickList(MouseEvent event) {
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

    private boolean isItemSelected(TableView<ToDoTask> collectionOfItems) {
        Object selectedItem = collectionOfItems.getSelectionModel().getSelectedItem();
        return selectedItem != null;
    }

    private boolean isItemSelected(ListView<TaskList> collectionOfItems) {
        Object selectedItem = collectionOfItems.getSelectionModel().getSelectedItem();
        return selectedItem != null;
    }
}
