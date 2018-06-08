package org.ticketmart.demo.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    public Event findByName(String name);

    public List<Event> findByVenueID(String venueID);

    public List<Event> findByDate(Date date);


}