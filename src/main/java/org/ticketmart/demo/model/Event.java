package org.ticketmart.demo.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Event {
    @Id
    public String id;

    public String name;
    public Date date;
    public String venueID;

    public Event() {
    }

    public Event(String name, Date date, String venueID) {
        this.name = name;
        this.venueID = venueID;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format(
                "Event[id=%s, name='%s', date='%s', venueID='%s']",
                id, name, date, venueID);
    }
}
