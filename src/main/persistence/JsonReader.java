package persistence;

import model.Game;
import model.GameParty;
import model.GamePartyFinder;
import model.Person;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public GamePartyFinder read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGamePartyFinder(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private GamePartyFinder parseGamePartyFinder(JSONObject jsonObject) {
        GamePartyFinder gpf = new GamePartyFinder();
        addListOfPeople(gpf, jsonObject);
        addListOfGames(gpf, jsonObject);
        addListOfParties(gpf, jsonObject);
        return gpf;
    }

    // MODIFIES: gpf
    // EFFECTS: parses people from JSON object and adds them to GamePartyFinder
    private void addListOfPeople(GamePartyFinder gpf, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("people");
        for (Object json : jsonArray) {
            JSONObject nextPerson = (JSONObject) json;
            addPerson(gpf, nextPerson);
        }
    }

    // MODIFIES: gpf
    // EFFECTS: parses Person from JSON object and adds it to GamePartyFinder
    private void addPerson(GamePartyFinder gpf, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        JSONArray roles = jsonObject.getJSONArray("roles");
//        JSONArray gameParties = jsonObject.getJSONArray("game parties");

        Person person = new Person(name);
        person.setRoles(toArrayGameList(roles));
//        person.setGameParties(toArrayGamePartyList(gameParties));

        gpf.addPerson(person);
    }

    // MODIFIES: gpf
    // EFFECTS: parses games from JSON object and adds them to GamePartyFinder
    private void addListOfGames(GamePartyFinder gpf, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("games");
        for (Object json : jsonArray) {
            JSONObject nextGame = (JSONObject) json;
            addGame(gpf, nextGame);
        }
    }

    // MODIFIES: gpf
    // EFFECTS: parses Game from JSON object and adds it to GamePartyFinder
    private void addGame(GamePartyFinder gpf, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int maxPartySize = jsonObject.getInt("maxPartyMembers");
//        JSONArray listOfPlayers = jsonObject.getJSONArray("listOfPlayers");
        Game game = new Game(name, maxPartySize);
//        game.setListOfPlayers(toArrayPersonList(listOfPlayers));
        gpf.addGame(game);
    }

    // MODIFIES: gpf
    // EFFECTS: parses gameParties from JSON object and adds them to GamePartyFinder
    private void addListOfParties(GamePartyFinder gpf, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("game parties");
        for (Object json : jsonArray) {
            JSONObject nextGameParty = (JSONObject) json;
            addParty(gpf, nextGameParty);
        }
    }

    // MODIFIES: gpf
    // EFFECTS: parses GameParty from JSON object and adds it to GamePartyFinder
    private void addParty(GamePartyFinder gpf, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int maxPartySize = jsonObject.getInt("maxPartySize");
        JSONArray currentMembers = jsonObject.getJSONArray("currentMembers");

        String gameName = jsonObject.getJSONObject("game").getString("name");
        int gameMaxPartyMembers = jsonObject.getJSONObject("game").getInt("maxPartyMembers");
//        JSONArray gameListOfPlayers = jsonObject.getJSONObject("game").getJSONArray("listOfPlayers");
        Game game = new Game(gameName, gameMaxPartyMembers);
//        game.setListOfPlayers(toArrayPersonList(gameListOfPlayers));

        GameParty gameParty = new GameParty(game, maxPartySize, name);
        gameParty.setCurrentMembers(toArrayPersonList(currentMembers));
        gpf.addGame(game);
        gpf.addGameParty(gameParty);
    }


    private ArrayList<Person> toArrayPersonList(JSONArray array) {
        ArrayList<Person> listPerson = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String name = array.getJSONObject(i).getString("name");
            Person person = new Person(name);

            listPerson.add(person);
        }
        return listPerson;
    }

    private ArrayList<Game> toArrayGameList(JSONArray array) {
        ArrayList<Game> listGames = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String name = array.getJSONObject(i).getString("name");
            int maxPartySize = array.getJSONObject(i).getInt("maxPartyMembers");
//            JSONArray listOfPlayersJson = array.getJSONObject(i).getJSONArray("listOfPlayers");
            Game game = new Game(name, maxPartySize);
//            game.setListOfPlayers(toArrayPersonList(listOfPlayersJson));
            listGames.add(game);
        }
        return listGames;
    }

    private ArrayList<GameParty> toArrayGamePartyList(JSONArray array) {
        ArrayList<GameParty> listGameParties = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String name = array.getJSONObject(i).getString("name");
            int maxPartySize = array.getJSONObject(i).getInt("maxPartySize");
            JSONArray currentMembers = array.getJSONObject(i).getJSONArray("currentMembers");
            JSONObject gameJson = array.getJSONObject(i).getJSONObject("game");
            Game game = createGameFromJsonObject(gameJson);

            GameParty gameParty = new GameParty(game, maxPartySize, name);
            gameParty.setCurrentMembers(toArrayPersonList(currentMembers));
            listGameParties.add(gameParty);
        }
        return listGameParties;
    }

    private Game createGameFromJsonObject(JSONObject gameObject) {
        String gameName = gameObject.getString("name");
        int maxPartySize = gameObject.getInt("maxPartyMembers");
//        JSONArray listOfPlayers = gameObject.getJSONArray("listOfPlayers");
        Game game = new Game(gameName, maxPartySize);
//        game.setListOfPlayers(toArrayPersonList(listOfPlayers));

        return game;
    }
}
