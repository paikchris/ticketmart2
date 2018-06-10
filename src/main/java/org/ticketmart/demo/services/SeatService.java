package org.ticketmart.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketmart.demo.model.Event;
import org.ticketmart.demo.model.Seat;
import org.ticketmart.demo.model.Ticket;
import org.ticketmart.demo.model.Venue;
import org.ticketmart.demo.model.repositories.EventRepository;
import org.ticketmart.demo.model.repositories.SeatRepository;
import org.ticketmart.demo.model.repositories.TicketRepository;
import org.ticketmart.demo.model.repositories.VenueRepository;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SeatService {
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    EventRepository eventRepository;

    final float topNPercentile = .1f;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public int numSeatsAvailable(String eventID){
        return ticketRepository.findByEventIDAndReservedAndHold(eventID, false, false).size();
    }

    public HashMap<String, Ticket> getSeatMap(String eventID) throws Exception {
        //TRUE = RESERVED
        //FALSE = UNRESERVED

        Event event = eventRepository.findById(eventID).orElseThrow(Exception::new);
        Venue venue =  venueRepository.findById(event.venueID).orElseThrow(Exception::new);

        //GET TICKETS AND MAP THEM TO GET AVAILABLE SEATS
        List<Ticket> ticketsForEvent = ticketRepository.findByEventID(eventID);
        List<Ticket> calculatedRecommendations = getNewRecommendedTicketsForEvent(event, venue);

        HashMap<String, Ticket> ticketMap = new HashMap<>();
        for (Ticket t: ticketsForEvent) {
            //THIS WILL BE SLOW, FIX
            Seat seat = seatRepository.findById(t.seatID).orElseThrow(Exception::new);
            String seatRowNum = seat.row + seat.number;

            ticketMap.put( seatRowNum, t);

        }

        return ticketMap;
    }
    public List<Ticket> getNewRecommendedTicketsForEvent (Event event, Venue venue){
        return ticketRepository.
                findByEventIDAndReservedAndHold(event.id,false, false).
                stream().
                sorted(byTicketDistanceFromFrontMid(venue)).
                collect(Collectors.toList()).subList(0, Math.round(numSeatsAvailable(event.id)*topNPercentile));
    }
    public List<Seat> getRankedSeats(String venueID) throws Exception {
        //DOES NOT TAKE INTO ACCOUNT WHETHER SEATS ARE RESERVED OR NOT
        Venue venue = venueRepository.findById(venueID).orElseThrow(Exception::new);

        return getAllSeatsInVenue(venueID).stream().sorted(bySeatDistanceFromFrontMid(venue)).collect(Collectors.toList());
    }
    public List<String> getRankedSeatsRowNum(String venueID) throws Exception {
        //GET ONLY THE SEATNUMS IN A LIST
        List<Seat> rankedSeats = getRankedSeats(venueID);

        return rankedSeats.stream().map( seat -> seat.row + seat.number ).collect(Collectors.toList());
    }
    public List<Seat> getAllSeatsInVenue(String venueID){
        return seatRepository.findAllByVenueID(venueID);
    }

    String reserveSeats(int seatHoldId, String customerEmail){
        return "";
    }

    public int getRowNumberFromLetter(String letter){
        int rowNum = 0;
        char letterChar = letter.charAt(0);

        for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
            if(letterChar == alpha){
                return rowNum;
            }
            rowNum++;
        }

        return -1;
    }
    public double getDistanceFromPoint(double midX, double seatX, double midY, double seatY){
        //PYTHAGOREAN
        return Math.hypot(midX - seatX, midY - seatY);
    }
    public double getTicketDistanceFromPoint(double pointX, double pointY, Ticket ticket) {
        Seat seat = seatRepository.findById(ticket.seatID).orElse(new Seat());
        return getDistanceFromPoint(pointX, Double.parseDouble(seat.number), pointY, getRowNumberFromLetter(seat.row));
    }
    public Comparator<Seat> bySeatDistanceFromFrontMid(Venue venue){
        //GET MATHEMATICAL REPRESENTATION OF FRONT CENTER
        double midX = venue.seatNumbers/2;
        double frontY = 0;

        return Comparator.comparing(
                seat -> getDistanceFromPoint(midX,Double.parseDouble(seat.number) , frontY, getRowNumberFromLetter(seat.row))
        );
    }
    public Comparator<Ticket> byTicketDistanceFromFrontMid(Venue venue){
        //GET MATHEMATICAL REPRESENTATION OF FRONT CENTER
        double midX = venue.seatNumbers/2;
        double frontY = 0;

        return Comparator.comparing(
                ticket -> getTicketDistanceFromPoint(midX, frontY, ticket)
        );
    }
}
