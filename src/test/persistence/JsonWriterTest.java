package persistence;

import model.Game;
import model.GameParty;
import model.GamePartyFinder;
import model.Person;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Code was referenced from the Json Serialization Demo provided by the CPSC 210 Team
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            GamePartyFinder gpf = new GamePartyFinder();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGamePartyFinder() {
        try {
            GamePartyFinder gpf = new GamePartyFinder();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGamePartyFinder.json");
            writer.open();
            writer.write(gpf);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyGamePartyFinder.json");
            gpf = reader.read();
            assertEquals(0, gpf.getPeople().size());
            assertEquals(0, gpf.getGames().size());
            assertEquals(0, gpf.getGameParties().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGamePartyFinder() {
        try {
            GamePartyFinder gpf = new GamePartyFinder();
            Game game = new Game("League of Legends", 5);
            Person person = new Person("Aaron");
            Person person2 = new Person("Paolo");
            Person person3 = new Person("Renee");
            GameParty party = new GameParty(game, 5, "party 1");
            gpf.addPerson(person);
            gpf.addPerson(person2);
            gpf.addPerson(person3);
            gpf.addGame(game);
            gpf.addGameParty(party);
            gpf.addRoleToPerson(person, game);
            gpf.addRoleToPerson(person2, game);
            gpf.addRoleToPerson(person3, game);
            gpf.addPersonToGameParty(person, party);
            gpf.addPersonToGameParty(person2, party);
            gpf.addPersonToGameParty(person3, party);
            gpf.endSession(party, 3, 3);
            JsonWriter writer = new JsonWriter("./data/testWriterGamePartyFinder.json");
            writer.open();
            writer.write(gpf);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGamePartyFinder.json");
            gpf = reader.read();
            assertEquals(3, gpf.getPeople().size());
            assertTrue(gpf.getPeopleNames().contains("Aaron"));
            assertEquals(1, gpf.getGames().size());
            assertTrue(gpf.getGameNames().contains("League of Legends"));
            assertEquals(0, gpf.getGameParties().size());
            assertFalse(gpf.getGamePartyNames().contains("party 1"));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
