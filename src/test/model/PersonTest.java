package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {
    private Person testPerson;
    private GameParty gameParty1;
    private GameParty gameParty2;
    private GameParty gameParty3;
    private Game game1;
    private Game game2;
    private Game game3;

    @BeforeEach
    public void runBefore() {
        testPerson = new Person("Aaron");
        game1 = new Game("League of Legends", 5);
        game2 = new Game("Valorant", 5);
        game3 = new Game("CS2", 5);
        gameParty1 = new GameParty(5, game1);
        gameParty2 = new GameParty(3, game2);
        gameParty3 = new GameParty(4, game3);
    }

    @Test
    public void testGetName() {
        assertEquals("Aaron", testPerson.getName());
    }

    @Test
    public void testGetNumOfRolesNoRolesAdded() {
        assertEquals(0, testPerson.getNumOfRoles());
    }

    @Test
    public void testGetNumOfRoles1RolesAdded() {
        testPerson.addRole(game1);
        assertEquals(1, testPerson.getNumOfRoles());
    }

    @Test
    public void testGetNumOfRolesMultipleRolesAdded() {
        testPerson.addRole(game1);
        testPerson.addRole(game2);
        testPerson.addRole(game3);
        assertEquals(3, testPerson.getNumOfRoles());
    }

    @Test
    public void testGetGamePartiesNoParty() {
        assertEquals(0, testPerson.getGameParties().size());
    }

    @Test
    public void testGetGameParties1Party() {
        testPerson.addToGameParty(gameParty1);
        assertEquals(1, testPerson.getGameParties().size());
    }

    @Test
    public void testGetGamePartiesMultipleParty() {
        testPerson.addToGameParty(gameParty1);
        testPerson.addToGameParty(gameParty2);
        testPerson.addToGameParty(gameParty3);
        assertEquals(3, testPerson.getGameParties().size());
    }

    @Test
    public void testAddToGamePartyAllDifferentParties() {
        assertEquals(0, testPerson.getGameParties().size());
        testPerson.addToGameParty(gameParty1);
        assertEquals(1, testPerson.getGameParties().size());
        testPerson.addToGameParty(gameParty2);
        assertEquals(2, testPerson.getGameParties().size());
        testPerson.addToGameParty(gameParty3);
        assertEquals(3, testPerson.getGameParties().size());
    }

    @Test
    public void testAddToGamePartySameParty() {
        assertEquals(0, testPerson.getGameParties().size());
        testPerson.addToGameParty(gameParty1);
        assertEquals(1, testPerson.getGameParties().size());
        testPerson.addToGameParty(gameParty1);
        assertEquals(1, testPerson.getGameParties().size());
    }

    @Test
    public void testAddRoleHasRoleInList() {
        testPerson.addRole(game1);
        assertEquals(1, testPerson.getNumOfRoles());
        testPerson.addRole(game1);
        assertEquals(1, testPerson.getNumOfRoles());
    }

    @Test
    public void testAddRoleNoRoleInList() {
        testPerson.addRole(game1);
        assertEquals(1, testPerson.getNumOfRoles());
        testPerson.addRole(game2);
        assertEquals(2, testPerson.getNumOfRoles());
    }

    @Test
    public void testDeleteRoleNoRole() {
        testPerson.addRole(game1);
        assertEquals(1, testPerson.getNumOfRoles());
        testPerson.deleteRole(game2);
        assertEquals(1, testPerson.getNumOfRoles());
    }

    @Test
    public void testDeleteRoleHasRole() {
        assertEquals(0, testPerson.getNumOfRoles());
        testPerson.addRole(game1);
        assertEquals(1, testPerson.getNumOfRoles());
        testPerson.deleteRole(game1);
        assertEquals(0, testPerson.getNumOfRoles());
    }
}