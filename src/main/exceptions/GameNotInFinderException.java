package exceptions;

import model.Game;

public class GameNotInFinderException extends NotInFinderException {

    public GameNotInFinderException(Game game) {
        super("(Game: " + game.getName() + ")" + " is not in GamePartyFinder");
    }
}
