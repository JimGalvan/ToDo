package ToDo.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TodoTask {
    private final SimpleIntegerProperty taskId;
    private SimpleStringProperty description;
    private SimpleIntegerProperty listId;

    public TodoTask(int taskId) {
        this.taskId = new SimpleIntegerProperty(taskId);
    }

    public TodoTask(int taskId, int listId, String description) {
        this.taskId = new SimpleIntegerProperty(taskId);
        this.listId = new SimpleIntegerProperty(listId);
        this.description = new SimpleStringProperty(description);
    }

    public int getListId() {
        return listId.get();
    }

    public SimpleIntegerProperty listIdProperty() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId.set(listId);
    }

    public int getTaskId() {
        return taskId.get();
    }

    public SimpleIntegerProperty taskIdProperty() {
        return taskId;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return "TodoTask{" +
                "taskId=" + taskId +
                ", description=" + description +
                ", listId=" + listId +
                '}';
    }
}
