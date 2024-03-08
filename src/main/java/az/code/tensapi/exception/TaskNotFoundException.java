package az.code.tensapi.exception;

public class TaskNotFoundException extends RuntimeException{
    private final int errorCode;

    public TaskNotFoundException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TaskNotFoundException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
