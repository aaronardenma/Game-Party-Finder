package exceptions;

public class InputEmptyException extends InvalidInputException {

    public InputEmptyException(String inputType) {
        super(inputType + " field is empty.");
    }
}
