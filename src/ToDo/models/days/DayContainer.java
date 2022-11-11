package ToDo.models.days;

import java.util.HashMap;
import java.util.Objects;

public class DayContainer {

    private final DayType DAY_TYPE;
    private final HashMap<String, TaskList> dayTaskListCollection;

    public DayContainer(DayType day_type) {
        DAY_TYPE = day_type;
        this.dayTaskListCollection = new HashMap<>();
    }

    public TaskList getList(String name) {
        return dayTaskListCollection.get(name);
    }

    public void addList(TaskList taskList) {
        Objects.requireNonNull(taskList);
        String listName = taskList.getName();
        dayTaskListCollection.put(listName, taskList);
    }

    public HashMap<String, TaskList> getItems(){
        return dayTaskListCollection;
    }
}
