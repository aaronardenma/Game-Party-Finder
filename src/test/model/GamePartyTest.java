package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePartyTest {
    private Person person1;
    private Person person2;
    private Person person3;
    private Person person4;
    private Person person5;
    private GameParty testGameParty;
    private Game game1;
    private Game game2;
    private Game game3;

    @BeforeEach
    public void runBefore() {
        person1 = new Person("Aaron");
        person2 = new Person("Paolo");
        person3 = new Person("Renee");
        person4 = new Person("Nic");
        person5 = new Person("Ryan");
        game1 = new Game("League of Legends", 5);
        game2 = new Game("Valorant", 5);
        game3 = new Game("CS2", 5);
        testGameParty = new GameParty(5, game1);
    }

    @Test
    public void testAddMemberAllHaveRole() {
        assertEquals(0, testGameParty.getCurrentNumOfMembers());
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        testGameParty.addMember(person1);
        assertEquals(1, testGameParty.getCurrentNumOfMembers());
        testGameParty.addMember(person2);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
        testGameParty.addMember(person3);
        assertEquals(3, testGameParty.getCurrentNumOfMembers());
    }

    @Test
    public void testAddMemberSomeHaveRole() {
        assertEquals(0, testGameParty.getCurrentNumOfMembers());
        person1.addRole(game1);
        person2.addRole(game2);
        person3.addRole(game1);
        person4.addRole(game2);
        testGameParty.addMember(person1);
        assertEquals(1, testGameParty.getCurrentNumOfMembers());
        testGameParty.addMember(person2);
        assertEquals(1, testGameParty.getCurrentNumOfMembers());
        testGameParty.addMember(person3);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
        testGameParty.addMember(person4);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
        testGameParty.addMember(person4);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
    }

    @Test
    public void testAddSameMember() {
        person1.addRole(game1);
        person2.addRole(game1);
        testGameParty.addMember(person1);
        assertEquals(1, testGameParty.getCurrentNumOfMembers());
        testGameParty.addMember(person1);
        assertEquals(1, testGameParty.getCurrentNumOfMembers());
    }

    @Test
    public void testDeleteMember() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        person4.addRole(game1);
        testGameParty.addMember(person1);
        testGameParty.addMember(person2);
        testGameParty.addMember(person3);

        testGameParty.deleteMember(person1);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
        testGameParty.deleteMember(person2);
        assertEquals(1, testGameParty.getCurrentNumOfMembers());
        testGameParty.deleteMember(person3);
        assertEquals(0, testGameParty.getCurrentNumOfMembers());
    }

    @Test
    public void testDeleteSameMember() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        testGameParty.addMember(person1);
        testGameParty.addMember(person2);
        testGameParty.addMember(person3);

        testGameParty.deleteMember(person1);
        assertEquals(2, testGameParty.getCurrentNumOfMembers());
        testGameParty.deleteMember(person1);
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
        testGameParty.addMember(person1);
        testGameParty.addMember(person2);
        testGameParty.addMember(person3);
        testGameParty.changeTotalSize(3);
        assertEquals(3, testGameParty.getMaxPartySize());
    }

    @Test
    public void testChangeTotalSizeAboveCurrentNumOfMembers() {
        person1.addRole(game1);
        person2.addRole(game1);
        person3.addRole(game1);
        testGameParty.addMember(person1);
        testGameParty.addMember(person2);
        testGameParty.addMember(person3);
        testGameParty.changeTotalSize(2);
        assertEquals(5, testGameParty.getMaxPartySize());
        testGameParty.changeTotalSize(1);
        assertEquals(5, testGameParty.getMaxPartySize());
    }
}
