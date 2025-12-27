package utils.exceptions;

/**
 * Exception thrown when input validation fails
 */
public class ValidationException extends LibraryException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
