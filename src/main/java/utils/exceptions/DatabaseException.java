package utils.exceptions;

/**
 * Exception thrown when database operations fail
 */
public class DatabaseException extends LibraryException {
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
