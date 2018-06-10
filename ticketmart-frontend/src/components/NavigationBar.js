import React from "react";
import {
    Navbar,
    NavbarDivider,
    NavbarGroup,
    NavbarHeading,
    Alignment
} from "@blueprintjs/core";

export class NavigationBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = { date: new Date() };
    }
    render() {
        return (
            <Navbar>
                <NavbarGroup align={Alignment.LEFT}>
                    <NavbarHeading>{this.props.brandText}</NavbarHeading>
                </NavbarGroup>
                <NavbarGroup align={Alignment.RIGHT}>

                    <NavbarDivider />
                </NavbarGroup>
            </Navbar>
        );
    }
}
