package ToDo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.hildan.fxgson.FxGson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataManager {

    private final Gson gson;
    private ArrayList<ToDoTask> taskList;

    public DataManager() {
        gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        taskList = new ArrayList<>();
        loadJSONList();
    }

    private void loadJSONList() {

        try {

            JsonReader jsonReader = new JsonReader(new FileReader("resources/data/task-list.json"));
            Type collectionType = new TypeToken<ArrayList<ToDoTask>>() {
            }.getType();

            taskList = gson.fromJson(jsonReader, collectionType);

            System.out.println("Data loaded");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadData(ArrayList<ToDoTask> arrayList) {
        arrayList.addAll(taskList);
    }

    public void saveTask(ToDoTask task) {
        taskList.add(task);
        saveChanges();
    }

    public void saveChanges() {
        try {
            Writer writer = new FileWriter("resources/data/task-list.json", false);
            gson.toJson(taskList, writer);

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTask(int taskIndex) {
        taskList.remove(taskIndex);
        saveChanges();
    }

    public ArrayList<ToDoTask> getList(){
        return taskList;
    }
}
