package ToDo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.hildan.fxgson.FxGson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
}
