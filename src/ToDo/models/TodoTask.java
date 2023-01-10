package ToDo.models;

public class TodoTask {

    private String description;
    private final int listId;
    private final int taskId;

    public TodoTask(int taskId, int listId, String description) {
        this.listId = listId;
        this.taskId = taskId;
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getListId() {
        return listId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
