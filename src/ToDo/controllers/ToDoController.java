package ToDo.controllers;


import ToDo.components.SnackBar;
import ToDo.components.SnackBarExecutor;
import ToDo.data.DataQueries;
import ToDo.models.TodoList;
import ToDo.models.TodoTask;
import ToDo.utils.NodeUtils;
import ToDo.utils.StringUtils;
import javafx.collections.FXCollections;
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
    private TableView<TodoTask> tableView;

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
    private ListView<TodoList> listView;
    @FXML
    private GridPane snackBar;

    private ObservableList<TodoList> todoLists;
    private ObservableList<TodoTask> todoTasks;
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private final Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    private final ButtonType buttonYes = new ButtonType("Yes");
    private final ButtonType buttonNo = new ButtonType("No");
    private final SnackBarExecutor snackBarExecutor = new SnackBarExecutor();
    private final DataQueries dataQueries = new DataQueries();

    public void initialize() {

        // Setup cache lists
        todoLists = FXCollections.observableArrayList(dataQueries.getTodoLists());
        todoTasks = FXCollections.observableArrayList();

        // Bind the data to the UI side list, and table.
        tableView.setItems(todoTasks);
        listView.setItems(todoLists);

        // Setup UI components
        addTaskPanel.setVisible(false);
        snackBar.setVisible(false);
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        // Set up the columns in the table
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    void markImportant(ActionEvent event) {
//        if (isItemSelected(tableView)) {
//            String selectedTaskName = tableView.getSelectionModel().getSelectedItem().getName();
//
//            if (!dataManager.isTaskInTheList(IMPORTANT_LIST_NAME, selectedTaskName)) {
//                dataManager.updateTaskList(IMPORTANT_LIST_NAME, new ToDoTask(selectedTaskName, null));
//
//                SnackBar tempSnackBar = new SnackBar(snackBar);
//                snackBarExecutor.start(tempSnackBar);
//            } else {
//                informationAlert.setContentText("Task is on important list already");
//                informationAlert.showAndWait();
//            }
//        } else {
//            informationAlert.setContentText("Select a task first");
//            informationAlert.showAndWait();
//        }
    }

    @FXML
    void deleteList(MouseEvent event) {
        TodoList selectedList;

        if (isItemSelected(listView)) {
            selectedList = listView.getSelectionModel().getSelectedItem();
            confirmationAlert.setContentText("Are you sure you want to delete this list?");
            Optional<ButtonType> userDialogChoice = confirmationAlert.showAndWait();

            if (userDialogChoice.isPresent() && userDialogChoice.get().equals(buttonYes)) {
                dataQueries.deleteTodoList(selectedList.getId());
                updateTodoLists();

                String todoListTitle = selectedList.getTitle();
                SnackBar tempSnackBar = new SnackBar(String.format("List deleted: %s", todoListTitle), snackBar);
                snackBarExecutor.start(tempSnackBar);
            }

        } else {
            informationAlert.setContentText("Select a list first");
            informationAlert.showAndWait();
        }

        // Clear the table items
        tableView.getItems().clear();
    }

    @FXML
    void addList(MouseEvent event) {
        TextInputDialog newListDialog = new TextInputDialog();
        newListDialog.setHeaderText("Enter list name");
        Optional<String> userSelection = newListDialog.showAndWait();

        if (userSelection.equals(Optional.empty()) && !userSelection.isPresent()) {
            return;
        }

        String taskListName = newListDialog.getEditor().getText();

        if (StringUtils.isInvalidText(taskListName)) {
            informationAlert.setContentText("List name can't be empty");
            informationAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            informationAlert.showAndWait();
            return;
        }

        dataQueries.addTodoList(userSelection.get());
        updateTodoLists();
    }

    /**
     * Opens the  dialog to enter the information for a new task
     */
    @FXML
    void addTask(ActionEvent event) {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
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
        String description = taskNameTextField.getText();
        String selectedTodoList = listView.getSelectionModel().getSelectedItem().getTitle();

        if (StringUtils.isInvalidText(description)) {
            informationAlert.setContentText("Task name or type can not be empty.");
            informationAlert.showAndWait();

        } else if (tableView.getItems().contains(description)) {
            informationAlert.setContentText("Task name is already is already taken. Please select another name.");
            informationAlert.showAndWait();

        } else {
            int selectedTodoListId = listView.getSelectionModel().getSelectedItem().getId();
            dataQueries.addTodoTask(selectedTodoListId, description);
            updateTodoTasks(selectedTodoListId);

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
                int selectTodoTaskId = tableView.getSelectionModel().getSelectedItem().getTaskId();
                int selectedTodoListId = listView.getSelectionModel().getSelectedItem().getId();

                dataQueries.deleteTodoTask(selectTodoTaskId);
                updateTodoTasks(selectedTodoListId);
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
        TodoList todoList = listView.getSelectionModel().getSelectedItem();

        Node selectedList = event.getPickResult().getIntersectedNode();
        if (selectedList == null) return;
        String listName;

        try {
            listName = NodeUtils.getNodeText(selectedList);
        } catch (ClassCastException e) {
            return;
        }

        if (StringUtils.isInvalidText(listName)) return;
        updateTodoTasks(todoList.getId());
    }

    private boolean isItemSelected(TableView<TodoTask> collectionOfItems) {
        Object selectedItem = collectionOfItems.getSelectionModel().getSelectedItem();
        return selectedItem != null;
    }

    private boolean isItemSelected(ListView<TodoList> collectionOfItems) {
        Object selectedItem = collectionOfItems.getSelectionModel().getSelectedItem();
        return selectedItem != null;
    }

    private void updateTodoLists() {
        todoLists.setAll(dataQueries.getTodoLists());
    }

    private void updateTodoTasks(int todoListId) {
        todoTasks.setAll(dataQueries.getTodoTasksFromList(todoListId));
    }
}
