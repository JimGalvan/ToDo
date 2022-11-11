package ToDo.models.days;

import ToDo.models.tasks.ToDoTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class TaskList {

    private final String name;
    private final HashMap<String, ToDoTask> tasks;

    public TaskList(String name) {
        this.name = name;
        this.tasks = new HashMap<>();
    }

    public HashMap<String, ToDoTask> getItems() {
        return tasks;
    }

    public void add(ToDoTask task) {
        String taskName = task.getName().toLowerCase();
        tasks.put(taskName, task);
    }

    public void get(String taskName) {
        tasks.get(taskName.toLowerCase());
    }

    public boolean contains(String taskName) {
        return tasks.containsKey(taskName.toLowerCase());
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public ObservableList<ToDoTask> getObservableList() {
        ObservableList<ToDoTask> observableTempList = FXCollections.observableArrayList();
        tasks.forEach((taskName, task) -> observableTempList.add(task));
        return observableTempList;
    }

    public void remove(String selectTaskName) {
        this.tasks.remove(selectTaskName.toLowerCase());
    }
}
