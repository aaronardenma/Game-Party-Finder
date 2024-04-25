package exceptions;

import model.Game;
import model.Person;

public class PersonDoesNotContainRoleException extends Exception {

    public PersonDoesNotContainRoleException(Person person, Game game) {
        super(person.getName() + " does not contain role: " + game.getName());
    }
}
