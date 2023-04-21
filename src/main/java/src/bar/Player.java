package src.bar;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.CloseBox;
import src.NamePicker;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Controller controller;
    private AnchorPane map;
    private ImageView playerImage;
    private short mapLocationX;
    private short mapLocationY;
    private byte playerIndex; // Player Id.
    private List<Image> images = new ArrayList<>(); // List of player frames.
    private final byte ANIMATION_SPEED = 65; // Walking animation speed.
    private byte imageIndex = 0;
    private final Rectangle PLAYER_HIT_BOX;
    private final byte PLAYER_HIT_BOX_WIDTH = 20;
    private final byte PLAYER_HIT_BOX_HEIGHT = 28;
    private final byte PLAYER_HIT_BOX_START_X = 70;
    private final byte PLAYER_HIT_BOX_START_Y = 92;
    private final List<Rectangle> MAP_HIT_BOX;
    private final List<Rectangle> EVENT_HIT_BOX;
    private Timeline timeline = new Timeline();
    private SequentialTransition sequentialTransition = new SequentialTransition();
    boolean north, south, west, east;
    private AnimationTimer animationTimer;
    private Label nameLabel = new Label();

    public Player(Controller controller, AnchorPane map, double mapLocationX, double mapLocationY,
                    ArrayList MAP_HIT_BOX, ArrayList EVENT_HIT_BOX) {

        this.controller = controller;
        this.map = map;
        this.mapLocationX = (short) mapLocationX;
        this.mapLocationY = (short) mapLocationY;
        images.add(new Image("images/Player/PlayerF0.png")); // 0 Front.
        images.add(new Image("images/Player/PlayerF1.png")); // 1.
        images.add(new Image("images/Player/PlayerF2.png")); // 2.
        images.add(new Image("images/Player/PlayerB0.png")); // 3 Back.
        images.add(new Image("images/Player/PlayerB1.png")); // 4.
        images.add(new Image("images/Player/PlayerB2.png")); // 5.
        images.add(new Image("images/Player/PlayerL0.png")); // 6 Left Side.
        images.add(new Image("images/Player/PlayerL1.png")); // 7.
        images.add(new Image("images/Player/PlayerL2.png")); // 8.
        images.add(new Image("images/Player/PlayerR0.png")); // 9 Right Side.
        images.add(new Image("images/Player/PlayerR1.png")); // 10.
        images.add(new Image("images/Player/PlayerR2.png")); // 11.
        playerImage = new ImageView(images.get(0));
        PLAYER_HIT_BOX = new Rectangle(PLAYER_HIT_BOX_WIDTH, PLAYER_HIT_BOX_HEIGHT);
        this.MAP_HIT_BOX = MAP_HIT_BOX;
        this.EVENT_HIT_BOX = EVENT_HIT_BOX;
        nameLabel.setText(NamePicker.getPlayerName());
    }

    public void addPlayer() {
        map.getChildren().add(playerImage); // Add Player to Screen.
        playerIndex = (byte) map.getChildren().indexOf(playerImage); // Get Player Id in AnchorPane.
        final short PLAYER_START_X = (short) (60 + mapLocationX);
        final short PLAYER_START_Y = (short) (58 + mapLocationY);
        playerImage.setTranslateX(PLAYER_START_X);
        playerImage.setTranslateY(PLAYER_START_Y);

//        map.getChildren().add(PLAYER_HIT_BOX);

        PLAYER_HIT_BOX.setTranslateX(PLAYER_HIT_BOX_START_X);
        PLAYER_HIT_BOX.setTranslateY(PLAYER_HIT_BOX_START_Y);

        setAnimationTimer();
        animationTimer.start();

    }
    // Works with KeyPressed in controller.
    // change direction boolean to true (Listened by setAnimationTimer), start movement animation.
    // Z key actives Events if collision was found by checkEventCollision.
    // Esc key active CloseBox class for closing game check.
    public void movePlayer(KeyEvent eventKey) {
        KeyCode key = eventKey.getCode();
        switch (key) {
            case UP:
                north = true;
                break;
            case DOWN:
                south = true;
                break;
            case LEFT:
                west = true;
                break;
            case RIGHT:
                east = true;
                break;
            case Z:
                // Check Event collision.
                checkEventCollision();
                // Move HitBox back to Player coordinates
                resetPlayerHitBox();
                break;
            case ESCAPE:
                // Block default ESC key.
                eventKey.consume();
                // Use this event instead.
                if (CloseBox.display()) {
                    Stage stage = (Stage) map.getScene().getWindow();
                    stage.close();
                }
                break;
            default:
                break;
        }
    }
    // change direction boolean to false (Listened by setAnimationTimer), stop movement animation.
    public void releasePlayer(KeyEvent eventKey) {
        KeyCode key = eventKey.getCode();
        switch (key) {
            case UP:
                north = false;
                break;
            case DOWN:
                south = false;
                break;
            case LEFT:
                west = false;
                break;
            case RIGHT:
                east = false;
                break;
            default:
                break;
        }
    }

    // Animation Timer that when active is called every frame.
    // Works with KeyPress
    private void setAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int x = 0, y = 0;
                if (sequentialTransition.getStatus() == Animation.Status.STOPPED) {
                    if (north) {
                        imageIndex = 3;
                        playerImage.setImage(images.get(3));
                        y = -8;
                        if (checkHitBoxCollision(x, y)) {
                            sequentialTransition.getChildren().add(moveAnimation(x, y));
                        }
                    } else if (south) {
                        imageIndex = 0;
                        playerImage.setImage(images.get(0));
                        y = 8;

                        if (checkHitBoxCollision(x, y)) {
                            sequentialTransition.getChildren().add(moveAnimation(x, y));
                        }
                    } else if (west) {
                        imageIndex = 6;
                        playerImage.setImage(images.get(6));
                        x = -8;
                        if (checkHitBoxCollision(x, y)) {
                            sequentialTransition.getChildren().add(moveAnimation(x, y));
                        }
                    } else if (east) {
                        imageIndex = 9;
                        playerImage.setImage(images.get(9));
                        x = 8;
                        if (checkHitBoxCollision(x, y)) {
                            sequentialTransition.getChildren().add(moveAnimation(x, y));
                        }
                    }
                }
                sequentialTransition.setOnFinished(event -> sequentialTransition.getChildren().clear());
                sequentialTransition.play();
            }
        };
    };
    // Stop the Animation Timer.
    public void stopAnimationTimer(){
        animationTimer.stop();
    }

    // Movement animation.
    private Timeline moveAnimation(int x, int y) {
        // Check if animation is still playing, if true skip input.
        if (timeline.getStatus() == Animation.Status.STOPPED){
            KeyFrame keyFrame = new KeyFrame(Duration.millis(ANIMATION_SPEED), event -> {
                playerImage.setImage(images.get(imageIndex + 1));
                playerImage.setLayoutX(playerImage.getLayoutX() + x);
                playerImage.setLayoutY(playerImage.getLayoutY() + y);

            });
            KeyFrame keyFrame2 = new KeyFrame(Duration.millis(ANIMATION_SPEED * 2), event -> {
                playerImage.setImage(images.get(imageIndex));
                playerImage.setLayoutX(playerImage.getLayoutX() + x);
                playerImage.setLayoutY(playerImage.getLayoutY() + y);

            });
            KeyFrame keyFrame3 = new KeyFrame(Duration.millis(ANIMATION_SPEED * 3), event -> {
                playerImage.setImage(images.get(imageIndex + 2));
                playerImage.setLayoutX(playerImage.getLayoutX() + x);
                playerImage.setLayoutY(playerImage.getLayoutY() + y);

            });
            KeyFrame keyFrame4 = new KeyFrame(Duration.millis(ANIMATION_SPEED * 4), event -> {
                playerImage.setImage(images.get(imageIndex));
                playerImage.setLayoutX(playerImage.getLayoutX() + x);
                playerImage.setLayoutY(playerImage.getLayoutY() + y);
            });
            timeline.setOnFinished(event -> timeline.getKeyFrames().clear());
            timeline.getKeyFrames().addAll(keyFrame, keyFrame2, keyFrame3, keyFrame4);
            return timeline;
        }
        return null;
    }

    // Check for collision.
    // If return true no collision detected.
    // If return false collision detected, block player movement.
    private boolean checkHitBoxCollision(int x, int y) {
        PLAYER_HIT_BOX.setLayoutX((playerImage.getLayoutX()) + (x * 4));
        PLAYER_HIT_BOX.setLayoutY(playerImage.getLayoutY() + (y * 4));
        for (Rectangle hit : MAP_HIT_BOX) {
            Shape inter = Shape.intersect(hit, PLAYER_HIT_BOX);
            boolean checker = inter.getBoundsInLocal().isEmpty();
            if (!checker) {
                return false;
            }
        }
        return true;
    }

    // Check for collision.
    // If return true no collision detected.
    // If return false collision detected, activate event.
    private void checkEventCollision() {
        final byte EXTEND = 16;
        switch (imageIndex) {
            case 0:
                // Down
                PLAYER_HIT_BOX.setHeight(PLAYER_HIT_BOX_HEIGHT + EXTEND);
                break;
            case 3:
                // Up
                PLAYER_HIT_BOX.setHeight(PLAYER_HIT_BOX_HEIGHT + EXTEND);
                PLAYER_HIT_BOX.setTranslateY(PLAYER_HIT_BOX_START_Y - EXTEND);
                break;
            case 6:
                // Left
                PLAYER_HIT_BOX.setWidth(PLAYER_HIT_BOX_WIDTH + EXTEND);
                PLAYER_HIT_BOX.setTranslateX(PLAYER_HIT_BOX_START_X - EXTEND);
                break;
            case 9:
                // Right
                PLAYER_HIT_BOX.setWidth(PLAYER_HIT_BOX_WIDTH + EXTEND);
                break;
            default:
                break;
        }
        for (Rectangle hit : EVENT_HIT_BOX) {
            Shape inter = Shape.intersect(hit, PLAYER_HIT_BOX);
            boolean checker = inter.getBoundsInLocal().isEmpty();
            if (!checker) {
                controller.startPokerGame();
            }
        }
    }

    // Move HitBox back to Player coordinates
    private void resetPlayerHitBox() {
        PLAYER_HIT_BOX.setTranslateX(PLAYER_HIT_BOX_START_X);
        PLAYER_HIT_BOX.setTranslateY(PLAYER_HIT_BOX_START_Y);
        PLAYER_HIT_BOX.setWidth(PLAYER_HIT_BOX_WIDTH);
        PLAYER_HIT_BOX.setHeight(PLAYER_HIT_BOX_HEIGHT);
    }
}