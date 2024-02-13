package model;

import java.util.ArrayList;

public class GameParty {
    private int size;
    private int currentSize;
    private Game game;
    private ArrayList<Person> currentMembers;
//    private ArrayList<GameParty> gameParties;

    public GameParty(int size, Game game) {
        this.size = size;
        this.currentSize = 1;
        this.game = game;
        this.currentMembers = new ArrayList<Person>();
    }

    // MODIFIES: this
    // EFFECTS: Add person to currentMembers list && increase currentSize by 1
    // if their roles contain the game of the GameParty

    public void addMember(Person p) {
        if (p.getRoles().contains(game)) {
            this.currentMembers.add(p);
            this.currentSize++;
        }
    }

    // REQUIRES: currentMembers.contains(p)
    // MODIFIES: this
    // EFFECTS: Removes person from currentMembers list
    // and reduces currentSize by 1

    public void deleteMember(Person p) {
        this.currentMembers.remove(p);
        this.currentSize--;
    }

    // REQUIRES: newSize must be bigger or equal to currentSize
    // MODIFIES: this
    // EFFECTS: changes this party size to newSize

    public void reduceSize(int newSize) {
        this.size = newSize;
    }

}
