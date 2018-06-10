package org.ticketmart.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.ticketmart.demo.model.Event;
import org.ticketmart.demo.model.Ticket;
import org.ticketmart.demo.model.Venue;
import org.ticketmart.demo.model.reactive.EventReactiveRepository;
import org.ticketmart.demo.model.reactive.VenueReactiveRepository;
import org.ticketmart.demo.services.EventService;
import org.ticketmart.demo.services.SeatService;
import org.ticketmart.demo.services.TicketService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class DemoController {
    @Autowired
    EventReactiveRepository eventReactiveRepository;
    @Autowired
    VenueReactiveRepository venueReactiveRepository;

    @Autowired
    EventService eventService;
    @Autowired
    SeatService seatService;
    @Autowired
    TicketService ticketService;

    public Mono<ServerResponse> index(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("Hello, Spring!"));
    }

    //EVENTS
    @GetMapping("/events")
    public Flux<Event> getAllEvents() {
        return eventReactiveRepository.findAll();
    }

    // Events are Sent to the client as Server Sent Events.
    @GetMapping(value = "/stream/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> streamAllEvents() {
        return eventReactiveRepository.findAll();
    }

    @GetMapping("/events/{query}")
    public Flux<Event> getEventNameLike(@PathVariable(value = "query") String query) {
        return eventReactiveRepository.findAllByNameLike(query);
    }

    //VENUES
    @GetMapping("/venue/{id}")
    public Mono<ResponseEntity<Venue>> getVenueByID(@PathVariable(value = "id") String id) {
        return venueReactiveRepository.findById(id)
                .map(venue -> ResponseEntity.ok(venue))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //TICKETS
    @GetMapping("/eventTickets/{eventID}")
    public Flux<Ticket> getAvailableTickets(@PathVariable(value = "eventID") String eventID) {
        List<Ticket> tickets =  eventService.findAvailableTickets(eventID);

        return Flux.fromIterable(tickets);
    }

    //SEATS
    @GetMapping("/seat/map/{eventID}")
    public Mono<HashMap<String, Ticket>> getSeatMap(@PathVariable(value = "eventID") String eventID) {
        HashMap<String, Ticket> seatMap = null;
        try {
            seatMap = seatService.getSeatMap(eventID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("");
        }

        return Mono.just(seatMap);
    }

    @GetMapping("/seat/rank/{venueID}")
    public Mono<List<String>> seatRank(@PathVariable(value = "venueID") String venueID) throws Exception {
        return Mono.just(seatService.getRankedSeatsRowNum(venueID));
    }

    @PostMapping("/seat/hold/{ticketID}")
    public Mono<Ticket> holdTicket(@PathVariable(value = "ticketID") String ticketID) {
        return Mono.just(ticketService.holdTicket(ticketID));
    }
    @PostMapping("/seat/reserve/{ticketID}")
    public Mono<Ticket> reserveTicket(@PathVariable(value = "ticketID") String ticketID) {
        return Mono.just(ticketService.reserveTicket(ticketID));
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String exception) {
            super(exception);
        }

    }
}