package src.texasHoldem.Cards;

import java.util.Collections;
import java.util.LinkedList;

public class Deck {
    private LinkedList<Card> cards = new LinkedList<>(); //The deck of the cards
    private final byte ZERO = 0;

    public Deck() {
        refillDeck();
    }
    // Clear Deck and refill it.
    public final void refillDeck() {
        cards.clear();
        for (Card.Symbol symbol : Card.Symbol.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(symbol, rank));
            }
        }
        Collections.shuffle(cards);
    }
    // Take first card from deck.
    public Card drawCard() {
        return cards.pollFirst();
    }

    // Look at the first card from in the deck.
    public Card peekAtCard() {
        return cards.peekFirst();
    }

    // Take out a specific card based on symbol.
    public Card drawSpecificSymbol(String symbol){
        for (byte i = 0; i < cards.size(); i++){
            // If card was found take it and return.
            if (cards.peekFirst().getSymbol().toString() == symbol){
                return cards.pollFirst();
            }
            // If card was not found readd card to the bottom of the list.
            cards.add(cards.pollFirst());
        }
        // In case card was not return first card.
        return cards.pollFirst();
    }

    // Take out a specific card based on Rank.
    public Card drawSpecificRank(String rank){
        for (byte i = 0; i < cards.size(); i++){
            // If card was found take it and return.
            if (cards.peekFirst().getRank().toString() == rank){
                return cards.pollFirst();
            }
            // If card was not found readd card to the bottom of the list.
            cards.add(cards.pollFirst());
        }
        // In case card was not return first card.
        return cards.pollFirst();
    }

    // Take out an exact card based on Symbol and Rank.
    public Card drawExactCard(String symbol, String rank){
        for (byte i = 0; i < cards.size(); i++){
            // If card was found take it and return.
            if (cards.peekFirst().getSymbol().toString() == symbol){
                if (cards.peekFirst().getRank().toString() == rank){
                    return cards.pollFirst();
                }
            }
            // If card was not found readd card to the bottom of the list.
            cards.add(cards.pollFirst());
        }
        // In case card was not return first card.
        return cards.pollFirst();
    }

    public byte getDeckSize() {
        return (byte) cards.size();
    }

    public LinkedList<Card> getCardsList(){
        return cards;
    }
}
