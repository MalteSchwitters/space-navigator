package com.spacetravel.navigator.exceptions;

public class NoSuchRouteException extends RuntimeException {
    public NoSuchRouteException() {
        super("NO SUCH ROUTE");
    }
}
