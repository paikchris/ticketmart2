import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import {InputGroup} from "@blueprintjs/core";
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import {Suggest} from "@blueprintjs/select/lib/cjs/components/select/suggest";
import {MenuItem} from "@blueprintjs/core/lib/cjs/components/menu/menuItem";
import * as FuzzySearch from "fuzzy-search";
import {SearchBar} from "./SearchBar";
import {EventDetailContainer} from "./EventDetail";

export class HomeContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            event: null,
            venue: null,
            availableTickets: null,
            seatTicketMap: null,
            seatRank: null,
        };

        this.onEventSelect = this.onEventSelect.bind(this);
        this.getVenue = this.getVenue.bind(this);
        this.getAvailableTickets = this.getAvailableTickets.bind(this);
        this.getSeatMap = this.getSeatMap.bind(this);
        this.getSeatRank = this.getSeatRank.bind(this);
    }

    onEventSelect(event){
        //TODO:MAKE THIS ASYNC
        this.getVenue(event.venueID)
        this.getAvailableTickets(event.id)
        this.getSeatMap(event.id)
        this.getSeatRank(event.venueID)
        this.setState((prevState, props) => {
            return { event: event };
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
        return (
            <div>
                <SearchBar onEventSelect={this.onEventSelect}/>
                <EventDetailContainer
                    event={this.state.event}
                    venue={this.state.venue}
                    availableTickets={this.state.availableTickets}
                    seatTicketMap={this.state.seatTicketMap}
                    seatRank={this.state.seatRank}
                />
            </div>

        );
    }
}