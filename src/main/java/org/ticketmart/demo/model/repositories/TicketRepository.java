package org.ticketmart.demo.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.ticketmart.demo.model.Ticket;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    public List<Ticket> findByEventID(String eventID);

    public List<Ticket> findByEventIDAndReserved(String eventID, boolean reserved);

    public List<Ticket> findByEventIDAndReservedAndHold(String eventID, boolean reserved, boolean hold);

    public List<Ticket> findByUserID(String userID);

    public List<Ticket> findByPrice(String price);




}