import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import {InputGroup} from "@blueprintjs/core";
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import {Suggest} from "@blueprintjs/select/lib/cjs/components/select/suggest";
import {MenuItem} from "@blueprintjs/core/lib/cjs/components/menu/menuItem";
import * as FuzzySearch from "fuzzy-search";
import * as TMUtils from "./../../utils/TMUtils.js"
import {Card} from "@blueprintjs/core/lib/cjs/components/card/card";



export class EventDetailContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            heldTickets: {},
            subTotal: 0,
        };

        this.addTicketHold = this.addTicketHold.bind(this);
        this.removeTicketHold = this.removeTicketHold.bind(this);
        this.getHeldSeatsDisplayString = this.getHeldSeatsDisplayString.bind(this);

    }

    addTicketHold(ticketObj){
        this.setState((prevState, props) => {
            //update subtotal first
            let subTotal = parseFloat(prevState.subTotal) + parseFloat(ticketObj.price)

            //update tickets held
            let heldTickets = prevState.heldTickets
            heldTickets[ticketObj.id] = ticketObj

            return { heldTickets: heldTickets, subTotal: subTotal.toFixed(2) };
        });
    }
    removeTicketHold(ticketObj){
        this.setState((prevState, props) => {
            //update subtotal first
            let subTotal = parseFloat(prevState.subTotal) - parseFloat(ticketObj.price)

            //update tickets held
            let heldTickets = prevState.heldTickets
            heldTickets[ticketObj.id] = undefined


            return { heldTickets: heldTickets, subTotal: subTotal.toFixed(2) };
        });
    }

    updateHeldTickets(event){
        //UPDATE STATE


        /*
        really updating held tickets, send update with ticketID to server

        public String ticketid;
        public String eventID;
        public String userID;
        public String price;
        public String seatID;
        public boolean reserved;
         */
    }

    getHeldSeatsDisplayString(){
        // Object.keys(this.state.heldTickets).forEach(function(ticketID){
        //     this.state.heldTickets[ticketID].
        // })
    }


    render() {
        let detailComponent = ""
        if(this.props.event && this.props.venue && this.props.seatTicketMap){
            detailComponent = (
                <div>
                    <h1>{this.props.event.name}</h1>
                    <h2>{TMUtils.formatDate(this.props.event.date)}</h2>
                    <h2>{this.props.venue.name}</h2>
                    <h3>Subtotal: ${this.state.subTotal}</h3>
                    <h4></h4>
                    <SeatMap
                        totalRows={this.props.venue.seatRows}
                        totalNumbers={this.props.venue.seatNumbers}
                        seatTicketMap={this.props.seatTicketMap}
                        seatRank={this.props.seatRank}
                        addTicketHold={this.addTicketHold}
                        removeTicketHold={this.removeTicketHold}
                    />
                    <Button
                        disabled={true}
                        text={"Hold Seats"}
                        intent={"primary"}
                    />


                </div>

            )
        }
        return (
            <div className="eventDetailContainer">
                {detailComponent}
            </div>

        );
    }
}

export class SeatMap extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
        };

        // this.getAvailableTickets = this.getAvailableTickets.bind(this);
    }
    componentDidMount(){
    }


    render() {
        let seatMapElement = "";
        if(this.props.seatTicketMap && this.props.seatRank){
            let seatTicketMap = this.props.seatTicketMap;
            let seatRankArray = this.props.seatRank;
            let recommendedSeatsArray = seatRankArray.slice(0, Math.round(seatRankArray.length * .1))

            let seatKeys = Object.keys(seatTicketMap).sort();



            seatMapElement = seatKeys.map((seatRowNum) =>

                <SeatCard
                    ticketObj={seatTicketMap[seatRowNum]}
                    reserved={seatTicketMap[seatRowNum].reserved}
                    recommended={ recommendedSeatsArray.includes(seatRowNum) }
                    className={"seatCard"}
                    // className={seatTicketMap[seatRowNum] ? "seatCard reserved" : "seatCard unreserved recommended"}
                    key={seatRowNum}
                    interactive={seatTicketMap[seatRowNum].reserved ? false : true}
                    elevation={seatTicketMap[seatRowNum].reserved ? 0 : 2}
                    seatRowNum={seatRowNum}
                    addTicketHold={this.props.addTicketHold}
                    removeTicketHold={this.props.removeTicketHold}
                />
            );
        }

        return (
            <div className="seatMap"
                 style={ {
                     gridTemplateColumns: "repeat(" + this.props.totalNumbers + ", 40px)",
                 }}
            >
                {seatMapElement}
            </div>

        );
    }
}

export class SeatCard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ticketObj: this.props.ticketObj,
            hold: false,
            recommended: this.props.recommended,
            reserved: this.props.reserved,
        };

        this.onSeatClick = this.onSeatClick.bind(this);
    }

    onSeatClick(event){
        if(this.state.reserved === false){
            this.setState((prevState, props) => {
                if(prevState.hold === false){
                    props.addTicketHold(prevState.ticketObj)
                    return { hold: true };
                }
                else{
                    props.removeTicketHold(prevState.ticketObj)
                    return { hold: false };
                }
            });
        }
    }

    render() {
        let classString = "seatCard"

        if( this.props.reserved){
            classString = "seatCard reserved"
        }
        else{
            classString = "seatCard"

            if(this.state.hold === true){
                classString = classString + " hold"
            }

            //ADD RECOMMENDED CLASS STRING IF NECESSARY
            if(this.props.recommended === true){
                classString = classString + " recommended"
            }
        }

        return (
            <Card className={classString}
                  key={this.props.key}
                  interactive={this.props.interactive}
                  elevation={this.props.elevation}
                  onClick={this.onSeatClick}
            >
                <div>{this.props.seatRowNum}</div>
                <div style={ {fontSize: "10px" } }>${this.state.ticketObj.price}</div>
            </Card>
        );
    }
}