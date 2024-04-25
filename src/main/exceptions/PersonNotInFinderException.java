package exceptions;

import model.Person;

public class PersonNotInFinderException extends NotInFinderException {

    public PersonNotInFinderException(Person person) {
        super("(Person: " + person.getName() + ") is not in GamePartyFinder");
    }
}
