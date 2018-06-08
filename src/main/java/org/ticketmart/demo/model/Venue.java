package org.ticketmart.demo.model;

import org.springframework.data.annotation.Id;

public class Venue {
    @Id
    public String id;

    public String name;
    public String address;
    public int seatRows;
    public int seatNumbers;



    public Venue() {
    }

    public Venue(String name, String address, int seatRows, int seatNumbers) {
        this.name = name;
        this.address = address;
        this.seatRows = seatRows;
        this.seatNumbers = seatNumbers;
    }

    @Override
    public String toString() {
        return String.format(
                "Venue[id=%s, name='%s', address='%s', seatRows='%s', seatRows='%s']",
                id, name, address, seatRows, seatNumbers);
    }
}
