package org.univaq.swa.eventsrest.model;

import java.time.ZonedDateTime;

/**
 * Recurrence
 */
public class Recurrence {

    private Integer count = null;
    private Integer interval = null;
    private ZonedDateTime until = null;

    public enum FrequencyEnum {
        DAILY, WEEKLY, MONTHLY, YEARLY,
    }
    private FrequencyEnum frequency = null;

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the interval
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    /**
     * @return the until
     */
    public ZonedDateTime getUntil() {
        return until;
    }

    /**
     * @param until the until to set
     */
    public void setUntil(ZonedDateTime until) {
        this.until = until;
    }

    /**
     * @return the frequency
     */
    public FrequencyEnum getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(FrequencyEnum frequency) {
        this.frequency = frequency;
    }

}
