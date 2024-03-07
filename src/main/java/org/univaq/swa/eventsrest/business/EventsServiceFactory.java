
package org.univaq.swa.eventsrest.business;

/**
 *
 * @author Giuseppe Della Penna
 */
public class EventsServiceFactory {
    private final static EventsServiceImpl service = new EventsServiceImpl();

    public static EventsServiceImpl getEventsService() {
        return service;
    }

    
}
