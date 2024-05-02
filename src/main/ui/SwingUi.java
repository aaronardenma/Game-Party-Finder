package ui;

import exceptions.*;
import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// GamePartyFinder Swing UI App
public class SwingUi extends JPanel {
    private GamePartyFinder gamePartyFinder;
    private static JMenuBar menuBar;
    private JMenu createMenu;
    private JMenu personMenu;
    private JMenu gameMenu;
    private JMenu gamePartyMenu;
    private JButton submitButton;
    private String personNameSelected;
    private String gameNameSelected;
    private String gamePartyNameSelected;
    private static final String JSON_STORE = "./data/testWriterGamePartyFinder.json";
//    private static final ArrayList<String> CLASSES = new ArrayList<>(Arrays.asList("Person", "Game", "GameParty"));
//    private static final ArrayList<String> FUNCTIONS = new ArrayList<>(Arrays.asList("Add Person to GameParty",
//            "End Session", "Add Role", "Delete Role", "Create GameParty"));
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // EFFECTS: Create GamePartyFinder and JMenuBar. Add Person, Game, and Game Parties menu item to Menu Bar. Create
    // save and load buttons with JsonWriter and JsonReader constructors. set personNameSelected and gameNameSelected
    // to null. Create ImageIcon of video-game.png to display as a splash screen.
    public SwingUi() {
        gamePartyFinder = new GamePartyFinder();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        personNameSelected = null;
        gameNameSelected = null;
        gamePartyNameSelected = null;

        setLayout(new BorderLayout());
        createMenuBar();
        addSplashImage();
        displayGames();
        addSaveLoadButtons();
    }

