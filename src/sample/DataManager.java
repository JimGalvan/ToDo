package sample;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import javafx.scene.control.ListView;

public class DataManager {

	Gson gson;
	ArrayList<Task> taskList;

	public DataManager() {

		gson = new GsonBuilder().setPrettyPrinting().create();

		taskList = new ArrayList<Task>();

//		checkIfJsonExits();

	}

	private void checkIfJsonExits() {

		Path path = Paths.get("Resources\\task-list.json");

		if (Files.exists(path)) {

			System.out.println("file exits");

			loadJSONList();
		}
	}

	private void loadJSONList() {

		try {

			JsonReader jsonReader = new JsonReader(new FileReader("src/Resources/task-list.json"));
			Type collectionType = new TypeToken<ArrayList<Task>>() {
			}.getType();

			taskList = gson.fromJson(jsonReader, collectionType);

			System.out.println("Data loaded");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadData(ListView<String> listView) {

		for (int i = 0; i < taskList.size(); i++) {

			String taskName = taskList.get(i).getName();

			listView.getItems().add(taskName);
		}
	}

	public void saveTask(Task task) {

		taskList.add(task);

		saveChanges();
	}

	public void saveChanges() {
		try {

			Writer writer = new FileWriter("src/Resources/task-list.json", false);
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
}
