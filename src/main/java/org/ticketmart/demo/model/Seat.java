package org.ticketmart.demo.model;

import org.springframework.data.annotation.Id;

import java.util.Comparator;
import java.util.Date;

public class Seat {
    @Id
    public String id;

    public String venueID;
    public String row;
    public String number;

    public Seat() {
    }

    public Seat(String venueID, String row, String number) {
        this.venueID = venueID;
        this.row = row;
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format(
                "Seat[id=%s, venueID='%s', row='%s', number='%s']",
                id, venueID, row, number);
    }



}
