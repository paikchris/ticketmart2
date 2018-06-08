import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import {InputGroup} from "@blueprintjs/core";
import {Button} from "@blueprintjs/core/lib/cjs/components/button/buttons";
import {Suggest} from "@blueprintjs/select/lib/cjs/components/select/suggest";

export class SearchBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            events:[ "Hello"]
        };

        this.itemRenderer = this.itemRenderer.bind(this);
        this.itemValueRenderer = this.itemValueRenderer.bind(this);
        this.onItemSelect = this.onItemSelect.bind(this);


    }
    componentDidMount() {
        fetch('http://localhost:8080')
            .then(results => results.json())
            .then(response => {
                const users = response.results;
                this.setState({ users });
            });
    }

    getEvents(){

    }

    itemValueRenderer(t){
        return t
    }
    itemRenderer(t){
        return t
    }
    onItemSelect(){

    }

    render() {
        return (
            <div>
                <InputGroup leftIcon={"search"}
                            rightElement={<Button minimal={true}  rightIcon={"arrow-right"}/>}
                            placeholder={"Search for an Event"}
                />
                <Suggest
                    inputValueRenderer={this.itemValueRenderer}
                    itemRenderer={this.itemRenderer}
                    items={this.state.events}
                    onItemSelect={this.onItemSelect}
                />
            </div>

        );
    }
}