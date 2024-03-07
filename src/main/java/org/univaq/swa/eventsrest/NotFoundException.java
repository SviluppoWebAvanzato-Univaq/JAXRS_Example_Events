package org.univaq.swa.eventsrest;

/**
 *
 * @author Giuseppe Della Penna
 */
public class NotFoundException extends Exception {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
    
}
