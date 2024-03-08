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

//    public ArrayList<Person> getListOfPlayers() {
//        return this.listOfPlayers;
//    }

    // getter

    public String getName() {
        return this.name;
    }

    // MODIFIES: this
    // EFFECTS: add p to listOfPlayers if listOfPlayers does not contain p
//    public void addPersonToGame(Person p) {
//        if (!listOfPlayers.contains(p)) {
//            this.listOfPlayers.add(p);
//        }
//    }

    // REQUIRES: listOfPlayers.contains(p)
    // MODIFIES: this
    // EFFECTS: remove p from listOfPlayers if listOfPlayers contains p
//    public void removePersonFromGame(Person p) {
//        if (listOfPlayers.contains(p)) {
//            this.listOfPlayers.remove(p);
//        }
//    }

//    public void setListOfPlayers(ArrayList<Person> array) {
//        this.listOfPlayers = array;
//    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("maxPartyMembers", maxPartyMembers);
//        json.put("listOfPlayers", playersToJson());

        return json;
    }

//    public JSONArray playersToJson() {
//        JSONArray listOfPlayersJson = new JSONArray();
//        for (Person p : listOfPlayers) {
//            listOfPlayersJson.put(p.toJson());
//        }
//        return listOfPlayersJson;
//    }

}
