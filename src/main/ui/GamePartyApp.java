package ui;

import exceptions.*;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

// Game Party Maker Console Application
public class GamePartyApp {
    private static final String JSON_STORE = "./data/testWriterGamePartyFinder.json";
    private GamePartyFinder partyFinder;
    private Scanner scanner;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the Game Party application
    public GamePartyApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runGamePartyMaker();
    }

    // MODIFIES: this
    // EFFECTS: processes user input and creates a main user
    private void runGamePartyMaker() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();
            command = scanner.next();
            command = command.toLowerCase();

            if (command.equals("quit")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: initializes people, games and gameParties lists
    private void init() {
        partyFinder = new GamePartyFinder();
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect actions involving:");
        System.out.println("\tp <- People");
        System.out.println("\tg <- Games");
        System.out.println("\tgp <- GameParties");
        System.out.println("\ts <- Save");
        System.out.println("\tl <- Load");
        System.out.println("\tquit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("p")) {
            peopleActions();
        } else if (command.equals("g")) {
            gameActions();
        } else if (command.equals("gp")) {
            gamePartyActions();
        } else if (command.equals("s")) {
            saveGamePartyFinder();
        } else if (command.equals("l")) {
            loadGamePartyFinder();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: saves the gamePartyFinder to file
    private void saveGamePartyFinder() {
        try {
            jsonWriter.open();
            jsonWriter.write(partyFinder);
            jsonWriter.close();
            System.out.println("Saved "  + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads gamePartyFinder from file
    private void loadGamePartyFinder() {
        try {
            partyFinder = jsonReader.read();
            System.out.println("Loaded " +  " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: displays a list of actions to the user involving the People class
    private void peopleActions() {
        System.out.println("Please select an action: "
                + "(\n1. Create \n2. Add to Game Party \n3. Add Role"
                + "\n4. Delete Role \n5. See all Roles \n6. View Game Stats \n7. back");
        int command = scanner.nextInt();

        if (command == 1) {
            createPerson();
        } else if (command == 2) {
            addToGameParty();
        } else if (command == 3) {
            addNewRole();
        } else if (command == 4) {
            deleteRole();
        } else if (command == 5) {
            viewRoles();
        } else if (command == 6) {
            viewGameStats();
        } else if (command == 7) {
            displayMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: Create new person
    private void createPerson() {
        System.out.println("What is this new Person's name?");
        String newName = scanner.next();
        Person person = new Person(newName);
        partyFinder.addPerson(person);
    }

    // MODIFIES: this
    // EFFECTS: Add Person to the GameParty specified by user input
    private void addToGameParty() {
        System.out.println("Who should be added to a game party?");
        Person person = getPersonFromUserResponse();
        System.out.println("Which Game Party should " + person.getName() + " be added to?");
        try {
            partyFinder.addPersonToGameParty(person, getGamePartyFromUserResponse());
        } catch (PersonNotInFinderException personNotInFinderException) {
            System.err.println("Person not found");
        } catch (PartyNotInFinderException partyNotInFinderException) {
            System.err.println("Game Party not found");
        } catch (NotInFinderException notInFinderException) {
            System.err.println("Not found exception thrown");
        }
    }

    // MODIFIES: this
    // EFFECTS: Add new Game/Role from list of existing roles to specified Person
    private void addNewRole() {
        System.out.println("Who do you want to have a new role?");
        Person person = getPersonFromUserResponse();
        System.out.println("What Game should we add to " + person.getName() + " list of roles?");
        Game newGame = getGameFromUserResponse();
        try {
            partyFinder.addRoleToPerson(person, newGame);
        } catch (PersonNotInFinderException pe) {
            System.err.println("Person not found");
        } catch (GameNotInFinderException ge) {
            System.err.println("Game not found");
        } catch (NotInFinderException ne) {
            System.err.println("Not found exception thrown");
        }
    }

    // MODIFIES: this
    // EFFECTS: Delete existing Role from person selected
    private void deleteRole() {
        System.out.println("Which person's roles would you like to change?");
        Person person = getPersonFromUserResponse();
        System.out.println("Which role should be deleted?");
        System.out.println(getRoleNames(person));
        int roleIndex = scanner.nextInt() - 1;
        ArrayList<Game> roles = partyFinder.getRolesFromPerson(person);

        partyFinder.deleteRoleFromPerson(person, roles.get(roleIndex));
    }

    // MODIFIES: this
    // EFFECTS: view all roles for Person determined by user Response
    private void viewRoles() {
        System.out.println("Whose roles would you like to see?");
        Person person = getPersonFromUserResponse();
        ArrayList<Game> roles = person.getRoles();
        ArrayList<String> roleNames = new ArrayList<>();
        roles.forEach(game -> roleNames.add(game.getName()));
        System.out.println(roleNames);
    }

    // EFFECTS: returns a list of role names that the person has
    private ArrayList<String> getRoleNames(Person person) {
        ArrayList<Game> roles = partyFinder.getRolesFromPerson(person);
        ArrayList<String> roleNames = new ArrayList<>();
        roles.forEach((game) -> roleNames.add(game.getName()));

        return roleNames;
    }

    // EFFECTS: returns a list of game statistics that the person has with others
    private void viewGameStats() {
        System.out.println("Whose Game Statistics would you like to see?");
        Person person = getPersonFromUserResponse();
        System.out.println("[WinRate (%), # of Wins, # Games Played]");
        System.out.println(person.getGameStats());
    }

    // EFFECTS: displays a list of actions to the user involving the Game class
    private void gameActions() {
        System.out.println("Please select an action: "
                + "(\n1. Add New Game\n2. back");
        int command = scanner.nextInt();

        if (command == 1) {
            createGame();
        } else if (command == 2) {
            displayMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: Add Game to the list of games
    private void createGame() {
        System.out.println("What is this new Game's name?");
        String newName = scanner.next();
        System.out.println("What is the max party size for this game?");
        int maxPartySize = scanner.nextInt();
        Game game = new Game(newName, maxPartySize);
        partyFinder.addGame(game);
    }

    // EFFECTS: displays a list of actions to the user involving the GameParty class
    private void gamePartyActions() {
        System.out.println("Please select an action: "
                + "(\n1. Create New Game Party\n2. View people in Game Party"
                + "\n3. Change Max Party Size\n4. End Game Party Session \n 5. back");
        int command = scanner.nextInt();

        if (command == 1) {
            createGameParty();
        } else if (command == 2) {
            viewPeopleInGameParty();
        } else if (command == 3) {
            changeGamePartyMaxSize();
        } else if (command == 4) {
            endGamePartySession();
        } else if (command == 5) {
            displayMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates new Game Party
    private void createGameParty() {
        System.out.println("Which game would you like to play?");
        Game gameSelected = getGameFromUserResponse();
        System.out.println("How many people would you like in this party?");
        int maxPartySize = scanner.nextInt();
        System.out.println("Give this party a name");
        String partyName = scanner.next();
        GameParty gameParty = new GameParty(gameSelected, maxPartySize, partyName);
        partyFinder.addGameParty(gameParty);
    }

    // MODIFIES: this
    // EFFECTS: Allow user to view all members from a Game Party that is specified by user input
    private void viewPeopleInGameParty() {
        System.out.println("Which of your Game Parties would you like to look at?");
        GameParty gamePartySelected = getGamePartyFromUserResponse();
        ArrayList<Person> currentMembers = gamePartySelected.getCurrentMembers();
        ArrayList<String> currentMemberNames = new ArrayList<>();
        currentMembers.forEach((person) -> currentMemberNames.add(person.getName()));
        System.out.println(currentMemberNames);
    }

    // MODIFIES: this
    // EFFECTS: Allow user to change a Game Party max size to a value specified by user input
    private void changeGamePartyMaxSize() {
        System.out.println("Which of your Game Parties would you like to reduce its size?");
        GameParty gamePartySelected = getGamePartyFromUserResponse();
        System.out.println("Set new size of the party");
        int newSize = scanner.nextInt();
        partyFinder.changePartySize(gamePartySelected, newSize);
    }

    // MODIFIES: this
    // EFFECTS: Allows user to update win rate statistics with one another and remove game party
    private void endGamePartySession() {
        System.out.println("Which of your Game Parties would you like to reduce its size?");
        GameParty gamePartySelected = getGamePartyFromUserResponse();
        System.out.println("How many games did you play?");
        int numGamesPlayed = scanner.nextInt();
        System.out.println("How many games did you win?");
        int numGamesWon = scanner.nextInt();

        try {
            partyFinder.endSession(gamePartySelected, numGamesWon, numGamesPlayed);
        } catch (PartyNotInFinderException partyNotInFinderException) {
            System.err.println("Party not found");
        }

    }

    // EFFECTS: return a Person specified by user response from a list of people in GamePartyFinder
    private Person getPersonFromUserResponse() {
        System.out.println(partyFinder.getPeopleNames());
        int personNameIndex = scanner.nextInt() - 1;
        return partyFinder.getPeople().get(personNameIndex);
    }

    // EFFECTS: return a Game specified by user response from a list of games in GamePartyFinder
    private Game getGameFromUserResponse() {
        System.out.println(partyFinder.getGameNames());
        int gameIndex = scanner.nextInt() - 1;
        return partyFinder.getGames().get(gameIndex);
    }

    // EFFECTS: return a GameParty specified by user response from a list of game parties in GamePartyFinder
    private GameParty getGamePartyFromUserResponse() {
        System.out.println(partyFinder.getGamePartyNames());
        int gamePartyIndex = scanner.nextInt() - 1;
        return partyFinder.getGameParties().get(gamePartyIndex);
    }
}
