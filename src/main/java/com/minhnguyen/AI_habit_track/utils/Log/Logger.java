package com.minhnguyen.AI_habit_track.utils.Log;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public final class Logger {
    private final String layer;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z");

    public Logger(String layer) {
        this.layer = layer;
    }

    /**
     * Logs a message to the standard output with a specific format.
     * The format is: "time: layer - message"
     ** @param message The descriptive message to be logged.
     */
    public void log(String message) {
        ZonedDateTime now = ZonedDateTime.now();
        String logMessage = String.format("%s: %s - %s", now.format(dtf), this.layer.toUpperCase(), message);
        System.out.println(logMessage);
    }
}
