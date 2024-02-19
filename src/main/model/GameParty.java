package model;

import java.util.ArrayList;

public class GameParty {
    private int totalSize;
    private int currentNumOfMembers;
    private Game game;
    private ArrayList<Person> currentMembers;

    public GameParty(int totalSize, Game game) {
        this.totalSize = totalSize;
        this.currentNumOfMembers = 0;
        this.game = game;
        this.currentMembers = new ArrayList<Person>();

    }

    // MODIFIES: this
    // EFFECTS: Add person to currentMembers list && increase currentSize by 1
    // if their roles contain the game of the GameParty

    public void addMember(Person p) {
        if (p.getRoles().contains(game) && !currentMembers.contains(p)) {
            this.currentMembers.add(p);
            this.currentNumOfMembers++;
        }
    }

    // REQUIRES: currentMembers.contains(p)
    // MODIFIES: this
    // EFFECTS: Removes person from currentMembers list
    // and reduces currentSize by 1

    public void deleteMember(Person p) {
        if (currentMembers.contains(p)) {
            this.currentMembers.remove(p);
            this.currentNumOfMembers--;
        }
    }

    // REQUIRES: newSize must be bigger or equal to currentSize
    // MODIFIES: this
    // EFFECTS: changes this party size to newSize

    public void changeTotalSize(int newSize) {
        if (newSize >= this.currentNumOfMembers && newSize <= game.getMaxPartyMembers()) {
            this.totalSize = newSize;
        }
    }

    public ArrayList<Person> getCurrentMembers() {
        return this.currentMembers;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public Game getGame() {
        return this.game;
    }

    public int getCurrentNumOfMembers() {
        return this.currentNumOfMembers;
    }

}
