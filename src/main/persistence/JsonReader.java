package persistence;

import model.Game;
import model.GameParty;
import model.GamePartyFinder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        gpf.addPerson(name);
    }

    // MODIFIES: gpf
    // EFFECTS: parses games from JSON object and adds them to GamePartyFinder
    private void addListOfGames(GamePartyFinder gpf, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("game");
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
        gpf.addGame(name, maxPartySize);
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
        Game game = (Game) jsonObject.get("game");
        gpf.createGameParty(game, maxPartySize, name);
    }
}
