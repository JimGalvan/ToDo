package ToDo.components;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.Timer;
import java.util.TimerTask;

public class SnackBar extends Task<Object> {

    @FXML
    private final GridPane snackBar;

    public SnackBar(GridPane snackBar) {
        this.snackBar = snackBar;
    }

    @Override
    protected Integer call() throws Exception {
        snackBar.setVisible(true);
        Thread.sleep(1000);

        int seconds = 3;
        double decreaseRate = 0.25;

        for (int i = 0; i < seconds * 6; i++) {
            Thread.sleep(200 / 2);
            double currentOpacity = snackBar.getOpacity();
            snackBar.setOpacity(currentOpacity - decreaseRate);
        }

        // restore snackbar initial status
        snackBar.setOpacity(1);
        snackBar.setVisible(false);
        return null;
    }
}
