package org.example;

public class LocationException extends Exception {
    public LocationException() {
        super("Country is missing.");
    }

    @Override
    public String toString() {
        return "LocationException: " + super.getMessage();
    }
}
