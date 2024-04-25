package model;

import exceptions.GameNotInFinderException;
import exceptions.NotInFinderException;
import exceptions.PersonNotInFinderException;
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
        assertFalse(testGamePartyFinder.getGameNames().contains(game3.getName()));
        testGamePartyFinder.addGame(game2);
        assertEquals(2, testGamePartyFinder.getGames().size());
        assertTrue(testGamePartyFinder.getGames().contains(game2));
    }

    @Test
    public void testAddGameParty() {
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
    public void testAddPersonToGamePartyNoExceptions() {
        try {
            testGamePartyFinder.addPerson(person1);
            testGamePartyFinder.addPerson(person2);
            testGamePartyFinder.addGame(game1);
            testGamePartyFinder.addGameParty(party1);
            testGamePartyFinder.addRoleToPerson(person1, game1);
            testGamePartyFinder.addPersonToGameParty(person1, party1);
        } catch (NotInFinderException e) {
            fail();
        }

        assertEquals(1, party1.getCurrentNumOfMembers());
        assertTrue(party1.getCurrentMembers().contains(person1));

         try {
             testGamePartyFinder.addRoleToPerson(person2, game1);
             testGamePartyFinder.addPersonToGameParty(person2, party1);
         } catch (NotInFinderException e) {
             fail();
         }

        assertEquals(2, party1.getCurrentNumOfMembers());
        assertTrue(party1.getCurrentMembers().contains(person2));
    }

    @Test
    public void testAddPersonToGamePartyException() {
        try {
            testGamePartyFinder.addPerson(person1);
            testGamePartyFinder.addPerson(person2);
            testGamePartyFinder.addGame(game1);
            testGamePartyFinder.addGameParty(party1);
            testGamePartyFinder.addRoleToPerson(person1, game1);
            testGamePartyFinder.addPersonToGameParty(person1, party1);
            assertEquals(1, party1.getCurrentNumOfMembers());
            assertTrue(party1.getCurrentMembers().contains(person1));
            testGamePartyFinder.addRoleToPerson(person2, game1);
            testGamePartyFinder.addPersonToGameParty(person2, party1);
            assertEquals(2, party1.getCurrentNumOfMembers());
            assertTrue(party1.getCurrentMembers().contains(person2));
        } catch (NotInFinderException notInFinderException) {
            fail();
        }

        try {
            testGamePartyFinder.addPersonToGameParty(person3, party1);
            fail();
        } catch (PersonNotInFinderException e) {
            assertEquals(2, party1.getCurrentNumOfMembers());
            assertFalse(party1.getCurrentMembers().contains(person3));
        } catch (NotInFinderException e) {
            fail();
        }
    }

    @Test
    public void testAddRoleToPersonNoExceptions() {
        try {
            testGamePartyFinder.addPerson((person1));
            testGamePartyFinder.addGame(game1);
            testGamePartyFinder.addGame(game2);
            testGamePartyFinder.addRoleToPerson(person1, game1);
            testGamePartyFinder.addRoleToPerson(person1, game2);
        } catch (NotInFinderException e) {
            fail();
        }

        assertTrue(testGamePartyFinder.getRolesFromPerson(person1).contains(game1));
        assertTrue(testGamePartyFinder.getRolesFromPerson(person1).contains(game2));
        assertEquals(2, testGamePartyFinder.getRolesFromPerson(person1).size());
    }

    @Test
    public void testAddRoleToPersonPersonException() {
        try {
            testGamePartyFinder.addGame(game1);
            testGamePartyFinder.addRoleToPerson(person1, game1);
            fail();
        } catch (PersonNotInFinderException personNotInFinderException) {
            // pass
        } catch (NotInFinderException notInFinderException) {
            fail();
        }
        assertFalse(testGamePartyFinder.getPeople().contains(person1));
        assertTrue(testGamePartyFinder.getGames().contains(game1));
    }

    @Test
    public void testAddRoleToPersonGameException() {
        try {
            testGamePartyFinder.addPerson(person1);
            testGamePartyFinder.addRoleToPerson(person1, game1);
            fail();
        } catch (GameNotInFinderException gameNotInFinderException) {
            // pass
        } catch (NotInFinderException notInFinderException) {
            fail();
        }
        assertTrue(testGamePartyFinder.getPeople().contains(person1));
        assertFalse(testGamePartyFinder.getGames().contains(game1));
    }

    @Test
    public void testAddRoleToPersonNotFoundException() {
        try {
            testGamePartyFinder.addRoleToPerson(person1, game1);
            fail();
        } catch (PersonNotInFinderException personNotInFinderException) {
            fail();
        } catch (GameNotInFinderException gameNotInFinderException) {
            fail();
        } catch (NotInFinderException notInFinderException) {
            // pass
        }
        assertFalse(testGamePartyFinder.getPeople().contains(person1));
        assertFalse(testGamePartyFinder.getGames().contains(game1));
    }

    @Test
    public void testDeleteRoleToPerson() {
        try {
            testGamePartyFinder.addPerson((person1));
            testGamePartyFinder.addGame(game1);
            testGamePartyFinder.addGame(game2);
            testGamePartyFinder.addRoleToPerson(person1, game1);
            testGamePartyFinder.addRoleToPerson(person1, game2);
        } catch (NotInFinderException e) {
            fail();
        }
        assertEquals(2, testGamePartyFinder.getRolesFromPerson(person1).size());
        testGamePartyFinder.deleteRoleFromPerson(person1, game2);
        assertEquals(1, testGamePartyFinder.getRolesFromPerson(person1).size());
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
        assertTrue(testGamePartyFinder.getGamePartyNames().contains("party 2"));
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
    public void testRemovePerson() {
        testGamePartyFinder.addPerson(person1);
        testGamePartyFinder.addGame(game1);
        testGamePartyFinder.addGameParty(party1);

        try {
            testGamePartyFinder.addRoleToPerson(person1, game1);
            testGamePartyFinder.addPersonToGameParty(person1, party1);
        } catch (NotInFinderException e) {
            fail();
        }

        assertEquals(1, testGamePartyFinder.getPeople().size());
        assertTrue(testGamePartyFinder.getPeople().contains(person1));
        assertTrue(party1.getCurrentMembers().contains(person1));

        testGamePartyFinder.removePerson(person2);
        assertEquals(1, testGamePartyFinder.getPeople().size());
        assertTrue(testGamePartyFinder.getPeople().contains(person1));
        assertTrue(party1.getCurrentMembers().contains(person1));

        testGamePartyFinder.removePerson(person1);
        assertEquals(0, testGamePartyFinder.getPeople().size());
        assertFalse(testGamePartyFinder.getPeople().contains(person1));
        assertFalse(party1.getCurrentMembers().contains(person1));
    }

    @Test
    public void testRemoveGame() {
        testGamePartyFinder.addPerson(person1);
        testGamePartyFinder.addGame(game1);
        testGamePartyFinder.addGame(game2);
        testGamePartyFinder.addGameParty(party1);
        testGamePartyFinder.addGameParty(party2);

        try {
            testGamePartyFinder.addRoleToPerson(person1, game1);
            testGamePartyFinder.addRoleToPerson(person1, game2);
            testGamePartyFinder.addPersonToGameParty(person1, party1);
            testGamePartyFinder.addPersonToGameParty(person1, party2);
        } catch (NotInFinderException e) {
            fail();
        }

        testGamePartyFinder.removeGame(game1);
        assertFalse(testGamePartyFinder.getRolesFromPerson(person1).contains(game1));
        assertFalse(testGamePartyFinder.getGames().contains(game1));
        assertFalse(testGamePartyFinder.getGameParties().contains(party1));
        testGamePartyFinder.removeGame(game3);
        assertEquals(1, testGamePartyFinder.getGameParties().size());
        assertEquals(1, testGamePartyFinder.getGames().size());
    }

    @Test
    public void testRemoveGameParty() {
        testGamePartyFinder.addGameParty(party1);
        testGamePartyFinder.removeGameParty(party1);
        assertFalse(testGamePartyFinder.getGameParties().contains(party1));
    }

    @Test
    public void testEndSession() {
        testGamePartyFinder.addPerson(person1);
        testGamePartyFinder.addPerson(person2);
        testGamePartyFinder.addPerson(person3);
        testGamePartyFinder.addGame(game1);
        testGamePartyFinder.addGameParty(party1);

        try {
            testGamePartyFinder.addRoleToPerson(person1, game1);
            testGamePartyFinder.addRoleToPerson(person2, game1);
            testGamePartyFinder.addRoleToPerson(person3, game1);

            testGamePartyFinder.addPersonToGameParty(person1, party1);
            testGamePartyFinder.addPersonToGameParty(person2, party1);
            testGamePartyFinder.addPersonToGameParty(person3, party1);

            assertEquals(1, testGamePartyFinder.getGameParties().size());

            testGamePartyFinder.endSession(party1, 3, 3);
        } catch (NotInFinderException e) {
            fail();
        }

        assertEquals(2, person1.getGameStats().size());
        assertTrue(person1.getGameStats().containsKey(person2.getName()));
        assertEquals(100, person1.getGameStats().get(person2.getName()).get(0));
        assertEquals(3, person1.getGameStats().get(person2.getName()).get(1));
        assertEquals(3, person1.getGameStats().get(person2.getName()).get(2));
        assertTrue(person1.getGameStats().containsKey(person3.getName()));
        assertEquals(100, person1.getGameStats().get(person3.getName()).get(0));
        assertEquals(3, person1.getGameStats().get(person3.getName()).get(1));
        assertEquals(3, person1.getGameStats().get(person3.getName()).get(2));

        assertEquals(2, person2.getGameStats().size());
        assertTrue(person2.getGameStats().containsKey(person1.getName()));
        assertEquals(100, person2.getGameStats().get(person1.getName()).get(0));
        assertEquals(3, person2.getGameStats().get(person1.getName()).get(1));
        assertEquals(3, person2.getGameStats().get(person1.getName()).get(2));
        assertTrue(person2.getGameStats().containsKey(person3.getName()));
        assertEquals(100, person2.getGameStats().get(person3.getName()).get(0));
        assertEquals(3, person2.getGameStats().get(person3.getName()).get(1));
        assertEquals(3, person2.getGameStats().get(person3.getName()).get(2));

        assertEquals(2, person3.getGameStats().size());
        assertTrue(person3.getGameStats().containsKey(person1.getName()));
        assertEquals(100, person3.getGameStats().get(person1.getName()).get(0));
        assertEquals(3, person3.getGameStats().get(person1.getName()).get(1));
        assertEquals(3, person3.getGameStats().get(person1.getName()).get(2));
        assertTrue(person3.getGameStats().containsKey(person2.getName()));
        assertEquals(100, person3.getGameStats().get(person2.getName()).get(0));
        assertEquals(3, person3.getGameStats().get(person2.getName()).get(1));
        assertEquals(3, person3.getGameStats().get(person2.getName()).get(2));

        assertEquals(0, testGamePartyFinder.getGameParties().size());
    }

}
