package src.texasHoldem.service;

import javafx.concurrent.Task;

public class TimerTask extends Task {
    @Override
    protected Object call() throws Exception {
        updateProgress(1, 10);
        Thread.sleep(1000);
        updateProgress(2, 10);
        Thread.sleep(1000);
        updateProgress(3, 10);
        Thread.sleep(1000);
        updateProgress(4, 10);
        Thread.sleep(1000);
        updateProgress(5, 10);
        Thread.sleep(1000);
        updateProgress(6, 10);
        Thread.sleep(1000);
        updateProgress(7, 10);
        Thread.sleep(1000);
        updateProgress(8, 10);
        Thread.sleep(1000);
        updateProgress(9, 10);
        Thread.sleep(1000);
        updateProgress(10, 10);
        Thread.sleep(1000);
        return null;
    }
}
