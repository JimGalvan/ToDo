package ToDo.models;

public class TodoList {
    private final int id;
    private String title;

    public TodoList(int id) {
        this.id = id;
    }

    public TodoList(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
