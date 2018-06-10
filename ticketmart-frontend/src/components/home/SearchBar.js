import React from "react";
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import {Suggest} from "@blueprintjs/select/lib/cjs/components/select/suggest";
import {MenuItem} from "@blueprintjs/core/lib/cjs/components/menu/menuItem";
import * as FuzzySearch from "fuzzy-search";
import * as TMUtils from "./../../utils/TMUtils.js"

export class SearchBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            events:[ "Hello"]
        };

        this.itemRenderer = this.itemRenderer.bind(this);
        this.inputValueRenderer = this.inputValueRenderer.bind(this);
        this.searchOnChange = this.searchOnChange.bind(this);
        this.itemListPredicate = this.itemListPredicate.bind(this);
        this.getVenue = this.getVenue.bind(this);

    }
    componentDidMount(){
        this.getEvents()
    }

    searchOnChange(event){
        let value = event.currentTarget.value;
        this.getEventResults(value)
    }

    itemListPredicate(query){
        //TODO: ADD DATE SEARCH
        const searcher = new FuzzySearch(this.state.events, ['name'], {
            caseSensitive: false,
        });
        return searcher.search(query);
    }

    getEventResults(query){
        fetch('http://localhost:8080/events/' + query)
            .then(results => results.json())
            .then(response => {
                this.setState((prevState, props) => {
                    return { events: response };
                });
            });
    }

    getEvents(){
        fetch('http://localhost:8080/events')
        .then(results => results.json())
        .then(response => {
            this.setState((prevState, props) => {
                return { events: response };
            });
        });
    }

    getEventsStream(){
        let evtSource = new EventSource("http://localhost:8080/stream/events");
        evtSource.onmessage = function (event) {
            console.log(JSON.parse(event.data));
        }
    }

    inputValueRenderer(event){
        return event.name + " - " + TMUtils.formatDate(event.date)
    }
    itemRenderer(event, { handleClick, modifiers, query }){
        let itemText = event.name + " - " + TMUtils.formatDate(event.date)
        return (
            <MenuItem
                key={event.id}
                label={event.name}
                onClick={handleClick}
                text={itemText}
            />
        );
    }

    getVenue(venueID){
        fetch('http://localhost:8080/venue/' + venueID)
            .then(results => results.json())
            .then(response => {
                this.setState((prevState, props) => {
                    return { events: response };
                });
            });
    }

    render() {
        let rightElement = <Button minimal={true}  rightIcon={"arrow-right"}/>
        return (
                <Suggest
                    inputProps={ {
                        leftIcon: "search",
                        rightElement: rightElement,
                        placeholder:"Search for an Event" ,
                        style: { width: "100%"}
                    } }
                    popoverProps={ {usePortal: false, position: "top"} }
                    inputValueRenderer={this.inputValueRenderer}
                    itemRenderer={this.itemRenderer}
                    items={this.state.events}
                    onItemSelect={this.props.onEventSelect}
                    itemListPredicate={this.itemListPredicate}

                />

        );
    }
}