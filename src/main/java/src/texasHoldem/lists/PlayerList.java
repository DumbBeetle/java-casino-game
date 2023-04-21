package src.texasHoldem.lists;

import src.texasHoldem.hands.Hand;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PlayerList {
    private Map<Byte, Hand> playersAtTable; // List players sitting in the table.
    private Map<Byte, Hand> playingPlayers; // List players in round.

    public PlayerList() {
        playersAtTable = new HashMap<>();
        playingPlayers = new HashMap<>();
    }

    // Players sitting in table.

    // Add Player to table.
    public void addPlayer(byte index, Hand player){
        playersAtTable.put(index, player);
    }
    // Remove Player from table.
    public void removePlayer(Hand player){
        playersAtTable.remove(player);
    }
    // Get specific Player.
    public Hand getPlayerBySeat(byte seat){
        return playersAtTable.get(seat);
    }
    // Get table list.
    public HashMap<Byte, Hand> getPlayersAtTable() {
        return (HashMap<Byte, Hand>) playersAtTable;
    }

    // Players in round.
    // Add players to list from table list.
    public void addPlayersToRound(){
        playingPlayers.clear();
        playingPlayers.putAll(playersAtTable);
    }
    public Hand getFirstKey(){
        TreeMap<Byte, Hand> treeMap = new TreeMap<>();
        treeMap.putAll(playersAtTable);
        return treeMap.firstEntry().getValue();
    }
    // Remove Player from round.
    public void removePlayerFromRound(Hand player){
        playingPlayers.remove(player);
    }
    public void removePlayerBySeat(byte seat){
        playingPlayers.remove(seat);
    }
    // Get list.
    public HashMap<Byte, Hand> getPlayingPlayers() {
        return (HashMap<Byte, Hand>) playingPlayers;
    }
    // Clear round list
    public void clearPlayersInRound(){
        playingPlayers.clear();
    }
}
