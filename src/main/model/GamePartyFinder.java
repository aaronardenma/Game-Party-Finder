package model;

import java.util.ArrayList;

// Represents a GamePartyFinder with a list of people, games, and game parties
public class GamePartyFinder {
    private ArrayList<Person> people;
    private ArrayList<Game> games;
    private ArrayList<GameParty> gameParties;

    public GamePartyFinder() {
        this.people = new ArrayList<>();
        this.games = new ArrayList<>();
        this.gameParties = new ArrayList<>();
    }

    public void addPerson(String name) {
        Person newPerson = new Person(name);
        if (!getPeopleNames().contains(name)) {
            people.add(newPerson);
        }
    }

    public void addGame(String name, int maxPartyMembers) {
        ArrayList<String> gameNames = new ArrayList<>();
        games.forEach((g) -> gameNames.add(g.getName().toLowerCase()));

        if (!getGameNames().contains(name.toLowerCase())) {
            Game newGame = new Game(name, maxPartyMembers);
            games.add(newGame);
        }

    }

    public void createGameParty(Game game, int maxPartySize, String partyName) {
        GameParty newGameParty = new GameParty(game, maxPartySize, partyName);
        gameParties.add(newGameParty);
    }

    // MODIFIES: this
    // EFFECTS: adds person to gameParty
    public void addPersonToGameParty(Person person, GameParty gameParty) {
        person.addToGameParty(gameParty);
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



}
