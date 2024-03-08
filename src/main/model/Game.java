package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a Game with a name, list of players, and max party member integer
public class Game implements Writable {
    private String name;
    private int maxPartyMembers;

    // REQUIRES: non-zero length name, and maxPartyMembers integer greater than 0
    // EFFECTS: name on Game is set to name; listOfPlayers is set to an empty array list;
    // maxPartyMembers is set to maxPartyMembers

    public Game(String name, int maxPartyMembers) {
        this.name = name;
        this.maxPartyMembers = maxPartyMembers;

    }

    // getter

    public int getMaxPartyMembers() {
        return this.maxPartyMembers;
    }

    // getter

    public String getName() {
        return this.name;
    }

    // EFFECTS: return JSON Object with this.name, and maxPartyMembers as JSON Objects
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("maxPartyMembers", maxPartyMembers);

        return json;
    }


}
