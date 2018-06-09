package org.ticketmart.demo.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.ticketmart.demo.model.User;
import org.ticketmart.demo.model.Venue;

import java.util.List;

public interface VenueRepository extends MongoRepository<Venue, String> {

    public Venue findByName(String name);

}