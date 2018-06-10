import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import {InputGroup} from "@blueprintjs/core";
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import {Suggest} from "@blueprintjs/select/lib/cjs/components/select/suggest";
import {MenuItem} from "@blueprintjs/core/lib/cjs/components/menu/menuItem";
import * as FuzzySearch from "fuzzy-search";
import * as TMUtils from "./../../utils/TMUtils.js"
import {Card} from "@blueprintjs/core/lib/cjs/components/card/card";



export class TicketCheckout extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            holdStartTime: this.props.holdStartTime,
            holdTimeRemaining: "",
        };
        console.log("TICKET CONST")
        console.log(props)


        this.getStartTime = this.getStartTime.bind(this);
        this.updateHoldtime = this.updateHoldtime.bind(this);
    }
    // componentDidMount(){
    //     setInterval(this.updateHoldtime, 1000);
    // }

    getStartTime(){
        let ticketKeys = Object.keys(this.props.heldTickets)
        return this.props.heldTickets[ticketKeys[0]].holdStartTime
    }

    updateHoldtime(){
        console.log("STATE: START TIME")
        console.log(this.state.holdStartTime)
        var start = new Date(this.state.holdStartTime);
        var end = new Date(start.getTime() + 15 *60000);
        var now = new Date();
        // var timeDiff = Math.abs(now.getTime() - end.getTime());
        var timeDiff = dateDiff(now, end)

        var secDiff = timeDiff / 1000; //in s
        var minDiff = timeDiff / 60 / 1000; //in minutes
        var hDiff = timeDiff / 3600 / 1000; //
        var diffMin = Math.round(((timeDiff % 86400000) % 3600000) / 60000);
        var diffSec = Math.round((timeDiff) / 1000)

        console.log("END DATE: " + start.toString())
        console.log("END DATE: " + end.toString())
        console.log("NOW DATE: " + now.toString())
        console.log(timeDiff)

        return (millisToMinutesAndSeconds(timeDiff))

        //
    }

    componentDidMount() {
        this.interval = setInterval(() => this.setState({ holdTimeRemaining : this.updateHoldtime() }), 1000);
    }
    componentWillUnmount() {
        clearInterval(this.interval);
    }

    render(props) {
        console.log("TICKET RENDER")
        console.log(this.props)
        let ticketTable =""
        if(this.props.heldTickets){
            let heldTickets = this.props.heldTickets
            let seatIDMap = this.props.rowNumSeatIDMap

            ticketTable = Object.keys(heldTickets).map((ticketID) =>
                <div>
                    <span>{seatIDMap[heldTickets[ticketID].seatID]} - ${parseFloat(heldTickets[ticketID].price)}</span>
                </div>
            )
        }


        return (
            <div>
                <h1>Ticket Checkout</h1>
                <h4>Hold time remaining: {this.state.holdTimeRemaining}</h4>
                <h2>{ticketTable}</h2>
                <h3>Total: ${this.props.subTotal}</h3>
            </div>
        );
    }
}

function dateDiff(date1, date2) {
    return Math.abs(date1.getTime() - date2.getTime());
}

function millisToMinutesAndSeconds(millis) {
    var minutes = Math.floor(millis / 60000);
    var seconds = ((millis % 60000) / 1000).toFixed(0);
    return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
}
