package src.texasHoldem.Cards;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Comparator;

public class Card implements Comparable<Card> {



    // Card Symbol
    enum Symbol {
        CLOVERS, DIAMONDS, HEARTS, SPADES;
    }
    // Card Values
    enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
        SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);
        final byte VALUE;

        //Constructor for adding value
        Rank(int value) {
            VALUE = (byte) value;
        }

    }

    private final Symbol CARD_SYMBOL;
    private final Rank CARD_RANK;

    public Card(Symbol symbol, Rank rank) {
        this.CARD_SYMBOL = symbol;
        this.CARD_RANK = rank;
    }

    public Symbol getSymbol() {
        return CARD_SYMBOL;
    }

    public Rank getRank() {
        return CARD_RANK;
    }

    public byte getValue() {
        return CARD_RANK.VALUE;
    }

    public Image getCardImage(Card card){
        Image image = new Image("images/Cards/" + card.getSymbol() + "/" + card.getRank() + ".png");
        return image;
    }
    public ImageView getCardImageView(Card card){
        ImageView image = new ImageView("images/Cards/" + card.getSymbol() + "/" + card.getRank() + ".png");
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Card)) return false;
        Card r = (Card) o;
        return CARD_RANK.equals(r.getRank());
    }

    @Override
    public int compareTo(Card o) {
        Card r = (Card) o;
        if ( CARD_RANK.equals(r.getRank())){
            return 1;
        }
        return 0;
    }

}
