package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import exceptions.PersonDoesNotContainRoleException;

import java.util.ArrayList;

// Represents a Game Party with a max party size, current number of members,
// game, and list of current members
public class GameParty implements Writable {
    private int maxPartySize;
    private final Game game;
    private ArrayList<Person> currentMembers;
    private final String gamePartyName;

    // REQUIRES: maxPartySize has an integer greater than 0
    // EFFECTS: maxPartySize on GameParty is set to maxPartySize;
    // currentNumOfMembers is set to 0; game is set to game;
    // currentMembers is set to an empty array list
    public GameParty(Game game, int maxPartySize, String name) {
        this.maxPartySize = maxPartySize;
        this.game = game;
        this.currentMembers = new ArrayList<>();
        this.gamePartyName = name;

    }

    // MODIFIES: this
    // EFFECTS: Add person to currentMembers list && increase currentSize by 1
    // if their roles contain the game of the GameParty
    public void addMember(Person p) throws PersonDoesNotContainRoleException {
        if (p.getRoles().contains(game) && !currentMembers.contains(p)) {
            this.currentMembers.add(p);
        } else if (!p.getRoles().contains(game)) {
            throw new PersonDoesNotContainRoleException(p, game);
        }
    }

    // REQUIRES: currentMembers.contains(p)
    // MODIFIES: this
    // EFFECTS: Removes person from currentMembers list
    // and reduces currentSize by 1 if currentMembers contains the Person p
    public void removeMember(Person p) {
        if (currentMembers.contains(p)) {
            this.currentMembers.remove(p);
        }
    }

    // MODIFIES: this
    // EFFECTS: changes this party size to newSize if newSize is bigger or equal than the
    // current Number of members in the Game Party and newSize is smaller or equal to the
    // maximum party members for the Game Party's game
    public void changeTotalSize(int newSize) {
        if (newSize >= getCurrentNumOfMembers() && newSize <= game.getMaxPartyMembers()) {
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
        return currentMembers.size();
    }

    // getter
    public String getName() {
        return this.gamePartyName;
    }

    // getter
    public Game getGame() {
        return this.game;
    }

    // setter
    public void setCurrentMembers(ArrayList<Person> members) {
        this.currentMembers = members;
    }


    // EFFECTS: return JSON Object that captures gamePartyName, maxPartySize, game, and currentMembers as JSON Array
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", gamePartyName);
        json.put("maxPartySize", maxPartySize);
        json.put("game", game.toJson());
        json.put("currentMembers", currentMembersToJson());

        return json;
    }


    // EFFECTS: returns Person as a JSON Array
    public JSONArray currentMembersToJson() {
        JSONArray currentMembersJson = new JSONArray();
        for (Person p : currentMembers) {
            currentMembersJson.put(p.toJson());
        }
        return currentMembersJson;
    }

}
