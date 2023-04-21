package src.texasHoldem;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;

public class CardAnimation {
    private AnchorPane mapScreen; // Main Screen Pane, used to add children to screen.
    private ImageView background; // Main Screen Background, used as a base for every coordinate translation.
    private final short BK_LAYOUT_X; // Background coordinate 0 for X.
    private final short BK_LAYOUT_Y; // Background coordinate 0 for Y.
    private ImageView effectCard = new ImageView(); // Used for additional effects, such as fading.
    private ImageView dealerHand; // Dealer's hand image.
    // Deck coordinates in dealer hands.
    private final short DECK_LAYOUT_X;
    private final short DECK_LAYOUT_Y;
    // Coordinates for where to place burned cards on table.
    private final short BURNED_CARD_LAYOUT_X;
    private final short BURNED_CARD_LAYOUT_Y;

    // Face down card images.
    private Image imgDownCardSmallest = new Image("images/Cards/DownCardSmallest.png"); // Smallest card image (0#).
    private Image imgDownCardSmallestTrans = new Image("images/Cards/DownCardSmallestTrans.png"); // Smallest card expend image (#1).
    private Image imgDownCardSmall = new Image("images/Cards/DownCardSmall.png"); // Small card image (#2).
    private Image imgDownCardSmallTrans = new Image("images/Cards/DownCardSmallTrans.png"); // Small card expend image (#3).
    private Image imgDownCard = new Image("images/Cards/DownCard.png"); // normal size card image (#4).
    private ArrayList<Image> cardFaceDownList = new ArrayList<>();
    // Dealer hand images.
    private Image imgDealerHand1 = new Image("images/Hand1.png");
    private Image imgDealerHand2 = new Image("images/Hand2.png");
    private Image imgDealerHand3 = new Image("images/Hand3.png");
    // Used for speeding or slowing animations.
    private float animationSpeeder = 1.0f;

    public CardAnimation(AnchorPane mapScreen, ImageView background, ImageView dealerHand) {
        this.mapScreen = mapScreen;
        this.background = background;
        this.dealerHand = dealerHand;
        mapScreen.getChildren().add(effectCard);

        // Get the 0 coordinates of the background image
        BK_LAYOUT_X = (short) background.getLayoutX();
        BK_LAYOUT_Y = (short) background.getLayoutY();

        // Arrange locations
        DECK_LAYOUT_X = (short) (BK_LAYOUT_X + 922);
        DECK_LAYOUT_Y = (short) (BK_LAYOUT_Y + 281);
        BURNED_CARD_LAYOUT_X = (short) (BK_LAYOUT_X + 629);
        BURNED_CARD_LAYOUT_Y = (short) (BK_LAYOUT_Y + 553);
        loadImageArray();
    }

    // Load images to list.
    private void loadImageArray(){
        cardFaceDownList.add(imgDownCardSmallest);
        cardFaceDownList.add(imgDownCardSmallestTrans);
        cardFaceDownList.add(imgDownCardSmall);
        cardFaceDownList.add(imgDownCardSmallTrans);
        cardFaceDownList.add(imgDownCard);
    }

    // Animate the dealer's hand when drawing.
    public Timeline dealerHandAnimation() {
        final byte DURATION_2 = 80;
        final short DURATION_3 = 220;
        Timeline drawTimeline = new Timeline();
        KeyFrame hand1 = new KeyFrame(Duration.ZERO, event -> dealerHand.setImage(imgDealerHand2));
        KeyFrame hand2 = new KeyFrame(Duration.millis(DURATION_2 * animationSpeeder), event -> dealerHand.setImage(imgDealerHand3));
        KeyFrame hand3 = new KeyFrame(Duration.millis(DURATION_3 * animationSpeeder), event -> dealerHand.setImage(imgDealerHand2));
        KeyFrame hand4 = new KeyFrame(Duration.millis((DURATION_2 + DURATION_3) * animationSpeeder), event -> dealerHand.setImage(imgDealerHand1));
        drawTimeline.getKeyFrames().addAll(hand1, hand2, hand3, hand4);
        return drawTimeline;
    }

    public TranslateTransition cardPath(double fromX, double fromY, double toX, double toY, ImageView card) {
        final byte MOVEMENT = 10;
        final byte DURATION = 80;
        final byte ONE = 1;
        TranslateTransition drawPath = new TranslateTransition();
        drawPath.setNode(card);
        // Starting point.
        drawPath.setFromX(fromX);
        drawPath.setFromY(fromY);
        // Final destination.
        drawPath.setToX(toX);
        drawPath.setToY(toY);
        // Movement speed.
        drawPath.setByX(MOVEMENT);
        drawPath.setByY(MOVEMENT);
        // Animation Speed, higher number means faster animation.
        drawPath.setRate(ONE / animationSpeeder);
        // Set Delay to start on keyframe "hand2" in dealerHandAnimation;
        drawPath.setDelay(Duration.millis(DURATION));
        return drawPath;
    }

