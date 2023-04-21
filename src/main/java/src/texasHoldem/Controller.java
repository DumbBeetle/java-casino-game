package src.texasHoldem;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import src.CloseBox;
import src.Main;
import src.NamePicker;
import src.texasHoldem.Cards.Card;
import src.texasHoldem.Cards.CardChecker;
import src.texasHoldem.Cards.CheckRemaining;
import src.texasHoldem.Cards.Deck;
import src.texasHoldem.hands.Hand;
import src.texasHoldem.lists.PlayerList;
import src.texasHoldem.service.TimerService;

import java.util.*;

public class Controller {
    @FXML
    public AnchorPane mapScreen;
    public ImageView background;
    public ImageView seat0;
    public ImageView seat1;
    public ImageView seat2;
    public ImageView seat3;
    public ImageView seat4;
    public ImageView seat5;
    public ImageView dealerHand;
    public ImageView dealerStaticHand;
    public ImageView buttonCheck;
    public ImageView buttonFold;
    public ImageView buttonRaise;
    public Button testButton;
    public Button testButton1;
    public Button returnButton;
    public Button checker;


    private Hand player;

    private short screenLayoutX;
    private short screenLayoutY;


    private ImageView foldCard = new ImageView(); // Additional card used for flip animation and such.
    private Image imgDownCardSmallest = new Image("images/Cards/DownCardSmallest.png"); // Additional card used for card burning.

    private PlayerList playerList;

    private TimerService timerService;

    private PokerGame pokerGame;

    private Deck deck;
    private CardChecker cardChecker;


