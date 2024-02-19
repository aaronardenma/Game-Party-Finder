package ui;

import model.*;

import java.util.Scanner;
import java.util.ArrayList;

// Game Party Maker application
public class GamePartyApp {
    private Person mainPerson;
    private Scanner scanner;
    private ArrayList<Person> people;
    private ArrayList<Game> games;
    private ArrayList<GameParty> gameParties;

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
        createMainUser();

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
        people = new ArrayList<>();
        games = new ArrayList<>();
        gameParties = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: Creates main Person user, primary Game, and GameParty
    // to go through the interface
    private void createMainUser() {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        System.out.println("What is your name?");
        String name = scanner.next();
        mainPerson = new Person(name);
        addPerson(mainPerson);

        System.out.println("What is a game you would like to play?");
        String gameName = scanner.next();
        System.out.println("What is the max party size " + gameName + " allows?");
        int maxGamePartySize = scanner.nextInt();
        Game newGame = new Game(gameName, maxGamePartySize);
        mainPerson.addRole(newGame);
        addGame(newGame);

        System.out.println("How many people would you like in this new party?");
        int maxPartySize = scanner.nextInt();
        GameParty newGameParty = new GameParty(maxPartySize, newGame);
        mainPerson.addToGameParty(newGameParty);
        addGameParty(newGameParty);
    }

    // MODIFIES: this
    // EFFECTS: switches main Person to user input
    private void switchUser() {
        System.out.println("Which User would you like to switch to?");
        System.out.println("Available Users:");
        System.out.println(people);
        String newUser = scanner.next();

        for (Person user: people) {
            if (user.getName().equals(newUser)) {
                mainPerson = user;
            }
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect actions involving:");
        System.out.println("\tpeople");
        System.out.println("\tgames");
        System.out.println("\tgame parties");
        System.out.println("\tswitch user");
        System.out.println("\tquit");
    }

    // MODIFIES: this
    // EFFECTS: Creates new Game Party according to user input for Game Party Game and max party size
    // if Game specified is not created as a role, print game not found
    private void createGameParty() {
        System.out.println("Which game would you like to create a party for?");
        System.out.println(mainPerson.getRoles());
        String gameNameSelected = scanner.next();

        Game gameSelected = null;
        for (Game game: mainPerson.getRoles()) {
            if (game.getName().equals(gameNameSelected)) {
                gameSelected = game;
            }
        }

        System.out.println("How many people would you like in this party?");
        int maxPartySize = scanner.nextInt();
        GameParty newGameParty = new GameParty(maxPartySize, gameSelected);
        mainPerson.addToGameParty(newGameParty);
        addGameParty(newGameParty);

    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("people")) {
            peopleActions();
        } else if (command.equals("games")) {
            gameActions();
        } else if (command.equals("game parties")) {
            gamePartyActions();
        } else if (command.equals("switch user")) {
            switchUser();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: displays a list of actions to the user involving the People class
    private void peopleActions() {
        System.out.println("Please select an action: "
                + "(\n1. Create \n2. Add to Game Party \n3. Add Role"
                + "\n4. Delete Role \n5. See all Roles)\n6. back");
        int command = scanner.nextInt();

        if (command == 1) {
            createNewPerson();
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
    // EFFECTS: view all roles for mainPerson user
    public void viewRoles() {
        System.out.println(mainPerson.getName() + " Roles:");
        ArrayList<Game> roles = mainPerson.getRoles();
        ArrayList<String> roleNames = new ArrayList<>();
        roles.forEach(game -> roleNames.add(game.getName()));
        System.out.println(roleNames);
    }

    // MODIFIES: this
    // EFFECTS: creates a new person with user input specifying the new Person's name
    private void createNewPerson() {
        System.out.println("What is this new Person's name?");
        String newName = scanner.next();
        Person newPerson = new Person(newName);
        people.add(newPerson);
    }

    // MODIFIES: this
    // EFFECTS: Add Person User is currently set as to the GameParty specified by user input
    private void addToGameParty() {
        System.out.println("Which Game Party should we be added to?");
        System.out.println(gameParties);
        int gamePartyIndex = scanner.nextInt() - 1;
        mainPerson.addToGameParty(gameParties.get(gamePartyIndex));
    }

    // MODIFIES: this
    // EFFECTS: Allow user to add new Role from list of existing roles to current Person
    private void addNewRole() {
        System.out.println("What Role should we add to "
                + mainPerson.getName() + " ?");
        System.out.println("Available Games:");
        ArrayList<String> gameNames = new ArrayList<>();
        games.forEach(game -> gameNames.add(game.getName()));
        System.out.println(gameNames);
        String newRoleName = scanner.next();

        Game newGame = null;
        for (Game game: games) {
            if (game.getName().equals(newRoleName)) {
                newGame = game;
                mainPerson.addRole(game);
            }
        }

        if (newGame == null) {
            System.out.println("Game not found");
        }
    }

    // MODIFIES: this
    // EFFECTS: Allow user to delete existing Role from current Person
    private void deleteRole() {
        System.out.println("Which role should be deleted?");
        System.out.println(mainPerson.getRoles());
        int index = scanner.nextInt() - 1;
        mainPerson.deleteRole(games.get(index));
    }

    // EFFECTS: displays a list of actions to the user involving the Game class
    private void gameActions() {
        System.out.println("Please select an action: "
                + "(\n1. Add New Game\n2. back");
        int command = scanner.nextInt();

        if (command == 1) {
            System.out.println("What is the name of the game?");
            String gameName = scanner.next();
            System.out.println("What is the max party size of this game?");
            int maxPartySize = scanner.nextInt();
            Game newGame = new Game(gameName, maxPartySize);
            mainPerson.addRole(newGame);
            addGame(newGame);
        } else if (command == 2) {
            displayMenu();
        }
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
    // EFFECTS: Allow user to view all members from a Game Party they are in and is specified by user input
    private void viewPeopleInGameParty() {
        System.out.println("Which of your Game Parties would you like to look at?");
        ArrayList<GameParty> parties = mainPerson.getGameParties();
        System.out.println(parties);
        int gamePartyIndex = scanner.nextInt() - 1;
        System.out.println(parties.get(gamePartyIndex).getCurrentMembers());
    }

    // MODIFIES: this
    // EFFECTS: Allow user to change Game Party max size to a value specified by user input
    private void changeGamePartyMaxSize() {
        System.out.println("Which of your Game Parties would you like to reduce its size?");
        ArrayList<GameParty> parties = mainPerson.getGameParties();
        System.out.println(parties);
        int gamePartyIndex = scanner.nextInt() - 1;
        System.out.println("Set new size of the party");
        int newSize = scanner.nextInt();
        parties.get(gamePartyIndex).changeTotalSize(newSize);
    }

    // MODIFIES: this
    // EFFECTS: Add Person to the list of persons
    private void addPerson(Person p) {
        if (!people.contains(p)) {
            people.add(p);
        }
    }

    // MODIFIES: this
    // EFFECTS: Add Game to the list of games
    private void addGame(Game g) {
        if (!games.contains(g)) {
            games.add(g);
        }
    }

    // MODIFIES: this
    // EFFECTS: Add GameParty to the list of game parties
    private void addGameParty(GameParty gp) {
        if (!gameParties.contains(gp)) {
            gameParties.add(gp);
        }
    }

}
