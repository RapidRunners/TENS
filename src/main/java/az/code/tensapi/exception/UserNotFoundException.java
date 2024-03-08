package az.code.tensapi.exception;

public class UserNotFoundException extends RuntimeException {
    private final int errorCode;

    public UserNotFoundException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserNotFoundException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
