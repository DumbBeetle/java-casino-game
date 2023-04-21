package src;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class OpController {
    public AnchorPane opening;
    public ImageView background;
    public ImageView newGame;
    public ImageView loadGame;
    public static String playerName;


    @FXML
    private void initialize() {

        Rectangle2D bounds = Screen.getPrimary().getBounds();
        background.setLayoutY((bounds.getHeight() / 2) - background.getImage().getHeight() / 2);
        background.setLayoutX((bounds.getWidth() / 2) - background.getImage().getWidth() / 2);

        newGame.setTranslateX(background.getLayoutX() + 223);
        newGame.setTranslateY(background.getLayoutY() + 240);

        loadGame.setTranslateX(background.getLayoutX() + 215);
        loadGame.setTranslateY(background.getLayoutY() + 339);
        loadGame.setVisible(false);
        activateButtons();
    }

    private void activateButtons() {
        newGame.setOnMouseClicked(event -> startGame());
    }


    public void startGame() {
//       NamePicker.display();
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/Bar.fxml"));
            // Set Poker Game background color to black.
            root.setStyle("-fx-background-color: black");
            // Get scene and set new root.
            Main.getWindow().getScene().setRoot(root);
            // Force stage to register key press.
            root.requestFocus();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeProgram() {
        Boolean answer = CloseBox.display();
        if (answer) {
            Stage stage = (Stage) opening.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            closeProgram();
        }
    }
}
