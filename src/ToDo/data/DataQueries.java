package ToDo.data;

import ToDo.models.TodoList;
import ToDo.models.TodoTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataQueries {
    private static final String DATABASE_URL = "jdbc:sqlite:resources/data/todo-data.db";
    private Connection connection;
    private Statement statement;

    /**
     * To-Do Lists queries
     */
    private PreparedStatement readAllTodoListsQuery;
    private PreparedStatement addTodoListQuery;
    private PreparedStatement readTodoListQuery;
    private PreparedStatement updateTodoListQuery;
    private PreparedStatement deleteTodoListQuery;
    private PreparedStatement deleteAllTodoTasksFromListQuery;

    /**
     * To-Do Tasks queries
     */
    private PreparedStatement readAllTodoTasksQuery;
    private PreparedStatement addTodoTaskQuery;
    private PreparedStatement readTodoTaskQuery;
    private PreparedStatement updateTodoTaskQuery;
    private PreparedStatement deleteTodoTaskQuery;

    public DataQueries() {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            statement = connection.createStatement();

            // handle To-Do lists queries

            readAllTodoListsQuery = connection.prepareStatement("SELECT * FROM Lists ORDER BY id");
            addTodoListQuery = connection.prepareStatement("INSERT INTO Lists (title) VALUES (?)");
            readTodoListQuery = connection.prepareStatement("SELECT * FROM Lists WHERE id = ?");
            updateTodoListQuery = connection.prepareStatement("UPDATE Lists SET title = ? WHERE id = ?;");
            deleteTodoListQuery = connection.prepareStatement("DELETE FROM Lists WHERE id = ?");
            deleteAllTodoTasksFromListQuery = connection.prepareStatement("DELETE FROM Tasks WHERE list_id = ?");

            // Handle To-Do tasks queries

            readAllTodoTasksQuery = connection.prepareStatement("SELECT * FROM Tasks ORDER BY task_id");
            addTodoTaskQuery = connection.prepareStatement("INSERT INTO Tasks (description, list_id) VALUES (?,?)");
            readTodoTaskQuery = connection.prepareStatement("SELECT * FROM Tasks WHERE task_id = ?");
            updateTodoTaskQuery = connection.prepareStatement("UPDATE Tasks SET description = ? WHERE task_id = ?;");
            deleteTodoTaskQuery = connection.prepareStatement("DELETE FROM Tasks WHERE task_id = ?");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTodoTask(int taskId) {
        try {
            deleteTodoTaskQuery.setInt(1, taskId);
            deleteTodoTaskQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTodoTask(int taskId, String description) {
        int numOfRowsUpdated = 0;
        try {
            updateTodoTaskQuery.setString(1, description);
            updateTodoTaskQuery.setInt(2, taskId);
            numOfRowsUpdated = updateTodoTaskQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.printf("Number of rows updated: %s%n", numOfRowsUpdated);
    }

    public TodoTask readTodoTask(int taskId) {
        TodoTask todoTask = null;

        try {
            readTodoTaskQuery.setInt(1, taskId);
            ResultSet result = readTodoTaskQuery.executeQuery();

            while (result.next()) {
                int listId = result.getInt("list_id");
                String description = result.getString("description");
                todoTask = new TodoTask(taskId, listId, description);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return todoTask;
    }

    public void addTodoTask(int listId, String description) {
        try {
            addTodoTaskQuery.setString(1, description);
            addTodoTaskQuery.setInt(2, listId);
            addTodoTaskQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TodoTask> readTodoTasks() {
        List<TodoTask> todoTasks = new ArrayList<>();

        try (ResultSet resultSet = readAllTodoTasksQuery.executeQuery()) {

            while (resultSet.next()) {
                int taskId = resultSet.getInt("task_id");
                int listId = resultSet.getInt("list_id");
                String description = resultSet.getString("description");
                TodoTask todoTask = new TodoTask(taskId, listId, description);
                todoTasks.add(todoTask);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todoTasks;
    }

    public List<TodoList> getTodoLists() {
        List<TodoList> todoLists = new ArrayList<>();

        try (ResultSet resultSet = readAllTodoListsQuery.executeQuery()) {

            while (resultSet.next()) {
                todoLists.add(new TodoList(resultSet.getInt("id"), resultSet.getString("title")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todoLists;
    }

    public int addTodoList(String title) {
        try {
            addTodoListQuery.setString(1, title);
            return addTodoListQuery.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // return 0 there was an error adding the new To Do List
            return 0;
        }
    }

    public void resetDB() {
        try {
            statement.executeUpdate("DELETE FROM movies");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // This method shuts down the DB to avoid Derby ID gap pattern
    public void close() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            connection.close();
        } catch (SQLException sqlException) {
            System.err.println("Database shutdown");
        }
    }

    public void readTodoList(int id) {
        try {
            readTodoListQuery.setInt(1, id);
            ResultSet resultSet = readTodoListQuery.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTodoList(int id, String title) {
        int numOfRowsUpdated = 0;
        try {
            updateTodoListQuery.setString(1, title);
            updateTodoListQuery.setInt(2, id);
            numOfRowsUpdated = updateTodoListQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.printf("Number of rows updated: %s%n", numOfRowsUpdated);
    }

    public void deleteTodoList(int id) {
        try {
            deleteTodoListQuery.setInt(1, id);
            deleteAllTodoTasksFromListQuery.setInt(1, id);
            deleteTodoListQuery.executeUpdate();
            deleteAllTodoTasksFromListQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
