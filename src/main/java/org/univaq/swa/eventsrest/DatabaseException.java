package org.univaq.swa.eventsrest;

/**
 *
 * @author Giuseppe Della Penna
 */
public class DatabaseException extends Exception {

    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
    
}
