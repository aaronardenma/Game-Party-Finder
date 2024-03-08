package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePartyFinderTest {
    private GamePartyFinder testGamePartyFinder;
    private Game game1;
    private Game game2;
    private Game game3;
    private Person person1;
    private Person person2;
    private Person person3;
    private GameParty party1;
    private GameParty party2;

    @BeforeEach
    public void runBefore() {
        testGamePartyFinder = new GamePartyFinder();
        game1 = new Game("League of Legends", 5);
        game2 = new Game("Valorant", 5);
        game3 = new Game("league of legends", 5);
        person1 = new Person("Aaron");
        person2 = new Person("Nic");
        person3 = new Person("Renee");
        party1 = new GameParty(game1, 5, "party 1");
        party2 = new GameParty(game2, 5, "party 2");
    }

    @Test
    public void testAddPerson() {
        testGamePartyFinder.addPerson(person1);
        assertEquals(1, testGamePartyFinder.getPeople().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Aaron"));
        testGamePartyFinder.addPerson(person2);
        assertEquals(2, testGamePartyFinder.getPeople().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Nic"));
        testGamePartyFinder.addPerson(person1);
        assertEquals(2, testGamePartyFinder.getPeople().size());
    }

    @Test
    public void testAddGame() {
        testGamePartyFinder.addGame(game1);
        assertEquals(1, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGames().contains(game1));
        testGamePartyFinder.addGame(game3);
        assertEquals(1, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGames().contains(game1));
        assertFalse(testGamePartyFinder.getGameNames().contains(game3));
        testGamePartyFinder.addGame(game2);
        assertEquals(2, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGames().contains(game2));

    }

    @Test
    public void testCreateGameParty() {
        testGamePartyFinder.addGameParty(party1);
        assertEquals(1, testGamePartyFinder.getGameParties().size());
        assertTrue(testGamePartyFinder.getGameParties().contains(party1));
        testGamePartyFinder.addGameParty(party1);
        assertEquals(1, testGamePartyFinder.getGameParties().size());
        testGamePartyFinder.addGameParty(party2);
        assertEquals(2, testGamePartyFinder.getGameParties().size());
        assertTrue(testGamePartyFinder.getGameParties().contains(party2));


    }

    @Test
    public void testAddPersonToGameParty() {
        testGamePartyFinder.addRoleToPerson(person1, game1);
        testGamePartyFinder.addPersonToGameParty(person1, party1);
        assertEquals(1, party1.getCurrentNumOfMembers());
        assertTrue(party1.getCurrentMembers().contains(person1));

        testGamePartyFinder.addRoleToPerson(person2, game1);
        testGamePartyFinder.addPersonToGameParty(person2, party1);
        assertEquals(2, party1.getCurrentNumOfMembers());
        assertTrue(party1.getCurrentMembers().contains(person2));

        testGamePartyFinder.addPersonToGameParty(person3, party1);
        assertEquals(2, party1.getCurrentNumOfMembers());
        assertFalse(party1.getCurrentMembers().contains(person3));
    }

    @Test
    public void testAddRoleToPerson() {
        testGamePartyFinder.addRoleToPerson(person1, game1);
        assertTrue(testGamePartyFinder.getRolesFromPerson(person1).contains(game1));
        testGamePartyFinder.addRoleToPerson(person1, game1);
        assertEquals(1, testGamePartyFinder.getRolesFromPerson(person1).size());
        testGamePartyFinder.addRoleToPerson(person1, game2);
        assertEquals(2, testGamePartyFinder.getRolesFromPerson(person1).size());
        assertTrue(testGamePartyFinder.getRolesFromPerson(person1).contains(game2));
    }

    @Test
    public void testDeleteRoleToPerson() {
        assertEquals(0, testGamePartyFinder.getRolesFromPerson(person1).size());
        testGamePartyFinder.addRoleToPerson(person1, game1);
        assertEquals(1, testGamePartyFinder.getRolesFromPerson(person1).size());
        assertTrue(testGamePartyFinder.getRolesFromPerson(person1).contains(game1));
        testGamePartyFinder.deleteRoleFromPerson(person1, game2);
        assertEquals(1, testGamePartyFinder.getRolesFromPerson(person1).size());
        testGamePartyFinder.addRoleToPerson(person1, game2);
        assertEquals(2, testGamePartyFinder.getRolesFromPerson(person1).size());
        assertTrue(testGamePartyFinder.getRolesFromPerson(person1).contains(game2));
        testGamePartyFinder.deleteRoleFromPerson(person1, game2);
        assertEquals(1, testGamePartyFinder.getRolesFromPerson(person1).size());

    }

    @Test
    public void testChangePartySize() {
        assertEquals(5, party1.getMaxPartySize());
        testGamePartyFinder.changePartySize(party1, 3);
        assertEquals(3, party1.getMaxPartySize());
        testGamePartyFinder.changePartySize(party1, 6);
        assertEquals(3, party1.getMaxPartySize());
    }

    @Test
    public void testGetGamePartyNames() {
        testGamePartyFinder.addGameParty(party1);
        assertEquals(1, testGamePartyFinder.getGamePartyNames().size());
        assertTrue(testGamePartyFinder.getGamePartyNames().contains("party 1"));
        testGamePartyFinder.addGameParty(party2);
        assertEquals(2, testGamePartyFinder.getGamePartyNames().size());
        assertTrue(testGamePartyFinder.getGamePartyNames().contains("party 3"));
    }

    @Test
    public void testGetGameNames() {
        testGamePartyFinder.addGame(game1);
        assertTrue(testGamePartyFinder.getGameNames().contains("League of Legends"));
        assertEquals(1, testGamePartyFinder.getGameNames().size());
        testGamePartyFinder.addGame(game2);
        assertTrue(testGamePartyFinder.getGameNames().contains("Valorant"));
        assertEquals(2, testGamePartyFinder.getGameNames().size());
    }

    @Test
    public void testGetPeopleNames() {
        testGamePartyFinder.addPerson(person1);
        assertEquals(1, testGamePartyFinder.getPeopleNames().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Aaron"));
        testGamePartyFinder.addPerson(person2);
        assertEquals(2, testGamePartyFinder.getPeopleNames().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Nic"));
        testGamePartyFinder.addPerson(person3);
        assertEquals(3, testGamePartyFinder.getPeopleNames().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Renee"));
    }

    @Test
    public void testEndSession() {
        testGamePartyFinder.addPerson(person1);
        testGamePartyFinder.addPerson(person2);
        testGamePartyFinder.addPerson(person3);
        testGamePartyFinder.addGame(game1);
        testGamePartyFinder.addRoleToPerson(person1, game1);
        testGamePartyFinder.addRoleToPerson(person2, game1);
        testGamePartyFinder.addRoleToPerson(person3, game1);
        testGamePartyFinder.addGameParty(party1);
        testGamePartyFinder.addPersonToGameParty(person1, party1);
        testGamePartyFinder.addPersonToGameParty(person2, party1);
        testGamePartyFinder.addPersonToGameParty(person3, party1);

        assertEquals(1, testGamePartyFinder.getGameParties().size());
        testGamePartyFinder.endSession(party1, 3, 3);
        assertEquals(2, person1.getGameStats().size());
        assertTrue(person1.getGameStats().containsKey(person2));
        assertEquals(100, person1.getGameStats().get(person2).get(0));
        assertEquals(3, person1.getGameStats().get(person2).get(1));
        assertEquals(3, person1.getGameStats().get(person2).get(2));
        assertTrue(person1.getGameStats().containsKey(person3));
        assertEquals(100, person1.getGameStats().get(person3).get(0));
        assertEquals(3, person1.getGameStats().get(person3).get(1));
        assertEquals(3, person1.getGameStats().get(person3).get(2));


        assertEquals(2, person2.getGameStats().size());
        assertTrue(person2.getGameStats().containsKey(person1));
        assertEquals(100, person2.getGameStats().get(person1).get(0));
        assertEquals(3, person2.getGameStats().get(person1).get(1));
        assertEquals(3, person2.getGameStats().get(person1).get(2));
        assertTrue(person2.getGameStats().containsKey(person3));
        assertEquals(100, person2.getGameStats().get(person3).get(0));
        assertEquals(3, person2.getGameStats().get(person3).get(1));
        assertEquals(3, person2.getGameStats().get(person3).get(2));


        assertEquals(2, person3.getGameStats().size());
        assertTrue(person3.getGameStats().containsKey(person1));
        assertEquals(100, person3.getGameStats().get(person1).get(0));
        assertEquals(3, person3.getGameStats().get(person1).get(1));
        assertEquals(3, person3.getGameStats().get(person1).get(2));
        assertTrue(person3.getGameStats().containsKey(person2));
        assertEquals(100, person3.getGameStats().get(person2).get(0));
        assertEquals(3, person3.getGameStats().get(person2).get(1));
        assertEquals(3, person3.getGameStats().get(person2).get(2));

        assertEquals(0, testGamePartyFinder.getGameParties().size());
    }

}
