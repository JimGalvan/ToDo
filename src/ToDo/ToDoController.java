package ToDo;

import com.jfoenix.controls.JFXTimePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ToDoController {

	DataManager dataManager;

	ObservableList<Task> observableList;

	@FXML
	private JFXTimePicker timer;

	@FXML
	private Button addButton;

	@FXML
	private Button removeButton;

	@FXML
	private TextField taskBox;

	@FXML
	private TableView<Task> tableView;

	@FXML
	private TableColumn<Task, String> taskColumn;

	@FXML
	private TableColumn<Task, String> timeColumn;

	public void initialize() {

		dataManager = new DataManager();

//		dataManager.loadData(listView);

		// Set up the columns in the table
		taskColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("taskTime"));

		observableList = FXCollections.observableArrayList();

		// Load data

	}

	@FXML
	void addTask(ActionEvent event) {

		boolean value = tableView.getItems().toString().contains(taskBox.getText());
		String timeValue = (timer.getValue() != null ? timer.getValue().toString() : "");

		if (value)
			System.out.println("That task is already in the list");

		else if (timeValue.equals(""))
			System.out.println("Select a time");

		else {
			String userInput = taskBox.getText();
			String time = timer.getValue().toString();

			Task newTask = new Task(userInput, time);
			addTaskToTable(newTask);
		}
	}

	private void addTaskToTable(Task task) {

		observableList.add(task);
		tableView.setItems(observableList);

		dataManager.saveTask(task);

	}

	@FXML
	void removeTask(ActionEvent event) {

//		int selectedTaskIndex = listView.getSelectionModel().getSelectedIndex();
//
//		try {
//			listView.getItems().remove(selectedTaskIndex);
//
//			dataManager.removeTask(selectedTaskIndex);
//
//		} catch (Exception e) {
//			System.out.println("Select a task");
//		}

	}
}
