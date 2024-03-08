package persistence;

import model.Game;
import model.GameParty;
import model.GamePartyFinder;
import model.Person;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JavaReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            GamePartyFinder gpf = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyGamePartyFinder() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyGamePartyFinder.json");
        try {
            GamePartyFinder gpf = reader.read();
            assertEquals(0, gpf.getPeople().size());
            assertEquals(0, gpf.getGames().size());
            assertEquals(0, gpf.getGameParties().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGamePartyFinder() {
        try {
            GamePartyFinder gpf = new GamePartyFinder();
            Game game = new Game("Valorant", 5);
            Person person = new Person("Paolo");
            GameParty party = new GameParty(game, 3, "valorant party");
            gpf.addPerson(person);
            gpf.addGame(game);
            gpf.addGameParty(party);
            gpf.addRoleToPerson(person, game);
            gpf.addPersonToGameParty(person, party);
            JsonWriter writer = new JsonWriter("./data/testReaderGamePartyFinder.json");
            writer.open();
            writer.write(gpf);
            writer.close();

            JsonReader reader = new JsonReader("./data/testReaderGamePartyFinder.json");
            gpf = reader.read();
            assertEquals(1, gpf.getGames().size());
            assertTrue(gpf.getGameNames().contains("Valorant"));
            assertEquals(1, gpf.getPeople().size());
            assertTrue(gpf.getPeopleNames().contains("Paolo"));
            assertEquals(1, gpf.getRolesFromPerson(person).size());
            assertTrue(gpf.getRolesFromPerson(person).contains(game));
            assertEquals(1, gpf.getGameParties().size());
            assertTrue(gpf.getGamePartyNames().contains("valorant party"));
            assertEquals(3, party.getMaxPartySize());
            assertTrue(party.getCurrentMembers().contains(person));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
