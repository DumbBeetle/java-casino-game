package src.texasHoldem;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MakeABet {
    static int bet;

    public static int display(int playerCash){
        // Create new Stage.
        Stage window = new Stage();
        // Defines a modal window that blocks events from being delivered to any other application window.
        window.initModality(Modality.APPLICATION_MODAL);
        // Defines a Stage style with a solid white background and no decorations.
        window.initStyle(StageStyle.UNDECORATED);
        // Set window size.
        window.setMinWidth(217);
        window.setMinHeight(112);
        // Set images.
        ImageView background = new ImageView("images/BetWindow.png");
        // Set Pane.
        Pane pane = new Pane();
        pane.getChildren().addAll(background);
        // Set text field.
        TextField textField = new TextField();
        textField.setMinSize(40, 30);
        textField.setTranslateX(46);
        textField.setTranslateY(64);

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !textField.getText().isEmpty()){
                if (textField.getText().matches("\\D*")){
                    textField.setText("");
                }else {
                    bet = Integer.parseInt(textField.getText());
                    window.close();
                }

            }
        });
        // Set scene.
        Scene scene = new Scene(pane);
        window.setScene(scene);
        // Shows this stage and waits for it to be hidden (closed) before returning to the caller.
        // This method temporarily blocks processing of the current event, and starts a nested event loop to handle other events.
        window.showAndWait();

        return bet;
    }


}
