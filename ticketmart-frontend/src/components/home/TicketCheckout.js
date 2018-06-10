import React from "react";
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import * as TMUtils from "./../../utils/TMUtils.js"



export class TicketCheckout extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            tickets: this.props.heldTickets,
            holdStartTime: this.props.holdStartTime,
            holdTimeRemaining: "",
            inProgress: true,
        };
        console.log("TICKET CONST")
        console.log(props)


        this.getStartTime = this.getStartTime.bind(this);
        this.updateHoldTime = this.updateHoldTime.bind(this);
        this.onReserveButtonClick = this.onReserveButtonClick.bind(this);
    }

    getStartTime(){
        let ticketKeys = Object.keys(this.props.heldTickets)
        return this.props.heldTickets[ticketKeys[0]].holdStartTime
    }

    updateHoldTime(){
        console.log("STATE: START TIME")
        console.log(this.state.holdStartTime)
        var start = new Date(this.state.holdStartTime);
        var end = new Date(start.getTime() + 15 *60000);
        var now = new Date();
        // var timeDiff = Math.abs(now.getTime() - end.getTime());
        var timeDiff = TMUtils.dateDiff(now, end)

        var secDiff = timeDiff / 1000; //in s
        var minDiff = timeDiff / 60 / 1000; //in minutes
        var hDiff = timeDiff / 3600 / 1000; //
        var diffMin = Math.round(((timeDiff % 86400000) % 3600000) / 60000);
        var diffSec = Math.round((timeDiff) / 1000)

        console.log("END DATE: " + start.toString())
        console.log("END DATE: " + end.toString())
        console.log("NOW DATE: " + now.toString())
        console.log(timeDiff)

        return (TMUtils.millisToMinutesAndSeconds(timeDiff))
    }

    componentDidMount() {
        if(this.state.inProgress === true){
            this.interval = setInterval(() => this.setState({ holdTimeRemaining : this.updateHoldTime() }), 1000);
        }
    }

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    onReserveButtonClick(){
        let completeFunction = this.props.changePageIndex
        let ticketIDKeys = Object.keys(this.state.tickets)
        ticketIDKeys.forEach(function(ticketID) {
            fetch('http://localhost:8080/seat/reserve/' + ticketID, {method: 'POST'})
                .then(results => results.json())
                .then(response => {
                    console.log("RESERVE TICKET RESPONSE")
                    console.log(response);
                    alert("RESERVED")

                    completeFunction("search", {})
                });
        });
    }

    render(props) {
        console.log("TICKET RENDER")
        console.log(this.props)
        let ticketTable =""
        if(this.props.heldTickets){
            let heldTickets = this.props.heldTickets
            let seatIDMap = this.props.rowNumSeatIDMap

            ticketTable = Object.keys(heldTickets).map((ticketID) =>
                <div key={ticketID}>
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
                <Button
                    text={"Reserve Seats"}
                    intent={"primary"}
                    onClick={this.onReserveButtonClick}
                />
            </div>
        );
    }
}

