package model;

import java.util.ArrayList;

public class Game {
    private String name;
    private int numberOfPlayers;
    private ArrayList<Person> listOfPlayers;

    public Game(String name) {
        this.name = name;
        this.numberOfPlayers = 0;
        this.listOfPlayers = new ArrayList<Person>();

    }


}
