package ToDo.models.days;

import ToDo.models.tasks.ToDoTask;

import java.util.List;

public class DayTaskList {

    private final String name;
    private final List<ToDoTask> toDoTasks;

    public DayTaskList(String name, List<ToDoTask> toDoTasks) {
        this.name = name;
        this.toDoTasks = toDoTasks;
    }

    @Override
    public String toString() {
        return name;
    }
}
