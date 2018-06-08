import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import "./css/blueprint.css";
import "./css/theme.css";
import { NavigationBar } from "./components/NavigationBar.js";
import BrowserRouter from "react-router-dom/es/BrowserRouter";
import Switch from "react-router-dom/es/Switch";
import {SearchBar} from "./components/home/SearchBar";


class App extends Component {
    render() {
        return (
            <BrowserRouter id="historyDIV">
                <div className="appContainer pt-dark">
                    <NavigationBar brandText="TicketMart" />
                    <div className="bodyContainer">
                        <Switch>
                        </Switch>
                        <SearchBar/>
                    </div>
                </div>
            </BrowserRouter>
        );
    }
}
export default App;
