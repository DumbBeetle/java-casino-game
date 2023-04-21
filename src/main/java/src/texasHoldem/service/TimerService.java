package src.texasHoldem.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class TimerService extends Service {
    private final AnchorPane ANCHOR_PANE;
    private final ImageView SCREEN;
    private final TimerTask timerTask;

    public TimerService(AnchorPane ANCHOR_PANE, ImageView SCREEN) {
        this.ANCHOR_PANE = ANCHOR_PANE;
        this.SCREEN = SCREEN;
        timerTask = new TimerTask();
    }

    @Override
    protected Task createTask() {
        return timerTask;
    }

    @Override
    protected void cancelled() {
        super.cancelled();
    }

    @Override
    public boolean cancel() {
        return super.cancel();
    }
}
