package model;

import java.util.ArrayList;

public class Game {
    private String name;
    private ArrayList<Person> listOfPlayers;
    private int maxPartyMembers;

    public Game(String name, int maxPartyMembers) {
        this.name = name;
        this.listOfPlayers = new ArrayList<Person>();
        this.maxPartyMembers = maxPartyMembers;

    }

    public int getMaxPartyMembers() {
        return this.maxPartyMembers;
    }

    public ArrayList<Person> getListOfPlayers() {
        return this.listOfPlayers;
    }
}
