package org.ticketmart.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketmart.demo.model.*;
import org.ticketmart.demo.model.repositories.EventRepository;
import org.ticketmart.demo.model.repositories.SeatRepository;
import org.ticketmart.demo.model.repositories.TicketRepository;
import org.ticketmart.demo.model.repositories.VenueRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SeatRepository seatRepository;


    public List<Event> findAll(){
        return eventRepository.findAll();
    }


    public List<Ticket> findAvailableTickets(String eventID){
        Optional<Event> event = eventRepository.findById(eventID);


        List <Ticket> availableTickets = ticketRepository.findByEventIDAndReserved( event.orElse(new Event()).id, false);

        return availableTickets;
    }

    public List<Event> holdTickets(){
        //need event id, which gives us venue id, which gives us seat id

        return eventRepository.findAll();
    }
}
