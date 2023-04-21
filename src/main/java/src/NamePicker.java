package src;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;

public class NamePicker {

        static String playerName;

        public static void display(){
            // Create new Stage.
            Stage window = new Stage();
            // Defines a modal window that blocks events from being delivered to any other application window.
            window.initModality(Modality.APPLICATION_MODAL);
            // Defines a Stage style with a solid white background and no decorations.
            window.initStyle(StageStyle.UNDECORATED);
            // Set window size.
            window.setMinWidth(242);
            window.setMinHeight(112);
            // Set image.
            ImageView background = new ImageView("images/NameWindow.png");
            // Set text field.
            TextField textField = new TextField();
            textField.setMinSize(40, 30);
//            textField.setAlignment(Pos.CENTER);
            textField.setTranslateX(46);
            textField.setTranslateY(64);
            // Set Pane.
            Pane pane = new Pane();
            pane.getChildren().addAll(background, textField);
            // Set scene.
            Scene scene = new Scene(pane);
            window.setScene(scene);

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER && !textField.getText().isEmpty()){
                    playerName = textField.getText();
                    window.close();
                }
            });
            // Shows this stage and waits for it to be hidden (closed) before returning to the caller.
            // This method temporarily blocks processing of the current event, and starts a nested event loop to handle other events.
            window.showAndWait();
        }

    public static String getPlayerName() {
        return playerName;
    }
}
