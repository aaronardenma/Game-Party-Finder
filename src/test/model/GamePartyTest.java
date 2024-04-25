package model;

import exceptions.PersonDoesNotContainRoleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePartyTest {
    private Person person1;
    private Person person2;
    private Person person3;
    private Person person4;
    private GameParty testGameParty;
    private Game game1;
    private Game game2;

    @BeforeEach
    public void runBefore() {
        person1 = new Person("Aaron");
        person2 = new Person("Paolo");
        person3 = new Person("Renee");
        person4 = new Person("Nic");
        game1 = new Game("League of Legends", 5);
        game2 = new Game("Valorant", 5);
        testGameParty = new GameParty(game1, 5, "party 1");
    }

    @Test
    public void testAddMemberAllHaveRoleNoExceptions() {
        assertEquals(0, testGameParty.getCurrentNumOfMembers());
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);

        try {
            testGameParty.addMember(person1);
            assertEquals(1, testGameParty.getCurrentNumOfMembers());
            testGameParty.addMember(person2);
            assertEquals(2, testGameParty.getCurrentNumOfMembers());
            testGameParty.addMember(person3);
            assertEquals(3, testGameParty.getCurrentNumOfMembers());
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }
    }

    @Test
    public void testAddMemberSomeHaveRole() {
        assertEquals(0, testGameParty.getCurrentNumOfMembers());
        person1.addRole(game1);
        person2.addRole(game2);
        person3.addRole(game1);
        person4.addRole(game2);

        try {
            testGameParty.addMember(person1);
            assertEquals(1, testGameParty.getCurrentNumOfMembers());
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }

        try {
            testGameParty.addMember(person2);
            fail();
        } catch (PersonDoesNotContainRoleException e) {
            assertEquals(1, testGameParty.getCurrentNumOfMembers());
        }

        try {
            testGameParty.addMember(person3);
            assertEquals(2, testGameParty.getCurrentNumOfMembers());
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }

        try {
            testGameParty.addMember(person4);
            fail();
        } catch (PersonDoesNotContainRoleException e) {
            assertEquals(2, testGameParty.getCurrentNumOfMembers());
        }
    }

    @Test
    public void testAddSameMember() {
        person1.addRole(game1);
        person2.addRole(game1);

        try {
            testGameParty.addMember(person1);
            assertEquals(1, testGameParty.getCurrentNumOfMembers());
            testGameParty.addMember(person1);
            assertEquals(1, testGameParty.getCurrentNumOfMembers());
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }
    }

    @Test
    public void testAddMemberNoRoleException() {
        person1.addRole(game2);

        try {
            testGameParty.addMember(person1);
            fail();
        } catch (PersonDoesNotContainRoleException e) {
            assertEquals(0, testGameParty.getCurrentNumOfMembers());
            assertFalse(testGameParty.getCurrentMembers().contains(person1));
        }
    }

    @Test
    public void testDeleteMember() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        person4.addRole(game1);

        try {
            testGameParty.addMember(person1);
            testGameParty.addMember(person2);
            testGameParty.addMember(person3);
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }

        testGameParty.removeMember(person1);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
        testGameParty.removeMember(person2);
        assertEquals(1, testGameParty.getCurrentNumOfMembers());
        testGameParty.removeMember(person3);
        assertEquals(0, testGameParty.getCurrentNumOfMembers());
    }

    @Test
    public void testDeleteSameMember() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);

        try {
            testGameParty.addMember(person1);
            testGameParty.addMember(person2);
            testGameParty.addMember(person3);
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }

        testGameParty.removeMember(person1);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
        testGameParty.removeMember(person1);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
    }

    @Test
    public void testChangeTotalSize() {
        testGameParty.changeTotalSize(4);
        assertEquals(4, testGameParty.getMaxPartySize());
        testGameParty.changeTotalSize(3);
        assertEquals(3, testGameParty.getMaxPartySize());
        testGameParty.changeTotalSize(5);
        assertEquals(5, testGameParty.getMaxPartySize());
    }

    @Test
    public void testChangeTotalSizeAboveGameMaxPartySize() {
        testGameParty.changeTotalSize(6);
        assertEquals(5, testGameParty.getMaxPartySize());
        testGameParty.changeTotalSize(20);
        assertEquals(5, testGameParty.getMaxPartySize());
    }

    @Test
    public void testChangeTotalSizeEqualToCurrentNumOfMembers() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);

        try {
            testGameParty.addMember(person1);
            testGameParty.addMember(person2);
            testGameParty.addMember(person3);
            testGameParty.changeTotalSize(3);
            assertEquals(3, testGameParty.getMaxPartySize());
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }
    }

    @Test
    public void testChangeTotalSizeAboveCurrentNumOfMembers() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);

        try {
            testGameParty.addMember(person1);
            testGameParty.addMember(person2);
            testGameParty.addMember(person3);
            testGameParty.changeTotalSize(2);
            assertEquals(5, testGameParty.getMaxPartySize());
            testGameParty.changeTotalSize(1);
            assertEquals(5, testGameParty.getMaxPartySize());
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }

    }

    @Test
    public void testGetCurrentMembers() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);

        try {
            testGameParty.addMember(person1);
            testGameParty.addMember(person3);
            assertTrue(testGameParty.getCurrentMembers().contains(person1));
            assertFalse(testGameParty.getCurrentMembers().contains(person2));
            assertTrue(testGameParty.getCurrentMembers().contains(person3));
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }
    }

    @Test
    public void testGetMaxPartySize() {
        assertEquals(5, testGameParty.getMaxPartySize());
    }

    @Test
    public void testGetCurrentNumOfMembers() {
        assertEquals(0, testGameParty.getCurrentNumOfMembers());
        person1.addRole(game1);

        try {
            testGameParty.addMember(person1);
            assertEquals(1, testGameParty.getCurrentNumOfMembers());
            testGameParty.addMember(person1);
            assertEquals(1, testGameParty.getCurrentNumOfMembers());
            testGameParty.removeMember(person1);
            assertEquals(0, testGameParty.getCurrentNumOfMembers());
        } catch (PersonDoesNotContainRoleException e) {
            fail();
        }
    }

    @Test
    public void testGetGame() {
        assertEquals(game1, testGameParty.getGame());
    }

    @Test
    public void testGetName() {
        assertEquals("party 1", testGameParty.getName());
    }
}
