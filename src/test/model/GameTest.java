package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameTest {
    private Person person1;
    private Person person2;
    private Person person3;
    private GameParty gameParty1;
    private GameParty gameParty2;
    private Game testGame;

    @BeforeEach
    public void runBefore() {
        person1 = new Person("Aaron");
        person2 = new Person("Paolo");
        person3 = new Person("Renee");
        gameParty1 = new GameParty(testGame, 5, "party 1");
        testGame = new Game("League of Legends", 5);
    }

    @Test
    public void testGetMaxPartyMembers() {
        assertEquals(5, testGame.getMaxPartyMembers());
    }

    @Test
    public void testGetName() {
        assertEquals("League of Legends", testGame.getName());
    }

//    @Test
//    public void testGetListOfPlayers() {
//        assertEquals(0, testGame.getListOfPlayers().size());
//        testGame.addPersonToGame(person1);
//        assertEquals(1, testGame.getListOfPlayers().size());
//        testGame.addPersonToGame(person2);
//        assertEquals(2, testGame.getListOfPlayers().size());
//    }
//
//    @Test
//    public void testAddPersonSamePerson() {
//        assertEquals(0, testGame.getListOfPlayers().size());
//        testGame.addPersonToGame(person1);
//        assertEquals(1, testGame.getListOfPlayers().size());
//        testGame.addPersonToGame(person1);
//        assertEquals(1, testGame.getListOfPlayers().size());
//    }
//
//    @Test
//    public void testAddPersonNoPersonInList() {
//        assertEquals(0, testGame.getListOfPlayers().size());
//        testGame.addPersonToGame(person1);
//        assertEquals(1, testGame.getListOfPlayers().size());
//        testGame.removePersonFromGame(person2);
//        assertEquals(1, testGame.getListOfPlayers().size());
//    }

    @Test
    public void testToJson() {

    }


}
