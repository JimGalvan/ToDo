package ToDo.data;

import ToDo.models.days.DayContainer;
import ToDo.models.days.TaskList;
import ToDo.models.days.DayType;
import ToDo.models.tasks.ToDoTask;
import ToDo.test.DummyTestData;
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
import java.util.ArrayList;

public class DataManager {

    private ArrayList<ToDoTask> tasks;
    private final Gson gson;
    private final String listJsonPath = "resources/data/task-list.json";
    private final DayContainer dayContainer = new DayContainer(DayType.TODAY);

    public DataManager() {
        gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        tasks = new ArrayList<>();
        loadJSONList();
    }

    public void loadDummyData(ListView<TaskList> todayList) {
        ObservableList<TaskList> observableList = FXCollections.observableArrayList();
        DummyTestData.loadDataIntoDay(dayContainer);
        dayContainer.getItems().forEach((listName, list) -> observableList.add(list));
        todayList.setItems(observableList);
    }

    private void loadJSONList() {
        try {

            JsonReader jsonReader = new JsonReader(new FileReader(listJsonPath));
            Type collectionType = new TypeToken<ArrayList<ToDoTask>>() {
            }.getType();

            tasks = gson.fromJson(jsonReader, collectionType);
            System.out.println("Data loaded");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadData(ArrayList<ToDoTask> arrayList) {
        arrayList.addAll(tasks);
    }

    public void saveTaskInJson(ToDoTask task) {
        tasks.add(task);
        saveChanges();
    }

    public void saveChanges() {
        try {
            Writer writer = new FileWriter(listJsonPath, false);
            gson.toJson(tasks, writer);

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTaskFromJson(int taskIndex) {
        tasks.remove(taskIndex);
        saveChanges();
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
        return taskList.getObservableList();
    }

    public ObservableList<ToDoTask> removeTaskFromList(String selectedList, String selectTaskName) {
        TaskList taskList = dayContainer.getList(selectedList);
        taskList.remove(selectTaskName);
        return taskList.getObservableList();
    }

    public ObservableList<TaskList> addTaskList(String taskListName) {
        TaskList taskList = new TaskList(taskListName);
        this.dayContainer.addList(taskList);
        return this.dayContainer.getObservableList();
    }

    public ObservableList<TaskList> removeTaskList(String selectedListName) {
        this.dayContainer.removeList(selectedListName);
        return dayContainer.getObservableList();
    }

    public boolean isTaskListPresent(TaskList selectedList) {
        return dayContainer.contains(selectedList);
    }
}
