package model;

import java.util.ArrayList;

// Represents a Game with a name, list of players, and max party member integer
public class Game {
    private String name;
    private ArrayList<Person> listOfPlayers;
    private int maxPartyMembers;

    // REQUIRES: non-zero length name, and maxPartyMembers integer greater than 0
    // EFFECTS: name on Game is set to name; listOfPlayers is set to an empty array list;
    // maxPartyMembers is set to maxPartyMembers

    public Game(String name, int maxPartyMembers) {
        this.name = name;
        this.listOfPlayers = new ArrayList<>();
        this.maxPartyMembers = maxPartyMembers;

    }

    // getter

    public int getMaxPartyMembers() {
        return this.maxPartyMembers;
    }

    // getter

    public ArrayList<Person> getListOfPlayers() {
        return this.listOfPlayers;
    }

    // getter

    public String getName() {
        return this.name;
    }

}
