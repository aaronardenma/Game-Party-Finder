package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        gameParty1 = new GameParty(game1, 5, "party 1");
        gameParty2 = new GameParty(game2, 3, "party 2");
        gameParty3 = new GameParty(game3, 4, "party 3");
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
        testPerson.removeRole(game2);
        assertEquals(1, testPerson.getNumOfRoles());
    }

    @Test
    public void testDeleteRoleHasRole() {
        assertEquals(0, testPerson.getNumOfRoles());
        testPerson.addRole(game1);
        assertEquals(1, testPerson.getNumOfRoles());
        testPerson.removeRole(game1);
        assertEquals(0, testPerson.getNumOfRoles());
    }

    @Test
    public void testUpdateWinRateDoNotContainAllKeys() {
        Person person2 = new Person("Renee");
        Person person3 = new Person("Paolo");
        testPerson.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        gameParty1.addMember(testPerson);
        gameParty1.addMember(person2);
        gameParty1.addMember(person3);

        assertEquals(0, testPerson.getGameStats().size());

        testPerson.updateWinRate(gameParty1, 3, 3);
        assertTrue(testPerson.getGameStats().containsKey(person2.getName()));
        assertEquals(100, testPerson.getGameStats().get(person2.getName()).get(0));
        assertEquals(3, testPerson.getGameStats().get(person2.getName()).get(1));
        assertEquals(3, testPerson.getGameStats().get(person2.getName()).get(2));

        assertTrue(testPerson.getGameStats().containsKey(person3.getName()));
        assertEquals(100, testPerson.getGameStats().get(person3.getName()).get(0));
        assertEquals(3, testPerson.getGameStats().get(person3.getName()).get(1));
        assertEquals(3, testPerson.getGameStats().get(person3.getName()).get(2));
    }

    @Test
    public void testUpdateWinRateContainsAllKeys() {
        Person person2 = new Person("Renee");
        Person person3 = new Person("Paolo");
        testPerson.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        gameParty1.addMember(testPerson);
        gameParty1.addMember(person2);
        gameParty1.addMember(person3);

        assertEquals(0, testPerson.getGameStats().size());

        testPerson.updateWinRate(gameParty1, 3, 3);
        assertTrue(testPerson.getGameStats().containsKey(person2.getName()));
        assertEquals(100, testPerson.getGameStats().get(person2.getName()).get(0));
        assertEquals(3, testPerson.getGameStats().get(person2.getName()).get(1));
        assertEquals(3, testPerson.getGameStats().get(person2.getName()).get(2));

        assertTrue(testPerson.getGameStats().containsKey(person3.getName()));
        assertEquals(100, testPerson.getGameStats().get(person3.getName()).get(0));
        assertEquals(3, testPerson.getGameStats().get(person3.getName()).get(1));
        assertEquals(3, testPerson.getGameStats().get(person3.getName()).get(2));

        assertEquals(2, testPerson.getGameStats().size());

        testPerson.updateWinRate(gameParty1, 0, 3);
        assertTrue(testPerson.getGameStats().containsKey(person2.getName()));
        assertEquals(50, testPerson.getGameStats().get(person2.getName()).get(0));
        assertEquals(3, testPerson.getGameStats().get(person2.getName()).get(1));
        assertEquals(6, testPerson.getGameStats().get(person2.getName()).get(2));

        assertTrue(testPerson.getGameStats().containsKey(person3.getName()));
        assertEquals(50, testPerson.getGameStats().get(person3.getName()).get(0));
        assertEquals(3, testPerson.getGameStats().get(person3.getName()).get(1));
        assertEquals(6, testPerson.getGameStats().get(person3.getName()).get(2));
    }

    @Test
    public void testUpdateWinRateContainsPartialKeys() {
        Person person2 = new Person("Renee");
        Person person3 = new Person("Paolo");
        testPerson.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        gameParty1.addMember(testPerson);
        gameParty1.addMember(person2);

        assertEquals(0, testPerson.getGameStats().size());

        testPerson.updateWinRate(gameParty1, 3, 3);
        assertTrue(testPerson.getGameStats().containsKey(person2.getName()));
        assertEquals(100, testPerson.getGameStats().get(person2.getName()).get(0));
        assertEquals(3, testPerson.getGameStats().get(person2.getName()).get(1));
        assertEquals(3, testPerson.getGameStats().get(person2.getName()).get(2));

        assertFalse(testPerson.getGameStats().containsKey(person3.getName()));

        GameParty newGameParty = new GameParty(game1, 3, "new party");

        newGameParty.addMember(testPerson);
        newGameParty.addMember(person2);
        newGameParty.addMember(person3);

        assertEquals(1, testPerson.getGameStats().size());

        testPerson.updateWinRate(newGameParty, 1, 3);
        assertTrue(testPerson.getGameStats().containsKey(person2.getName()));
        assertEquals(67, testPerson.getGameStats().get(person2.getName()).get(0));
        assertEquals(4, testPerson.getGameStats().get(person2.getName()).get(1));
        assertEquals(6, testPerson.getGameStats().get(person2.getName()).get(2));

        assertTrue(testPerson.getGameStats().containsKey(person3.getName()));
        assertEquals(33, testPerson.getGameStats().get(person3.getName()).get(0));
        assertEquals(1, testPerson.getGameStats().get(person3.getName()).get(1));
        assertEquals(3, testPerson.getGameStats().get(person3.getName()).get(2));
        assertEquals(2, testPerson.getGameStats().size());
    }
}