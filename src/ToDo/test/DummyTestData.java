package ToDo.test;

import ToDo.models.days.DayContainer;
import ToDo.models.days.TaskList;
import ToDo.models.tasks.ToDoTask;

/**
 * This class is for testing purposes, it generates dummy test data.
 */
public class DummyTestData {

    public static void loadDataIntoDay(DayContainer dayContainer) {

        TaskList list1 = new TaskList("List 1");
        TaskList list2 = new TaskList("List 2");
        TaskList list3 = new TaskList("List 3");

        list1.add(new ToDoTask("list1 - task1", "type"));
        list2.add(new ToDoTask("list2 - task1", "type"));
        list2.add(new ToDoTask("list3 - task1", "type"));

        dayContainer.addList(list1);
        dayContainer.addList(list2);
        dayContainer.addList(list3);
    }
}
