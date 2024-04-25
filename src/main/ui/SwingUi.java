package ui;

import exceptions.GameNotInFinderException;
import exceptions.NotInFinderException;
import exceptions.PartyNotInFinderException;
import exceptions.PersonNotInFinderException;
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

// GamePartyFinder Swing UI App
public class SwingUi extends JPanel implements ActionListener {
    private GamePartyFinder gamePartyFinder;
    private static JMenuBar menuBar;
    private final JMenu personMenu;
    private final JMenu gameMenu;
    private final JMenu gamePartyMenu;
    private JButton submitButton;
    private String personNameSelected;
    private String gameNameSelected;
    private String gamePartyNameSelected;
    private static final String JSON_STORE = "./data/testWriterGamePartyFinder.json";
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // EFFECTS: Create GamePartyFinder and JMenuBar. Add Person, Game, and Game Parties menu item to Menu Bar. Create
    // save and load buttons with JsonWriter and JsonReader constructors. set personNameSelected and gameNameSelected
    // to null. Create ImageIcon of video-game.png to display as a splash screen.
    public SwingUi() {
        gamePartyFinder = new GamePartyFinder();
        menuBar = new JMenuBar();
        personMenu = new JMenu("People");
        gameMenu = new JMenu("Game");
        gamePartyMenu = new JMenu("Game Parties");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        personNameSelected = null;
        gameNameSelected = null;
        gamePartyNameSelected = null;
        menuBar.add(personMenu);
        menuBar.add(gameMenu);
        menuBar.add(gamePartyMenu);
        setLayout(new BorderLayout());
        ImageIcon icon = createImageIcon();
        ImageIcon scaledIcon = scaleImageIcon(icon, 200, 200);
        JLabel imageLabel = new JLabel(scaledIcon);
        add(imageLabel, BorderLayout.CENTER);

        addPersonMenuItems();
        addGameMenuItems();
        addGamePartyMenuItems();
        displayGames();
        addSaveLoadButtons();
    }

    // MODIFIES: this
    // EFFECT: adds menu items to the Person Menu
    private void addPersonMenuItems() {
        JMenuItem addPersonItem = new JMenuItem("Add Person");
        addPersonItem.addActionListener(this);
        addPersonItem.setActionCommand("Add Person");

        JMenuItem addRoleItem = new JMenuItem("Add Role");
        addRoleItem.addActionListener(this);
        addRoleItem.setActionCommand("Add Role");

        JMenuItem deleteRoleItem = new JMenuItem("Delete Role");
        deleteRoleItem.addActionListener(this);
        deleteRoleItem.setActionCommand("Delete Role");

        JMenuItem addToGamePartyItem = new JMenuItem("Add to Game Party");
        addToGamePartyItem.addActionListener(this);
        addToGamePartyItem.setActionCommand("Add to Game Party");
        personMenu.add(addPersonItem);
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
        JMenuItem addGamePartyItem = new JMenuItem("Create Game Party");
        addGamePartyItem.addActionListener(e -> createGameParty());

        gamePartyMenu.add(addGamePartyItem);
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: if action command equals "Add Person", call addPersonFields(). if equals "Add Role",
    // call addRoleFields(). if equals "Delete Role", call deleteRoles()
    public void actionPerformed(ActionEvent e) {
        if ("Add Person".equals(e.getActionCommand())) {
            addPersonFields();
        } else if ("Add Role".equals(e.getActionCommand())) {
            addRoleFields();
        } else if ("Delete Role".equals(e.getActionCommand())) {
            deleteRoles();
        } else if ("Add to Game Party".equals(e.getActionCommand())) {
            addToGamePartySelectionDialogs("Person", false);
        }
    }

    // MODIFIES: this
    // EFFECTS: create a text field for user to input new Person's name with corresponding label to create a new person
    private void addPersonFields() {
        removeAll();
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField(20);
        nameField.addActionListener(e -> {
            submitButton.doClick(); // Simulate button click when Enter is pressed
        });

        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        labelPane.add(nameLabel);
        JPanel fieldPane = new JPanel(new GridLayout(0, 1));
        fieldPane.add(nameField);

        createLabelFieldPanel(labelPane, fieldPane);
        createButtonPanel(createAddPersonSubmitButton(nameField));

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: create a submit button for person name text field that creates a new Person based on that name
    private JButton createAddPersonSubmitButton(JTextField nameField) {
        // Create a submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // Get the text from the text field
            String name = nameField.getText();
            gamePartyFinder.addPerson(new Person(name));
            returnOriginalState();
        });
        return submitButton;
    }

