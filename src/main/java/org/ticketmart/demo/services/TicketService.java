package org.ticketmart.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.ticketmart.demo.model.Ticket;
import org.ticketmart.demo.model.repositories.EventRepository;
import org.ticketmart.demo.model.repositories.TicketRepository;
import org.ticketmart.demo.model.repositories.VenueRepository;

import javax.xml.ws.Holder;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    VenueRepository venueRepository;


    static final long HOLD_TIME_IN_MIN = 15 ;

    ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(5);


    public Ticket holdTicket(String ticketID){
        Ticket ticket = new Ticket();
        try {
            System.out.println("Starting Hold on Ticket: " + ticketID);
            ticket = ticketRepository.findById(ticketID).orElseThrow(Exception::new);
            ticket.hold = true;
            ticket.holdStartTime = new Date();
            ticketRepository.save(ticket);
            System.out.println("Hold Placed on: " + ticketID);


            startTicketHoldTimer(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ticket;
    }

    public boolean releaseTicketHold(String ticketID){
        try {
            Ticket ticket = ticketRepository.findById(ticketID).orElseThrow(Exception::new);
            ticket.hold = false;
            ticket.holdStartTime = null;
            ticketRepository.save(ticket);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Runnable getReleaseTickeHoldRunnable( Ticket ticket ){
        return new Runnable() {
            @Override
            public void run() {
                ticket.hold = false;
                ticket.holdStartTime = null;
                ticketRepository.save(ticket);
            }
        };
    }

    public void startTicketHoldTimer(Ticket ticket){
        Runnable releaseTicketHoldRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("REMOVING Hold on : " + ticket.id);
                ticket.hold = false;
                ticket.holdStartTime = null;
                ticketRepository.save(ticket);
                System.out.println("Hold removed on : " + ticket.id);

            }
        };

        System.out.println("Starting Hold TIMER on : " + ticket.id);
        System.out.println("DELAY : " + HOLD_TIME_IN_MIN);


//        scheduler.scheduleWithFixedDelay(
//                releaseTicketHoldRunnable,
//                new Date(),
//                HOLD_TIME_IN_MILLIS);
        scheduler.schedule(releaseTicketHoldRunnable, HOLD_TIME_IN_MIN, TimeUnit.MINUTES);


    }






}