    public ParallelTransition handAndPathAnimation(double layoutX, double layoutY, ImageView drawnCard) {
        drawnCard.setImage(imgDownCardSmallest); // Set card image.
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(dealerHandAnimation());
        parallelTransition.getChildren().add(cardPath(DECK_LAYOUT_X, DECK_LAYOUT_Y, layoutX, layoutY, drawnCard));
        return parallelTransition;
    }
    // If boolean is true play expending animation.
    // If boolean is false play shrinking animation.
    public Timeline expendCard(ImageView faceDownCard, boolean toExpend) {
        final byte DURATION = 50;
        byte cycles;
        Timeline expendTimeline = new Timeline();
        if (toExpend) {
            for (cycles = 0; cycles < 5; cycles++) {
                byte index = cycles;
                expendTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(DURATION * cycles),
                        event ->faceDownCard.setImage(cardFaceDownList.get(index))));
            }
            return expendTimeline;
        }
        byte delay = 0;
        for (cycles = 4; cycles >= 0; cycles--) {
            byte index = cycles;
            expendTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(DURATION * delay),
                    event ->faceDownCard.setImage(cardFaceDownList.get(index))));
            delay++;
        }
        return expendTimeline;
    }


    // Method receives card to scale, and boolean to decides in what direction.
    // Method will scale card from 1 to 0 if false.
    // If boolean is true will scale card from 1 to 0.
    // If boolean is false will scale card from 0 to 1.
    // If boolean is false cardImage should be null.
    public Timeline flipCard(ImageView card, boolean toFlipDown, Image cardImage) {
        Timeline flipTimeLine = new Timeline();
        final byte DURATION = 20;
        final float SCALING = 0.1f;
        final byte CYCLES = 10;
        // cycle animation 5 times.
        flipTimeLine.setCycleCount(CYCLES);
        // If card is in a face down state.
        if (toFlipDown) {
            // Flip face down card animation.
            KeyFrame flipDown = new KeyFrame(Duration.millis(DURATION * animationSpeeder),
                    event -> card.setScaleX(card.getScaleX() - SCALING));
            // Add flip down key frame.
            flipTimeLine.getKeyFrames().add(flipDown);
            // Change card image after hiding it.
            flipTimeLine.setOnFinished(event -> card.setImage(cardImage));
            return flipTimeLine;
        }
        // If card is in a face up state.
        // Flip face up card animation.
        KeyFrame flipUp = new KeyFrame(Duration.millis(DURATION * animationSpeeder),
                event -> card.setScaleX(card.getScaleX() + SCALING));
        // Add flip up key frame.
        flipTimeLine.getKeyFrames().add(flipUp);
        return flipTimeLine;
    }

    public SequentialTransition drawAnimation(short layoutX, short layoutY, ImageView card, Image image, byte index) {
        final byte DEFAULT_VIEW_ORDER = 1; // View order baseline.
        final byte DELAY = 80;
        SequentialTransition drawSequential = new SequentialTransition();
        // Set viewing order in Anchor Pane (make it the below dealer's hands), index is used for placing each card image on deck without overlapping them.
        card.setViewOrder(DEFAULT_VIEW_ORDER + index);
        // Set translation coordinates.
        card.setTranslateX(DECK_LAYOUT_X);
        card.setTranslateY(DECK_LAYOUT_Y);
        // Add animations to sequential animation.
        drawSequential.getChildren().add(handAndPathAnimation(layoutX, layoutY, card));
        drawSequential.getChildren().add(new PauseTransition(Duration.millis(DELAY)));
        drawSequential.getChildren().add(expendCard(card, true));
        drawSequential.getChildren().add(new PauseTransition(Duration.millis(DELAY)));
        drawSequential.getChildren().add(flipCard(card, true, image));
        drawSequential.getChildren().add(flipCard(card, false, null));
        return drawSequential;
    }

    public SequentialTransition burnCardAnimation() {
        SequentialTransition burnCardSequential = new SequentialTransition();
        final byte ONE = 1;
        // Set temporary card from draw animation.
        ImageView drawnCard = new ImageView();
        mapScreen.getChildren().add(drawnCard);
        drawnCard.setViewOrder(ONE); // Set viewing order in Anchor Pane (make it the below dealer's hands).
        drawnCard.setTranslateX(DECK_LAYOUT_X);
        drawnCard.setTranslateY(DECK_LAYOUT_Y);
        // Add animations to sequential animation.
        burnCardSequential.getChildren().add(handAndPathAnimation(BURNED_CARD_LAYOUT_X, BURNED_CARD_LAYOUT_Y, drawnCard));
        burnCardSequential.setOnFinished(event -> mapScreen.getChildren().remove(drawnCard));
        return burnCardSequential;
    }
    // Return all cards to dealer except of burned card.
    public SequentialTransition returnToDealer(ImageView card) {
        final byte DELAY = 80;
        SequentialTransition returnSequential = new SequentialTransition();
        returnSequential.getChildren().add(flipCard(card, true, imgDownCard));
        returnSequential.getChildren().add(flipCard(card, false, null));
        returnSequential.getChildren().add(new PauseTransition(Duration.millis(DELAY)));
        returnSequential.getChildren().add(expendCard(card, false));
        returnSequential.getChildren().add(new PauseTransition(Duration.millis(DELAY)));
        returnSequential.getChildren().add(cardPath(card.getTranslateX(), card.getTranslateY(), DECK_LAYOUT_X, DECK_LAYOUT_Y, card));
        returnSequential.setOnFinished(event -> card.setImage(null));
        return returnSequential;
    }
    // Return burned card to dealer.
    public Animation returnBurnedCard(ImageView burnedCard) {
        return cardPath(BURNED_CARD_LAYOUT_X, BURNED_CARD_LAYOUT_Y, DECK_LAYOUT_X, DECK_LAYOUT_Y, burnedCard);
    }


}
