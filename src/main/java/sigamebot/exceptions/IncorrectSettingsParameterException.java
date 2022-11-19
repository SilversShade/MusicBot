package sigamebot.exceptions;

public class IncorrectSettingsParameterException extends RuntimeException{

    private final String message;
    public IncorrectSettingsParameterException(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
