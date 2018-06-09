package org.ticketmart.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.ticketmart.demo.model.repositories.EventRepository;
import org.ticketmart.demo.model.repositories.TicketRepository;
import org.ticketmart.demo.model.repositories.VenueRepository;

public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    VenueRepository venueRepository;





}