    // MODIFIES: this
    // EFFECTS: adds a loading screen splash image
    private void addSplashImage() {
        ImageIcon imageIcon = createImageIcon("video-game.png");
        ImageIcon scaledImageIcon = scaleImageIcon(imageIcon, 200, 200);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        add(imageLabel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates a new menu bar with menu items: "Create", "People", "Game", "Game Parties". Adds corresponding
    // menu functionalities
    private void createMenuBar() {
        menuBar = new JMenuBar();
        createMenu = new JMenu("Create");
        personMenu = new JMenu("People");
        gameMenu = new JMenu("Game");
        gamePartyMenu = new JMenu("Game Parties");

        addCreateMenuItems();
        addPersonMenuItems();
        addGameMenuItems();
        addGamePartyMenuItems();

        menuBar.add(createMenu);
        menuBar.add(personMenu);
        menuBar.add(gameMenu);
        menuBar.add(gamePartyMenu);
    }

    // MODIFIES: this
    // EFFECTS: adds menu items to the Create Menu
    private void addCreateMenuItems() {
        JMenuItem addPersonItem = new JMenuItem("Add Person");
        addPersonItem.addActionListener(e -> addPersonFields());

        JMenuItem addGameItem = new JMenuItem("Add Game");
        addGameItem.addActionListener(e -> addGameFields());

        JMenuItem addGamePartyItem = new JMenuItem("Create Game Party");
        addGamePartyItem.addActionListener(e -> createGameParty());

        createMenu.add(addPersonItem);
        createMenu.add(addGameItem);
        createMenu.add(addGamePartyItem);
    }

    // MODIFIES: this
    // EFFECT: adds menu items to the Person Menu
    private void addPersonMenuItems() {
        JMenuItem addRoleItem = new JMenuItem("Add Role");
        addRoleItem.addActionListener(e -> createAddRoleToPersonFields());

        JMenuItem deleteRoleItem = new JMenuItem("Delete Role");
        deleteRoleItem.addActionListener(e -> deleteRoles());

        JMenuItem addToGamePartyItem = new JMenuItem("Add to Game Party");
        addToGamePartyItem.addActionListener(e ->
                createAddPersonToGamePartyFields());

        personMenu.add(addRoleItem);
        personMenu.add(deleteRoleItem);
        personMenu.add(addToGamePartyItem);
    }

    // MODIFIES: this
    // EFFECT: adds game items to the Game Menu
    private void addGameMenuItems() {
        JMenuItem addGameItem = new JMenuItem("Add Game");
        addGameItem.addActionListener(e -> addGameFields());

        gameMenu.add(addGameItem);
    }

    // MODIFIES: this
    // EFFECT: adds game party items to the Game Party Menu
    private void addGamePartyMenuItems() {
        JMenuItem removePersonFromGameParty = new JMenuItem("Remove Party Member");
        removePersonFromGameParty.addActionListener(e ->
                addRemovePersonFromGamePartySelectionDialogs("GameParty", false));

        JMenuItem endGameSession = new JMenuItem("End Game Party Session");
        endGameSession.addActionListener(e -> endGameSession());

        gamePartyMenu.add(removePersonFromGameParty);
        gamePartyMenu.add(endGameSession);
    }

    // MODIFIES: this
    // EFFECTS: create a text field for user to input new Person's name with corresponding label to create a new person
    private void addPersonFields() {
        removeAll();
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(20, 5));
        nameField.addActionListener(e -> {
            submitButton.doClick(); // Simulate button click when Enter is pressed
        });

        JPanel labelPane = new JPanel(new GridLayout(1, 1));
        labelPane.add(nameLabel);
        JPanel fieldPane = new JPanel(new GridLayout(1, 1));
        fieldPane.add(nameField);

        createLabelFieldPanel(labelPane, fieldPane);
        createButtonPanel(createAddPersonSubmitButton(nameField));

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: create a submit button for person name text field that creates a new Person based on that name
    private JButton createAddPersonSubmitButton(JTextField nameField) {
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                try {
                    returnOriginalState();
                    throw new InputEmptyException("Person Name");
                } catch (InvalidInputException ex) {
                    ex.printStackTrace();
                }
            } else {
                gamePartyFinder.addPerson(new Person(name));
            }
            returnOriginalState();
        });
        return submitButton;
    }

    // MODIFIES: this
    // EFFECTS: calls openClassSelectionDialog() to add selection dialogs to select person
    private void createAddRoleToPersonFields() {
        openClassSelectionDialog("Person", "Add Role");
    }

    // MODIFIES: this
    // EFFECTS: add JScrollPane to this with an itemList from a JList with values from getGamePartyFinderFieldListNames
    // with className. Create a button panel with a submit button createSelectionDialogSubmitButton()
    private void openClassSelectionDialog(String className, String function) {
        removeAll();
        JList<String> itemList;

        try {
            itemList = new JList<>(getGamePartyFinderFieldListNames(className).toArray(new String[0]));
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            returnOriginalState();
            return;
        }

        setLayout(new BorderLayout());
        add(new JScrollPane(itemList), BorderLayout.CENTER);
        createButtonPanel(createSelectionDialogSubmitButton(className, itemList, function));

        revalidate();
        repaint();
    }

    // EFFECTS: returns a "Submit" JButton that when className equals to "Person", set personNameSelected field as a
    // selected value from itemList. Else if className equals to "Game", set gameNameSelected field to a selected
    // value from itemList, then add selected gameNameSelected Game to selected personNameSelected Person. Then check
    // for additional functionality or method calls from switchSelectionDialogSubmitButton()
    private JButton createSelectionDialogSubmitButton(String className, JList<String> itemList, String function) {
        JButton setButton = new JButton("Submit");
        setButton.addActionListener(e -> {
            if (className.equals("Person")) {
                personNameSelected = itemList.getSelectedValue();
            } else if (className.equals("Game")) {
                gameNameSelected = itemList.getSelectedValue();
            } else if (className.equals("GameParty")) {
                gamePartyNameSelected = itemList.getSelectedValue();
            } else {
                throw new NoSuchElementException();
            }

            switchSelectionDialogSubmitButton(className, function);
        });
        return setButton;
    }

    // EFFECTS: if function isn't in FUNCTIONS or className isn't in CLASSES, throw new InvalidInputException.
    // Else, call switchSelectionDialogSubmitButtonFUNCTION for different FUNCTIONS
    private void switchSelectionDialogSubmitButton(String className, String function) {
        switchSelectionDialogSubmitButtonAddRole(function, className);
        switchSelectionDialogSubmitButtonDeleteRole(function, className);
        switchSelectionDialogSubmitButtonCreateGameParty(function, className);
        switchSelectionDialogSubmitButtonAddPersonToGameParty(function, className);
        switchSelectionDialogSubmitButtonEndSession(function, className);
    }

    // EFFECTS: if function equals "Add Role" and className equals "Person", openClassSelection for class "Game" and
    // function "Add Role". Else if, function equals "Add Role" and className equals "Game", addRoleToPerson(). Else,
    // throw new InvalidInputException.
    private void switchSelectionDialogSubmitButtonAddRole(String function, String className) {
        if (function.equals("Add Role") && className.equals("Person")) {
            openClassSelectionDialog("Game", "Add Role");
        } else if (function.equals("Add Role") && className.equals("Game")) {
            addRoleToPerson();
        }
    }

    // EFFECTS: if function equals "Delete Role" and className equals "Person", viewPersonRolesSelectionDialogToDelete.
    // Else if, function equals "Delete Role" and className equals "Game", deleteRoleFromPersonSelected(). Else,
    // throw new InvalidInputException.
    private void switchSelectionDialogSubmitButtonDeleteRole(String function, String className) {
        if (function.equals("Delete Role") && className.equals("Person")) {
            viewPersonRolesSelectionDialogToDelete();
        } else if (function.equals("Delete Role") && className.equals("Game")) {
            deleteRoleFromPersonSelected();
        }
    }

    // EFFECTS: if function equals "Create GameParty" and className equals "Game", addCreateGamePartyFields().
    private void switchSelectionDialogSubmitButtonCreateGameParty(String function, String className) {
        if (function.equals("Create GameParty") && className.equals("Game")) {
            addCreateGamePartyFields();
        }
    }

    // EFFECTS: if function equals "Add Person to GameParty" and className equals "Person",
    // viewPersonRolesSelectionDialogToDelete. Else if, function equals "Add Person to GameParty" and className equals
    // "GameParty", deleteRoleFromPersonSelected()
    private void switchSelectionDialogSubmitButtonAddPersonToGameParty(String function, String className) {
        if (function.equals("Add Person to GameParty") && className.equals("Person")) {
            openEligibleGamePartiesSelectionDialogAddToGameParty("GameParty", "Add Person to GameParty");
        } else if (function.equals("Add Person to GameParty") && className.equals("GameParty")) {
            addPersonToGameParty();
        }
    }

    // MODIFIES: this
    // EFFECTS: add JScrollPane to this with an itemList from a JList with values from getGamePartyFinderFieldListNames
    // with className. Create a button panel with a submit button createSelectionDialogSubmitButton()
    private void openEligibleGamePartiesSelectionDialogAddToGameParty(String className, String function) {
        removeAll();
        JList<String> itemList;
        Person person = (Person) matchNameToClass("Person");
        ArrayList<String> eligibleGameParties = new ArrayList<>();

        for (Game role : person.getRoles()) {
            for (GameParty gameParty: gamePartyFinder.getGameParties()) {
                if (gameParty.getGame().equals(role)) {
                    eligibleGameParties.add(gameParty.getName());
                }
            }
        }

        try {
            itemList = new JList<>(eligibleGameParties.toArray(new String[0]));
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            returnOriginalState();
            return;
        }

        setLayout(new BorderLayout());
        add(new JScrollPane(itemList), BorderLayout.CENTER);
        createButtonPanel(createSelectionDialogSubmitButton(className, itemList, function));

        revalidate();
        repaint();
    }

    // EFFECTS: if function equals "End Session" and className equals "GameParty", addEndGameSessionFields().
    private void switchSelectionDialogSubmitButtonEndSession(String function, String className) {
        if (function.equals("End Session") && className.equals("GameParty")) {
            addEndGameSessionFields();
        }
    }


    // MODIFIES: this, gamePartyFinder
    // EFFECTS: use personNameSelected and gameNameSelected to find person and game. then add role game to person
    private void addRoleToPerson() {
        Person person = (Person) matchNameToClass("Person");
        Game game = (Game) matchNameToClass("Game");

        try {
            gamePartyFinder.addRoleToPerson(person, game);
            setVisible(true);
            returnOriginalState();
        } catch (PersonNotInFinderException pe) {
            pe.printStackTrace();
        } catch (GameNotInFinderException ge) {
            ge.printStackTrace();
        } catch (NotInFinderException ne) {
            ne.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: open people selection dialog and view roles to allow user to select role to delete with a cancel and
    // submit button
    private void deleteRoles() {
        openClassSelectionDialog("Person", "Delete Role");
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: create a selection dialog to display all roles of a person that can be submitted to delete role
    // from person with a cancel and submit button
    private void viewPersonRolesSelectionDialogToDelete() {
        removeAll();
        Person person = (Person) matchNameToClass("Person");
        JList<String> itemList = new JList<>(person.getRoleNames().toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        createButtonPanel(createSelectionDialogSubmitButton("Game", itemList, "Delete Role"));

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: get Game and Person from matchNameToClass(), and delete game from person in gamePartyFinder through
    // gamePartyFinder.deleteRoleFromPerson() then returnOriginalState()
    private void deleteRoleFromPersonSelected() {
        Game role = (Game) matchNameToClass("Game");
        Person person = (Person) matchNameToClass("Person");

        gamePartyFinder.deleteRoleFromPerson(person, role);
        returnOriginalState();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: add fields related to Game construction and add to gamePartyFinder with a submit and cancel button
    private void addGameFields() {
        removeAll();
        JLabel nameLabel = new JLabel("Game Name: ");
        JLabel maxPartySizeLabel = new JLabel("Max Party Size: ");
        JTextField nameField = new JTextField(10);
        JFormattedTextField maxPartySizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
        maxPartySizeField.setColumns(10);

        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        labelPane.add(nameLabel);
        labelPane.add(maxPartySizeLabel);

        JPanel fieldPane = new JPanel(new GridLayout(0, 1));
        fieldPane.add(nameField);
        fieldPane.add(maxPartySizeField);

        createLabelFieldPanel(labelPane, fieldPane);
        createButtonPanel(createAddGameSubmitButton(nameField, maxPartySizeField));

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: create a submit button that upon action, adds a game to the game party finder
    private JButton createAddGameSubmitButton(JTextField nameField, JFormattedTextField maxPartySizeField) {
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            int maxPartySize = ((Long) maxPartySizeField.getValue()).intValue();
            gamePartyFinder.addGame(new Game(name, maxPartySize));
            returnOriginalState();
        });

        return submitButton;
    }

    // MODIFIES: this
    // EFFECTS: open up class selection dialog for Game class and apply function "Create Game Party: Game"
    private void createGameParty() {
        openClassSelectionDialog("Game", "Create GameParty");
    }

    // MODIFIES: this
    // EFFECTS: Create JTextField and JFormattedTextField accepting Numbers only for "Party Name" and "Max Party Size".
    // Then create a button panel with a set button by createAddGamePartySubmitButton().
    private void addCreateGamePartyFields() {
        removeAll();
        JLabel nameLabel = new JLabel("Party Name: ");
        JLabel maxPartySizeLabel = new JLabel("Max Party Size: ");
        JTextField nameField = new JTextField(10);
        JFormattedTextField maxPartySizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
        maxPartySizeField.setColumns(10);

        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        labelPane.add(nameLabel);
        labelPane.add(maxPartySizeLabel);

        JPanel fieldPane = new JPanel(new GridLayout(0, 1));
        fieldPane.add(nameField);
        fieldPane.add(maxPartySizeField);

        createLabelFieldPanel(labelPane, fieldPane);

        createButtonPanel(createAddGamePartySubmitButton(nameField, maxPartySizeField));

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: Create a submit button that adds a GameParty to the gamePartyFinder for a specified game with the
    // partyName and maxPartySize from JTextField and JFormattedTextField
    private JButton createAddGamePartySubmitButton(JTextField partyName, JFormattedTextField maxPartySize) {
        JButton button = new JButton("Submit");
        Game game = (Game) matchNameToClass("Game");

        button.addActionListener(e -> {
            String name = partyName.getText();
            int partySize = ((Long) maxPartySize.getValue()).intValue();
            gamePartyFinder.addGameParty(new GameParty(game, partySize, name));
            returnOriginalState();
        });

        return button;
    }

    // MODIFIES: this
    // EFFECTS: open up class selection dialog for Person class and apply function "Add Person to Game Party: Person"
    private void createAddPersonToGamePartyFields() {
        openClassSelectionDialog("Person", "Add Person to GameParty");
    }

    // MODIFIES: gamePartyFinder
    // EFFECTS: add personNameSelected Person and gamePartyNameSelected GameParty to the gamePartyFinder. Then
    // returnOriginalState()
    private void addPersonToGameParty() {
        Person person = (Person) matchNameToClass("Person");
        GameParty gameParty = (GameParty) matchNameToClass("GameParty");

        try {
            gamePartyFinder.addPersonToGameParty(person, gameParty);
            returnOriginalState();
        } catch (PersonNotInFinderException pe) {
            pe.printStackTrace();
        } catch (PartyNotInFinderException pex) {
            pex.printStackTrace();
        } catch (NotInFinderException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: if selectionDialogClass = "Person", set ArrayList<String> valuesList getGamePartySelectedMemberNames().
    // Else if, selectionDialogClass is "GameParty", set valuesList getGamePartyFinderFieldListNames(). Else, throw new
    // NoSuchElementException. Then check if valuesList is empty, if so, throw new NoSuchElementException. Catch
    // NoSuchElementException and print stack trace. create JList with valuesList and create button panel with
    // createRemovePersonFromGamePartySetButton().
    private void addRemovePersonFromGamePartySelectionDialogs(String selectionDialogClass, boolean last) {
        removeAll();
        ArrayList<String> valuesList;

        try {
            if (selectionDialogClass.equals("Person")) {
                valuesList = getGamePartySelectedMemberNames();
            } else if (selectionDialogClass.equals("GameParty")) {
                valuesList = getGamePartyFinderFieldListNames(selectionDialogClass);
            } else {
                throw new NoSuchElementException("List of " + selectionDialogClass + " is empty");
            }
            if (valuesList.isEmpty()) {
                throw new NoSuchElementException("List of " + selectionDialogClass + " is empty");
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return;
        }

        JList<String> itemList = new JList<>(valuesList.toArray(new String[0]));

        setLayout(new BorderLayout());
        add(new JScrollPane(itemList), BorderLayout.CENTER);
        createButtonPanel(createRemovePersonFromGamePartySetButton(selectionDialogClass, last, itemList));

        revalidate();
        repaint();
    }

    // EFFECTS: return current gamePartyNameSelected GameParty's current members in a ArrayList<String>
    private ArrayList<String> getGamePartySelectedMemberNames() {
        GameParty currentGameParty = (GameParty) matchNameToClass("GameParty");

        return (ArrayList<String>) currentGameParty.getCurrentMembers().stream().map(
                Person::getName).collect(Collectors.toList());
    }

    // MODIFIES: this
    // EFFECTS: return a JButton. If className is "GameParty" getSelectedValue() from itemList as gamePartyNameSelected.
    // Else if, className is "Person", getSelectedValue() from itemList as personNameSelected and
    // removePersonFromGameParty. Then check recursive call with checkRecursiveRemovePersonFromGameParty to assess last
    // boolean.
    private JButton createRemovePersonFromGamePartySetButton(String className, boolean last, JList<String> itemList) {
        JButton setButton = new JButton("Set");

        setButton.addActionListener(e -> {
            if (className.equals("GameParty")) {
                gamePartyNameSelected = itemList.getSelectedValue();

            } else if (className.equals("Person")) {
                personNameSelected = itemList.getSelectedValue();
                removePersonFromGameParty();
            }
            checkRecursiveRemovePersonFromGameParty(last);
        });
        return setButton;
    }

    // MODIFIES: this
    // EFFECTS: Check if last is true, if true, returnOriginalState(). If not true, open selection dialogs for removing
    // a person from a game party addRemovePersonFromGamePartySelectionDialogs() for Person selection with last as true
    private void checkRecursiveRemovePersonFromGameParty(Boolean last) {
        if (last) {
            setVisible(true);
            returnOriginalState();
        } else {
            addRemovePersonFromGamePartySelectionDialogs("Person", true);
        }
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: remove Person and GameParty from personNameSelected and gamePartyNameSelected from gamePartyFinder.
    // Print stack trace when catching PartyNotInFinderException.
    private void removePersonFromGameParty() {
        Person person = (Person) matchNameToClass("Person");
        GameParty gameParty = (GameParty) matchNameToClass("GameParty");

        try {
            gamePartyFinder.removePersonFromGameParty(gameParty, person);
        } catch (PartyNotInFinderException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: Add 2 Formatted Text fields that only accept Numbers for "# of Games Played" and "# of Games Won".
    // Create a button panel with createSubmitButtonEndGameSession() as a submitButton.
    private void addEndGameSessionFields() {
        removeAll();
        JLabel numGamesPlayedLabel = new JLabel("# of Games Played: ");
        JLabel numGamesWonLabel = new JLabel("# of Games Won");
        JFormattedTextField numGamesPlayedField = new JFormattedTextField(NumberFormat.getNumberInstance());
        JFormattedTextField numGamesWonField = new JFormattedTextField(NumberFormat.getNumberInstance());

        JPanel labelPane = new JPanel(new GridLayout(0, 20));
        labelPane.add(numGamesPlayedLabel);
        labelPane.add(numGamesWonLabel);

        JPanel fieldPane = new JPanel(new GridLayout(0, 20));
        fieldPane.add(numGamesPlayedField);
        fieldPane.add(numGamesWonField);

        GameParty gameParty = (GameParty) matchNameToClass("GameParty");
        createLabelFieldPanel(labelPane, fieldPane);
        createButtonPanel(createSubmitButtonEndGameSession(gameParty, numGamesPlayedField, numGamesWonField));

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: return a JButton that incorporates number inputs from numGamesPlayedField and numGamesWonField
    // JFormattedTextFields and endSession in gamePartyFinder
    private JButton createSubmitButtonEndGameSession(GameParty gameParty, JFormattedTextField numGamesPlayedField,
                                               JFormattedTextField numGamesWonField) {
        JButton submitButton = new JButton("End Session");
        submitButton.addActionListener(e -> {
            float numGamesPlayed = ((Long) numGamesPlayedField.getValue()).floatValue();
            float numGamesWon = ((Long) numGamesWonField.getValue()).floatValue();
            try {
                gamePartyFinder.endSession(gameParty, numGamesWon, numGamesPlayed);

            } catch (PartyNotInFinderException partyNotInFinderException) {
                System.err.println(partyNotInFinderException.getMessage());
            }
            returnOriginalState();
        });
        return submitButton;
    }

    // MODIFIES: this
    // EFFECTS: open selection dialogs to select a GameParty from a list in gamePartyFinder
    private void endGameSession() {
        openClassSelectionDialog("GameParty", "End Session");
    }

    // EFFECTS: If className is "Person", index through gamePartyFinder.getPeople() and find match to personNameSelected
    // and return Person as Object. Else if className is "Game", index through gamePartyFinder.getGames() and find match
    // to gameNameSelected and return Game as Object. Else if className is "GameParty", index through
    // gamePartyFinder.getGameParties() and find match to gamePartyNameSelected and return GameParty as Object.
    private Object matchNameToClass(String className) {
        Object object = null;
        if (className.equals("Person")) {
            for (Person p : gamePartyFinder.getPeople()) {
                if (p.getName().equals(personNameSelected)) {
                    object = p;
                }
            }
        } else if (className.equals("Game")) {
            for (Game g : gamePartyFinder.getGames()) {
                if (g.getName().equals(gameNameSelected)) {
                    object = g;
                }
            }
        } else if (className.equals("GameParty")) {
            for (GameParty gp : gamePartyFinder.getGameParties()) {
                if (gp.getName().equals(gamePartyNameSelected)) {
                    object = gp;
                }
            }
        }
        return object;
    }

    // EFFECTS: if selectionType equals "Person", generate list of people names. if equals "Game", generate list of game
    // names. if equals "Game Party", generate list of game parties
    private ArrayList<String> getGamePartyFinderFieldListNames(String fieldClassName) throws NoSuchElementException {
        ArrayList<String> list = new ArrayList<>();
        switch (fieldClassName) {
            case "Person":
                list = gamePartyFinder.getPeopleNames();
                break;
            case "Game":
                list = gamePartyFinder.getGameNames();
                break;
            case "GameParty":
                list = gamePartyFinder.getGamePartyNames();
                break;
        }

        if (list.isEmpty()) {
            throw new NoSuchElementException("List of " + fieldClassName + " is empty");
        } else {
            return list;
        }
    }

    // EFFECTS: creates a cancel button that brings user back to the home screen
    private JButton createCancelButton() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> returnOriginalState());

        return cancelButton;
    }

    // MODIFIES: this
    // EFFECTS: adds a button panel to this, with a submit button and cancel button
    private void createButtonPanel(JButton submitButton) {
        JPanel buttonPane = new JPanel();
        buttonPane.add(createCancelButton());
        buttonPane.add(submitButton);
        add(buttonPane, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: add label and field panels to this
    private void createLabelFieldPanel(JPanel labelPanel, JPanel fieldPanel) {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(labelPanel, BorderLayout.WEST);
        add(fieldPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: displays people and roles, adds a save load button
    private void returnOriginalState() {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        displayGames();
        addSaveLoadButtons();
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: generates JLabels of each person in the gamePartyFinder and all of a person's associated games
    private void displayGames() {
        for (Person person : gamePartyFinder.getPeople()) {
            JPanel personPanel = new JPanel();
            personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));

            JLabel personLabel = new JLabel("Person: " + person.getName());
            personPanel.add(personLabel);

            ArrayList<Game> games = person.getRoles();
            for (Game game : games) {
                JLabel gameLabel = new JLabel("Game: " + game.getName());
                personPanel.add(gameLabel);
            }

            add(personPanel);
            add(Box.createVerticalStrut(10));
        }
    }

    // MODIFIES: this
    // EFFECTS: add load and save buttons to a panel, and add panel to this
    private void addSaveLoadButtons() {
        JPanel saveLoadPanel = new JPanel();
        saveLoadPanel.add(createLoadButton());
        saveLoadPanel.add(createSaveButton());
        add(saveLoadPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: gamePartyFinder
    // EFFECTS: creates a JButton that loads JSON data to gamePartyFinder from JSON_STORE path
    private JButton createLoadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            try {
                gamePartyFinder = jsonReader.read();
                System.out.println("Loaded " +  " from " + JSON_STORE);
                returnOriginalState();
            } catch (IOException exception) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        });
        return loadButton;
    }

    // MODIFIES: gamePartyFinder
    // EFFECTS: creates a JButton that saves JSON data from gamePartyFinder to JSON_STORE path
    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                jsonWriter.open();
                jsonWriter.write(gamePartyFinder);
                jsonWriter.close();
                System.out.println("Saved "  + " to " + JSON_STORE);
            } catch (FileNotFoundException exception) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        });
        return saveButton;
    }

    // EFFECT: Returns an ImageIcon, or null if the path was invalid.
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SwingUi.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    // EFFECT: Scale ImageIcon down to specified width and height
    private ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        if (icon != null) {
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            return new ImageIcon(scaledImg);
        } else {
            throw new NullPointerException("Image not found");
        }
    }

    // EFFECT: print out all event descriptions in eventLog
    private static void printEventLog() {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.getDescription());
        }
    }

    // EFFECT: Create the GUI and show it
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MainMenu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEventLog();
            }
        });

        SwingUi newContentPane = new SwingUi();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setJMenuBar(menuBar);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setSize(500, 500);
        frame.setVisible(true);
    }


    // EFFECT: run the UI menu
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(SwingUi::createAndShowGUI);
    }
}