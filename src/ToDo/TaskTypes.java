package ToDo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskTypes {

    final static String[] types = {"Work", "School", "Home", "Personal", "Family"};

    public static ObservableList<String> getTaskTypes() {

        return FXCollections.observableArrayList(types);
    }
}
