package ToDo.data;

import ToDo.models.days.DayContainer;
import ToDo.models.days.TaskList;
import ToDo.models.tasks.ToDoTask;
import ToDo.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.hildan.fxgson.FxGson;

import java.io.*;
import java.lang.reflect.Type;

public class DataManager {

    private DayContainer dayContainer;
    private final Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final String listJsonPath = "resources/data/task-data.json";

    public void loadJsonData(ListView<TaskList> todayList) {
        try {
            JsonReader jsonReader = new JsonReader(new FileReader(listJsonPath));
            Type collectionType = new TypeToken<DayContainer>() {
            }.getType();

            dayContainer = gson.fromJson(jsonReader, collectionType);
            ObservableList<TaskList> observableList = FXCollections.observableArrayList();
            dayContainer.getItems().forEach((listName, list) -> observableList.add(list));
            todayList.setItems(observableList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveUpdates() {
        try {
            Writer writer = new FileWriter(listJsonPath, false);
            gson.toJson(dayContainer, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<ToDoTask> getObservableList(String listName) {
        if (StringUtils.isInvalidText(listName)) {
            throw new RuntimeException("Invalid listName");
        }

        TaskList taskList = dayContainer.getList(listName);
        ObservableList<ToDoTask> listTasks = FXCollections.observableArrayList();
        taskList.getItems().forEach((taskName, task) -> listTasks.add(task));
        return listTasks;
    }

    public boolean isTaskInTheList(String selectedList, String taskName) {
        return dayContainer.getItems().get(selectedList).contains(taskName);
    }

    public ObservableList<ToDoTask> updateTaskList(String selectedList, ToDoTask newTask) {
        TaskList taskList = dayContainer.getList(selectedList);
        taskList.add(newTask);
        saveUpdates();
        return taskList.getObservableList();
    }

    public ObservableList<ToDoTask> removeTaskFromList(String selectedList, String selectTaskName) {
        TaskList taskList = dayContainer.getList(selectedList);
        taskList.remove(selectTaskName);
        saveUpdates();
        return taskList.getObservableList();
    }

    public ObservableList<TaskList> addTaskList(String taskListName) {
        TaskList taskList = new TaskList(taskListName);
        this.dayContainer.addList(taskList);
        saveUpdates();
        return this.dayContainer.getObservableList();
    }

    public ObservableList<TaskList> removeTaskList(String selectedListName) {
        this.dayContainer.removeList(selectedListName);
        saveUpdates();
        return dayContainer.getObservableList();
    }

    public boolean isTaskListPresent(TaskList selectedList) {
        return dayContainer.contains(selectedList);
    }
}
