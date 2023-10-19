package com.spacetravel.navigator.exceptions;

public class InvalidRouteException extends Exception {
    public InvalidRouteException(String reason) {
        super("INVALID ROUTE: " + reason);
    }
}
