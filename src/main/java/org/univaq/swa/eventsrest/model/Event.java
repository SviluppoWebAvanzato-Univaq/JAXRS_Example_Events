package org.univaq.swa.eventsrest.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Event
 */
public class Event {

    private String uid = null;
    private String summary = null;
    private String location = null;
    private ZonedDateTime start = null;
    private ZonedDateTime end = null;
    private List<String> categories = new ArrayList<>();
    private byte[] attachment = null;
    private List<Participant> participants = new ArrayList<>();
    private Recurrence recurrence = null;

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the start
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /**
     * @return the categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * @return the attachment
     */
    public byte[] getAttachment() {
        return attachment;
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    /**
     * @return the participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * @param participants the participants to set
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * @return the recurrence
     */
    public Recurrence getRecurrence() {
        return recurrence;
    }

    /**
     * @param recurrence the recurrence to set
     */
    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

}
