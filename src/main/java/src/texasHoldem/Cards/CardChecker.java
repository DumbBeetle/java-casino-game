package src.texasHoldem.Cards;

import java.util.*;
import java.util.stream.Collectors;

public class CardChecker {

    private byte playerValue; // Based on cards values (above^).
    private byte pair1; // Highest card from pair.
    private byte pair1Value;
    private byte pair2; // lowest pair (In case of 2 pairs or full house).
    private byte pair2Value;
    private byte highestOfFive; // Highest card from non pair.

    public byte getPlayerValue() {
        return playerValue;
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

    //TODO:
    // Problem with checking values after pair check cause of clash with NO, ONE, TWO pairs values.
    public List<Card> checkList(List<Card> list) {
        final byte ZERO = 0;
        // Add elements of list.
        List<Card> fullCardList = new LinkedList<>(list);
        // Sort list by Symbol.
        fullCardList.sort(Comparator.comparing(Card::getSymbol));

        // Check if Flush.
        boolean isFlush = (checkFlush(fullCardList));

        if (!isFlush) {
            // Clear fullCardList.
            fullCardList.clear();
            // Add elements of list.
            fullCardList.addAll(list);
        }
        // Sort list by Rank, low to high.
        fullCardList.sort(Comparator.comparing(Card::getValue));
        // Reverse order, high to low.
        Collections.reverse(fullCardList);

        // Check if Straight.
        boolean isStraight = checkStraight(fullCardList);

        if (isStraight && isFlush) {
            // Check if last card is an Ace, if it is has to be Royal Flush.
            if (fullCardList.get(ZERO).getRank().toString().equals("ACE")) {
                // (Only 10 J Q K A with the same symbol).
                final byte ROYAL_FLUSH = 10;
                playerValue = ROYAL_FLUSH;
                return fullCardList;
            }
            // If not then it's a Straight Flush.
            // (2 3 4 5 6 with the same symbol).
            final byte STRAIGHT_FLUSH = 9;
            playerValue = STRAIGHT_FLUSH;
            return fullCardList;
        }
        if (isStraight || isFlush) {
            return fullCardList;
        }
        // Clear fullCardList.
        fullCardList.clear();
        // Add elements of list.
        fullCardList.addAll(list);
        // Sort list by Rank, low to high.
        fullCardList.sort(Comparator.comparing(Card::getValue));
        // Reverse order, high to low.
        Collections.reverse(fullCardList);

        checkPairs(fullCardList);

        final byte ONE_PAIR = 2; // (2 2).
        final byte TWO_PAIR = 3; // (2 2 , 3 3).
        final byte THREE_OF_A_KIND = 4; // (2 2 2).
        final byte FULL_HOUSE = 7; // (2 2 2 , 3 3).
        final byte FOUR_OF_A_KIND = 8; // (2 2 2 2).

        if (pair1 == FOUR_OF_A_KIND) {
            playerValue = FULL_HOUSE;
            return fullCardList;
        } else if (pair1 == THREE_OF_A_KIND - 1 && pair2 == ONE_PAIR) {
            playerValue = FULL_HOUSE;
            return fullCardList;
        } else if (pair1 == THREE_OF_A_KIND - 1) {
            playerValue = THREE_OF_A_KIND;
            return fullCardList;
        } else if (pair1 == ONE_PAIR && pair2 == ONE_PAIR) {
            playerValue = TWO_PAIR;
            return fullCardList;
        } else if (pair1 == ONE_PAIR) {
            playerValue = ONE_PAIR;
            return fullCardList;
        }
        // Cards Values
        // Check for high card.
        final byte NO_PAIRS = 1;
        playerValue = NO_PAIRS;
        return fullCardList;
    }

    public void printSymbolRank(List<Card> fullCardList) {
        for (byte i = 0; i < fullCardList.size(); i++) {
            System.out.println("Symbol: " + fullCardList.get(i).getSymbol() + ", Rank: " +
                    fullCardList.get(i).getRank() + "(" + fullCardList.get(i).getValue() + "). ");
        }
        System.out.println();
    }

    // Check for Flush.
    private boolean checkFlush(List<Card> fullCardList) {
        final byte MINIMUM = 5;
        // Group cards with same symbol and count their amount.
        Map<Card.Symbol, List<Card>> group = fullCardList.stream().collect(Collectors.groupingBy(Card::getSymbol));
        // Iterator map.
        for (Card.Symbol symbol : group.keySet()) {
            // If count >= 5.
            if (group.get(symbol).size() >= MINIMUM) {
                // Clear list.
                fullCardList.clear();
                // Add group to list.
                fullCardList.addAll(group.get(symbol));
                // Hand value.
                // any five cards with the same symbol.
                final byte FLUSH = 6;
                playerValue = FLUSH;
                return true;
            }
        }
        return false;
    }


    // All ready ordered from high to low.
    // Should run in loop and count till last card.
    // If counted five means straight, remove non related cards.
    // If founded duplicate remove it and continue counting from there.
    // If didn't counted 5 means not straight.

    // TODO: problem with checking sometimes in case of straight in table won't count it and check for pair in player hand.
    private boolean checkStraight(List<Card> fullCardList) {
        final byte ZERO = 0;
        final byte ONE = 1;
        final byte TWO = 2;
        final byte FOUR = 4;
        final byte FIVE = 5;
        byte counter = ZERO;

        while (fullCardList.size() > FIVE) {
            // If in decreasing order (10, 9, 8, 7, 6).
            // Counter =                0, 1, 2, 3, 4.
            // Check if (Index = N) equals (Index+1 = N+1).
            if (fullCardList.get(counter).getValue() == fullCardList.get(counter + ONE).getValue() + ONE) {
                counter++;
                if (counter == FOUR) {
                    while (fullCardList.size() > FIVE) {
                        fullCardList.remove(fullCardList.size() - ONE);
                    }
                    final byte STRAIGHT = 5;
                    playerValue = STRAIGHT;
                    // High card in case of tie.
                    highestOfFive = fullCardList.get(ZERO).getValue();
                    return true;
                }
            }
            // In case of duplicates remove next card.
            else if (fullCardList.get(counter).getValue() == fullCardList.get(counter + ONE).getValue()) {
                fullCardList.remove(counter + ONE);
            }
            // If cards not related to straight remove by counter.
            else {
                do {
                    fullCardList.remove(counter);
                    counter--;
                } while (counter > ZERO);
                counter = ZERO;
            }
        }
        return false;
    }

    // TODO:
    //  Try using poll first instead of checking if pair was replaced.
    //  Need to find a way to order map by values instead of using a loop.
    private void checkPairs(List<Card> fullCardList) {
        final byte ZERO = 0;
        final byte ONE = 1;
        final byte LIMIT = 5;
        // Count duplicates.
        byte count = ZERO;
        // Look for key by string char at 0;.
        byte lookFor;
        // Add keys to string.
        StringBuilder string = new StringBuilder();



        // Group cards with same rank and count their amount, order by value from low to high.
        Map<Byte, List<Card>> group = fullCardList.stream().collect(Collectors.groupingBy(Card::getValue));

        for (byte value : group.keySet()) {
            string.append(group.get(value).size());
        }
        // String of sorted values from high to low, used with lookFor.
        string = sortString(string);

        // New treeMap with reverse order of group, order by value from high to low.
        TreeMap<Byte, List<Card>> treeMap = new TreeMap<>(group);
        fullCardList.clear();

        // Flag to true if took pair1 value.
        boolean tookPair1 = false;
        // Flag to true if took pair2 value.
        boolean tookPair2 = false;
        // Flag to true if took high card value.
        boolean tookHighCard = false;

        while (count < LIMIT) {
            // Take first char at zero, and look for it's first occurrence in tree map.
            lookFor = (byte) Character.getNumericValue(string.charAt(ZERO));
            // Iterate tree map.
            for (byte value : treeMap.descendingKeySet()) {
                // If key matches lookFor.
                if (lookFor == (byte) treeMap.get(value).size()) {
                    // Add group to list.
                    fullCardList.addAll(treeMap.get(value));
                    // Take first occurrence of key.
                    if (!tookPair1) {
                        pair1 = (byte) treeMap.get(value).size();
                        pair1Value = value;
                        count += pair1;
                        tookPair1 = true;
                    }
                    // Take second occurrence of key.
                    else if (!tookPair2) {
                        pair2 = (byte) treeMap.get(value).size();
                        pair2Value = value;
                        count += pair2;
                        tookPair2 = true;
                        // Check if count is more then 5.
                        if (count > LIMIT) {
                            byte lowerBy = (byte) (count - LIMIT);
                            // Lowers pair2 value.
                            pair2 -= lowerBy;
                            // Remove cards from list until size = 5.
                            for (byte loop = ZERO; loop < lowerBy; loop++) {
                                // Remove last card from list.
                                fullCardList.remove(fullCardList.size() - ONE);
                            }
                        }
                    }
                    // If already set new values for pair1 and pair2.
                    else {
                        // Take first key in tree map.
                        if (!tookHighCard) {
                            // Poll first value in map will be the highest value that doesn't belong in a pair.
                            highestOfFive = treeMap.descendingKeySet().pollFirst();
                            tookHighCard = true;
                        }
                        count += ONE;
                    }

                    // Remove key after.
                    treeMap.remove(value);
                    // Remove string at 0.
                    string = new StringBuilder(string.substring(ONE));
                    break;
                }
            }
        }
    }

    // Get stringBuilder make a string out of it.
    // Add that to char array and sort from high to low.
    // Re add back to stringBuilder and return.
    private StringBuilder sortString(StringBuilder counter) {
        final byte ZERO = 0;
        final byte ONE = 1;

        // Create string from counter.
        String str = new String(counter);
        // Add to char array.
        char[] chars = str.toCharArray();
        // Sort array by values from high to low.
        Arrays.sort(chars);
        // Override counter.
        counter = new StringBuilder();
        // Add values to counter from array.
        for (byte i = (byte) (chars.length - ONE); i >= ZERO; i--) {
            counter.append(chars[i]);
        }
        return counter;
    }

    // Return values in array.
    public byte[] sendValues() {
        return new byte[]{playerValue, pair1, pair1Value, pair2, pair2Value, highestOfFive};
    }

    // Reset values.
    public void clearValues() {
        final byte ZERO = 0;
        playerValue = ZERO; // Based on Cards values (above^).
        pair1 = ZERO; // Highest card from pair.
        pair1Value = ZERO;
        pair2 = ZERO; // lowest pair (In case of 2 pairs or full house).
        pair2Value = ZERO;
        highestOfFive = ZERO; // Highest card from non pair.
    }


}
