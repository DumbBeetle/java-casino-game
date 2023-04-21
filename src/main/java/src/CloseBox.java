package src;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CloseBox {

    static boolean answer;

    public static boolean display(){
        // Create new Stage.
        Stage window = new Stage();
        // Defines a modal window that blocks events from being delivered to any other application window.
        window.initModality(Modality.APPLICATION_MODAL);
        // Defines a Stage style with a solid white background and no decorations.
        window.initStyle(StageStyle.UNDECORATED);
        // Set window size.
        window.setMinWidth(238);
        window.setMinHeight(162);
        // Set images.
        ImageView background = new ImageView("images/CloseWindow.png");
        ImageView yesButton = new ImageView("images/Buttons/Yes.png");
        ImageView noButton = new ImageView("images/Buttons/No.png");
        // Set Pane.
        Pane pane = new Pane();
        pane.getChildren().addAll(background, yesButton, noButton);
        // Set location of buttons.
        // Yes Button.
        yesButton.setLayoutX(83);
        yesButton.setLayoutY(50);
        // No Button.
        noButton.setLayoutX(83);
        noButton.setLayoutY(106);
        // Set action on mouse click.
        // Yes Button.
        yesButton.setOnMouseClicked(event -> {
            answer = true;
            window.close();
        });
        // No Button.
        noButton.setOnMouseClicked(event -> {
            answer = false;
            window.close();
        });
        // Set scene.
        Scene scene = new Scene(pane);
        window.setScene(scene);
        // Shows this stage and waits for it to be hidden (closed) before returning to the caller.
        // This method temporarily blocks processing of the current event, and starts a nested event loop to handle other events.
        window.showAndWait();

        return answer;
    }
}
