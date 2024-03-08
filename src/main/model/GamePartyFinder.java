package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a GamePartyFinder with a list of people, games, and game parties
public class GamePartyFinder implements Writable {
    private ArrayList<Person> people;
    private ArrayList<Game> games;
    private ArrayList<GameParty> gameParties;

    public GamePartyFinder() {
        this.people = new ArrayList<>();
        this.games = new ArrayList<>();
        this.gameParties = new ArrayList<>();
    }

    public void addPerson(Person person) {
//        Person newPerson = new Person(name);
        if (!getPeople().contains(person)) {
            people.add(person);
        }
    }

    public void addGame(Game game) {
        ArrayList<String> gameNames = new ArrayList<>();
        games.forEach((g) -> gameNames.add(g.getName().toLowerCase()));

        if (!gameNames.contains(game.getName().toLowerCase())) {
//            Game newGame = new Game(name, maxPartyMembers);
            games.add(game);
        }

    }

//    public void createGameParty(Game game, int maxPartySize, String partyName) {
//        GameParty newGameParty = new GameParty(game, maxPartySize, partyName);
//        gameParties.add(newGameParty);
//    }

    public void addGameParty(GameParty gameParty) {
        ArrayList<String> partyNames = new ArrayList<>();
        gameParties.forEach((gp) -> partyNames.add(gp.getName().toLowerCase()));
//        GameParty newGameParty = new GameParty(game, maxPartySize, partyName);
        if (!partyNames.contains(gameParty.getName().toLowerCase())) {
            gameParties.add(gameParty);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds person to gameParty
    public void addPersonToGameParty(Person person, GameParty gameParty) {
        gameParty.addMember(person);
    }

    // EFFECTS: add game to list of roles a person has
    public void addRoleToPerson(Person person, Game game) {
        person.addRole(game);
    }

    public void deleteRoleFromPerson(Person person, Game game) {
        person.deleteRole(game);
    }

    public ArrayList<Game> getRolesFromPerson(Person person) {
        return person.getRoles();
    }

    public void changePartySize(GameParty gameParty, int newSize) {
        gameParty.changeTotalSize(newSize);
    }

    public ArrayList<GameParty> getGameParties() {
        return this.gameParties;
    }

    public ArrayList<String> getGamePartyNames() {
        ArrayList<String> gamePartyNames = new ArrayList<>();
        gameParties.forEach((gp) -> gamePartyNames.add(gp.getName()));
        return gamePartyNames;
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public ArrayList<String> getGameNames() {
        ArrayList<String> gameNames = new ArrayList<>();
        games.forEach((g) -> gameNames.add(g.getName()));
        return gameNames;
    }

    public ArrayList<Person> getPeople() {
        return this.people;
    }

    public ArrayList<String> getPeopleNames() {
        ArrayList<String> peopleNames = new ArrayList<>();
        people.forEach((p) -> peopleNames.add(p.getName()));
        return peopleNames;
    }

    public void endSession(GameParty gameParty, float numOfWins, float numGamesPlayed) {
        ArrayList<Person> members = gameParty.getCurrentMembers();
        for (int i = 0; i < members.size(); i++) {
            Person p = members.get(i);
            p.updateWinRate(gameParty, numOfWins, numGamesPlayed);
        }
        gameParties.remove(gameParty);
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("people", peopleToJson());
        json.put("games", gamesToJson());
        json.put("game parties", partiesToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray peopleToJson() {
        JSONArray peopleJson = new JSONArray();

        for (Person p : people) {
            peopleJson.put(p.toJson());
        }

        return peopleJson;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray gamesToJson() {
        JSONArray gamesJson = new JSONArray();

        for (Game g : games) {
            gamesJson.put(g.toJson());
        }

        return gamesJson;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray partiesToJson() {
        JSONArray gamePartiesJson = new JSONArray();
        for (GameParty gp : gameParties) {
            gamePartiesJson.put(gp.toJson());
        }

        return gamePartiesJson;
    }

}
