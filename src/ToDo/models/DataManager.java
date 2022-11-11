package ToDo.models;

import ToDo.models.days.Day;
import ToDo.models.days.DayTaskList;
import ToDo.models.days.DayType;
import ToDo.models.tasks.ToDoTask;
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
import java.util.List;

public class DataManager {

    private ArrayList<ToDoTask> tasks;
    private final Gson gson;
    private final static String listJsonPath = "resources/data/task-list.json";

    public DataManager() {
        gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        tasks = new ArrayList<>();
        loadJSONList();
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

    public void saveTask(ToDoTask task) {
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

    public void removeTask(int taskIndex) {
        tasks.remove(taskIndex);
        saveChanges();
    }

    public ArrayList<ToDoTask> getTasks() {
        return tasks;
    }

    public void initDayListData(ListView<DayTaskList> todayList) {

        // Load data

        // Data
        List<ToDoTask> tasks = new ArrayList<>();
        tasks.add(new ToDoTask("Test1", "test2"));
        DayTaskList dayTaskListTwo = new DayTaskList("test list 2", tasks);
        DayTaskList dayTaskList = new DayTaskList("Test list 1", tasks);
        Day day = new Day(DayType.TODAY, dayTaskList);

        // JavaFx
        ObservableList<DayTaskList> observableList = FXCollections.observableArrayList();
        observableList.add(dayTaskList);
        observableList.add(dayTaskListTwo);
        todayList.setItems(observableList);
    }

    public ObservableList<ToDoTask> getListTasks(String listName) {
        ObservableList<ToDoTask> listTasks = FXCollections.observableArrayList();
        for (int i = 0; i < 5; i++) {
            listTasks.add(new ToDoTask(String.format("Task %s", i), listName));
        }
        return listTasks;
    }

    public boolean isTaskInTheList(String newTaskName) {
        for (ToDoTask value : this.getTasks()) {
            if (value.toString().equals(newTaskName)) return true;
        }
        return false;
    }

    public void addTaskToTable(ToDoTask task) {

    }
}