    @FXML
    private void initialize() {
        // Center background image on screen by display size.
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        background.setLayoutX((bounds.getWidth() / 2) - background.getImage().getWidth() / 2);
        background.setLayoutY((bounds.getHeight() / 2) - background.getImage().getHeight() / 2);
        background.setViewOrder(20); // Set viewing order in Anchor Pane (make it the bottom one).
        screenLayoutX = (short) background.getLayoutX();
        screenLayoutY = (short) background.getLayoutY();
        arrangeObjectsOnScreen();

        deck = new Deck();
        player = new Hand(mapScreen, "Ami");

        arrangeSeats();
        setSeatAction();

        playerList = new PlayerList();

        pokerGame = new PokerGame(mapScreen, background, dealerHand, testButton, testButton1, checker, returnButton, buttonCheck, buttonFold);


//        // Testing for CardChecker
//        List<Card> test = new LinkedList<>(addRandom());
//
//        CardChecker cardChecker = new CardChecker();
//        System.out.println("Table Cards are = ");
//        cardChecker.printSymbolRank(test);
////        test = cardChecker.checkList(test);
////        System.out.println("Ends with =");
////        cardChecker.printSymbolRank(test);
////        System.out.println("Value = " + cardChecker.getPlayerValue());
////        System.out.println("Pair 1 = " + cardChecker.getPair1());
////        System.out.println("Pair 1 value = " + cardChecker.getPair1Value());
////        System.out.println("Pair 2 = " + cardChecker.getPair2());
////        System.out.println("Pair 2 value = " + cardChecker.getPair2Value());
////        System.out.println("High card = " + cardChecker.getHighestOfFive());
//
//
//
//        player.addCardToList(deck.drawCard());
//        player.addCardToList(deck.drawCard());
////        player.addCardToList(deck.drawExactCard("SPADES", "KING"));
////        player.addCardToList(deck.drawExactCard("CLOVERS", "FOUR"));
//
//        System.out.println("Ami got = ");
//        player.printCards();
//        player.addToList(test);
//
//
//        Hand player2 = new Hand(mapScreen, "Mike");
//        player2.addCardToList(deck.drawCard());
//        player2.addCardToList(deck.drawCard());
////        player2.addCardToList(deck.drawExactCard("DIAMONDS", "SEVEN"));
////        player2.addCardToList(deck.drawExactCard("HEARTS", "SIX"));
//        System.out.println("Mike got = ");
//        player2.printCards();
//        player2.addToList(test);
//
//
//        Hand player3 = new Hand(mapScreen, "Jack");
//        player3.addCardToList(deck.drawCard());
//        player3.addCardToList(deck.drawCard());
////        player3.addCardToList(deck.drawExactCard("CLOVERS", "TEN"));
////        player3.addCardToList(deck.drawExactCard("CLOVERS", "EIGHT"));
//        System.out.println("Jack got = ");
//        player3.printCards();
//        player3.addToList(test);
//
//
//        Hand player4 = new Hand(mapScreen, "Jose");
//        player4.addCardToList(deck.drawCard());
//        player4.addCardToList(deck.drawCard());
////        player4.addCardToList(deck.drawExactCard("CLOVERS", "KING"));
////        player4.addCardToList(deck.drawExactCard("HEARTS", "THREE"));
//        System.out.println("Jose got = ");
//        player4.printCards();
//        player4.addToList(test);
//
//
//        playerList.addPlayer((byte) 0, player);
//        playerList.addPlayer((byte) 1, player2);
//        playerList.addPlayer((byte) 2, player3);
//        playerList.addPlayer((byte) 3, player4);
//
//        playerList.addPlayersToRound();
//        System.out.println("Size = " + playerList.getPlayingPlayers().size());
//
//        checkPlayerCards();

//        Main.getWindow().close();

    }

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
                    player.checkCards();
                }
                playerHandValue = player.getHandValue();
                if (loop == 0) {
                    System.out.println(player.getDisplayName() + " has = " + playerHandValue);
                    player.printCards();

                    if (highestHandValue < playerHandValue) {
                        highestHandValue = playerHandValue;
                    }
                }
                if (loop == 1) {
                    if (highestHandValue > playerHandValue) {
                        player.setPlaying(false);
                        player.playerFolded();
//                        System.out.println("Highest = " + highestHandValue);
//                        System.out.println("Value = " + playerHandValue);
//                        System.out.println("Hand = " + player.getHandValue());
                        System.out.println(player.getDisplayName() + " Folded");
                        iterator.remove();
                    }
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


    private List<Card> addRandom() {
        List<Card> testList = new LinkedList<>();
        Card card;
        for (byte i = 0; i < 5; i++) {
            card = deck.drawCard();
            testList.add(card);
//            System.out.println("Symbol: " + card.getSymbol() + ", Rank: " + card.getRank());
        }
        return testList;
    }

    private List<Card> addPairs() {
        List<Card> testList = new LinkedList<>();
        Card card;
        card = deck.drawSpecificRank("QUEEN");
        testList.add(card);
        card = deck.drawSpecificRank("JACK");
        testList.add(card);
        card = deck.drawSpecificRank("TWO");
        testList.add(card);
        card = deck.drawSpecificRank("KING");
        testList.add(card);
        card = deck.drawSpecificRank("ACE");
        testList.add(card);
        card = deck.drawSpecificRank("ACE");
        testList.add(card);
        card = deck.drawSpecificRank("ACE");
        testList.add(card);

        return testList;
    }

    private List<Card> addStraight() {
        List<Card> testList = new LinkedList<>();
        Card card;
        card = deck.drawSpecificRank("EIGHT");
        testList.add(card);
        card = deck.drawSpecificRank("NINE");
        testList.add(card);
        card = deck.drawSpecificRank("QUEEN");
        testList.add(card);
        card = deck.drawSpecificRank("TEN");
        testList.add(card);
        card = deck.drawSpecificRank("JACK");
        testList.add(card);
        card = deck.drawSpecificRank("SEVEN");
        testList.add(card);
        card = deck.drawSpecificRank("TEN");
        testList.add(card);

        return testList;
    }

    private List<Card> addFlush() {
        List<Card> testList = new LinkedList<>();
        Card card;
        card = deck.drawSpecificSymbol("HEARTS");
        testList.add(card);
        card = deck.drawSpecificSymbol("HEARTS");
        testList.add(card);
        card = deck.drawSpecificSymbol("HEARTS");
        testList.add(card);
        card = deck.drawSpecificSymbol("HEARTS");
        testList.add(card);
        card = deck.drawSpecificSymbol("HEARTS");
        testList.add(card);
        card = deck.drawSpecificSymbol("HEARTS");
        testList.add(card);
        card = deck.drawSpecificSymbol("HEARTS");
        testList.add(card);

        return testList;
    }

    private List<Card> addStraightFlush() {
        List<Card> testList = new LinkedList<>();
        Card card;
        card = deck.drawExactCard("SPADES", "NINE");
        testList.add(card);
        card = deck.drawExactCard("SPADES", "KING");
        testList.add(card);
        card = deck.drawExactCard("SPADES", "QUEEN");
        testList.add(card);
        card = deck.drawExactCard("SPADES", "JACK");
        testList.add(card);
        card = deck.drawExactCard("CLOVER", "TEN");
        testList.add(card);
        card = deck.drawExactCard("SPADES", "SEVEN");
        testList.add(card);
        card = deck.drawExactCard("SPADES", "ACE");
        testList.add(card);

        return testList;
    }

    private List<Card> addProblematicCards() {
        List<Card> testList = new LinkedList<>();
        Card card;
        card = deck.drawExactCard("SPADES", "FIVE");
        testList.add(card);
        card = deck.drawExactCard("CLOVERS", "QUEEN");
        testList.add(card);
        card = deck.drawExactCard("HEARTS", "TWO");
        testList.add(card);
        card = deck.drawExactCard("SPADES", "FOUR");
        testList.add(card);
        card = deck.drawExactCard("SPADES", "SIX");
        testList.add(card);
//        card = deck.drawExactCard("HEARTS", "QUEEN");
//        testList.add(card);
//        card = deck.drawExactCard("HEARTS", "NINE");
//        testList.add(card);

        return testList;
    }


    private void arrangeObjectsOnScreen() {
        dealerHand.setTranslateX(screenLayoutX + 877);
        dealerHand.setTranslateY(screenLayoutY + 313);
        dealerHand.setViewOrder(0); // Set viewing order in Anchor Pane (make it the upper one).

        dealerStaticHand.setTranslateX(screenLayoutX + 965);
        dealerStaticHand.setTranslateY(screenLayoutY + 317);
        dealerStaticHand.setViewOrder(0); // Set viewing order in Anchor Pane (make it the upper one).

        buttonCheck.setTranslateX(screenLayoutX + 1415);
        buttonCheck.setTranslateY(screenLayoutY + 1014);

        buttonFold.setTranslateX(screenLayoutX + 1591);
        buttonFold.setTranslateY(screenLayoutY + 1014);

        buttonRaise.setTranslateX(screenLayoutX + 1767);
        buttonRaise.setTranslateY(screenLayoutY + 1014);

    }

    private void arrangeSeats() {
        // Seat #1
        seat0.setTranslateX(screenLayoutX + 1478);
        seat0.setTranslateY(screenLayoutY + 402);
        // Seat #2
        seat1.setTranslateX(screenLayoutX + 1478);
        seat1.setTranslateY(screenLayoutY + 728);
        // Seat #3
        seat2.setTranslateX(screenLayoutX + 1036);
        seat2.setTranslateY(screenLayoutY + 865);
        // Seat #4
        seat3.setTranslateX(screenLayoutX + 526);
        seat3.setTranslateY(screenLayoutY + 865);
        // Seat #5
        seat4.setTranslateX(screenLayoutX + 84);
        seat4.setTranslateY(screenLayoutY + 728);
        // Seat #6
        seat5.setTranslateX(screenLayoutX + 84);
        seat5.setTranslateY(screenLayoutY + 402);
    }

    // Set on click event for each seat
    private void setSeatAction() {
        seat0.setOnMouseClicked(e -> {
            takeSeat(seat0);
        });
        seat1.setOnMouseClicked(e -> {
            takeSeat(seat1);
        });
        seat2.setOnMouseClicked(e -> {
            takeSeat(seat2);
        });
        seat3.setOnMouseClicked(e -> {
            takeSeat(seat3);
        });
        seat4.setOnMouseClicked(e -> {
            takeSeat(seat4);
        });
        seat5.setOnMouseClicked(e -> {
            takeSeat(seat5);
        });
    }

    private void takeSeat(ImageView seat) {
        Hand player = new Hand(mapScreen, nameArray());
//        if (playerList.getPlayersAtTable().isEmpty()){
//            player = new Hand(mapScreen, NamePicker.getPlayerName());
//        }else {
//            player = new Hand(mapScreen, nameArray());
//        }
        boolean tookSeat = player.takeSeat(seat);
        if (tookSeat) {
            seat.setDisable(true); // Disable seat on click action.
            seat.setVisible(false); // Hide seat.
            playerList.addPlayer(player.getSeatNumber(), player); // Add player to list.
            pokerGame.updatePlayerList(playerList); // Send updated player list to PokerGame.
        }
    }

    private String nameArray() {
        final byte MAX = 19;
        final byte MIN = 1;
        final byte RANGE = MAX - MIN + 1;
        String[] names = {"John", "Mike", "Bart", "Lois", "Clark", "Bruce", "James",
                "Marco", "Ben", "Pablo", "Mark", "Dor", "Rose", "Marcus", "Paul",
                "Johnny", "Ed", "Thomas", "Barack"};
        byte random = (byte) ((Math.random() * RANGE) + MIN);
        return names[random];
    }

    private void closeProgram() {
        Boolean answer = CloseBox.display();
        if (answer) {
            Stage stage = (Stage) mapScreen.getScene().getWindow();
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
