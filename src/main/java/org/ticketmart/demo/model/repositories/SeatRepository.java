package org.ticketmart.demo.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.ticketmart.demo.model.Seat;

import java.util.List;

public interface SeatRepository extends MongoRepository<Seat, String> {
    public List<Seat> findAllByVenueID(String venueID);

    public List<Seat> findByRow(String row);

    public List<Seat> findByNumber(String number);

    public Seat findByVenueIDAndRowAndNumber(String venueID, String row, String number);


}