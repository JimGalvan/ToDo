package ToDo.models.tasklist;

public enum TaskListType {
    IMPORTANT("IMPORTANT");

    private final String type;

    TaskListType(String type) {
        this.type = type;
    }
}
