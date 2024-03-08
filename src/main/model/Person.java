package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.round;

// Represents a Person having a name, number of roles, list of roles, and list of Game Parties
public class Person implements Writable {
    private final String name;
    private ArrayList<Game> roles;
    private HashMap<String, ArrayList<Float>> gameStats;

    // REQUIRES: name has a non-zero length
    // EFFECTS: name on Person is set to name; numOfRoles is set to 0;
    // roles and GameParties are empty ArrayLists

    public Person(String name) {
        this.name = name;
        this.roles = new ArrayList<>();
        this.gameStats = new HashMap<>();
    }

    // getter

    public String getName() {
        return this.name;
    }

    // getter

    public int getNumOfRoles() {
        return roles.size();
    }


    // MODIFIES: this
    // EFFECTS: Add Game to list of roles and increment numOfRoles by 1 if list of roles
    // does not contain the Game

    public void addRole(Game g) {
        if (!roles.contains(g)) {
            this.roles.add(g);
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes Game from list of roles if list of roles contains the Game

    public void deleteRole(Game g) {
        if (roles.contains(g)) {
            this.roles.remove(g);
        }

    }

    // getter

    public ArrayList<Game> getRoles() {
        return this.roles;
    }

    // setter

    public void setRoles(ArrayList<Game> newRoles) {
        this.roles = newRoles;
    }

    // getter

    public HashMap<String, ArrayList<Float>> getGameStats() {
        return this.gameStats;
    }

    public void updateWinRate(GameParty gameParty, float numOfWins, float numGamesPlayed) {
        ArrayList<Person> members = new ArrayList<>();
        gameParty.getCurrentMembers().forEach((p) -> members.add(p));
        members.remove(this);

        for (Person p : members) {
            ArrayList<Float> newStats = new ArrayList<>();
            if (getGameStats().containsKey(p.getName())) {
                ArrayList<Float> stats = gameStats.get(p.getName());
                float newNumOfWins = stats.get(1) + numOfWins;
                float newNumGamesPlayed = stats.get(2) + numGamesPlayed;
                float newWinRate = round((newNumOfWins / newNumGamesPlayed) * 100);
                this.gameStats.remove(p.getName());
                newStats.add(newWinRate);
                newStats.add(newNumOfWins);
                newStats.add(newNumGamesPlayed);
                this.gameStats.put(p.getName(), newStats);
            } else {
                float winRate = round((numOfWins / numGamesPlayed) * 100);
                newStats.add(winRate);
                newStats.add(numOfWins);
                newStats.add(numGamesPlayed);
            }
            this.gameStats.put(p.getName(), newStats);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("roles", rolesToJson());
        json.put("game stats", gameStatsToJson());

        return json;
    }

    public JSONArray rolesToJson() {
        JSONArray rolesJson = new JSONArray();
        for (Game g : roles) {
            rolesJson.put(g.toJson());
        }
        return rolesJson;
    }

    public JSONObject gameStatsToJson() {
        JSONObject jsonMap = new JSONObject();
        for (Map.Entry<String, ArrayList<Float>> entry : gameStats.entrySet()) {
            String key = entry.getKey();
            ArrayList<Float> value = entry.getValue();
            JSONArray jsonArray = new JSONArray(value);
            jsonMap.put(key, jsonArray);
        }
        return jsonMap;
    }

    // setter
    public void setGameStats(HashMap<String, ArrayList<Float>> hashMap) {
        this.gameStats = hashMap;
    }
}