    // MODIFIES: this
    // EFFECTS: calls openPersonAndGameSelectionDialog() to add selection dialogs to select person and game to add
    private void addRoleFields() {
        choosePersonAndRoleThenAdd("Person", false);

    }

    // MODIFIES: this
    // EFFECTS: calls openPersonAndGameSelectionDialog() to add selection dialogs to select person and game to add
    private void choosePersonAndRoleThenAdd(String selectionType, Boolean last) {
        removeAll();
        JList<String> itemList = new JList<>(matchType(selectionType).toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JButton setButton = new JButton("Set");

        setButton.addActionListener(e -> {
            if (selectionType.equals("Person")) {
                personNameSelected = itemList.getSelectedValue();
            } else if (selectionType.equals("Game")) {
                gameNameSelected = itemList.getSelectedValue();
                addRole();
            }
            checkRecursiveAddRole(last);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createCancelButton());
        buttonPanel.add(setButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: use personNameSelected and gameNameSelected to find person and game. then add role game to person
    private void addRole() {
        Person person = (Person) matchNameToClass("Person");
        Game game = (Game) matchNameToClass("Game");

        try {
            gamePartyFinder.addRoleToPerson(person, game);
        } catch (PersonNotInFinderException pe) {
            System.err.println("Person not found");
        } catch (GameNotInFinderException ge) {
            System.err.println("Game not found");
        } catch (NotInFinderException ne) {
            System.err.println("Not found exception thrown");
        }
    }

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
        } else if (className.equals("Game Party")) {
            for (GameParty gp : gamePartyFinder.getGameParties()) {
                if (gp.getName().equals(gamePartyNameSelected)) {
                    object = gp;
                }
            }
        }
        return object;
    }

    // MODIFIES: this
    // EFFECTS: if last is true, go back to the original state of the ui, if not, continue with the game
    // selection dialog
    private void checkRecursiveAddRole(Boolean last) {
        if (last) {
            setVisible(true);
            returnOriginalState();
        } else {
            choosePersonAndRoleThenAdd("Game", true);
        }
    }

    // MODIFIES: this
    // EFFECTS: open people selection dialog and view roles to allow user to select role to delete with a cancel and
    // submit button
    private void deleteRoles() {
        removeAll();
        JList<String> itemList = new JList<>(gamePartyFinder.getPeopleNames().toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JButton setButton = new JButton("Set");

        setButton.addActionListener(e -> {
            personNameSelected = itemList.getSelectedValue();
            ArrayList<Person> people = gamePartyFinder.getPeople();
            Person personSelected = null;

            for (Person p : people) {
                if (p.getName().equals(personNameSelected)) {
                    personSelected = p;
                }
            }
            viewRoles(personSelected);
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        createButtonPanel(setButton);

        revalidate();
        repaint();
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: create a selection dialog to display all roles of a person that can be submitted to delete role
    // from person with a cancel and submit button
    private void viewRoles(Person p) {
        removeAll();
        JList<String> itemList = new JList<>(p.getRoleNames().toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JButton setButton = new JButton("Set");

        setButton.addActionListener(e -> {
            gameNameSelected = itemList.getSelectedValue();
            Game gameSelected = null;
            for (Game g : gamePartyFinder.getGames()) {
                if (g.getName().equals(gameNameSelected)) {
                    gameSelected = g;
                }
            }
            gamePartyFinder.deleteRoleFromPerson(p, gameSelected);
            returnOriginalState();
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        createButtonPanel(setButton);

        revalidate();
        repaint();
    }

    // EFFECTS: if selectionType equals "Person", generate list of people names. if equals "Game", generate list of game
    // names. if equals "Game Party", generate list of game parties
    private ArrayList<String> matchType(String selectionType) {
        ArrayList<String> list = null;
        switch (selectionType) {
            case "Person":
                list = gamePartyFinder.getPeopleNames();
                break;
            case "Game":
                list = gamePartyFinder.getGameNames();
                break;
            case "Game Party":
                list = gamePartyFinder.getGamePartyNames();
                break;
        }
        return list;
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: add fields related to Game construction and add to gamePartyFinder with a submit and cancel button
    private void addGameFields() {
        removeAll();
        JLabel nameLabel = new JLabel("Game Name: ");
        JLabel maxPartySizeLabel = new JLabel("Max Party Size: ");
        JTextField nameField = new JTextField(10);
        nameField.addActionListener(this);
        JFormattedTextField maxPartySizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
        maxPartySizeField.addActionListener(this);
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

    private void createGameParty() {
        removeAll();
        JList<String> itemList = new JList<>(matchType("Game").toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JButton setButton = new JButton("Set");

        setButton.addActionListener(e -> {
            gameNameSelected = itemList.getSelectedValue();
            Game game = (Game) matchNameToClass("Game");
            addCreateGamePartyFields(game);
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        createButtonPanel(setButton);

        revalidate();
        repaint();
    }

    private void addCreateGamePartyFields(Game game) {
        removeAll();
        JLabel nameLabel = new JLabel("Party Name: ");
        JLabel maxPartySizeLabel = new JLabel("Max Party Size: ");
        JTextField nameField = new JTextField(10);
        nameField.addActionListener(this);
        JFormattedTextField maxPartySizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
        maxPartySizeField.addActionListener(this);
        maxPartySizeField.setColumns(10);

        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        labelPane.add(nameLabel);
        labelPane.add(maxPartySizeLabel);

        JPanel fieldPane = new JPanel(new GridLayout(0, 1));
        fieldPane.add(nameField);
        fieldPane.add(maxPartySizeField);

        createLabelFieldPanel(labelPane, fieldPane);
        createButtonPanel(createAddGamePartySubmitButton(game, nameField, maxPartySizeField));

        revalidate();
        repaint();
    }

    private void addToGamePartySelectionDialogs(String selectionType, boolean last) {
        removeAll();
        JList<String> itemList = new JList<>(matchType(selectionType).toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(itemList);
        JButton setButton = new JButton("Set");

        setButton.addActionListener(e -> {
            if (selectionType.equals("Person")) {
                personNameSelected = itemList.getSelectedValue();
            } else if (selectionType.equals("Game Party")) {
                gamePartyNameSelected = itemList.getSelectedValue();
                addToGameParty();
            }
            checkRecursiveAddToGameParty(last);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createCancelButton());
        buttonPanel.add(setButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: if last is true, go back to the original state of the ui, if not, continue with the game
    // selection dialog
    private void checkRecursiveAddToGameParty(Boolean last) {
        if (last) {
            setVisible(true);
            returnOriginalState();
        } else {
            addToGamePartySelectionDialogs("Game Party", true);
        }
    }

    private void addToGameParty() {
        Person person = (Person) matchNameToClass("Person");
        GameParty gameParty = (GameParty) matchNameToClass("Game Party");

        try {
            gamePartyFinder.addPersonToGameParty(person, gameParty);
        } catch (PersonNotInFinderException personNotInFinderException) {
            System.err.println("Person not found");
        } catch (PartyNotInFinderException partyNotInFinderException) {
            System.err.println("Game Party not found");
        } catch (NotInFinderException e) {
            System.err.println("Not found in finder");
        }
    }

    private JButton createAddGamePartySubmitButton(Game game, JTextField partyName, JFormattedTextField maxPartySize) {
        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            // Get the text from the text field
            String name = partyName.getText();
            int partySize = ((Long) maxPartySize.getValue()).intValue();
            gamePartyFinder.addGameParty(new GameParty(game, partySize, name));
            returnOriginalState();
        });
        return button;
    }

    // MODIFIES: this, gamePartyFinder
    // EFFECTS: create a submit button that upon action, adds a game to the game party finder
    private JButton createAddGameSubmitButton(JTextField nameField, JFormattedTextField maxPartySizeField) {
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // Get the text from the text field
            String name = nameField.getText();
            int maxPartySize = ((Long) maxPartySizeField.getValue()).intValue();
            gamePartyFinder.addGame(new Game(name, maxPartySize));
            returnOriginalState();
        });
        return submitButton;
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
    private ImageIcon createImageIcon() {
        String path = "video-game.png";
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
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImg);
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