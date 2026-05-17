package feira.exception;

public class FeiraException extends RuntimeException {
    public FeiraException(String message) { super(message); }
    public FeiraException(String message, Throwable cause) { super(message, cause); }
}
