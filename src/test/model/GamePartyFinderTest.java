package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePartyFinderTest {
    private GamePartyFinder testGamePartyFinder;
    private Game game1;
    private Game game2;
    private Person person1;
    private Person person2;
    private Person person3;
    private GameParty party1;

    @BeforeEach
    public void runBefore() {
        testGamePartyFinder = new GamePartyFinder();
        game1 = new Game("League of Legends", 5);
        game2 = new Game("Valorant", 5);
        person1 = new Person("Aaron");
        person2 = new Person("Nic");
        person3 = new Person("Renee");
        party1 = new GameParty(game1, 5, "party 1");
    }

    @Test
    public void testAddPerson() {
        testGamePartyFinder.addPerson("Aaron");
        assertEquals(1, testGamePartyFinder.getPeople().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Aaron"));
        testGamePartyFinder.addPerson("Nic");
        assertEquals(2, testGamePartyFinder.getPeople().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Nic"));
        testGamePartyFinder.addPerson("Aaron");
        assertEquals(2, testGamePartyFinder.getPeople().size());
    }

    @Test
    public void testAddGame() {
        testGamePartyFinder.addGame("League", 5);
        assertEquals(1, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGameNames().contains("League"));
        testGamePartyFinder.addGame("league", 5);
        assertEquals(1, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGameNames().contains("League"));
        assertFalse(testGamePartyFinder.getGameNames().contains("league"));
        testGamePartyFinder.addGame("Valorant", 3);
        assertEquals(2, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGameNames().contains("Valorant"));
        testGamePartyFinder.addGame("VALORANT", 3);
        assertEquals(2, testGamePartyFinder.getGames().size());
        assertFalse(testGamePartyFinder.getGameNames().contains("VALORANT"));
        testGamePartyFinder.addGame("CS2", 5);
        assertEquals(3, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGameNames().contains("CS2"));

    }

    @Test
    public void testCreateGameParty() {
        testGamePartyFinder.createGameParty(game1, 5, "league 1");
        assertEquals(1, testGamePartyFinder.getGameParties().size());
        testGamePartyFinder.createGameParty(game1, 3, "league 2");
        assertEquals(2, testGamePartyFinder.getGameParties().size());

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
        testGamePartyFinder.createGameParty(game2, 5, "party 2");
        assertEquals(1, testGamePartyFinder.getGamePartyNames().size());
        assertTrue(testGamePartyFinder.getGamePartyNames().contains("party 2"));
        testGamePartyFinder.createGameParty(game2, 5, "party 3");
        assertEquals(2, testGamePartyFinder.getGamePartyNames().size());
        assertTrue(testGamePartyFinder.getGamePartyNames().contains("party 3"));
    }

    @Test
    public void testGetGameNames() {
        testGamePartyFinder.addGame("Diablo 3", 4);
        assertTrue(testGamePartyFinder.getGameNames().contains("Diablo 3"));
        assertEquals(1, testGamePartyFinder.getGameNames().size());
        testGamePartyFinder.addGame("Fortnite", 4);
        assertTrue(testGamePartyFinder.getGameNames().contains("Fortnite"));
        assertEquals(2, testGamePartyFinder.getGameNames().size());
        testGamePartyFinder.addGame("CS2", 5);
        assertEquals(3, testGamePartyFinder.getGameNames().size());
        assertTrue(testGamePartyFinder.getGameNames().contains("CS2"));
    }

    @Test
    public void testGetPeopleNames() {
        testGamePartyFinder.addPerson("Paolo");
        assertEquals(1, testGamePartyFinder.getPeopleNames().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Paolo"));
        testGamePartyFinder.addPerson("Ryan");
        assertEquals(2, testGamePartyFinder.getPeopleNames().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Ryan"));
        testGamePartyFinder.addPerson("Caeleb");
        assertEquals(3, testGamePartyFinder.getPeopleNames().size());
        assertTrue(testGamePartyFinder.getPeopleNames().contains("Caeleb"));
    }

}
