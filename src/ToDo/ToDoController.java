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

import java.util.ArrayList;

public class ToDoController {

	DataManager dataManager;

	ObservableList<Task> observableList;

	ArrayList<Task> taskArrayList;

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
		taskArrayList = new ArrayList<>();

		// Set up the columns in the table
		taskColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("taskTime"));

		observableList = FXCollections.observableArrayList();

		dataManager.loadData(taskArrayList);
		observableList.setAll(taskArrayList);
		tableView.setItems(observableList);
	}

	@FXML
	void addTask(ActionEvent event) {
		String timeValue = (timer.getValue() != null ? timer.getValue().toString() : "");

		if (tableView.getItems().toString().contains(taskBox.getText()))
			System.out.println("That task is already in the list");
		else if (timeValue.equals(""))
			System.out.println("Select a time");
		else {
			String userInput = taskBox.getText() ;
			String time = timer.getValue().toString();
			addTaskToTable(new Task(userInput, time));
		}
	}

	private void addTaskToTable(Task task) {
		dataManager.saveTask(task);
		observableList.add(task);
		tableView.setItems(observableList);
	}

	@FXML
	void removeTask(ActionEvent event) {
		int selectedTaskIndex = tableView.getSelectionModel().getSelectedIndex();

		try {
			dataManager.removeTask(selectedTaskIndex);
			observableList.remove(selectedTaskIndex);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Select a task");
		}
	}
}
