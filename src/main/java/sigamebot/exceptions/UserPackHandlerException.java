package sigamebot.exceptions;

public class UserPackHandlerException extends RuntimeException{

    private final String message;

    public UserPackHandlerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
