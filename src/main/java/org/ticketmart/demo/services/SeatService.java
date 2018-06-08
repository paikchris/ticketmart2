package org.ticketmart.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.ticketmart.demo.model.Venue;
import org.ticketmart.demo.model.VenueRepository;

public class SeatService {
    @Autowired
    VenueRepository venueRepository;


    /**
     * The number of capacity in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    public int numSeatsAvailable(String eventID, String venueID){

        return 0;
    }

    /**
     * Find and hold the best available capacity for a customer
     *
     * @param numSeats the number of capacity to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific capacity and related
    information
     */
    SeatHold findAndHoldSeats(int numSeats, String customerEmail){
        return new SeatHold();
    }

    /**
     * Commit capacity held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the
    seat hold is assigned
     * @return a reservation confirmation code
     */
    String reserveSeats(int seatHoldId, String customerEmail){
        return "";
    }
}
