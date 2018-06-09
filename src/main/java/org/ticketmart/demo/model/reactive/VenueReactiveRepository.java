package org.ticketmart.demo.model.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.ticketmart.demo.model.Venue;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

@Repository
public interface VenueReactiveRepository extends ReactiveMongoRepository<Venue, String> {
    public Venue findByName(String name);

}
