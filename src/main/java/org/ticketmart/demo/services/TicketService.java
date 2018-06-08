package org.ticketmart.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.ticketmart.demo.model.EventRepository;
import org.ticketmart.demo.model.TicketRepository;
import org.ticketmart.demo.model.VenueRepository;

public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    VenueRepository venueRepository;





}