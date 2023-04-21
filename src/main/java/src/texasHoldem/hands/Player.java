package src.texasHoldem.hands;

import javafx.animation.SequentialTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import src.texasHoldem.Cards.Card;

public class Player extends HandOld {

    // TODO: after finishing the essential remove none essential methods and
    //  change to private what doesn't need to be public

    public Player(AnchorPane mapScreen, String holderName) {
        super(mapScreen, holderName);
    }

    @Override
    public Pane getHolderDisplay() {
        return super.getHolderDisplay();
    }

    @Override
    public double getHolderLayoutX() {
        return super.getHolderLayoutX();
    }

    @Override
    public double getHolderLayoutY() {
        return super.getHolderLayoutY();
    }

    @Override
    public void setHolderDisplay() {
        super.setHolderDisplay();
    }

    @Override
    public boolean takeSeat(ImageView seat) {
        return super.takeSeat(seat);
    }

    @Override
    public void takeCard(Card card) {
        super.takeCard(card);
    }

    @Override
    public byte getHandCardsNumber() {
        return super.getHandCardsNumber();
    }

    @Override
    public ImageView getCard1() {
        return super.getCard1();
    }

    @Override
    public ImageView getCard2() {
        return super.getCard2();
    }

    @Override
    public void clearHand() {
        super.clearHand();
    }

    @Override
    public byte getSeatNumber() {
        return super.getSeatNumber();
    }

    @Override
    public boolean isPlaying() {
        return super.isPlaying();
    }

    @Override
    public void setPlaying(boolean playing) {
        super.setPlaying(playing);
    }
}
