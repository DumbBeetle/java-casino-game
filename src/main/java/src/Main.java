package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application {
    private static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        window.setTitle("Budget Casino");


        //  Opening scene
        Parent root = FXMLLoader.load(src.Main.class.getResource("/Opening.fxml"));
        Scene scene = new Scene(root, Color.BLACK);
        // Walking map.
//        Parent root = FXMLLoader.load(Main.class.getResource("/Bar.fxml"));
//        Scene scene = new Scene(root, Color.BLACK);
        //  Poker game
//        Parent root = FXMLLoader.load(src.Main.class.getResource("/TexasHoldem.fxml"));
//        Scene scene = new Scene(root, 1920, 1080, Color.BLACK);

        // Set Action on closing Stage event.
        window.setOnCloseRequest(event -> {
            closeProgram();
        });
        // Defines a Stage style with a solid white background and no decorations.
        window.initStyle(StageStyle.UNDECORATED);
        // Set scene.
        window.setScene(scene);
        // Display stage.
        window.show();
        // Set window minimum size by screen size.
        window.setMinHeight(Screen.getPrimary().getBounds().getHeight());
        window.setMinWidth(Screen.getPrimary().getBounds().getWidth());
        // Remove full screen popup.
        window.setFullScreenExitHint("");
        // Set maximum screen size.
        window.setMaximized(true);
        // Set full screen
        window.setFullScreen(true);
        // Force stage to register key press.
        root.requestFocus();
    }

    // Close Window event.
    // If CloseBox returns true, close stage.
    // If CloseBox returns false, do nothing.
    private void closeProgram() {
        if (CloseBox.display()) {
            window.close();  // Close stage.
        }
    }

    public static Stage getWindow() {
        return window;
    }
}
