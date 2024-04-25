package exceptions;

import model.GameParty;

public class PartyNotInFinderException extends NotInFinderException {

    public PartyNotInFinderException(GameParty gameParty) {
        super("(Game Party: " + gameParty.getName() + ") is not in GamePartyFinder");
    }
}
