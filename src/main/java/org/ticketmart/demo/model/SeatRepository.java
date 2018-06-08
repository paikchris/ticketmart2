package org.ticketmart.demo.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SeatRepository extends MongoRepository<Seat, String> {
    public Seat findByVenueID(String venueID);

    public List<Seat> findByRow(String row);

    public List<Seat> findByNumber(String number);

    public List<Seat> findByVenueIDAndRowAndNumber(String venueID, String row, String number);


}