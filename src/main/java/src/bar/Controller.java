package src.bar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.CloseBox;
import src.Main;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    @FXML
    public AnchorPane screen; // Screen window.
    public ImageView background; // Screen Image, use this as baseline for placing objects in window.
    public Player player;
    // Border HitBox.
    private final Rectangle LEFT_HIT_BOX = new Rectangle(1, 448); // Wall Left.
    private final Rectangle RIGHT_HIT_BOX = new Rectangle(1, 448); // Wall Right.
    private final Rectangle TOP_HIT_BOX = new Rectangle(400, 1); // Wall Top.
    private final Rectangle BOTTOM_HIT_BOX = new Rectangle(640, 1); // Wall Bottom.
    private final Rectangle LEFT_BAR_HIT_BOX = new Rectangle(1, 140); // Bar Left.
    private final Rectangle BOTTOM_BAR_HIT_BOX = new Rectangle(180, 1); // Bar Bottom.
    private final Rectangle POKER_WIDTH_HIT_BOX = new Rectangle(160, 52); // Poker Table Width.
    private final Rectangle POKER_HEIGHT_HIT_BOX = new Rectangle(60, 120); // Poker Table Height.
    // Event HitBox.
    private final Rectangle TABLE_EVENT_BOX = new Rectangle(140, 50); // Poker Game event.
    // HitBox lists
    private final ArrayList<Rectangle> MAP_HIT_BOX = new ArrayList<>(); // Contain every border HitBox in map.
    private final ArrayList<Rectangle> EVENT_BOX = new ArrayList<>();// List of HitBoxes related to Game events.


    @FXML
    private void initialize() {
        // Center background image on screen by display size.
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        background.setLayoutX((bounds.getWidth() / 2) - background.getImage().getWidth() / 2);
        background.setLayoutY((bounds.getHeight() / 2) - background.getImage().getHeight() / 2);
        // Arrange HitBoxes.
        arrangeBorderHitBox();
        arrangeEventHitBox();
        // Set Player.
        player = new Player(this, screen, background.getLayoutX(), background.getLayoutY(), MAP_HIT_BOX, EVENT_BOX);
        player.addPlayer();

//        showWallsHitBox();
    }

    @FXML
    void keyPressed(KeyEvent event) {
        player.movePlayer(event);
    }


    public void keyRelease(KeyEvent event) {
        player.releasePlayer(event);
    }

    // Add Border HitBox and arrange them on Screen.
    private void arrangeBorderHitBox() {
        // Wall Left.
        LEFT_HIT_BOX.setTranslateX(-20);
        MAP_HIT_BOX.add(LEFT_HIT_BOX);
        // Wall Right.
        RIGHT_HIT_BOX.setTranslateX(648);
        MAP_HIT_BOX.add(RIGHT_HIT_BOX);
        // Wall Top.
        TOP_HIT_BOX.setTranslateY(64);
        MAP_HIT_BOX.add(TOP_HIT_BOX);
        // Wall Bottom.
        BOTTOM_HIT_BOX.setTranslateY(448);
        MAP_HIT_BOX.add(BOTTOM_HIT_BOX);
        // Bar Left.
        LEFT_BAR_HIT_BOX.setTranslateX(440);
        MAP_HIT_BOX.add(LEFT_BAR_HIT_BOX);
        // Bar Bottom.
        BOTTOM_BAR_HIT_BOX.setTranslateX(440);
        BOTTOM_BAR_HIT_BOX.setTranslateY(140);
        MAP_HIT_BOX.add(BOTTOM_BAR_HIT_BOX);
        // Poker Table Width.
        POKER_WIDTH_HIT_BOX.setTranslateX(420);
        POKER_WIDTH_HIT_BOX.setTranslateY(320);
        MAP_HIT_BOX.add(POKER_WIDTH_HIT_BOX);
        // Poker Table Height.
        POKER_HEIGHT_HIT_BOX.setTranslateX(466);
        POKER_HEIGHT_HIT_BOX.setTranslateY(260);
        MAP_HIT_BOX.add(POKER_HEIGHT_HIT_BOX);
    }
    // Display Wall HitBox (for testing).
    private void showWallsHitBox() {
        screen.getChildren().add(LEFT_HIT_BOX);
        screen.getChildren().add(RIGHT_HIT_BOX);
        screen.getChildren().add(TOP_HIT_BOX);
        screen.getChildren().add(BOTTOM_HIT_BOX);
        screen.getChildren().add(LEFT_BAR_HIT_BOX);
        screen.getChildren().add(BOTTOM_BAR_HIT_BOX);
        screen.getChildren().add(POKER_WIDTH_HIT_BOX);
        screen.getChildren().add(POKER_HEIGHT_HIT_BOX);
    }
    // Add Event HitBox and arrange them on Screen.
    private void arrangeEventHitBox() {
        // Poker Game starter event.
        TABLE_EVENT_BOX.setTranslateX(426);
        TABLE_EVENT_BOX.setTranslateY(320);
        EVENT_BOX.add(TABLE_EVENT_BOX);
//        mapScreen.getChildren().add(TABLE_EVENT_BOX); // Display HitBox for testing
    }
    // Start Poker Game by event HitBox
    public void startPokerGame() {
        try {
            player.stopAnimationTimer(); // Stop player movement.
            Parent root = FXMLLoader.load(Main.class.getResource("/TexasHoldem.fxml"));
            root.setStyle("-fx-background-color: black"); // Set Poker Game background color to black.
            Main.getWindow().getScene().setRoot(root);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeProgram() {
        Boolean answer = CloseBox.display();
        if (answer) {
            Stage stage = (Stage) screen.getScene().getWindow();
            stage.close();
        }
    }
}
