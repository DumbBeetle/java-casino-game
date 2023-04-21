package src.texasHoldem;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import src.texasHoldem.Cards.Card;
import src.texasHoldem.Cards.CardChecker;
import src.texasHoldem.Cards.CheckRemaining;
import src.texasHoldem.Cards.Deck;
import src.texasHoldem.hands.Hand;
import src.texasHoldem.lists.PlayerList;


import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class PokerGame {
    private AnchorPane mapScreen;
    private ImageView background;
    private ImageView dealerHand;
    // Buttons
    public ImageView buttonCheck;
    public ImageView buttonFold;
    public ImageView buttonRaise;

    private final short BK_LAYOUT_X; // Background coordinate 0 for X.
    private final short BK_LAYOUT_Y; // Background coordinate 0 for Y.

    // Community cards base coordinates.
    private final short COMMUNITY_CARDS_X;
    private final short COMMUNITY_CARDS_Y;

    private Button testButton, testButton1, checker, adder, button3;
    private Deck deck; // Card's deck class.
    private CardAnimation cardAnimation; // Animation class.
    private ImageView tempCard; // Temporary card for animation.
    // Community/Table Cards.
    private ImageView communityCard1 = new ImageView();
    private ImageView communityCard2 = new ImageView();
    private ImageView communityCard3 = new ImageView();
    private ImageView communityCard4 = new ImageView();
    private ImageView communityCard5 = new ImageView();
    private ImageView burnedCard = new ImageView();
    // Lists.
    private LinkedList<Card> communityCardsList = new LinkedList<>(); // Holds cards values.
    private PlayerList playerList; // Holds current players
    private boolean secondDraw = true;
    private Image imgDownCardSmallest = new Image("images/Cards/DownCardSmallest.png"); // Smallest card image.
    private int playTime;
    private byte roundNumber = 0;

    public PokerGame(AnchorPane mapScreen, ImageView background, ImageView dealerHand, Button testButton, Button testButton1,
                     Button checker, Button button3, ImageView buttonCheck, ImageView buttonFold) {
        this.mapScreen = mapScreen;
        this.background = background;
        this.dealerHand = dealerHand;

        this.testButton = testButton;
        this.testButton1 = testButton1;
        this.checker = checker;
        this.button3 = button3;
        this.buttonCheck = buttonCheck;
        this.buttonFold = buttonFold;

        deck = new Deck();
        cardAnimation = new CardAnimation(this.mapScreen, this.background, this.dealerHand);

        BK_LAYOUT_X = (short) background.getLayoutX();
        BK_LAYOUT_Y = (short) background.getLayoutY();

        COMMUNITY_CARDS_X = (short) (BK_LAYOUT_X + 710);
        COMMUNITY_CARDS_Y = (short) (BK_LAYOUT_Y + 553);
        // Add community cards to AnchorPane.
        addCommunityCards();
        activateButtons();

//        ProgressIndicator progressIndicator = new ProgressIndicator();
//        progressIndicator.setProgress(0.5);
//        progressIndicator.getStylesheets().add("progress.css");
//        progressIndicator.setMinHeight(200);
//        progressIndicator.setMinWidth(200);
//        mapScreen.getChildren().add(progressIndicator);


    }


    private void testingPhase() {
        playerList.getPlayerBySeat((byte) 0).addCardToList(deck.drawExactCard("SPADES", "JACK"));
        playerList.getPlayerBySeat((byte) 0).addCardToList(deck.drawExactCard("CLOVERS", "TWO"));

        playerList.getPlayerBySeat((byte) 1).addCardToList(deck.drawExactCard("HEARTS", "QUEEN"));
        playerList.getPlayerBySeat((byte) 1).addCardToList(deck.drawExactCard("CLOVERS", "SIX"));

        playerList.getPlayerBySeat((byte) 2).addCardToList(deck.drawExactCard("HEARTS", "FIVE"));
        playerList.getPlayerBySeat((byte) 2).addCardToList(deck.drawExactCard("CLOVERS", "KING"));
    }


    // Set on start of first game.
    private void setPlayTime() {
        playTime = (int) (System.currentTimeMillis() / 1000);
    }

    // Get after each game to check if 10 minutes were passed to raise blind.
    private int getPlayTime() {
        return playTime;
    }

    // Set coordinate translation of burnCard imageView.
    private void setBurnedCardTranslation() {
        burnedCard.setTranslateX(BK_LAYOUT_X + 629);
        burnedCard.setTranslateY(BK_LAYOUT_Y + 553);
    }

    // Add community cards to AnchorPane and set their coordinates.
    // Reuse after starting each turn.
    private void addCommunityCards() {
        mapScreen.getChildren().add(communityCard1); // Card 1.
        mapScreen.getChildren().add(communityCard2); // Card 2.
        mapScreen.getChildren().add(communityCard3); // Card 3.
        mapScreen.getChildren().add(communityCard4); // Card 4.
        mapScreen.getChildren().add(communityCard5); // Card 5.
        // Arrange burned card.
        mapScreen.getChildren().add(burnedCard);
        setBurnedCardTranslation();
        burnedCard.setViewOrder(2); // Set as lower than 1, to prevent card overlapping in burning card animation.
    }

    private void activateButtons() {
        testButton.setOnAction(event -> playerDraw());
        testButton1.setOnAction(event -> drawCommunityCard());
        checker.setOnAction(event -> checkPlayerCards());
        button3.setOnAction(event -> returnCards());
    }

    // Update the player list in this class.
    public void updatePlayerList(PlayerList playerList) {
        this.playerList = playerList;
    }

    private SequentialTransition drawPlayerCard(Hand player, byte index) {
        Card card = deck.drawCard(); // Draw card from deck.
        player.addCardToList(card);
        Image cardImage = card.getCardImage(card); // Get drawn card image.
        // Get holders translate coordinates X and Y.
        final short CARD_1_TRANSLATE_X = (short) (player.getHolderDisplay().getTranslateX() + 138);
        final short CARD_1_TRANSLATE_Y = (short) (player.getHolderDisplay().getTranslateY() - 72);
        if (player.getCard1().getImage() == null) {
            return cardAnimation.drawAnimation(CARD_1_TRANSLATE_X, CARD_1_TRANSLATE_Y, player.getCard1(), cardImage, index);
        }
        final short CARD_2_TRANSLATE_X = 83;
        return cardAnimation.drawAnimation((short) (CARD_1_TRANSLATE_X + CARD_2_TRANSLATE_X), CARD_1_TRANSLATE_Y, player.getCard2(), cardImage, index);
    }

    private void playerDraw() {
        ParallelTransition parallelDraw = new ParallelTransition();
        // Delay between each player draw based on full duration of dealer's hand animation,
        // short delay may causes animation acceleration.
        final short DELAY = 400;
        byte index = 0;
        // Player list hashMap loop draw 1.
        for (Map.Entry<Byte, Hand> player : playerList.getPlayersAtTable().entrySet()) {
            // Create new Sequential Transition.
            SequentialTransition playerDrawSequential = new SequentialTransition();
            // Add pause animation, duration cause delay between each player draw animation.
            playerDrawSequential.getChildren().add(new PauseTransition(Duration.millis(DELAY * index)));
            // Add draw animation.
            playerDrawSequential.getChildren().add(drawPlayerCard(player.getValue(), index));
            // Add Sequential animation to Parallel Draw animation.
            parallelDraw.getChildren().add(playerDrawSequential);
            index++;

            player.getValue().setHasCards(true);
        }
        // Player list hashMap loop draw 2.
        for (Map.Entry<Byte, Hand> player : playerList.getPlayersAtTable().entrySet()) {
            // Create new Sequential Transition.
            SequentialTransition playerDrawSequential = new SequentialTransition();
            // Add pause animation, duration cause delay between each player draw animation.
            playerDrawSequential.getChildren().add(new PauseTransition(Duration.millis(DELAY * index)));
            // Add draw animation.
            playerDrawSequential.getChildren().add(drawPlayerCard(player.getValue(), index));
            // Add Sequential animation to Parallel Draw animation.
            parallelDraw.getChildren().add(playerDrawSequential);
            index++;
        }
        parallelDraw.play();

    }

    private void drawCommunityCard() {
        final byte INDEX = 1; // Default as 1 in case of burning card animation to properly use viewing order.
        final byte SPACING = 106; // Spacing between each card.
        Card card;
        Image cardImage;
        roundNumber++; // Add 1 to round number.
        // Add community card imageView by cardNumber.
        SequentialTransition sequentialTransition = new SequentialTransition();
        if (roundNumber == 1) {
            // Spacing multiplier.
            final byte TWO = 2;
            // Community card 1.
            // Draw card from deck.
            card = deck.drawCard();
            // Add card to community list.
            communityCardsList.add(card);
            // Get drawn card image.
            cardImage = card.getCardImage(card);
            // Add burn card animation.
            sequentialTransition.getChildren().add(cardBurning());
            // Add draw animation.
            sequentialTransition.getChildren().add(cardAnimation.drawAnimation(COMMUNITY_CARDS_X, COMMUNITY_CARDS_Y, communityCard1, cardImage, INDEX));

            // Community card 2.
            // Draw card from deck.
            card = deck.drawCard();
            // Add card to community list.
            communityCardsList.add(card);
            // Get drawn card image.
            cardImage = card.getCardImage(card);
            // Add draw animation.
            sequentialTransition.getChildren().add(cardAnimation.drawAnimation((short) (COMMUNITY_CARDS_X + SPACING), COMMUNITY_CARDS_Y, communityCard2, cardImage, INDEX));

            // Community card 3.
            // Draw card from deck.
            card = deck.drawCard();
            // Add card to community list.
            communityCardsList.add(card);
            // Get drawn card image.
            cardImage = card.getCardImage(card);
            // Add draw animation.
            sequentialTransition.getChildren().add(cardAnimation.drawAnimation((short) (COMMUNITY_CARDS_X + SPACING * TWO), COMMUNITY_CARDS_Y, communityCard3, cardImage, INDEX));
        } else if (roundNumber == 2) {
            // Community card 4.
            // Spacing multiplier.
            final byte THREE = 3;
            // Draw card from deck.
            card = deck.drawCard();
            // Add card to community list.
            communityCardsList.add(card);
            // Get drawn card image.
            cardImage = card.getCardImage(card);
            // Add burn card animation.
            sequentialTransition.getChildren().add(cardBurning());
            // Add draw animation.
            sequentialTransition.getChildren().add(cardAnimation.drawAnimation((short) (COMMUNITY_CARDS_X + SPACING * THREE), COMMUNITY_CARDS_Y, communityCard4, cardImage, INDEX));
        } else {
            // Community card 5.
            // Spacing multiplier.
            final byte FOUR = 4;
            // Draw card from deck.
            card = deck.drawCard();
            // Add card to community list.
            communityCardsList.add(card);
            // Get drawn card image.
            cardImage = card.getCardImage(card);
            // Add burn card animation.
            sequentialTransition.getChildren().add(cardBurning());
            // Add draw animation.
            sequentialTransition.getChildren().add(cardAnimation.drawAnimation((short) (COMMUNITY_CARDS_X + SPACING * FOUR), COMMUNITY_CARDS_Y, communityCard5, cardImage, INDEX));
        }
        sequentialTransition.play();
    }

    private SequentialTransition cardBurning() {
        SequentialTransition burnSequential = new SequentialTransition();
        // Remove card from deck.
        deck.drawCard();
        // Add burning animation.
        burnSequential.getChildren().add(cardAnimation.burnCardAnimation());
        // after animation finish set burnedCard image.
        burnSequential.setOnFinished(event -> burnedCard.setImage(imgDownCardSmallest));
        return burnSequential;
    }

    private void returnCards() {
        ParallelTransition returnAnimation = new ParallelTransition();
        // Return player's hand.
        for (Map.Entry<Byte, Hand> player : playerList.getPlayersAtTable().entrySet()) {
            if (player.getValue().hasCards()) {
                returnAnimation.getChildren().add(cardAnimation.returnToDealer(player.getValue().getCard1()));
                returnAnimation.getChildren().add(cardAnimation.returnToDealer(player.getValue().getCard2()));
                player.getValue().clearCardList();
            }
        }
        // Return community cards.
        switch (roundNumber) {
            case 3:
                returnAnimation.getChildren().add(cardAnimation.returnToDealer(communityCard5));
            case 2:
                returnAnimation.getChildren().add(cardAnimation.returnToDealer(communityCard4));
            case 1:
                returnAnimation.getChildren().add(cardAnimation.returnToDealer(communityCard3));
                returnAnimation.getChildren().add(cardAnimation.returnToDealer(communityCard2));
                returnAnimation.getChildren().add(cardAnimation.returnToDealer(communityCard1));
                break;
            default:
                break;
        }
        // Return burned card.
        returnAnimation.getChildren().add(cardAnimation.returnBurnedCard(burnedCard));
        // After finishing clearing the cards.
        returnAnimation.setOnFinished(event -> {
            // Clear the community cards list.
            communityCardsList.clear();
            // Set secondDraw to true for playerDraw recursion.
            secondDraw = true;
            // Set burned card image to null.
            burnedCard.setImage(null);
            // Set burned card coordinates.
            setBurnedCardTranslation();
            // Reset round number.
            roundNumber = 0;
            // Reset deck
            deck.refillDeck();
            // Re add players back to game
            playerList.addPlayersToRound();
        });
        returnAnimation.play();
    }


    @FXML
    public void checkPlayerCards() {
        byte highestHandValue = 0;
        byte playerHandValue;

        playerList.addPlayersToRound();
        System.out.println("PlayerList size before = " + playerList.getPlayingPlayers().size());
        // get a set of entries
        Set<Map.Entry<Byte, Hand>> setOfEntries = playerList.getPlayingPlayers().entrySet();
        // get the iterator from entry set
        Iterator<Map.Entry<Byte, Hand>> iterator = setOfEntries.iterator();

        for (byte loop = 0; loop < 2; loop++) {
            while (iterator.hasNext()) {
                Map.Entry<Byte, Hand> entry = iterator.next();
                Hand player = entry.getValue();

                if (loop == 0) {
                    player.addToList(communityCardsList);
                    player.checkCards();
//                    System.out.println(player.getDisplayName() + " has = " + player.getHandValue());
                }

                playerHandValue = player.getHandValue();
                if (highestHandValue > playerHandValue) {
                    player.setPlaying(false);
                    player.playerFolded();
                    iterator.remove();
                } else {
                    highestHandValue = playerHandValue;
                }
            }
            iterator = setOfEntries.iterator();
        }

        if (playerList.getPlayingPlayers().size() > 1) {
            CheckRemaining checkRemaining = new CheckRemaining();
            System.out.println("Check remaining");
            checkRemaining.checkRemainingPlayers(playerList, highestHandValue);
            System.out.println("Size after = " + playerList.getPlayingPlayers().size());
        }
    }






}

