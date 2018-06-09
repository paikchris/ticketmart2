package org.ticketmart.demo.model.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.ticketmart.demo.model.Event;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

@Repository
public interface EventReactiveRepository extends ReactiveMongoRepository<Event, String> {
    public Event findByName(String name);

    public Flux<Event> findAllByNameLike(String query);

    public List<Event> findByVenueID(String venueID);

    public List<Event> findByDate(Date date);

}
