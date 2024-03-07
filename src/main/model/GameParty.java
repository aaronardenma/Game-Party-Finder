package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a Game Party with a max party size, current number of members,
// game, and list of current members
public class GameParty implements Writable {
    private int maxPartySize;
    private int currentNumOfMembers;
    private final Game game;
    private ArrayList<Person> currentMembers;
    private String status;
    private String gamePartyName;

    // REQUIRES: maxPartySize has an integer greater than 0
    // EFFECTS: maxPartySize on GameParty is set to maxPartySize;
    // currentNumOfMembers is set to 0; game is set to game;
    // currentMembers is set to an empty array list

    public GameParty(Game game, int maxPartySize, String name) {
        this.maxPartySize = maxPartySize;
        this.currentNumOfMembers = 0;
        this.game = game;
        this.currentMembers = new ArrayList<>();
        this.status = "Pending";
        this.gamePartyName = name;

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
    // and reduces currentSize by 1 if currentMembers contains the Person p

    public void deleteMember(Person p) {
        if (currentMembers.contains(p)) {
            this.currentMembers.remove(p);
            this.currentNumOfMembers--;
        }
    }

    // MODIFIES: this
    // EFFECTS: changes this party size to newSize if newSize is bigger or equal than the
    // current Number of members in the Game Party and newSize is smaller or equal to the
    // maximum party members for the Game Party's game

    public void changeTotalSize(int newSize) {
        if (newSize >= this.currentNumOfMembers && newSize <= game.getMaxPartyMembers()) {
            this.maxPartySize = newSize;
        }
    }

    // getter
    public ArrayList<Person> getCurrentMembers() {
        return this.currentMembers;
    }

    // getter
    public int getMaxPartySize() {
        return this.maxPartySize;
    }

    // getter
    public int getCurrentNumOfMembers() {
        return this.currentNumOfMembers;
    }

    // getter
    public String getName() {
        return this.gamePartyName;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", gamePartyName);
        json.put("maxPartySize", maxPartySize);
        json.put("game", game);
        json.put("status", status);
        json.put("currentNumOfMembers", currentNumOfMembers);

        JSONArray currentMembersJson = new JSONArray();
        for (Person p : currentMembers) {
            currentMembersJson.put(p.toJson());
        }

        json.put("currentMembers", currentMembersJson);

        return json;
    }
}
