package ui;

import model.*;

import java.util.Scanner;
import java.util.ArrayList;

// Game Party Maker application
public class GamePartyApp {
    private GamePartyFinder partyFinder;
    private Scanner scanner;

    // EFFECTS: runs the Game Party application
    public GamePartyApp() {
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
        System.out.println("\t1. People");
        System.out.println("\t2. Games");
        System.out.println("\t3. GameParties");
        System.out.println("\tquit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("People")) {
            peopleActions();
        } else if (command.equals("Games")) {
            gameActions();
        } else if (command.equals("GameParties")) {
            gamePartyActions();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: displays a list of actions to the user involving the People class
    private void peopleActions() {
        System.out.println("Please select an action: "
                + "(\n1. Create \n2. Add to Game Party \n3. Add Role"
                + "\n4. Delete Role \n5. See all Roles \n6. back");
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
            displayMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: Create new person
    private void createPerson() {
        System.out.println("What is this new Person's name?");
        String newName = scanner.next();
        partyFinder.addPerson(newName);
    }

    // MODIFIES: this
    // EFFECTS: Add Person to the GameParty specified by user input
    private void addToGameParty() {
        System.out.println("Who should be added to a game party?");
        Person person = getPersonFromUserResponse();
        System.out.println("Which Game Party should " + person.getName() + " be added to?");
        partyFinder.addPersonToGameParty(person, getGamePartyFromUserResponse());
    }

    // MODIFIES: this
    // EFFECTS: Add new Game/Role from list of existing roles to specified Person
    private void addNewRole() {
        System.out.println("Who do you want to have a new role?");
        Person person = getPersonFromUserResponse();
        System.out.println("What Game should we add to " + person.getName() + " list of roles?");
        Game newGame = getGameFromUserResponse();
        person.addRole(newGame);
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
    public void viewRoles() {
        System.out.println("Whose roles would you like to see?");
        System.out.println(partyFinder.getPeopleNames());
        int peopleIndex = scanner.nextInt() - 1;
        Person person = partyFinder.getPeople().get(peopleIndex);
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
        partyFinder.addGame(newName, maxPartySize);
    }

    // EFFECTS: displays a list of actions to the user involving the GameParty class
    private void gamePartyActions() {
        System.out.println("Please select an action: "
                + "(\n1. Create New Game Party\n2. View people in Game Party"
                + "\n3. Change Max Party Size\n4. back");
        int command = scanner.nextInt();

        if (command == 1) {
            createGameParty();
        } else if (command == 2) {
            viewPeopleInGameParty();
        } else if (command == 3) {
            changeGamePartyMaxSize();
        } else if (command == 4) {
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
        partyFinder.createGameParty(gameSelected, maxPartySize, partyName);
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
