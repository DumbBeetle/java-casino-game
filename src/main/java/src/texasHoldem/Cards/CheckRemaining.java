package src.texasHoldem.Cards;

import src.texasHoldem.hands.Hand;
import src.texasHoldem.lists.PlayerList;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CheckRemaining {


    private Iterator getIterator(PlayerList playerList){
        Set<Map.Entry<Byte, Hand>> setOfEntries = playerList.getPlayingPlayers().entrySet();
        Iterator<Map.Entry<Byte, Hand>> iterator = setOfEntries.iterator();
        return iterator;
    }

    // Check highest card value
    public PlayerList checkTieHighCard(PlayerList playerList) {
        byte highCard;
        byte highCardValue = 0;

        Iterator<Map.Entry<Byte, Hand>> iterator = getIterator(playerList);

        for (byte loop = 0; loop < 2; loop++) {
            while (iterator.hasNext()) {
                Map.Entry<Byte, Hand> entry = iterator.next();
                Hand player = entry.getValue();
                highCard = player.getHighestOfFive();
                // Chose which pair to check
                if (loop == 0){
                    if (highCardValue < highCard){
                        highCardValue = highCard;
                    }
                }else {
                    if (highCardValue > highCard) {
                        System.out.println(player.getDisplayName() + " Folded");
                        player.setPlaying(false);
                        player.playerFolded();
                        iterator.remove();
                    }
                }
            }
            iterator = getIterator(playerList);
        }
        return playerList;
    }

    // Check card value of pair.
    public PlayerList checkTiePair(PlayerList playerList, boolean checkPair1) {
        byte pair;
        byte highestPairValue = 0;
        Iterator<Map.Entry<Byte, Hand>> iterator = getIterator(playerList);

        for (byte loop = 0; loop < 2; loop++) {
            while (iterator.hasNext()) {
                Map.Entry<Byte, Hand> entry = iterator.next();
                Hand player = entry.getValue();

                // Chose which pair to check
                if (checkPair1) {
                    pair = player.getPair1Value();
                } else {
                    pair = player.getPair2Value();
                }

                // If player pair is lower remove him from play.
                if (loop == 0){
                    if (highestPairValue < pair){
                        highestPairValue = pair;
                    }
                }else {

                    if (highestPairValue > pair) {
                        System.out.println(player.getDisplayName() + " Folded");
                        player.setPlaying(false);
                        player.playerFolded();
                        iterator.remove();
                    }
                }
            }
            iterator = getIterator(playerList);
        }
        return playerList;
    }

    // TODO: need to find way to check player 1 without adding to index.
    public PlayerList checkTieFlush(PlayerList playerList) {
        byte index = 0;
        byte cardValue = 0;
        byte playerCard;

        Iterator<Map.Entry<Byte, Hand>> iterator = getIterator(playerList);
        System.out.println("Checking Flush");

        while (index < 5) {
            while (iterator.hasNext()) {
                Map.Entry<Byte, Hand> entry = iterator.next();
                Hand player = entry.getValue();
                playerCard = player.getHandCards().get(index).getValue();
                if (cardValue > playerCard) {
                    System.out.println(player.getDisplayName() + " Folded");
                    player.setPlaying(false);
                    player.playerFolded();
                    iterator.remove();
                } else {
                    cardValue = playerCard;
                }
            }
            cardValue = 0;
            iterator = getIterator(playerList);
            index++;
        }
        return playerList;
    }

    // check high card
    // check pair value
    public void checkRemainingPlayers(PlayerList playerList, byte highestHandValue) {
        System.out.println("Highest = " + highestHandValue);
        switch (highestHandValue) {
            case 1: // High card.
            case 5: // Straight.
            case 9: // Straight flush.
                checkTiePair(playerList, true);
                break;
            case 2: // Pair
            case 4: // Pair of three.
            case 8: // Pair of four.
            case 7: // Full house.
                checkTiePair(playerList, true);
                if (playerList.getPlayingPlayers().size() != 1) {
                    checkTiePair(playerList, false);
                }
                break;
            case 3: // 2 Pairs.
                checkTiePair(playerList, true);
                if (playerList.getPlayingPlayers().size() != 1) {
                    checkTiePair(playerList, false);
                }
                if (playerList.getPlayingPlayers().size() != 1) {
                    checkTieHighCard(playerList);
                }
                break;
            case 6: // Flush.
                checkTieFlush(playerList);
                break;
            case 10: // Royal flush.
                for (Map.Entry<Byte, Hand> player : playerList.getPlayingPlayers().entrySet()) {
                    player.getValue().playerTie();
                }
        }
        System.out.println("Done checking");
    }
}
