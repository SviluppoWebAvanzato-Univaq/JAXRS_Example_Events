package org.univaq.swa.eventsrest.business;

import jakarta.ws.rs.core.SecurityContext;
import java.time.ZonedDateTime;
import java.util.List;
import org.univaq.swa.eventsrest.DatabaseException;
import org.univaq.swa.eventsrest.NotFoundException;
import org.univaq.swa.eventsrest.model.Event;
import org.univaq.swa.eventsrest.model.Participant;
import org.univaq.swa.eventsrest.model.Recurrence;

/**
 *
 * @author Giuseppe Della Penna
 */
public interface EventsService {

    String addEvent(Event body);

    String addParticipant(String uid, Participant body) throws NotFoundException;

    void deleteEvent(String uid) throws NotFoundException, DatabaseException;

    void deleteParticipant(String uid, String partid) throws NotFoundException, DatabaseException;

    List<Event> getCurrentEvents(List<String> cat);

    Event getEvent(String uid) throws NotFoundException;

    List<Event> getEvents(ZonedDateTime from, ZonedDateTime to, List<String> cat);

    int getNumberOfEvents(ZonedDateTime from, ZonedDateTime to, List<String> cat);

    void updateEvent(String uid, Event body) throws NotFoundException, DatabaseException;

    void updateRecurrence(String uid, Recurrence body) throws NotFoundException, DatabaseException;
    
    

}
