# TICKETMART

I may have tried to do too much in not enough time...

Quick Start:
1. Clone Project
2. ./launch.sh

Overview: 

1. Application on startup will generate fake Users, Events, Venues, Seats, and Tickets.
2. Front End was built using React, BlueprintJS (Palantir) and a webpack dev server to serve the UI
3. Back End was build using Spring 5 and the new Reactive Web Server

- Search for Events
- Will recommend seats based on distance from the Front-Middle of the venue. 
They are highlighted by a pulsing animation during Seat Reservation.
- Will count down the remaining hold time for tickets on the Ticket Checkout page.

Note: Does not use the original assignment Java Interface. 
I think the original Interface was designed to only have one Venue.
The original interface methods can be found in the Seat Service class.



Future Improvements:
- Better algorithms and logic needed to improve site performance.
- Initial development was done very quick and with functionality in mind not performance.
- UI needs to be prettier