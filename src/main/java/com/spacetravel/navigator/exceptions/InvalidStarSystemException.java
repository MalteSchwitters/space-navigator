package com.spacetravel.navigator.exceptions;

public class InvalidStarSystemException extends Exception {
    public InvalidStarSystemException(String reason) {
        super("INVALID STAR SYSTEM: " + reason);
    }
}
