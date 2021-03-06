package ToDo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ToDoTask {

	private SimpleStringProperty taskName;
	private SimpleStringProperty taskTime;

	public ToDoTask(String taskName, String time) {
		this.taskName = new SimpleStringProperty(taskName);
		this.taskTime = new SimpleStringProperty(time);
	}

	public String getName() {
		return taskName.get();
	}

	public void setName(String taskName) {
		this.taskName.set(taskName);
	}

	public StringProperty taskNameProperty() {
		return this.taskName;
	}

	public String getTime() {
		return taskTime.get();
	}

	public void setTime(String value) {
		this.taskTime.set(value);
	}
	
	public StringProperty taskTimeProperty() {
		return this.taskTime;
	}

	@Override
	public String toString(){
		return getName();
	}
}
