package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a Person having a name, number of roles, list of roles, and list of Game Parties
public class Person implements Writable {
    private final String name;
    private ArrayList<Game> roles;
//    private ArrayList<GameParty> gameParties;

    // REQUIRES: name has a non-zero length
    // EFFECTS: name on Person is set to name; numOfRoles is set to 0;
    // roles and GameParties are empty ArrayLists

    public Person(String name) {
        this.name = name;
        this.roles = new ArrayList<>();
//        this.gameParties = new ArrayList<>();
    }

    // getter

    public String getName() {
        return this.name;
    }

    // getter

    public int getNumOfRoles() {
        return roles.size();
    }

    // getter

//    public ArrayList<GameParty> getGameParties() {
//        return this.gameParties;
//    }

    // MODIFIES: this, and GameParty
    // EFFECTS: Adds this GameParty to list of gameParties and adds Person to GameParty currentMembers
    // gameParties does not contain the GameParty

//    public void addToGameParty(GameParty gp) {
//        if (!gameParties.contains(gp)) {
//            gp.addMember(this);
//            gameParties.add(gp);
//        }
//    }

    // MODIFIES: this
    // EFFECTS: Add Game to list of roles and increment numOfRoles by 1 if list of roles
    // does not contain the Game

    public void addRole(Game g) {
        if (!roles.contains(g)) {
            this.roles.add(g);
//            g.addPersonToGame(this);
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes Game from list of roles and reduces numOfRoles by 1 if list of roles
    // contains the Game

    public void deleteRole(Game g) {
        if (roles.contains(g)) {
            this.roles.remove(g);
//            g.removePersonFromGame(this);
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

    // setter

//    public void setGameParties(ArrayList<GameParty> newParties) {
//        this.gameParties = newParties;
//    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("roles", rolesToJson());
//        json.put("game parties", gamePartiesToJson());

        return json;
    }

    public JSONArray rolesToJson() {
        JSONArray rolesJson = new JSONArray();
        for (Game g : roles) {
            rolesJson.put(g.toJson());
        }
        return rolesJson;
    }

//    public JSONArray gamePartiesToJson() {
//        JSONArray gamePartiesJson = new JSONArray();
//        for (GameParty gp : gameParties) {
//            gamePartiesJson.put(gp.toJson());
//        }
//        return gamePartiesJson;
//    }

}
