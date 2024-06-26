package model;

import exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a GamePartyFinder with a list of people, games, and game parties
public class GamePartyFinder implements Writable {
    private ArrayList<Person> people;
    private ArrayList<Game> games;
    private ArrayList<GameParty> gameParties;

    // EFFECTS: people is an empty ArrayList<Person>
    // games is an empty ArrayList<Game>
    // gameParties is an empty ArrayList<GameParty>
    public GamePartyFinder() {
        this.people = new ArrayList<>();
        this.games = new ArrayList<>();
        this.gameParties = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if people ArrayList<Person> does not contain person, add to list
    public void addPerson(Person person) {
        if (!getPeople().contains(person)) {
            people.add(person);
            EventLog.getInstance().logEvent(new Event("(Person: " + person.getName()
                    + ") has been added to the Game Party Finder."));
        }
    }

    // MODIFIES: this
    // EFFECTS: if games ArrayList<Game> does not contain game, add to list
    public void addGame(Game game) {
        ArrayList<String> gameNames = new ArrayList<>();
        games.forEach((g) -> gameNames.add(g.getName().toLowerCase()));

        if (!gameNames.contains(game.getName().toLowerCase())) {
            games.add(game);
            EventLog.getInstance().logEvent(new Event("(Game: " + game.getName()
                    + ") has been added to the Game Party Finder."));
        }
    }

    // MODIFIES: this
    // EFFECTS: if gameParty ArrayList<GameParty> does not contain gameParty, add to list
    public void addGameParty(GameParty gameParty) {
        ArrayList<String> partyNames = new ArrayList<>();
        gameParties.forEach((gp) -> partyNames.add(gp.getName().toLowerCase()));
        if (!partyNames.contains(gameParty.getName().toLowerCase())) {
            gameParties.add(gameParty);
            EventLog.getInstance().logEvent(new Event(
                    "(Game Party: " + gameParty.getName() + ") has been added to the Game Party Finder."));
        }
    }

    // MODIFIES: gameParty
    // EFFECTS: adds person to gameParty
    public void addPersonToGameParty(Person person, GameParty gameParty) throws NotInFinderException {
        if (!people.contains(person) && !gameParties.contains(gameParty)) {
            throw new NotInFinderException("Person & Game Party not found");
        } else if (!people.contains(person)) {
            throw new PersonNotInFinderException(person);
        } else if (!gameParties.contains(gameParty)) {
            throw new PartyNotInFinderException(gameParty);
        } else {
            try {
                gameParty.addMember(person);
            } catch (PersonDoesNotContainRoleException personDoesNotContainRoleException) {
                System.err.println(person.getName() + " does not contain Game Party role (Game: "
                        + gameParty.getGame().getName() + ")");
            }
        }
    }

    // MODIFIES: gameParty
    // EFFECTS: if gameParty not in this, throw PartyNotInFinderException. Else, remove person from gameParty
    public void removePersonFromGameParty(GameParty gameParty, Person person) throws PartyNotInFinderException {
        if (!gameParties.contains(gameParty)) {
            throw new PartyNotInFinderException(gameParty);
        } else {
            gameParty.removeMember(person);
            EventLog.getInstance().logEvent(new Event(
                    "(Person: " + person.getName() + ") has been removed from "
                            + "(Game Party: " + gameParty.getName() + ")"));
        }
    }

    // EFFECTS: if gameParty is in gameParties, throw PartyNotInFinderException. Else, return gameParty's list of party
    // members
    public ArrayList<Person> viewPartyMembers(GameParty gameParty) throws PartyNotInFinderException {
        if (!gameParties.contains(gameParty)) {
            throw new PartyNotInFinderException(gameParty);
        } else {
            return gameParty.getCurrentMembers();
        }
    }

    // MODIFIES: person
    // EFFECTS: add game to list of roles a person has
    public void addRoleToPerson(Person person, Game game) throws NotInFinderException {
        if (!people.contains(person) && !games.contains(game)) {
            throw new NotInFinderException("Person & Game not found");
        } else if (!people.contains(person)) {
            throw new PersonNotInFinderException(person);
        } else if (!games.contains(game)) {
            throw new GameNotInFinderException(game);
        } else {
            person.addRole(game);
        }
    }

    // MODIFIES: person
    // EFFECTS: remove game from list of roles person has
    public void deleteRoleFromPerson(Person person, Game game) {
        person.removeRole(game);
    }

    // EFFECTS: return list of roles from person
    public ArrayList<Game> getRolesFromPerson(Person person) {
        return person.getRoles();
    }

    // MODIFIES: gameParty
    // EFFECTS: change gameParty max party size to newSize
    public void changePartySize(GameParty gameParty, int newSize) {
        gameParty.changeTotalSize(newSize);
    }

    // getter
    public ArrayList<GameParty> getGameParties() {
        return this.gameParties;
    }

    // EFFECTS: get names of all GameParty in gameParties in a list
    public ArrayList<String> getGamePartyNames() {
        ArrayList<String> gamePartyNames = new ArrayList<>();
        gameParties.forEach((gp) -> gamePartyNames.add(gp.getName()));
        return gamePartyNames;
    }

    // getter
    public ArrayList<Game> getGames() {
        return this.games;
    }

    // EFFECTS: get names of all Game in games in a list
    public ArrayList<String> getGameNames() {
        ArrayList<String> gameNames = new ArrayList<>();
        games.forEach((g) -> gameNames.add(g.getName()));
        return gameNames;
    }

    // getter
    public ArrayList<Person> getPeople() {
        return this.people;
    }

    // EFFECTS: get names of all Person in people in a list
    public ArrayList<String> getPeopleNames() {
        ArrayList<String> peopleNames = new ArrayList<>();
        people.forEach((p) -> peopleNames.add(p.getName()));
        return peopleNames;
    }

    // MODIFIES: this
    // EFFECTS: update game statistics for every person in gameParty and remove gameParty from
    // this.gameParties
    public void endSession(GameParty gameParty, float numOfWins,
                           float numGamesPlayed) throws PartyNotInFinderException {
        if (!gameParties.contains(gameParty)) {
            throw new PartyNotInFinderException(gameParty);
        } else {
            ArrayList<Person> members = gameParty.getCurrentMembers();
            for (Person p : members) {
                p.updateWinRate(gameParty, numOfWins, numGamesPlayed);
            }
            gameParties.remove(gameParty);
        }
    }

    // MODIFIES: this, GameParty
    // EFFECTS: remove Person from people, check GameParty in gameParties and remove person from any GameParty that
    // have Person as a member
    public void removePerson(Person person) {
        people.remove(person);
        for (GameParty gp: gameParties) {
            if (gp.getCurrentMembers().contains(person)) {
                gp.removeMember(person);
            }
        }
    }

    // MODIFIES: this, Person
    // EFFECTS: removes game from games, deletes GameParties that are set for game, and deletes game role from all
    // persons
    public void removeGame(Game game) {
        games.remove(game);
        gameParties.removeIf((gp) -> (gp.getGame() == game));
        people.forEach((p) -> p.removeRole(game));
    }

    // MODIFIES: this
    // EFFECTS: remove gameParty from gameParties
    public void removeGameParty(GameParty gameParty) {
        gameParties.remove(gameParty);
    }

    // EFFECTS: return JSON Object that captures people, games, and game parties as JSON arrays
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("people", peopleToJson());
        json.put("games", gamesToJson());
        json.put("game parties", partiesToJson());
        return json;
    }

    // EFFECTS: returns people in this GamePartyFinder as a JSON array
    private JSONArray peopleToJson() {
        JSONArray peopleJson = new JSONArray();

        for (Person p : people) {
            peopleJson.put(p.toJson());
        }

        return peopleJson;
    }

    // EFFECTS: returns games in this GamePartyFinder as a JSON array
    private JSONArray gamesToJson() {
        JSONArray gamesJson = new JSONArray();

        for (Game g : games) {
            gamesJson.put(g.toJson());
        }

        return gamesJson;
    }

    // EFFECTS: returns gameParties in this GamePartyFinder as a JSON array
    private JSONArray partiesToJson() {
        JSONArray gamePartiesJson = new JSONArray();
        for (GameParty gp : gameParties) {
            gamePartiesJson.put(gp.toJson());
        }

        return gamePartiesJson;
    }

}
