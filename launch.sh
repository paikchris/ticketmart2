#!/usr/bin/env bash


java -jar target/demo-0.0.1-SNAPSHOT.jar & npm start --prefix ticketmart-frontend/

exit_script() {
    trap - SIGINT SIGTERM # clear the trap
    kill -- -$$ # Sends SIGTERM to child/sub processes
}

trap exit_script SIGINT SIGTERM



