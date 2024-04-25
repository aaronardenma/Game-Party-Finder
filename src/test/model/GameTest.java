package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameTest {
    private Game testGame;

    @BeforeEach
    public void runBefore() {
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

}
