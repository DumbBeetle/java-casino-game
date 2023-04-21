package src.texasHoldem.hands;

import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import src.texasHoldem.Cards.Card;

import java.util.LinkedList;
import java.util.List;

public class HandOld {
    private AnchorPane mapScreen; // Game main Screen.
    protected List <Card> handCards = new LinkedList<>(); // List of cards hold by the hand.
    protected byte handValue = 0; // Value of current Hand.
    protected byte highCard = 0; // Highest Card value (used in case of ties in player hand value).
    private final ImageView PORTRAIT = new ImageView("images/Player/Portrait.png");
    private Pane holderDisplay = new Pane(); // Main Player Pane.
    private Label displayName = new Label(); // Label that display the holder name.
    private Label displayMoney = new Label(); // Label that display the holder money.
    private Label displayStats = new Label(); // Label that display the holder cards value.
    private String holderName;
    private byte seatNumber; // Index of seat used by player.
    private ImageView seat = null; //  Image of seat used by player.
    private ImageView card1 = new ImageView();
    private ImageView card2 = new ImageView();
    private boolean isPlaying = true; // Check if player is playing in current round.


    protected HandOld(AnchorPane mapScreen, String holderName){
        this.mapScreen = mapScreen; // Get main screen (Anchor Pane).
        this.holderName = holderName; // Get Player name.
    }
    // Set and arrange player name label.
    private void arrangeNameDisplay(){
        displayName.setText(holderName);
        displayName.setFont(Font.font(24));
        displayName.setTextFill(Color.WHITE);
        displayName.setAlignment(Pos.CENTER);
        displayName.setPrefHeight(35);
        displayName.setPrefWidth(185);
        displayName.setTranslateX(120);
        displayName.setTranslateY(40);
    }
    // Set and arrange player money label.
    private void arrangeMoneyDisplay(){
        displayMoney.setText("$5000");
        displayMoney.setFont(Font.font(24));
        displayMoney.setTextFill(Color.WHITE);
        displayMoney.setAlignment(Pos.CENTER);
        displayMoney.setPrefHeight(35);
        displayMoney.setPrefWidth(185);
        displayMoney.setTranslateX(120);
        displayMoney.setTranslateY(67);
    }
    // Set and arrange player cards label.
    private void arrangeStatsDisplay(){
        displayStats.setText("Pair of Ace");
        displayStats.setFont(Font.font(24));
        displayStats.setTextFill(Color.WHITE);
        displayStats.setAlignment(Pos.CENTER);
        displayStats.setPrefHeight(35);
        displayStats.setPrefWidth(185);
        displayStats.setTranslateX(120);
        displayStats.setTranslateY(93);
    }

    protected Pane getHolderDisplay() {
        return holderDisplay;
    }

    protected double getHolderLayoutX() {
        return holderDisplay.getLayoutX();
    }

    protected double getHolderLayoutY() {
        return holderDisplay.getLayoutY();
    }
    // add Portrait and Labels to player Pane.
    protected void setHolderDisplay() {
        holderDisplay.getChildren().addAll(PORTRAIT, displayName, displayMoney, displayStats);
        arrangeNameDisplay();
        arrangeMoneyDisplay();
        arrangeStatsDisplay();
    }
    // Take and block seat for other player.
    protected boolean takeSeat(ImageView seat){
        if (this.seat == null){
            this.seat = seat;
            holderDisplay.setLayoutX(seat.getTranslateX());
            holderDisplay.setLayoutY(seat.getTranslateY());
            setHolderDisplay();
            mapScreen.getChildren().add(holderDisplay);
            String getSeatNumber = seat.getId().substring(4);
            seatNumber = Byte.parseByte(getSeatNumber);
            return true;
        }
        return false;
    }
    // Add Card to Hand.
    protected void takeCard(Card card){
        // Add card to hand list.
        handCards.add(card);
        short CARD_1_LAYOUT_X = 138;
        byte CARD_LAYOUT_Y = -72;
        // Card 1.
        if (card1 == null){
            card1.setImage(card.getCardImage(card));
            card1.setScaleX(0);
            holderDisplay.getChildren().add(card1);
            card1.setTranslateX(CARD_1_LAYOUT_X);
            card1.setTranslateY(CARD_LAYOUT_Y);
        }else {
            //Card 2.
            card2.setImage(card.getCardImage(card));
            card2.setScaleX(0);
            holderDisplay.getChildren().add(card2);
            byte CARD_2_LAYOUT_X = 83;
            card2.setTranslateX(CARD_1_LAYOUT_X + CARD_2_LAYOUT_X);
            card2.setTranslateY(CARD_LAYOUT_Y);
        }
    }


    public byte getHandCardsNumber() {
        return (byte) handCards.size();
    }

    public ImageView getCard1() {
        return card1;
    }

    public ImageView getCard2() {
        return card2;
    }

    protected void clearHand(){
        handCards.clear();
        holderDisplay.getChildren().removeAll(card1, card2);
        card1.setImage(null);
        card2.setImage(null);
    }
    // Get player seat index.
    protected byte getSeatNumber() {
        return seatNumber;
    }
    // Return boolean for player playing or folding.
    protected boolean isPlaying() {
        return isPlaying;
    }
    // Set boolean for player playing or folding.
    protected void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
