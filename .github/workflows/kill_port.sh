#!/bin/bash

# --- Configuration ---
PORT_TO_KILL="8080" # Change this to the port you want to target

# --- Functions ---

# Function to display usage information
usage() {
    echo "Usage: $0 [PORT_NUMBER]"
    echo "  If PORT_NUMBER is provided, it overrides the default $PORT_TO_KILL."
    echo "  This script finds and kills the process listening on the specified port."
    exit 1
}

# --- Main Script ---

# Check if a port number is provided as an argument
if [ -n "$1" ]; then
    # Basic validation for the port number
    if [[ "$1" =~ ^[0-9]+$ ]] && [ "$1" -ge 1 ] && [ "$1" -le 65535 ]; then
        PORT_TO_KILL="$1"
    else
        echo "Error: Invalid port number '$1'. Please provide a number between 1 and 65535."
        usage
    fi
fi

echo "Attempting to shutdown server on port $PORT_TO_KILL..."

# Find the PID using the specified port
# Using ss for efficiency, filtering for LISTEN state and the specific port, then extracting the PID
PID=$(sudo ss -lntp 2>/dev/null | grep ":$PORT_TO_KILL" | grep -oP 'pid=\K\d+' | head -n 1)

# Check if a PID was found
if [ -z "$PID" ]; then
    echo "No process found listening on port $PORT_TO_KILL."
else
    echo "Found process with PID: $PID listening on port $PORT_TO_KILL."
    echo "Attempting to terminate PID $PID gracefully..."

    # Kill the process gracefully (SIGTERM)
    sudo kill "$PID"

    # Give it a moment to terminate
    sleep 2

    # Check if the process is still running
    if ps -p "$PID" > /dev/null; then
        echo "Process $PID is still running after graceful termination. Attempting forceful termination..."
        sudo kill -9 "$PID"
        sleep 1 # Give it another moment
        if ps -p "$PID" > /dev/null; then
            echo "Error: Process $PID could not be terminated."
        else
            echo "Process $PID forcefully terminated."
        fi
    else
        echo "Process $PID terminated successfully."
    fi
fi

# Verify the port is free
echo "Verifying port $PORT_TO_KILL status..."
if sudo ss -lntp 2>/dev/null | grep -q ":$PORT_TO_KILL"; then
    echo "Warning: Port $PORT_TO_KILL is still in use."
else
    echo "Port $PORT_TO_KILL is now free."
fi

echo "Stop server successfully !"



