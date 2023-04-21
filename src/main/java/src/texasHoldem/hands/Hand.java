package src.texasHoldem.hands;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import src.texasHoldem.Cards.Card;
import src.texasHoldem.Cards.CardChecker;

import java.util.LinkedList;
import java.util.List;

public class Hand {
    private AnchorPane mapScreen; // Game main Screen.
    protected List<Card> handCards = new LinkedList<>(); // List of cards hold by the hand.
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

    private boolean isPlaying = true; // Check if player is playing in current round.  // TODO: may not be needed.
    private boolean hasCards = false; // Check if players has cards.

    private CardChecker cardChecker = new CardChecker();
    private byte handValue = 0; // Based on cards values.
    private byte pair1 = 0; // Highest card from pair.
    private byte pair1Value = 0;
    private byte pair2 = 0; // lowest pair (In case of 2 pairs or full house).
    private byte pair2Value = 0;
    private byte highestOfFive = 0; // Highest card from non pair.
    private int playerCash;

    // Constructor.
    public Hand(AnchorPane mapScreen, String holderName) {
        this.mapScreen = mapScreen;
        this.holderName = holderName;
        displayName.setText(holderName);
    }

    public void addCardToList(Card card){
        handCards.add(card);
    }

    public void addToList(List<Card> list){
        handCards.addAll(list);
    }

    public void clearCardList(){
        handCards.clear();
        cardChecker.clearValues();
        hasCards = false;
        displayStats.setText("");
        isPlaying = true;
    }

    //Getter.
    public List<Card> getHandCards() {
        return handCards;
    }

    public Pane getHolderDisplay() {
        return holderDisplay;
    }

    public byte getSeatNumber() {
        return seatNumber;
    }

    public ImageView getSeat() {
        return seat;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean hasCards() {
        return hasCards;
    }

    public ImageView getCard1() {
        return card1;
    }

    public ImageView getCard2() {
        return card2;
    }

    public String getDisplayName() {
        return displayName.getText();
    }

    public void printCards(){
        cardChecker.printSymbolRank(handCards);
    }

    // Setter.
    public void setSeat(ImageView seat) {
        this.seat = seat;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setHasCards(boolean hasCards) {
        this.hasCards = hasCards;
    }

    public void setCard1Image(Image card) {
        card1.setImage(card);
    }

    public void setCard2Image(Image card) {
        card2.setImage(card);
    }

    // Hand values.
    public byte getHandValue() {
        return handValue;
    }

    public byte getPair1() {
        return pair1;
    }

    public byte getPair1Value() {
        return pair1Value;
    }

    public byte getPair2() {
        return pair2;
    }

    public byte getPair2Value() {
        return pair2Value;
    }

    public byte getHighestOfFive() {
        return highestOfFive;
    }





    // Set and arrange player name label.
    private void arrangeNameDisplay() {
//        displayName.setText(holderName);
        displayName.setFont(Font.font(24));
        displayName.setTextFill(Color.WHITE);
        displayName.setAlignment(Pos.CENTER);
        displayName.setPrefHeight(35);
        displayName.setPrefWidth(185);
        displayName.setTranslateX(120);
        displayName.setTranslateY(40);
    }

    // Set and arrange player money label.
    private void arrangeMoneyDisplay() {
        playerCash = 5000;
        displayMoney.setText("$" + playerCash);
        displayMoney.setFont(Font.font(24));
        displayMoney.setTextFill(Color.WHITE);
        displayMoney.setAlignment(Pos.CENTER);
        displayMoney.setPrefHeight(35);
        displayMoney.setPrefWidth(185);
        displayMoney.setTranslateX(120);
        displayMoney.setTranslateY(67);

    }

    // Set and arrange player cards label.
    private void arrangeStatsDisplay() {
        displayStats.setText("");
        displayStats.setFont(Font.font(24));
        displayStats.setTextFill(Color.WHITE);
        displayStats.setAlignment(Pos.CENTER);
        displayStats.setPrefHeight(35);
        displayStats.setPrefWidth(185);
        displayStats.setTranslateX(120);
        displayStats.setTranslateY(93);
    }

    private void arrangeCardsDisplay() {
        // Card 1.
        mapScreen.getChildren().add(card1);
        // Card 2.
        mapScreen.getChildren().add(card2);
    }

    public void arrangeHolderDisplay() {
        holderDisplay.getChildren().addAll(PORTRAIT, displayName, displayMoney, displayStats);
        arrangeNameDisplay();
        arrangeMoneyDisplay();
        arrangeStatsDisplay();
        arrangeCardsDisplay();
    }

    public boolean takeSeat(ImageView seat) {
        final byte CROP = 4;
        if (this.seat == null) {
            this.seat = seat;
            holderDisplay.setTranslateX(seat.getTranslateX());
            holderDisplay.setTranslateY(seat.getTranslateY());
            arrangeHolderDisplay();
            mapScreen.getChildren().add(holderDisplay);
            seatNumber = Byte.parseByte(seat.getId().substring(CROP));
            return true;
        }
        return false;
    }

    private void getCheckerValues(){
        byte[] arr = cardChecker.sendValues();
        handValue = arr[0];
        pair1 = arr[1];
        pair1Value = arr[2];
        pair2 = arr[3];
        pair2Value = arr[4];
        highestOfFive = arr[5];
        displayNewState(handValue);
    }

    public void checkCards(){
        handCards = cardChecker.checkList(handCards);
        getCheckerValues();
    }

    private void displayNewState(byte handValue){
        switch (handValue){
            case 1:
                displayStats.setText("High Card");
                break;
            case 2:
                displayStats.setText("One Pair");
                break;
            case 3:
                displayStats.setText("Two Pairs");
                break;
            case 4:
                displayStats.setText("Three of a Kind");
                break;
            case 5:
                displayStats.setText("Straight");
                break;
            case 6:
                displayStats.setText("Flush");
                break;
            case 7:
                displayStats.setText("Full House");
                break;
            case 8:
                displayStats.setText("Four of a Kind");
                break;
            case 9:
                displayStats.setText("Straight Flush");
                break;
            case 10:
                displayStats.setText("Royal Flush");
                break;
            default:
                displayStats.setText("Error");
                break;
        }
    }

    public void playerFolded(){
        displayStats.setText("Folded");
    }

    public void playerTie(){
        displayStats.setText("Tie");
    }

}
