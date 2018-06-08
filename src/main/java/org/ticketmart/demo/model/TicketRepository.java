package org.ticketmart.demo.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    public List<Ticket> findByEventID(String eventID);

    public List<Ticket> findByEventIDAndReserved(String eventID, boolean reserved);

    public List<Ticket> findByUserID(String userID);

    public List<Ticket> findByPrice(String price);




}