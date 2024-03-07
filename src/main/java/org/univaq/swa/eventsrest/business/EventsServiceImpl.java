package org.univaq.swa.eventsrest.business;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.univaq.swa.eventsrest.model.Event;
import org.univaq.swa.eventsrest.model.Participant;
import org.univaq.swa.eventsrest.model.Recurrence;

/**
 *
 * @author Giuseppe Della Penna
 */
public class EventsServiceImpl implements EventsService {

    @Override
    public String addEvent(Event body) {
        //dummy
        return "IDabcdef";
    }

    @Override
    public void deleteEvent(String uid) {
        //dummy
    }

    @Override
    public void deleteParticipant(String uid, String partid) {
        //dummy
    }

    @Override
    public List<Event> getCurrentEvents(List<String> cat) {
        return createDummyEventsList();
    }

    @Override
    public Event getEvent(String uid) {
        return createDummyEvent(uid);
    }

    @Override
    public List<Event> getEvents(ZonedDateTime from, ZonedDateTime to, List<String> cat) {
        return createDummyEventsList();
    }

    @Override
    public int getNumberOfEvents(ZonedDateTime from, ZonedDateTime to, List<String> cat) {
        return 10;
    }

    @Override
    public void updateEvent(String uid, Event body) {
        //dummy
    }

    @Override
    public String addParticipant(String uid, Participant body) {
        return body.getEmail();
    }

    @Override
    public void updateRecurrence(String uid, Recurrence body) {
        //dummy
    }

    ///////
    Random random = new Random();

    private String createUID() {
        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'
        int targetStringLength = 10;

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return "ID" + generatedString;
    }

    private List<Event> createDummyEventsList() {
        List<Event> result = new ArrayList<>();
        int n = random.nextInt(1, 10);
        for (int i = 0; i < n; ++i) {
            result.add(createDummyEvent(createUID()));
        }
        return result;
    }

    private Event createDummyEvent(String uid) {
        Event e = new Event();
        e.setUid(uid);
        e.setSummary("Event " + e.getUid());
        e.setStart(ZonedDateTime.now().plus(random.nextInt(30), ChronoUnit.DAYS));
        e.setEnd(e.getStart().plus(random.nextInt(5), ChronoUnit.HOURS));
        int np = random.nextInt(0, 3);
        for (int i = 0; i < np; ++i) {
            Participant p = new Participant();
            p.setName("Pinco Pallino #" + i);
            p.setEmail("pinco.pallino" + i + "@univaq.it");
            e.getParticipants().add(p);
        }
        if (random.nextBoolean()) {
            Recurrence r = new Recurrence();
            r.setFrequency(Recurrence.FrequencyEnum.WEEKLY);
            r.setInterval(2);
            r.setUntil(e.getEnd().plus(2, ChronoUnit.MONTHS));
            e.setRecurrence(r);
        }
        e.setAttachment("ciao a tutti".getBytes());
        return e;
    }

}
