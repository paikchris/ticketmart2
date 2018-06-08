package org.ticketmart.demo.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Ticket {
    @Id
    public String id;

    public String eventID;
    public String userID;
    public String price;
    public String seatID;
    public boolean reserved;

    public Ticket() {
    }

    public Ticket(String eventID, String userID, String price, String seatID, boolean reserved) {
        this.eventID = eventID;
        this.userID = userID;
        this.price = price;
        this.seatID = seatID;
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return String.format(
                "Ticket[id=%s, eventID='%s', date='%s', venueID='%s', seatID='%s', reserved='%s']",
                id, eventID, userID, price, seatID, reserved);
    }
}
