package org.ticketmart.demo;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.ticketmart.demo.model.*;
import org.ticketmart.demo.model.repositories.*;
import org.ticketmart.demo.services.EventService;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private VenueRepository venueRepo;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private SeatRepository seatRepo;
    @Autowired
    private TicketRepository ticketRepo;
    @Autowired
    private EventService eventService;

    Faker faker = new Faker(); //FOR TEST DATA

    final int numUsers = 5;
    final int numVenues = 10;
    final int numEvents = 10;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        bootstrapDatabase();

    }

    private void bootstrapDatabase() {
        clearDatabase();
        createUsers();
        createVenues();
        createSeatsForVenues();
        createEvents();
        createTickets();
    }
    private void clearDatabase(){
        userRepo.deleteAll();
        venueRepo.deleteAll();
        eventRepo.deleteAll();
        seatRepo.deleteAll();
        ticketRepo.deleteAll();
    }
    private void createUsers(){
        System.out.println("CREATING USERS...");
        int count = 0;
        for (int i = 0; i < numUsers; i++) {
            count++;
            userRepo.save(new User(faker.name().firstName(),
                    faker.name().lastName(),
                    faker.internet().emailAddress()
            ));
        }

        System.out.println(count + " Users created");


    }
    private void createVenues(){
        System.out.println("CREATING VENUES...");
        int count = 0;

        String companyName;
        for (int j = 0; j < numVenues; j++) {
            companyName = faker.name().lastName() + " Arena";
            count++;
            venueRepo.save(new Venue( companyName,
                    faker.address().streetAddress(),
                    faker.number().numberBetween(5,24),
                    faker.number().numberBetween(0, 20))
            );
        }

        System.out.println(count + " VENUES created");
    }
    private void createSeatsForVenues(){
        System.out.println("CREATING Seats...");
        int count = 0;

        ArrayList <Seat> seatsToSave = new ArrayList<>();

        for (Venue venue: venueRepo.findAll()) {
            int rows = venue.seatRows;
            int numbers = venue.seatNumbers;

            System.out.println("\tVenue rows: " + rows);
            System.out.println("\tVenue seats per row: " + numbers);
            System.out.println("\tSeat total should be: " + (rows * numbers));



            //FOR EACH ROW
            for (int r = 0; r < rows; r++) {
                for (int n = 0; n < numbers; n++) {
                    count++;
                    seatsToSave.add( new Seat(venue.id, getRowLetterFromInt(r), Integer.toString(n)) );
                }
            }
        }

        seatRepo.insert(seatsToSave);

        System.out.println(count + " Seats created");


    }
    private void createEvents(){
        System.out.println("CREATING EVENTS...");
        int count = 0;
        for (int j = 0; j < numEvents; j++) {
            count++;
            eventRepo.save(new Event( faker.esports().event(), faker.date().future(365, TimeUnit.DAYS), getRandomVenueID()));
        }

        System.out.println(count + " EVENTS created");
    }
    private void createTickets(){
        System.out.println("CREATING TICKETS...");
        final int[] count = {0};

        List <Event> events = eventRepo.findAll();
        final int[] totalTickets = {0};
        for (Event e: events) {
            //GET CAPACITY OF VENUE = TICKETS NUM
            Optional<Venue> v = venueRepo.findById(e.venueID);
            v.ifPresent( venue -> {
                int capacity = venue.seatRows * venue.seatNumbers;
                ArrayList <Ticket> tickets = new ArrayList<>();
                totalTickets[0] = totalTickets[0] + capacity;

                for (int r = 0; r < venue.seatRows; r++) {
                    for (int n = 0; n < venue.seatNumbers; n++) {
                        //find seatID
                        Seat seatMatch = seatRepo.findByVenueIDAndRowAndNumber(venue.id, getRowLetterFromInt(r), Integer.toString(n) );
                        //random reserved true or false

                        count[0]++;
                        tickets.add(new Ticket(e.id,
                                getRandomUserID(),
                                Integer.toString(faker.number().numberBetween(10, 4000)),
                                seatMatch.id,
                                faker.random().nextBoolean(),
                                false
                        ));
                    }
                }
                ticketRepo.insert(tickets);
            });
        }
        System.out.println(count[0] + " Tickets created");
    }

    private String getRowLetterFromInt(int i){
        ArrayList <String> alphabet = new ArrayList<>();
        for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
            alphabet.add(String.valueOf(alpha));
        }

        return alphabet.get(i);
    }
    private String getRandomUserID(){
        List<User> users = userRepo.findAll();

        int randomIndex = faker.number().numberBetween(0, users.size()-1);

        return users.get(randomIndex).id;
    }

    private String getRandomVenueID(){
        List<Venue> venues = venueRepo.findAll();
        int randomIndex = faker.number().numberBetween(0, venues.size()-1);
        return venues.get(randomIndex).id;
    }
}
