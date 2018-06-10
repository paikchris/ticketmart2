import React from "react";
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import {SearchBar} from "./SearchBar";
import {EventDetailContainer} from "./EventDetail";
import {TicketCheckout} from "./TicketCheckout";

export class HomeContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            event: null,
            venue: null,
            availableTickets: null,
            seatTicketMap: null,
            seatRank: null,
            index: "search",
            indexProps: {}
        };

        this.onEventSelect = this.onEventSelect.bind(this);
        this.getVenue = this.getVenue.bind(this);
        this.getAvailableTickets = this.getAvailableTickets.bind(this);
        this.getSeatMap = this.getSeatMap.bind(this);
        this.getSeatRank = this.getSeatRank.bind(this);
        this.changePageIndex = this.changePageIndex.bind(this);

    }

    onEventSelect(event){
        //TODO:MAKE THIS ASYNC
        this.getVenue(event.venueID)
        this.getAvailableTickets(event.id)
        this.getSeatMap(event.id)
        this.getSeatRank(event.venueID)
        this.setState((prevState, props) => {
            return { event: event, index: "event" };
        });
    }

    changePageIndex(indexString, indexProps){
        console.log("SETTING INDEX PROPS")
        console.log(indexProps)
        this.setState((prevState, props) => {
            return { index: indexString, indexProps: indexProps };
        });
    }

    getVenue(venueID){
        fetch('http://localhost:8080/venue/' + venueID)
            .then(results => results.json())
            .then(response => {
                this.setState((prevState, props) => {
                    return { venue: response };
                });
            });
    }

    getAvailableTickets(eventID){
        fetch('http://localhost:8080/eventTickets/' + eventID)
            .then(results => results.json())
            .then(response => {
                this.setState((prevState, props) => {
                    return { availableTickets: response };
                });
            });
    }

    getSeatMap(eventID){
        fetch('http://localhost:8080/seat/map/' + eventID)
            .then(results => results.json())
            .then(response => {
                this.setState((prevState, props) => {
                    return { seatTicketMap: response };
                });
            });
    }
    getSeatRank(venueID){
        fetch('http://localhost:8080/seat/rank/' + venueID)
            .then(results => results.json())
            .then(response => {
                this.setState((prevState, props) => {
                    return { seatRank: response };
                });
            });
    }

    getEventsStream(){
        let evtSource = new EventSource("http://localhost:8080/stream/events");
        evtSource.onmessage = function (event) {
            console.log(JSON.parse(event.data));
        }
    }

    render() {
        let rightElement = <Button minimal={true}  rightIcon={"arrow-right"}/>
        let bodyElement = ""

        if(this.state.index === "event"){
            bodyElement = <EventDetailContainer
                event={this.state.event}
                venue={this.state.venue}
                availableTickets={this.state.availableTickets}
                seatTicketMap={this.state.seatTicketMap}
                seatRank={this.state.seatRank}
                changePageIndex={this.changePageIndex}
            />
        }
        else if(this.state.index === "ticket"){
            console.log("BUILDING TICKET COMPONENT")
            console.log(this.state.indexProps)
            bodyElement = (
                <TicketCheckout
                    {...this.state.indexProps}
                    changePageIndex={this.changePageIndex}
                    // props={this.state.indexProps}
                />)
        }

        return (
            <div>
                <SearchBar onEventSelect={this.onEventSelect}/>
                {bodyElement}
            </div>

        );
    }
}