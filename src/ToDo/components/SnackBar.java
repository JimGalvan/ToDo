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
        int millSecondsWaitPerFrame = 25;
        int frames = 36;
        double decreaseRate = 0.10;

        for (int i = 0; i < seconds * frames; i++) {
            Thread.sleep(millSecondsWaitPerFrame);
            double currentOpacity = snackBar.getOpacity();
            snackBar.setOpacity(currentOpacity - decreaseRate);
        }

        // restore snackbar initial status
        snackBar.setOpacity(1);
        snackBar.setVisible(false);
        return null;
    }
}
