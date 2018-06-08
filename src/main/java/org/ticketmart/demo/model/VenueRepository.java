package org.ticketmart.demo.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VenueRepository extends MongoRepository<Venue, String> {

    public Venue findByName(String name);

    public List<User> findByAddress(String address);

}