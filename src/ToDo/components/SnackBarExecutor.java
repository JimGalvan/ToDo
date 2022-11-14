package ToDo.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnackBarExecutor {

    public void start(SnackBar snackBar) {
        try {
            // Create a new Thread pool to store tasks
            ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.execute(snackBar);
            executorService.shutdown();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
